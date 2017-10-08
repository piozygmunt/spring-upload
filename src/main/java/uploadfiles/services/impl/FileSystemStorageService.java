package uploadfiles.services.impl;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoProperties;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import uploadfiles.exceptions.StorageException;
import uploadfiles.exceptions.StorageFileNotFoundException;
import uploadfiles.StorageProperties;
import uploadfiles.services.StorageService;

@Service
public class FileSystemStorageService implements StorageService {

	static private final Logger logger = Logger.getLogger(FileSystemStorageService.class);  
    private final Path rootLocation;

    @Autowired
    public FileSystemStorageService(StorageProperties properties) {
        this.rootLocation = Paths.get(properties.getLocation());
        logger.info("root location: " + this.rootLocation);
        
    }

    @Override
    public void store(MultipartFile file, String path) {
        Path relativePath = this.rootLocation.resolve(Paths.get(path));
        logger.info("realtive path: " + relativePath.toString());
        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        try {
            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file " + filename);
            }

            if(new File(relativePath.resolve(filename).toString()).exists())
            {
                throw new StorageException("File named " + filename + " already exists.");
            }

            if (filename.contains("..")) {
                // This is a security check
                throw new StorageException(
                        "Cannot store file with relative path outside current directory "
                                + filename);
            }
            logger.info("info: " + relativePath.resolve(filename).toString());

            Files.copy(file.getInputStream(), relativePath.resolve(filename),
                    StandardCopyOption.REPLACE_EXISTING);
        }
        catch (IOException e) {
            throw new StorageException("Failed to store file " + filename, e);
        }
    }

    @Override
    public Map<Path, Boolean> loadAll(String filePath) {
        Path relativePath = this.rootLocation.resolve(Paths.get(filePath));

        try {

            Map<Path, Boolean> map = Files.walk(relativePath, 1)
                    .filter(path -> !path.equals(relativePath))
                    .collect(Collectors.toMap(path -> this.rootLocation.relativize(path), path-> Files.isDirectory(path)));

            return map;
        }
        catch (IOException e) {
            throw new StorageException("Failed to read stored files", e);
        }

    }

    @Override
    public Path load(String filepath) {
        return rootLocation.resolve(filepath);
    }

    @Override
    public Resource loadAsResource(String filepath) {
        try {
            Path file = load(filepath);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            }
            else {
                throw new StorageFileNotFoundException(
                        "Could not read file: " + filepath);

            }
        }
        catch (MalformedURLException e) {
            throw new StorageFileNotFoundException("Could not read file: " + filepath, e);
        }
    }

    @Override
    public String getParentDir(String current) {
        Path currentPath = Paths.get(current);
        logger.info("" + currentPath + " " + current);
        if(this.rootLocation.resolve(currentPath).equals(this.rootLocation))
            return currentPath.toString();
        Path parentPath = currentPath.getParent();
        return parentPath == null ? "" : parentPath.toString();
    }

    @Override
    public void deleteFile(String filepath) {
        logger.info("filepath: " + filepath);
        Path path = load(filepath);
        File file = path.toFile();
        if(file.exists() && file.canWrite()) {
            logger.info("path: " + path.toString());
            if(file.isDirectory()) FileSystemUtils.deleteRecursively(file);
            logger.info("result " +  file.delete());

        }
        else throw new StorageFileNotFoundException(
                "Could not read file: " + filepath);
    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }

    @Override
    public void createNewFolder(String path, String dirname) {

        logger.info("path : " + path + ", dirname: " + dirname);

        Path dirPath = rootLocation.resolve(path);
        Path fullDirPath = dirPath.resolve(dirname);
        File directory = fullDirPath.toFile();
        try {
            if(!directory.mkdir())
            {
                throw new StorageException("Could not create directory with name " + dirname);
            }
        }catch(SecurityException ex)
        {
            throw new StorageException("Could not create directory with name " + dirname);
        }
    }

    @Override
    public void deleteFolder(String dirname) {
        String mainDirPath = rootLocation.toString();
        File directory = new File(mainDirPath + "/"+ dirname);
        if(directory.exists() && directory.canWrite())
            directory.delete();
        else throw new StorageFileNotFoundException(
                "Could not read file: " + dirname);
    }

    @Override
    public void init() {
        try {
            Files.createDirectories(rootLocation);
        }
        catch (FileAlreadyExistsException e)
        {
        	
        }
        catch (IOException e) {
            throw new StorageException("Could not initialize services", e);
        }
    }
}

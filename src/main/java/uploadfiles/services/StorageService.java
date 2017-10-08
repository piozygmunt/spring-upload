package uploadfiles.services;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.Map;
import java.util.stream.Stream;

public interface StorageService {

    void init();

    void store(MultipartFile file, String path);

    Map<Path, Boolean> loadAll(String path);

    Path load(String filename);

    Resource loadAsResource(String filename);

    String getParentDir(String current);

    void deleteFile(String filename);

    void deleteAll();

    void createNewFolder(String path, String name);

    void deleteFolder(String name);
}

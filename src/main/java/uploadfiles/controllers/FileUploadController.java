package uploadfiles.controllers;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.springframework.http.MediaType;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import uploadfiles.dto.DirectoryDTO;
import uploadfiles.dto.DirectoryResponseDTO;
import uploadfiles.dto.FileInfoDTO;
import uploadfiles.dto.ResponseUploadDTO;
import uploadfiles.exceptions.StorageException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import uploadfiles.exceptions.StorageFileNotFoundException;
import uploadfiles.services.StorageService;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/uploadfiles")
public class FileUploadController {

    private final Logger logger = Logger.getLogger(FileUploadController.class);
    private final StorageService storageService;



    static public String extractFilePath(String pattern, String uri)
    {
        return new AntPathMatcher()
                .extractPathWithinPattern( pattern, uri );
    }

    static public String decodeFilePath(String filePath)
    {
        String decoded = null;
        try {
            decoded = URLDecoder.decode(filePath, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return decoded;
    }

    static public String encodeFilePath(String filePath)
    {
        String encoded = null;
        try {
            filePath = filePath.replaceAll("%(?![0-9a-fA-F]{2})", "%25");
            filePath = filePath.replaceAll("\\+", "%2B");
            encoded = URLDecoder.decode(filePath, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return encoded;
    }

    @Autowired
    public FileUploadController(StorageService storageService) {
        this.storageService = storageService;
    }

    @GetMapping("**")
    public String listUploadedFiles(Model model, HttpServletRequest request) throws IOException {

        String filePath = extractFilePath("/uploadfiles/**", request.getRequestURI());
        String decoded = decodeFilePath(filePath);

        Map<Path, Boolean> paths =  storageService.loadAll(decoded);
        List<FileInfoDTO> links = new LinkedList<>();

        String baseUrl =
                MvcUriComponentsBuilder.fromController(FileUploadController.class).build().toString();

        for (Map.Entry<Path, Boolean> entry : paths.entrySet()) {
           /* String fullPath = entry.getKey().toString();
            String filename = fullPath.split("/")[fullPath.split("/").length-1];
            String removeURL = MvcUriComponentsBuilder.fromController(FileUploadController.class).build().toString() + "/files/" +
                    URLEncoder.encode(entry.getKey().toString(), "UTF-8").replace("%2F", "/");
            String downloadURL;


          if(entry.getValue())
          {

              downloadURL = MvcUriComponentsBuilder.fromController(FileUploadController.class).build().toString() + "/" +
                      URLEncoder.encode(entry.getKey().toString(), "UTF-8").replace("%2F", "/");
          }

          else
          {
              downloadURL = new String(removeURL);
          }*/

           links.add(new FileInfoDTO(baseUrl,null,entry.getKey().toString(), entry.getValue()));

        }

        model.addAttribute("files", links);
        model.addAttribute("currentPath", decoded);
        model.addAttribute("currentPathEncoded", filePath);
        model.addAttribute("parentDir",baseUrl + "/" + storageService.getParentDir(filePath));

        return "uploadForm";
    }

    @GetMapping("files/**")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(HttpServletRequest request) {

        String filePath = extractFilePath("/uploadfiles/files/**", request.getRequestURI());
        String decoded = decodeFilePath(filePath);

        logger.info("files downloading");
        Resource file = storageService.loadAsResource(decoded);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    @DeleteMapping(value="files/**", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<ResponseUploadDTO> deleteFile(String currentPath, HttpServletRequest request) {

        String filePath = extractFilePath("/uploadfiles/files/**", request.getRequestURI());
        String decoded = decodeFilePath(filePath);


        String baseUrl =
                MvcUriComponentsBuilder.fromController(FileUploadController.class).build().toString();
        try {
            storageService.deleteFile(decoded);
        }
        catch(StorageException ex)
        {
            return new ResponseEntity<ResponseUploadDTO>(new ResponseUploadDTO(ex.getMessage(), null), HttpStatus.BAD_REQUEST);
        }

        //String downloadURL = baseUrl + "/files/" + filePath;

        FileInfoDTO fileInfoDTO = new FileInfoDTO(baseUrl, null, decoded, false);

        return new ResponseEntity<ResponseUploadDTO>(new ResponseUploadDTO("You successfully deleted file " + fileInfoDTO.getFilename() + "!", fileInfoDTO), HttpStatus.OK);
    }

    @PostMapping("**")
    @ResponseBody
    public ResponseEntity<ResponseUploadDTO> handleFileUpload(@RequestParam("file") MultipartFile file, HttpServletRequest request) {

        String filePath = extractFilePath("/uploadfiles/files/**", request.getRequestURI());
        String decoded = decodeFilePath(filePath);

        String baseUrl =
                MvcUriComponentsBuilder.fromController(FileUploadController.class).build().toString();

        try {
            storageService.store(file, decoded);
        }
        catch(StorageException ex)
        {
            return new ResponseEntity<ResponseUploadDTO>(new ResponseUploadDTO(ex.getMessage(), null), HttpStatus.BAD_REQUEST);
        }
/*
        String downloadURL =  MvcUriComponentsBuilder.fromController(FileUploadController.class).build().toString() + "/files/" + filePath
                + "/" + file.getOriginalFilename();

        String fullPath = filePath + "/" + file.getOriginalFilename();

        FileInfoDTO fileInfoDTO = new FileInfoDTO(downloadURL, downloadURL, fullPath, file.getOriginalFilename());*/

        FileInfoDTO fileInfoDTO = new FileInfoDTO(baseUrl, file.getOriginalFilename(), decoded, false);

        return new ResponseEntity<ResponseUploadDTO>(new ResponseUploadDTO("You successfully uploaded " + file.getOriginalFilename() + "!", fileInfoDTO), HttpStatus.OK);
    }

    @PostMapping(value = "directory/**")
    @ResponseBody
    public ResponseEntity<ResponseUploadDTO> createNewDirectory(@RequestBody  DirectoryDTO directoryDTO, HttpServletRequest request) {

        String filePath = extractFilePath("/uploadfiles/directory/**", request.getRequestURI());
        String decoded = decodeFilePath(filePath);

        String baseUrl =
                MvcUriComponentsBuilder.fromController(FileUploadController.class).build().toString();


        try {
            storageService.createNewFolder(decoded, directoryDTO.getDirname());
        }
        catch(StorageException ex)
        {
            return new ResponseEntity<>(new ResponseUploadDTO( ex.getMessage(), null), HttpStatus.BAD_REQUEST);
        }

       /* String fullPath = filePath + "/" + directoryDTO.getDirname();

        String encoded = encodeFilePath(directoryDTO.getDirname());

        String downloadURL =  MvcUriComponentsBuilder.fromController(FileUploadController.class).build().toString() + "/" + filePath
                + "/" + encoded;

        String removeURL =  MvcUriComponentsBuilder.fromController(FileUploadController.class).build().toString() + "/directory/" + filePath
                + "/" + encoded;*/

//        FileInfoDTO fileInfoDTO = new FileInfoDTO(downloadURL, removeURL, fullPath, directoryDTO.getDirname());
        FileInfoDTO fileInfoDTO = new FileInfoDTO(baseUrl, directoryDTO.getDirname(), decoded, true);

        return new ResponseEntity<>(new ResponseUploadDTO("You successfully createad " + directoryDTO.getDirname() + "!", fileInfoDTO), HttpStatus.OK);
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }

}

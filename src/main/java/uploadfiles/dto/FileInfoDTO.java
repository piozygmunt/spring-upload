package uploadfiles.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import uploadfiles.controllers.FileUploadController;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@AllArgsConstructor
@Data
public class FileInfoDTO {
    private String fullDownloadURL;
    private String removeURL;
    private String filePath;
    private String filename;


    public FileInfoDTO(String baseUrl, String filename, String fullPath, boolean isDirectory)
    {
        if(filename == null){
            filePath = fullPath;
            this.filename = filePath.split("/")[filePath.split("/").length-1];
        }
        else
        {
            filePath = fullPath + "/" + filename;
            this.filename = filename;
        }
        try {
            removeURL = baseUrl + "/files/" +
                    URLEncoder.encode(filePath, "UTF-8").replace("%2F", "/");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


        if(isDirectory)
        {

            try {
                fullDownloadURL = baseUrl+ "/" +
                        URLEncoder.encode(filePath, "UTF-8").replace("%2F", "/");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        else
        {
            fullDownloadURL = removeURL;
        }
    }

    private void initUrls(String baseUrl, boolean isDirectory)
    {
        try {
            removeURL = baseUrl + "/files/" +
                    URLEncoder.encode(filePath, "UTF-8").replace("%2F", "/");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


        if(isDirectory)
        {

            try {
                fullDownloadURL = baseUrl+ "/" +
                        URLEncoder.encode(filePath, "UTF-8").replace("%2F", "/");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        else
        {
            fullDownloadURL = removeURL;
        }
    }
}

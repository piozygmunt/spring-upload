package uploadfiles.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class FileInfoDTO {
    private String fullDownloadURL;
    private String removeURL;
    private String filePath;
    private String filename;
}

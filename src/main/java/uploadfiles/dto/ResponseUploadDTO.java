package uploadfiles.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by piotrek on 02.08.17.
 */
@Data
@AllArgsConstructor
public class ResponseUploadDTO {
    private String message;
    private FileInfoDTO fileInfo;
}

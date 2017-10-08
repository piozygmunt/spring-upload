package uploadfiles.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DirectoryResponseDTO {
    private String dirname;
    private String message;
}

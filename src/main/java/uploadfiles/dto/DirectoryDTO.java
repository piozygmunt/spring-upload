package uploadfiles.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
@Data
public class DirectoryDTO {

    public DirectoryDTO()
    { }

    @NotNull
    @NotEmpty
    private String dirname;
}

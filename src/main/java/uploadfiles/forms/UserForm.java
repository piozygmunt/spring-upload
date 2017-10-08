package uploadfiles.forms;

import lombok.Data;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class UserForm {

    private String username;

    private String password;

    private String passwordConfirm;

    private String email;

    public boolean doPasswordMatch()
    {
        return password.equals(passwordConfirm);
    }
}

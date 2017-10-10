package uploadfiles.forms;

import lombok.Data;
import uploadfiles.entities.Role;
import uploadfiles.entities.User;

import java.util.HashSet;
import java.util.Set;

@Data
public class UserEditForm extends UserForm{
    private long id;
    private boolean enabled;
    private Set<Role> roles = new HashSet<>();

    public UserEditForm()
    {

    }

    public UserEditForm(User user)
    {
        id = user.getId();
        setUsername(user.getUsername());
        setEmail(user.getEmail());
        enabled = user.isEnabled();
        roles = user.getRoles();
    }

}

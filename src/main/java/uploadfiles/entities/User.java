package uploadfiles.entities;

import lombok.Data;
import org.hibernate.annotations.Fetch;
import org.hibernate.mapping.FetchProfile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import uploadfiles.forms.UserEditForm;
import uploadfiles.forms.UserForm;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="users")
@Data
public class User {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;
    @Column
    private String username;
    @Column
    private String password;
    @Column
    private String email;
    @Column
    private boolean enabled;

    public User()
    {

    }

    public User(UserForm form)
    {
        username = form.getUsername();
        password = form.getPassword();
        email = form.getEmail();
    }

    public User(UserEditForm userEditForm)
    {
        id = userEditForm.getId();
        username = userEditForm.getUsername();
        password = userEditForm.getUsername();
        email = userEditForm.getEmail();
        enabled = userEditForm.isEnabled();
        roles = userEditForm.getRoles();
    }

    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.EAGER)
    @JoinTable(name="user_roles",
        joinColumns = @JoinColumn(name="user_id"),
        inverseJoinColumns = @JoinColumn(name="role_id") )
    private Set<Role> roles = new HashSet<>();


    public void addRole(Role role)
    {
        roles.add(role);
        role.getUsers().add(this);
    }

    public void removeRole(Role role)
    {
        roles.remove(role);
        role.getUsers().remove(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        User user = (User) o;

        return id == user.id;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (int) (id ^ (id >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", enabled=" + enabled +
                '}';
    }
}

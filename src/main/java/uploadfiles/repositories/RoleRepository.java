package uploadfiles.repositories;

import org.springframework.data.repository.CrudRepository;
import uploadfiles.entities.Role;

public interface RoleRepository extends CrudRepository <Role, Long>{
    Role findByName(String name);
}

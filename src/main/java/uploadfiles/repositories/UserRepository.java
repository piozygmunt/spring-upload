package uploadfiles.repositories;

import org.springframework.data.repository.CrudRepository;
import uploadfiles.entities.User;

public interface UserRepository extends CrudRepository<User, Long>{
    User findByUsername(String username);
}

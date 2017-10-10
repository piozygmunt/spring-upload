package uploadfiles.services;

import uploadfiles.entities.User;

public interface UserService {
    User findByUsername(String username);
    void createNewUser(User user);
    Iterable<User> findAll();
    User findByID(long id);
    void delete(long id);
    void save(User user);
}

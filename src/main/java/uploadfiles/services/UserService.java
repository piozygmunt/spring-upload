package uploadfiles.services;

import uploadfiles.entities.User;

public interface UserService {
    User findByUsername(String username);
    void createNewUser(User user);
}

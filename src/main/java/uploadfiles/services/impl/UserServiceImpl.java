package uploadfiles.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import uploadfiles.entities.Role;
import uploadfiles.entities.User;
import uploadfiles.repositories.RoleRepository;
import uploadfiles.repositories.UserRepository;
import uploadfiles.services.UserService;

@Service
public class UserServiceImpl implements UserService{
    private static final String DEFAULT_USER_ROLE = "USER";

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository repoRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public void createNewUser(User user) {
        //user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        Role role = repoRepository.findByName(DEFAULT_USER_ROLE);
        user.addRole(role);
        user.setEnabled(true);
        userRepository.save(user);
    }

    @Override
    public Iterable<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User findByID(long id) {
        return userRepository.findOne(id);
    }

    @Override
    public void delete(long id) {
        userRepository.delete(id);
    }

    @Override
    public void save(User user) {
        userRepository.save(user);
    }


}

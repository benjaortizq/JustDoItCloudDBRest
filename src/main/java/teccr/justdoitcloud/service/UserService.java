package teccr.justdoitcloud.service;

import org.springframework.stereotype.Service;
import teccr.justdoitcloud.data.User;
import teccr.justdoitcloud.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Service responsible for user-related business logic.
 */
@Service
public class UserService {

    private final UserRepository userRepository;

    // Constructor injection of UserRepository
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Authenticate a user by username. For initial purposes this only looks
     * up the user by username and returns it if present.
     *
     * @param username the username to authenticate
     * @return Optional containing the User if found
     */
    public Optional<User> authenticate(String username) {
        return getUserByUserName(username);
    }

    public Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        if (id == null || id < 0) {
            return Optional.empty();
        }
        return userRepository.findById(id);
    }

    /**
     * Look up a single user by its username. Surrounding whitespace is ignored.
     *
     * @param userName the username to look up
     * @return Optional containing the User if found, or empty if the username
     *         is blank or no user has it
     */
    public Optional<User> getUserByUserName(String userName) {
        if (userName == null || userName.trim().isEmpty()) {
            return Optional.empty();
        }
        return userRepository.findByUserName(userName.trim());
    }

    public User createUser(User user) {
        user.setCreatedAt(LocalDateTime.now());
        return userRepository.save(user);
    }

    public User updateUserFields(Long id, User updatedUser) {
        return userRepository.findById(id)
                .map(existingUser -> {
                    if (updatedUser.getName() != null && !updatedUser.getName().trim().isEmpty()) {
                        existingUser.setName(updatedUser.getName().trim());
                    }
                    if (updatedUser.getEmail() != null && !updatedUser.getEmail().trim().isEmpty()) {
                        existingUser.setEmail(updatedUser.getEmail().trim());
                    }
                    if (updatedUser.getType() != null) {
                        existingUser.setType(updatedUser.getType());
                    }
                    return userRepository.save(existingUser);
                })
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

}

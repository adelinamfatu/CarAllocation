package org.car.allocation.service;

import org.car.allocation.model.User;
import org.car.allocation.repository.UserRepository;
import org.car.allocation.util.UserRole;

import java.util.List;
import java.util.Optional;

public class UserService {

    private final UserRepository userRepository = new UserRepository();

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void addUser(User user) {
        userRepository.save(user);
    }

    public Optional<User> findUserById(long id) {
        return userRepository.findById(id);
    }

    public Optional<User> findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public boolean deleteUserById(long id) {
        Optional<User> userOpt = userRepository.findById(id);
        if (userOpt.isPresent()) {
            userRepository.delete(userOpt.get());
            return true;
        }
        return false;
    }

    public void updateUser(User user) {
        userRepository.update(user);
    }

    public void createUser(String firstName, String lastName, String email, String phoneNumber, String username, String password, UserRole role) {
        User newUser = new User(role, firstName, lastName, email, phoneNumber, username, password, null, null);
        addUser(newUser);
    }
}

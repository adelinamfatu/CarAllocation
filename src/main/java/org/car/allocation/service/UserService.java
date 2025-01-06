package org.car.allocation.service;

import org.car.allocation.model.Car;
import org.car.allocation.model.Truck;
import org.car.allocation.model.User;
import org.car.allocation.model.Vehicle;
import org.car.allocation.repository.UserRepository;
import org.car.allocation.singleton.DatabaseUtil;
import org.car.allocation.util.UserRole;

import java.util.List;
import java.util.Optional;

import org.car.allocation.util.VehicleStatus;
import org.hibernate.Session;
import org.mindrot.jbcrypt.BCrypt;

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
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        User newUser = new User(role, firstName, lastName, email, phoneNumber, username, hashedPassword, null, null);
        addUser(newUser);
    }


    public Optional<User> findAvailableDriver() {
        try (Session session = DatabaseUtil.openSession()) {
            List<User> drivers = session.createQuery("from User u where u.role = :role and u.car is null and u.truck is null", User.class)
                    .setParameter("role", UserRole.DRIVER)
                    .list();

            return drivers.stream().findFirst(); // first driver available
        }
    }

    public void allocateVehicleToDriver(User driver, Vehicle vehicle) {
        DatabaseUtil.executeTransaction(session -> {
            if (vehicle instanceof Car) {
                driver.setCar((Car) vehicle);
            } else if (vehicle instanceof Truck) {
                driver.setTruck((Truck) vehicle);
            }
            session.update(driver);
            vehicle.setVehicleStatus(VehicleStatus.IN_USE);
            session.update(vehicle);
        });
    }
}

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

/**
 * Service class that provides operations for managing users and allocating vehicles to drivers.
 */
public class UserService {

    private final UserRepository userRepository = new UserRepository();

    /**
     * Retrieves a list of all users.
     *
     * @return a list of all users.
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Adds a new user.
     *
     * @param user the user to be added.
     */
    public void addUser(User user) {
        userRepository.save(user);
    }

    /**
     * Finds a user by their username.
     *
     * @param username the username of the user to find.
     * @return an Optional containing the found user, or empty if not found.
     */
    public Optional<User> findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    /**
     * Deletes a user by their ID.
     * @param id the ID of the user to be deleted.
     * @return true if the user was deleted successfully, false if not found.
     */
    public boolean deleteUserById(long id) {
        Optional<User> userOpt = userRepository.findById(id);
        if (userOpt.isPresent()) {
            userRepository.delete(userOpt.get());
            return true;
        }
        return false;
    }

    /**
     * Updates the details of an existing user.
     * @param user the user with updated information.
     */
    public void updateUser(User user) {
        userRepository.update(user);
    }

    /**
     * Creates a new user with hashed password and saves it.
     *
     * @param firstName    the user's first name.
     * @param lastName     the user's last name.
     * @param email        the user's email.
     * @param phoneNumber  the user's phone number.
     * @param username     the user's username.
     * @param password     the user's password.
     * @param role         the role of the user (e.g., DRIVER).
     */
    public void createUser(String firstName, String lastName, String email, String phoneNumber, String username, String password, UserRole role) {
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        User newUser = new User(role, firstName, lastName, email, phoneNumber, username, hashedPassword, null, null);
        addUser(newUser);
    }

    /**
     * Finds an available driver (a user with the role DRIVER and no vehicle assigned).
     *
     * @return an Optional containing the available driver, or empty if none is found.
     */
    public Optional<User> findAvailableDriver() {
        try (Session session = DatabaseUtil.openSession()) {
            List<User> drivers = session.createQuery("from User u where u.role = :role and u.car is null and u.truck is null", User.class)
                    .setParameter("role", UserRole.DRIVER)
                    .list();

            return drivers.stream().findFirst(); //First driver available
        }
    }

    /**
     * Allocates a vehicle (Car or Truck) to a driver.
     *
     * @param driver  the driver to whom the vehicle will be allocated.
     * @param vehicle the vehicle to be allocated.
     */
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

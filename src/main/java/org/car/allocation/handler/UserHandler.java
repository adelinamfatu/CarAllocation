package org.car.allocation.handler;

import org.car.allocation.model.User;
import org.car.allocation.service.UserService;
import org.car.allocation.util.LoggedInUserContext;
import org.car.allocation.util.UserRole;

import java.util.Optional;
import java.util.Scanner;
import java.text.MessageFormat;
import java.util.ResourceBundle;
import org.mindrot.jbcrypt.BCrypt;

/**
 * This class handles user-related functionalities such as login, signup, user updates, and user deletion.
 * It communicates with the UserService to perform CRUD operations on users.
 */
public class UserHandler {
    private final Scanner scanner;
    private final UserService userService;
    private static final ResourceBundle messages = ResourceBundle.getBundle("messages");

    public UserHandler(Scanner scanner) {
        this.scanner = scanner;
        this.userService = new UserService();
    }

    /**
     * Handles the user login process by verifying username and password.
     * If the login is successful, the user is logged in and redirected to the appropriate menu options.
     *
     * @throws *InputMismatchException If an invalid input type is entered for username or password.
     */
    public void handleLogin() {
        System.out.println(messages.getString("login.prompt"));
        String username = scanner.nextLine();
        System.out.println(messages.getString("login.password.prompt"));
        String password = scanner.nextLine();

        User user = userService.findUserByUsername(username).orElse(null);

        if (user != null && BCrypt.checkpw(password, user.getPassword())) {
            LoggedInUserContext.setLoggedInUser(user); //Save the logged-in user
            System.out.println(MessageFormat.format(messages.getString("welcome.message"), username, user.getRole()));
            new MenuHandler(scanner, user.getRole(), this).showOptions();
        } else {
            System.out.println(messages.getString("login.invalid"));
        }
    }

    /**
     * Handles the user signup process, which includes gathering necessary information
     * (such as name, email, password, role) and storing the user in the system.
     *
     * @throws *InputMismatchException If an invalid input type is entered for any field.
     */
    public void handleSignUp() {
        System.out.println(messages.getString("signup.prompt"));

        System.out.println(messages.getString("signup.firstname"));
        String firstName = scanner.nextLine();

        System.out.println(messages.getString("signup.lastname"));
        String lastName = scanner.nextLine();

        String email = "";
        while (true) {
            System.out.println(messages.getString("signup.email"));
            email = scanner.nextLine();
            if (isValidEmail(email)) {
                break;
            } else {
                System.out.println(messages.getString("signup.email.invalid"));
            }
        }

        String phoneNumber = "";
        while (true) {
            System.out.println(messages.getString("signup.phone_number"));
            phoneNumber = scanner.nextLine();
            if (isValidPhoneNumber(phoneNumber)) {
                break;
            } else {
                System.out.println(messages.getString("signup.phone_number.invalid"));
            }
        }

        System.out.println(messages.getString("login.prompt"));
        String username = scanner.nextLine();

        String password = "";
        while (true) {
            System.out.println(messages.getString("login.password.prompt"));
            password = scanner.nextLine();
            if (isValidPassword(password)) {
                break;
            } else {
                System.out.println(messages.getString("signup.password.invalid"));
            }
        }

        System.out.println(messages.getString("main.select_role"));
        System.out.println("1. Driver");
        System.out.println("2. Manager");
        System.out.println("3. Admin");
        int roleChoice = scanner.nextInt();
        scanner.nextLine();

        UserRole role = switch (roleChoice) {
            case 1 -> UserRole.DRIVER;
            case 2 -> UserRole.MANAGER;
            case 3 -> UserRole.ADMIN;
            default -> null;
        };

        if (role != null) {
            userService.createUser(firstName, lastName, email, phoneNumber, username, password, role);
            System.out.println(messages.getString("signup.successful"));
            showPermissions(role);
            new MenuHandler(scanner, role, this).showOptions();
        } else {
            System.out.println(messages.getString("main.invalid_role"));
        }
    }

    /**
     * Displays the details of the provided user, hiding sensitive information like password.
     *
     * @param user The user whose details are to be displayed.
     */
    private void displayUserDetails(User user) {
        System.out.println("Username: " + user.getUsername());
        System.out.println("Password: [PROTECTED]");
        System.out.println("First name: " + user.getFirstName());
        System.out.println("Last name: " + user.getLastName());
        System.out.println("Email: " + user.getEmail());
        System.out.println("Phone number: " + user.getPhoneNumber());
    }

    /**
     * Allows the logged-in user to update their details (username, password, email, phone number, etc.).
     * After making the changes, the updated user details are displayed.
     *
     * @throws *InputMismatchException If an invalid input type is entered for any field.
     */
    public void updateUserDetails() {
        displayUserDetails(LoggedInUserContext.getLoggedInUser());
        System.out.println(messages.getString("update.user.prompt"));
        System.out.println(messages.getString("update.user.options"));

        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1:
                System.out.println(messages.getString("signup.username"));
                String newUsername = scanner.nextLine();
                LoggedInUserContext.getLoggedInUser().setUsername(newUsername);
                break;
            case 2:
                System.out.println(messages.getString("login.password.prompt"));
                String newPassword = scanner.nextLine();
                LoggedInUserContext.getLoggedInUser().setPassword(BCrypt.hashpw(newPassword, BCrypt.gensalt()));
                break;
            case 3:
                System.out.println(messages.getString("signup.firstname"));
                String firstName = scanner.nextLine();
                LoggedInUserContext.getLoggedInUser().setFirstName(firstName);
                break;
            case 4:
                System.out.println(messages.getString("signup.lastname"));
                String lastName = scanner.nextLine();
                LoggedInUserContext.getLoggedInUser().setLastName(lastName);
                break;
            case 5:
                System.out.println(messages.getString("signup.email"));
                String email = scanner.nextLine();
                if (isValidEmail(email)) {
                    LoggedInUserContext.getLoggedInUser().setEmail(email);
                } else {
                    System.out.println(messages.getString("invalid.email"));
                    break;
                }
                break;
            case 6:
                System.out.println(messages.getString("signup.phone_number"));
                String phoneNumber = scanner.nextLine();
                if (isValidPhoneNumber(phoneNumber)) {
                    LoggedInUserContext.getLoggedInUser().setPhoneNumber(phoneNumber);
                } else {
                    System.out.println(messages.getString("invalid.phone_number"));
                    break;
                }
                break;
            default:
                System.out.println(messages.getString("invalid.option"));
                return;
        }

        userService.updateUser(LoggedInUserContext.getLoggedInUser());
        System.out.println(messages.getString("update.user.successful"));
        displayUserDetails(LoggedInUserContext.getLoggedInUser());
    }

    /**
     * Deletes a user from the system. Only an ADMIN can delete users.
     *
     * @throws IllegalAccessException If a non-admin user tries to delete another user.
     */
    public void deleteUser() {
        if (LoggedInUserContext.getLoggedInUser().getRole() != UserRole.ADMIN) {
            System.out.println(messages.getString("delete.user.permission.denied"));
            return;
        }

        System.out.println(messages.getString("delete.user.prompt"));
        String usernameToDelete = scanner.nextLine();

        if (usernameToDelete.equals(LoggedInUserContext.getLoggedInUser().getUsername())) {
            System.out.println(messages.getString("delete.user.self.denied"));
            return;
        }

        Optional<User> userOptional = userService.findUserByUsername(usernameToDelete);
        if (!userOptional.isPresent()) {
            System.out.println(messages.getString("delete.user.not.found"));
            return;
        }

        userService.deleteUserById(userOptional.get().getId());
        System.out.println(MessageFormat.format(messages.getString("delete.user.successful"), usernameToDelete));
    }

    /**
     * Validates if the given email follows a correct format.
     *
     * @param email The email to be validated.
     * @return true if the email format is valid, false otherwise.
     */
    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        return email.matches(emailRegex);
    }

    /**
     * Validates if the given phone number follows a correct format.
     *
     * @param phoneNumber The phone number to be validated.
     * @return true if the phone number format is valid, false otherwise.
     */
    private boolean isValidPhoneNumber(String phoneNumber) {
        String phoneRegex = "^(\\+?[0-9]{1,3})?([0-9]{10})$";
        return phoneNumber.matches(phoneRegex);
    }

    /**
     * Validates if the given password meets the required format (minimum 8 characters, at least one uppercase, one lowercase, and one digit).
     *
     * @param password The password to be validated.
     * @return true if the password format is valid, false otherwise.
     */
    private boolean isValidPassword(String password) {
        String passwordRegex = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d).{8,}$";
        return password.matches(passwordRegex);
    }

    /**
     * Displays the permissions associated with a specific user role.
     *
     * @param role The role of the user (DRIVER, MANAGER, ADMIN).
     */
    private void showPermissions(UserRole role) {
        switch (role) {
            case DRIVER:
                System.out.println(messages.getString("permissions.driver"));
                break;
            case MANAGER:
                System.out.println(messages.getString("permissions.manager"));
                break;
            case ADMIN:
                System.out.println(messages.getString("permissions.admin"));
                break;
        }
    }
}

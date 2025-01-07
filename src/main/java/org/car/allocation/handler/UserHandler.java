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

public class UserHandler {
    private final Scanner scanner;
    private final UserService userService;
    private static final ResourceBundle messages = ResourceBundle.getBundle("messages");

    public UserHandler(Scanner scanner) {
        this.scanner = scanner;
        this.userService = new UserService();
    }

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

    private void displayUserDetails(User user) {
        System.out.println("Username: " + user.getUsername());
        System.out.println("Password: [PROTECTED]");
        System.out.println("First name: " + user.getFirstName());
        System.out.println("Last name: " + user.getLastName());
        System.out.println("Email: " + user.getEmail());
        System.out.println("Phone number: " + user.getPhoneNumber());
    }

    public void updateUserDetails() {
        displayUserDetails(LoggedInUserContext.getLoggedInUser());
        System.out.println(messages.getString("update.user.prompt"));
        System.out.println(messages.getString("update.user.options"));

        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1:
                System.out.println(messages.getString("signin.username"));
                String newUsername = scanner.nextLine();
                LoggedInUserContext.getLoggedInUser().setUsername(newUsername);
                break;
            case 2:
                System.out.println(messages.getString("login.password.prompt"));
                String newPassword = scanner.nextLine();
                LoggedInUserContext.getLoggedInUser().setPassword(BCrypt.hashpw(newPassword, BCrypt.gensalt()));
                break;
            case 3:
                System.out.println(messages.getString("signin.firstname"));
                String firstName = scanner.nextLine();
                LoggedInUserContext.getLoggedInUser().setFirstName(firstName);
                break;
            case 4:
                System.out.println(messages.getString("signin.lastname"));
                String lastName = scanner.nextLine();
                LoggedInUserContext.getLoggedInUser().setLastName(lastName);
                break;
            case 5:
                System.out.println(messages.getString("signin.email"));
                String email = scanner.nextLine();
                if (isValidEmail(email)) {
                    LoggedInUserContext.getLoggedInUser().setEmail(email);
                } else {
                    System.out.println(messages.getString("invalid.email"));
                    break;
                }
                break;
            case 6:
                System.out.println(messages.getString("signin.phone_number"));
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

    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        return email.matches(emailRegex);
    }

    private boolean isValidPhoneNumber(String phoneNumber) {
        String phoneRegex = "^(\\+?[0-9]{1,3})?([0-9]{10})$";
        return phoneNumber.matches(phoneRegex);
    }

    private boolean isValidPassword(String password) {
        String passwordRegex = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d).{8,}$";
        return password.matches(passwordRegex);
    }

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

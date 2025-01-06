package org.car.allocation.handler;

import org.car.allocation.model.User;
import org.car.allocation.service.UserService;
import org.car.allocation.util.UserRole;
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

    public void handleLogin(UserRole role) {
        System.out.println(messages.getString("login.prompt"));
        String username = scanner.nextLine();
        System.out.println(messages.getString("login.password.prompt"));
        String password = scanner.nextLine();

        User user = userService.findUserByUsername(username).orElse(null);

        if (user != null && BCrypt.checkpw(password, user.getPassword())) {
            if (user.getRole() == role) {
                System.out.println(MessageFormat.format(messages.getString("welcome.message"), username, role));
                showPermissions(role);
                new MenuHandler(scanner, role).showOptions();
            } else {
                System.out.println(MessageFormat.format(messages.getString("login.unauthorized"), role));
            }
        } else {
            System.out.println(messages.getString("login.invalid"));
        }
    }

    public void handleSignIn(UserRole role) {
        System.out.println(messages.getString("signin.prompt"));

        System.out.println(messages.getString("signin.firstname"));
        String firstName = scanner.nextLine();

        System.out.println(messages.getString("signin.lastname"));
        String lastName = scanner.nextLine();

        String email = "";
        while (true) {
            System.out.println(messages.getString("signin.email"));
            email = scanner.nextLine();
            if (isValidEmail(email)) {
                break;
            } else {
                System.out.println("Email invalid. Vă rugăm să introduceți un email valid.");
            }
        }

        String phoneNumber = "";
        while (true) {
            System.out.println(messages.getString("signin.phone_number"));
            phoneNumber = scanner.nextLine();
            if (isValidPhoneNumber(phoneNumber)) {
                break;
            } else {
                System.out.println("Număr de telefon invalid. Vă rugăm să introduceți un număr valid.");
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
                System.out.println("Parolă invalidă. Vă rugăm să introduceți o parolă validă.");
            }
        }

        userService.createUser(firstName, lastName, email, phoneNumber, username, password, role);
        System.out.println(messages.getString("signin.successful"));
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

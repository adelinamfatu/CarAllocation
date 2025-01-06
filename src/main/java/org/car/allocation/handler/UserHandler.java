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

        if (user!=null) {
            if (BCrypt.checkpw(password, user.getPassword())) {
                System.out.println(MessageFormat.format(messages.getString("welcome.message"), username, role));
                showPermissions(role);
                new MenuHandler(scanner, role).showOptions();

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

        System.out.println(messages.getString("signin.email"));
        String email = scanner.nextLine();

        System.out.println(messages.getString("signin.phone_number"));
        String phoneNumber = scanner.nextLine();

        System.out.println(messages.getString("login.prompt"));
        String username = scanner.nextLine();

        System.out.println(messages.getString("login.password.prompt"));
        String password = scanner.nextLine();

        userService.createUser(firstName, lastName, email, phoneNumber, username, password, role);

        System.out.println(messages.getString("signin.successful"));
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

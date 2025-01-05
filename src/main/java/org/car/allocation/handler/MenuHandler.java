package org.car.allocation.handler;

import org.car.allocation.model.User;
import org.car.allocation.service.UserService;
import org.car.allocation.util.UserRole;
import org.car.allocation.service.VehicleService;
import org.car.allocation.model.Vehicle;

import java.util.*;
import java.text.MessageFormat;

public class MenuHandler {
    private final Scanner scanner;
    private final UserRole userRole;
    private final VehicleHandler vehicleHandler;
    private final UserService userService = new UserService();
    private static final ResourceBundle messages = ResourceBundle.getBundle("messages");

    public MenuHandler(Scanner scanner, UserRole role) {
        this.scanner = scanner;
        this.userRole = role;
        this.vehicleHandler = new VehicleHandler(scanner, userRole);
    }

    public void handleLogin() {
        System.out.println(messages.getString("login.prompt"));
        String username = scanner.nextLine();
        System.out.println(messages.getString("login.password.prompt"));
        String password = scanner.nextLine();

        User user = userService.findUserByUsername(username).orElse(null);

        if (user != null && user.getPassword().equals(password)) {
            System.out.println(MessageFormat.format(messages.getString("welcome.message"), username, userRole));
            showPermissions();
            showOptions();
        } else {
            System.out.println(messages.getString("sigin.user.invalid"));
        }
    }

    public void handleSignIn() {
        System.out.println(messages.getString("signin.invalid"));

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


        UserService userService = new UserService();
        UserRole role = UserRole.DRIVER;
        userService.createUser(firstName, lastName, email, phoneNumber, username, password, role);

        System.out.println("sigin.user.created.succesfully");
        handleLogin();
    }

    private void showPermissions() {
        switch (userRole) {
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

    private void showOptions() {
        boolean backToMenu = false;
        while (!backToMenu) {
            System.out.println("\n" + messages.getString("options.prompt"));

            switch (userRole) {
                case ADMIN:
                    System.out.println(messages.getString("admin.options"));
                    break;
                case MANAGER:
                    System.out.println(messages.getString("manager.options"));
                    break;
                case DRIVER:
                    System.out.println(messages.getString("driver.options"));
                    break;
            }

            int choice = scanner.nextInt();
            scanner.nextLine();

            if (userRole == UserRole.ADMIN) {
                switch (choice) {
                    case 1:
                        vehicleHandler.viewAllVehicles();
                        break;
                    case 2:
                        vehicleHandler.viewVehiclesByStatus();
                        break;
                    case 3:
                        vehicleHandler.addNewVehicle();
                        break;
                    case 4:
                        vehicleHandler.updateVehicle();
                        break;
                    case 5:
                        vehicleHandler.deleteVehicle();
                        break;
                    case 6:
                        vehicleHandler.allocateVehicle();
                        break;
                    case 7:
                        backToMenu = true;
                        break;
                    default:
                        System.out.println(messages.getString("invalid.option"));
                        break;
                }
            } else if (userRole == UserRole.MANAGER) {
                switch (choice) {
                    case 1:
                        break;
                    case 2:
                        vehicleHandler.viewAvailableVehicles();
                        break;
                    case 3:
                        vehicleHandler.viewVehiclesByStatus();
                        break;
                    case 4:
                        backToMenu = true;
                        break;
                    default:
                        System.out.println(messages.getString("invalid.option"));
                        break;
                }
            } else if (userRole == UserRole.DRIVER) {
                switch (choice) {
                    case 1:
                        vehicleHandler.viewAvailableVehicles();
                        break;
                    case 2:
                        vehicleHandler.releaseVehicle();
                        break;
                    case 3:
                        break;
                    case 4:
                        break;
                    case 5:
                        backToMenu = true;
                        break;
                    default:
                        System.out.println(messages.getString("invalid.option"));
                        break;
                }
            }
        }
    }
}

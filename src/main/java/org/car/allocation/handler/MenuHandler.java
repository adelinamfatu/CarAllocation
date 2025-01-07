package org.car.allocation.handler;

import org.car.allocation.model.User;
import org.car.allocation.service.UserService;
import org.car.allocation.util.UserRole;
import java.util.*;
import java.text.MessageFormat;

/**
 * This class handles the display and processing of menu options for different user roles.
 * It delegates actions based on the user role (ADMIN, MANAGER, DRIVER).
 */
public class MenuHandler {
    private final Scanner scanner;
    private final UserRole userRole;
    private final VehicleHandler vehicleHandler;
    private final UserHandler userHandler;
    private static final ResourceBundle messages = ResourceBundle.getBundle("messages");

    public MenuHandler(Scanner scanner, UserRole role, UserHandler userHandler) {
        this.scanner = scanner;
        this.userRole = role;
        this.vehicleHandler = new VehicleHandler(scanner, userRole);
        this.userHandler = userHandler;
    }
    /**
     * Displays the menu options available to an ADMIN user and processes the input.
     * The ADMIN can manage vehicles and users.
     *
     * @throws InputMismatchException If an invalid input type is entered.
     */
    private void showAdminOptions() {
        boolean backToMenu = false;
        while (!backToMenu) {
            System.out.println(messages.getString("admin.options"));
            int choice = scanner.nextInt();
            scanner.nextLine();

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
                    userHandler.updateUserDetails();
                    break;
                case 8:
                    userHandler.deleteUser();
                    break;
                case 9:
                    backToMenu = true;
                    break;
                default:
                    System.out.println(messages.getString("invalid.option"));
                    break;
            }
        }
    }
    /**
     * Displays the menu options available to a MANAGER user and processes the input.
     * The MANAGER can manage vehicle availability, maintenance, and allocate vehicles.
     *
     * @throws InputMismatchException If an invalid input type is entered.
     */
    private void showManagerOptions() {
        boolean backToMenu = false;
        while (!backToMenu) {
            System.out.println(messages.getString("manager.options"));
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    vehicleHandler.viewAvailableVehicles();
                    break;
                case 2:
                    vehicleHandler.viewVehiclesByStatus();
                    break;
                case 3:
                    vehicleHandler.allocateVehicle();
                    break;
                case 4:
                    System.out.println(messages.getString("enter.maintenance"));
                    String licensePlate = scanner.nextLine();
                    vehicleHandler.putVehicleInMaintenanceByLicensePlate(licensePlate,UserRole.MANAGER);
                    break;
                case 5:
                    userHandler.updateUserDetails();
                    break;
                case 6:
                    backToMenu = true;
                    break;
                default:
                    System.out.println(messages.getString("invalid.option"));
                    break;
            }
        }
    }
    /**
     * Displays the menu options available to a DRIVER user and processes the input.
     * The DRIVER can view available vehicles and release a vehicle.
     *
     * @throws InputMismatchException If an invalid input type is entered.
     */
    private void showDriverOptions() {
        boolean backToMenu = false;
        while (!backToMenu) {
            System.out.println(messages.getString("driver.options"));
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    vehicleHandler.viewAvailableVehicles();
                    break;
                case 2:
                    vehicleHandler.releaseVehicle();
                    break;
                case 3:
                    userHandler.updateUserDetails();
                    break;
                case 4:
                    backToMenu = true;
                    break;
                default:
                    System.out.println(messages.getString("invalid.option"));
                    break;
            }
        }
    }
    /**
     * Displays the menu options based on the user role.
     * Calls the appropriate method based on the role (ADMIN, MANAGER, DRIVER).
     *
     * @throws InputMismatchException If an invalid input type is entered.
     */
    public void showOptions() {
        switch (userRole) {
            case ADMIN:
                showAdminOptions();
                break;
            case MANAGER:
                showManagerOptions();
                break;
            case DRIVER:
                showDriverOptions();
                break;
        }
    }
}

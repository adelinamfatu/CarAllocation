package org.car.allocation.handler;

import org.car.allocation.abstract_factory.CarFactory;
import org.car.allocation.abstract_factory.TruckFactory;
import org.car.allocation.abstract_factory.VehicleFactory;
import org.car.allocation.model.Car;
import org.car.allocation.model.Truck;
import org.car.allocation.model.User;
import org.car.allocation.util.EngineType;
import org.car.allocation.util.UserRole;
import org.car.allocation.service.VehicleService;
import org.car.allocation.model.Vehicle;

import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.text.MessageFormat;
import java.util.stream.Collectors;

public class UniversalHandler {
    private final Scanner scanner;
    private final UserRole userRole;
    private final VehicleService<Vehicle> vehicleService = new VehicleService<>();
    private static final ResourceBundle messages = ResourceBundle.getBundle("messages");


    public UniversalHandler(Scanner scanner, UserRole role) {
        this.scanner = scanner;
        this.userRole = role;
    }

    public void handleLogin() {
        System.out.println(messages.getString("login.prompt"));
        String username = scanner.nextLine();
        User user = new User(username, userRole);

        System.out.println(MessageFormat.format(messages.getString("welcome.message"), username, userRole));
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

        showOptions();
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
                        viewAllVehicles();
                        break;
                    case 2:
                        // Implement view vehicle status logic
                        break;
                    case 3:
                        addNewVehicle();
                        break;
                    case 4:
                        deleteVehicle();
                        break;
                    case 5:
                        backToMenu = true;
                        break;
                    default:
                        System.out.println(messages.getString("invalid.option"));
                        break;
                }
            } else if (userRole == UserRole.MANAGER) {
                switch (choice) {
                    case 1:
                        // Implement reserve vehicle logic
                        break;
                    case 2:
                        viewAllVehicles();
                        break;
                    case 3:
                        // Implement view vehicle status logic
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
                        viewAllVehicles();
                        break;
                    case 2:
                        // Implement reserve vehicle logic
                        break;
                    case 3:
                        // Implement view reservation history logic
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
    }

    private void addNewVehicle() {
        System.out.println("\nWhat type of vehicle would you like to add?");
        System.out.println("1. Car");
        System.out.println("2. Truck");

        int typeChoice = scanner.nextInt();
        scanner.nextLine();

        VehicleFactory factory;
        Vehicle vehicle;

        switch (typeChoice) {
            case 1:
                System.out.println("Enter passenger capacity for the car:");
                int passengerCapacity = scanner.nextInt();
                scanner.nextLine();
                factory = new CarFactory(passengerCapacity);

                System.out.println("Enter license plate:");
                String carLicensePlate = scanner.nextLine();

                System.out.println("Enter model:");
                String carModel = scanner.nextLine();

                System.out.println("Enter fuel level:");
                double carFuelLevel = scanner.nextDouble();
                scanner.nextLine();

                System.out.println("Enter engine type (PETROL, DIESEL, ELECTRIC):");
                String carEngineType = scanner.nextLine().toUpperCase();

                vehicle = factory.createVehicle(carLicensePlate, carModel, carFuelLevel, EngineType.valueOf(carEngineType));
                vehicleService.addCar((Car) vehicle);
                System.out.println("Car added successfully!");
                break;

            case 2:
                System.out.println("Enter cargo capacity for the truck:");
                double cargoCapacity = scanner.nextDouble();
                scanner.nextLine();
                factory = new TruckFactory(cargoCapacity);

                System.out.println("Enter license plate:");
                String truckLicensePlate = scanner.nextLine();

                System.out.println("Enter model:");
                String truckModel = scanner.nextLine();

                System.out.println("Enter fuel level:");
                double truckFuelLevel = scanner.nextDouble();
                scanner.nextLine();

                System.out.println("Enter engine type (PETROL, DIESEL, ELECTRIC):");
                String truckEngineType = scanner.nextLine().toUpperCase();

                vehicle = factory.createVehicle(truckLicensePlate, truckModel, truckFuelLevel, EngineType.valueOf(truckEngineType));
                vehicleService.addTruck((Truck) vehicle);
                System.out.println("Truck added successfully!");
                break;

            default:
                System.out.println("Invalid option, returning to menu.");
                break;
        }
    }

    private void viewAllVehicles() {
        System.out.println("\nAll Vehicles:");

        // Display all cars grouped by engine type
        System.out.println("Cars:");
        List<Car> cars = vehicleService.getAllCars();
        if (cars.isEmpty()) {
            System.out.println("No cars available.");
        } else {
            Map<EngineType, List<Car>> groupedCars = cars.stream()
                    .collect(Collectors.groupingBy(Car::getEngineType));
            groupedCars.forEach((engineType, carList) -> {
                System.out.println("- Engine Type: " + engineType + ":");
                carList.forEach(car -> {
                    System.out.println("  > ID: " + car.getId() + " - License Plate: " + car.getLicensePlate() +
                            ", Model: " + car.getModel() +
                            ", Fuel Level: " + car.getFuelLevel() +
                            ", Passenger Capacity: " + car.getPassengerCapacity());
                });
            });
        }

        // Display all trucks grouped by engine type
        System.out.println("Trucks:");
        List<Truck> trucks = vehicleService.getAllTrucks();
        if (trucks.isEmpty()) {
            System.out.println("No trucks available.");
        } else {
            Map<EngineType, List<Truck>> groupedTrucks = trucks.stream()
                    .collect(Collectors.groupingBy(Truck::getEngineType));
            groupedTrucks.forEach((engineType, truckList) -> {
                System.out.println("- Engine Type: " + engineType + ":");
                truckList.forEach(truck -> {
                    System.out.println("  > ID: " + truck.getId() + " - License Plate: " + truck.getLicensePlate() +
                            ", Model: " + truck.getModel() +
                            ", Fuel Level: " + truck.getFuelLevel() +
                            ", Cargo Capacity: " + truck.getCargoCapacity());
                });
            });
        }
    }

    private void deleteVehicle() {
        viewAllVehicles();
        System.out.println("\nWhat type of vehicle would you like to DELETE?");
        System.out.println("1. Car");
        System.out.println("2. Truck");
        int typeChoice = scanner.nextInt();
        scanner.nextLine();

        if (typeChoice == 1 || typeChoice == 2) {
            System.out.println("Enter the ID of the vehicle to delete:");
            int vehicleId = scanner.nextInt();
            scanner.nextLine();

            boolean success = (typeChoice == 1) ? vehicleService.deleteCarById(vehicleId) : vehicleService.deleteTruckById(vehicleId);

            if (success) {
                System.out.println("Vehicle deleted successfully.");
                viewAllVehicles();
            } else {
                System.out.println("No vehicle found with ID: " + vehicleId + " or error occurred during deletion.");
            }
        } else {
            System.out.println("Invalid option. Please select 1 for Car or 2 for Truck.");
        }
    }

}


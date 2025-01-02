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
import java.util.Scanner;

public class UniversalHandler {
    private final Scanner scanner;
    private final UserRole userRole;
    private final VehicleService<Vehicle> vehicleService = new VehicleService<>();

    public UniversalHandler(Scanner scanner, UserRole role) {
        this.scanner = scanner;
        this.userRole = role;
    }

    public void handleLogin() {
        System.out.println("\nPlease enter your username:");
        String username = scanner.nextLine();
        User user = new User(username, userRole);

        System.out.println("Welcome " + username + "! You are logged in as a " + userRole + ".");
        if (userRole == UserRole.DRIVER) {
            System.out.println("You can view available vehicles and make reservations.");
        } else if (userRole == UserRole.MANAGER) {
            System.out.println("You can reserve vehicles, view available vehicles, and view vehicle status.");
        } else if (userRole == UserRole.ADMIN) {
            System.out.println("You have full access to all operations: view, add, and delete vehicles.");
        }

        showOptions();
    }

    private void showOptions() {
        boolean backToMenu = false;
        while (!backToMenu) {
            System.out.println("\nWhat would you like to do?");
            if (userRole == UserRole.ADMIN || userRole == UserRole.MANAGER) {
                System.out.println("1. Add a new vehicle");
                System.out.println("2. Delete a vehicle");
            }
            System.out.println("3. View all vehicles");
            System.out.println("4. Reserve a vehicle");
            System.out.println("5. Go back to main menu");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    if (userRole == UserRole.ADMIN || userRole == UserRole.MANAGER) {
                        System.out.println("You chose option 1: Add a new vehicle.");
                        addNewVehicle();
                    }
                    break;
                case 2:
                    if (userRole == UserRole.ADMIN || userRole == UserRole.MANAGER) {
                        System.out.println("You chose option 2: Delete a vehicle.");
                        // Implement delete vehicle logic here
                    }
                    break;
                case 3:
                    System.out.println("You chose option 3: View all vehicles.");
                    viewAllVehicles();
                    break;
                case 4:
                    System.out.println("You chose option 4: Reserve a vehicle.");
                    // Implement reserve vehicle logic here
                    break;
                case 5:
                    backToMenu = true;
                    break;
                default:
                    System.out.println("Invalid option, please try again.");
                    break;
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
        System.out.println("\nCars:");
        List<Car> cars = vehicleService.getAllCars();
        for (Car car : cars) {
            System.out.println("- License Plate: " + car.getLicensePlate() +
                    ", Model: " + car.getModel() +
                    ", Fuel Level: " + car.getFuelLevel() +
                    ", Passenger Capacity: " + car.getPassengerCapacity() +
                    ", Engine Type: " + car.getEngineType());
        }
    }
}


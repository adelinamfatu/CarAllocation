package org.car.allocation.handler;

import org.car.allocation.abstract_factory.CarFactory;
import org.car.allocation.abstract_factory.TruckFactory;
import org.car.allocation.model.Car;
import org.car.allocation.model.Truck;
import org.car.allocation.model.User;
import org.car.allocation.util.EngineType;
import org.car.allocation.util.UserRole;
import org.car.allocation.service.VehicleService;
import org.car.allocation.model.Vehicle;

import java.util.*;
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
                        break;
                    case 3:
                        addNewVehicle();
                        break;
                    case 4:
                        updateVehicle();
                        break;
                    case 5:
                        deleteVehicle();
                        break;
                    case 6:
                        allocateVehicle();
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
                        viewAllVehicles();
                        break;
                    case 3:
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
                        break;
                    case 3:
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

        // Common vehicle properties
        System.out.println("Enter license plate:");
        String licensePlate = scanner.nextLine();

        System.out.println("Enter model:");
        String model = scanner.nextLine();

        System.out.println("Enter fuel level:");
        double fuelLevel = scanner.nextDouble();
        scanner.nextLine();

        System.out.println("Enter max speed:");
        double maxSpeed = scanner.nextDouble();
        scanner.nextLine();

        System.out.println("Enter engine type (PETROL, DIESEL, ELECTRIC, HYBRID):");
        String engineType = scanner.nextLine().toUpperCase();

        Vehicle vehicle;

        switch (typeChoice) {
            case 1: //Car
                System.out.println("Enter passenger capacity for the car:");
                int passengerCapacity = scanner.nextInt();
                scanner.nextLine();

                System.out.println("Enter comfort level for the car:");
                int comfortLevel = scanner.nextInt();
                scanner.nextLine();

                CarFactory carFactory = new CarFactory(passengerCapacity, comfortLevel);
                vehicle = carFactory.createVehicle(licensePlate, model, fuelLevel, maxSpeed, EngineType.valueOf(engineType));
                vehicleService.addCar((Car) vehicle);
                System.out.println("Car added successfully!");
                break;

            case 2: //Truck
                System.out.println("Enter cargo capacity for the truck:");
                double cargoCapacity = scanner.nextDouble();
                scanner.nextLine();

                TruckFactory truckFactory = new TruckFactory(cargoCapacity);
                vehicle = truckFactory.createVehicle(licensePlate, model, fuelLevel, maxSpeed, EngineType.valueOf(engineType));
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

        //Display all cars grouped by engine type
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

        //Display all trucks grouped by engine type
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

    private void updateVehicle() {
        System.out.println("\nWhat type of vehicle would you like to UPDATE?");
        System.out.println("1. Car");
        System.out.println("2. Truck");
        int typeChoice = scanner.nextInt();
        scanner.nextLine();

        if (typeChoice != 1 && typeChoice != 2) {
            System.out.println("Invalid option. Please select 1 for Car or 2 for Truck.");
            return;
        }

        System.out.println("Enter the ID of the vehicle to update:");
        int vehicleId;
        try {
            vehicleId = scanner.nextInt();
            scanner.nextLine();
        } catch (Exception e) {
            System.out.println("Invalid input for ID. Please enter a valid integer.");
            scanner.next(); //Clear scanner buffer
            return;
        }

        try {
            Optional<? extends Vehicle> vehicleOptional = (typeChoice == 1) ?
                    vehicleService.findCarById(vehicleId) :
                    vehicleService.findTruckById(vehicleId);

            if (!vehicleOptional.isPresent()) {
                System.out.println("No vehicle found with ID: " + vehicleId);
                return;
            }

            Vehicle vehicle = vehicleOptional.get();

            System.out.println("Select the property to update:");
            String[] properties = (vehicle instanceof Car) ?
                    new String[]{"License Plate", "Model", "Fuel Level", "Engine Type", "Passenger Capacity"} :
                    new String[]{"License Plate", "Model", "Fuel Level", "Engine Type", "Cargo Capacity"};

            for (int i = 0; i < properties.length; i++) {
                System.out.printf("%d. %s\n", (i + 1), properties[i]);
            }

            int propertyChoice = scanner.nextInt();
            scanner.nextLine();

            if (propertyChoice < 1 || propertyChoice > properties.length) {
                System.out.println("Invalid property choice.");
                return;
            }

            System.out.println("Enter new value for " + properties[propertyChoice - 1] + ":");
            String newValue = scanner.nextLine();

            //Apply updates based on choice
            updateVehicleProperty(vehicle, propertyChoice, newValue);

            if (vehicle instanceof Car) {
                vehicleService.updateCar((Car) vehicle);
            } else if (vehicle instanceof Truck) {
                vehicleService.updateTruck((Truck) vehicle);
            }

            System.out.println("Vehicle updated successfully:\n" + vehicle);
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void updateVehicleProperty(Vehicle vehicle, int propertyChoice, String newValue) {
        switch (propertyChoice) {
            case 1:
                vehicle.setLicensePlate(newValue);
                break;
            case 2:
                vehicle.setModel(newValue);
                break;
            case 3:
                vehicle.setFuelLevel(Double.parseDouble(newValue));
                break;
            case 4:
                vehicle.setEngineType(EngineType.valueOf(newValue.toUpperCase()));
                break;
            case 5:
                if (vehicle instanceof Car) {
                    ((Car)vehicle).setPassengerCapacity(Integer.parseInt(newValue));
                } else if (vehicle instanceof Truck) {
                    ((Truck)vehicle).setCargoCapacity(Double.parseDouble(newValue));
                }
                break;
        }
    }

    private void allocateVehicle() {
        Vehicle vehicle = vehicleService.allocateVehicle();
        if (vehicle != null) {
            System.out.println("Allocated Vehicle: " + vehicle);
        } else {
            System.out.println("No vehicle available that fits the criteria.");
        }
    }
}

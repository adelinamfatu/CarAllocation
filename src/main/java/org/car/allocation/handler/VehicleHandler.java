package org.car.allocation.handler;

import org.car.allocation.abstract_factory.CarFactory;
import org.car.allocation.abstract_factory.TruckFactory;
import org.car.allocation.model.Car;
import org.car.allocation.model.Truck;
import org.car.allocation.model.Vehicle;
import org.car.allocation.service.VehicleService;
import org.car.allocation.util.EngineType;
import org.car.allocation.util.UserRole;
import org.car.allocation.util.VehicleStatus;

import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collectors;

public class VehicleHandler {
    private final Scanner scanner;
    private UserRole userRole;
    private final VehicleService<Vehicle> vehicleService = new VehicleService<>();
    private static final ResourceBundle messages = ResourceBundle.getBundle("messages");

    public VehicleHandler(Scanner scanner, UserRole role) {
        this.scanner = scanner;
        this.userRole = role;
    }

    public void addNewVehicle() {
        System.out.println("Enter vehicle type (1. Car, 2. Truck):");
        int typeChoice = scanner.nextInt();
        scanner.nextLine();

        if (typeChoice != 1 && typeChoice != 2) {
            System.out.println("Invalid choice. Returning to menu.");
            return;
        }

        //Common vehicle properties
        System.out.println("Enter License Plate:");
        String licensePlate = scanner.nextLine();

        System.out.println("Enter Model:");
        String model = scanner.nextLine();

        System.out.println("Enter Fuel Level:");
        double fuelLevel = scanner.nextDouble();
        scanner.nextLine();

        System.out.println("Enter Max Speed:");
        double maxSpeed = scanner.nextDouble();
        scanner.nextLine();

        System.out.println("Enter Engine Type (e.g., DIESEL, ELECTRIC):");
        String engineType = scanner.nextLine().toUpperCase();

        System.out.println("Enter Mileage:");
        double mileage = scanner.nextDouble();
        scanner.nextLine();

        Vehicle vehicle;

        switch (typeChoice) {
            case 1: //Car
                System.out.println("Enter Passenger Capacity:");
                int passengerCapacity = scanner.nextInt();
                scanner.nextLine();

                System.out.println("Enter Comfort Level:");
                int comfortLevel = scanner.nextInt();
                scanner.nextLine();

                CarFactory carFactory = new CarFactory(passengerCapacity, comfortLevel);
                vehicle = carFactory.createVehicle(licensePlate, model, fuelLevel, maxSpeed, EngineType.valueOf(engineType), mileage);
                vehicleService.addCar((Car) vehicle);
                System.out.println("Car added successfully.");
                break;

            case 2: //Truck
                System.out.println("Enter Cargo Capacity:");
                double cargoCapacity = scanner.nextDouble();
                scanner.nextLine();

                System.out.println("Does the truck have a refrigeration unit? (yes/no):");
                boolean hasRefrigerationUnit = scanner.nextLine().trim().equalsIgnoreCase("yes");

                TruckFactory truckFactory = new TruckFactory(cargoCapacity, hasRefrigerationUnit);
                vehicle = truckFactory.createVehicle(licensePlate, model, fuelLevel, maxSpeed, EngineType.valueOf(engineType), mileage);
                vehicleService.addTruck((Truck) vehicle);
                System.out.println("Truck added successfully.");
                break;
        }
    }

    public void viewAllVehicles() {
        System.out.println("All Vehicles:");

        //Display all cars grouped by engine type
        List<Car> cars = vehicleService.getAllCars();
        if (cars.isEmpty()) {
            System.out.println("No cars available.");
        } else {
            Map<EngineType, List<Car>> groupedCars = cars.stream()
                    .collect(Collectors.groupingBy(Car::getEngineType));
            groupedCars.forEach((engineType, carList) -> {
                System.out.println("- Engine Type: " + engineType + ":");
                carList.forEach(car -> System.out.println(car));
            });
        }

        //Display all trucks grouped by engine type
        List<Truck> trucks = vehicleService.getAllTrucks();
        if (trucks.isEmpty()) {
            System.out.println("No trucks available.");
        } else {
            Map<EngineType, List<Truck>> groupedTrucks = trucks.stream()
                    .collect(Collectors.groupingBy(Truck::getEngineType));
            groupedTrucks.forEach((engineType, truckList) -> {
                System.out.println("- Engine Type: " + engineType + ":");
                truckList.forEach(truck -> System.out.println(truck));
            });
        }
    }

    public void viewVehiclesByStatus() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Introduce the desired status: (AVAILABLE, IN_USE, IN_MAINTENANCE, RESERVED):");
        String statusInput = scanner.nextLine().trim().toUpperCase();

        try {
            VehicleStatus status = VehicleStatus.valueOf(statusInput);
            List<Vehicle> vehicles = vehicleService.getVehiclesByStatus(status);
            if (vehicles.isEmpty()) {
                System.out.println("There are no vehicles with status: " + status);
            } else {
                System.out.println("Vehicles with status " + status + ":");
                vehicles.forEach(vehicle -> System.out.println(vehicle));
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid status. Please reintroduce the correct status.");
        }
    }

    public void viewAvailableVehicles() {
        List<Vehicle> availableVehicles = vehicleService.getAvailableVehicles();
        if (availableVehicles.isEmpty()) {
            System.out.println("No available vehicles at the moment.");
        } else {
            System.out.println("Available Vehicles:");
            for (Vehicle vehicle : availableVehicles) {
                System.out.println(vehicle);
            }
        }
    }

    public void deleteVehicle() {
        System.out.println("Enter vehicle type to delete (1. Car, 2. Truck):");
        int typeChoice = scanner.nextInt();
        scanner.nextLine();

        if (typeChoice == 1 || typeChoice == 2) {
            System.out.println("Enter Vehicle ID to delete:");
            int vehicleId = scanner.nextInt();
            scanner.nextLine();

            boolean success = (typeChoice == 1) ? vehicleService.deleteCarById(vehicleId) : vehicleService.deleteTruckById(vehicleId);

            if (success) {
                System.out.println("Vehicle deleted successfully.");
            } else {
                System.out.println("No vehicle found with ID: " + vehicleId);
            }
        } else {
            System.out.println("Invalid option.");
        }
    }

    public void updateVehicle() {
        System.out.println(messages.getString("vehicle.update.prompt"));
        System.out.println(messages.getString("vehicle.type.prompt"));

        int typeChoice = scanner.nextInt();
        scanner.nextLine();

        if (typeChoice != 1 && typeChoice != 2) {
            System.out.println(messages.getString("invalid.option"));
            return;
        }

        System.out.println(messages.getString("vehicle.id.prompt.update"));
        int vehicleId;
        try {
            vehicleId = scanner.nextInt();
            scanner.nextLine();
        } catch (Exception e) {
            System.out.println(messages.getString("invalid.option"));
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

            System.out.println(messages.getString("vehicle.property"));
            String[] properties = (vehicle instanceof Car) ?
                    new String[]{"License Plate", "Model", "Fuel Level", "Engine Type", "Passenger Capacity", "Mileage"} :
                    new String[]{"License Plate", "Model", "Fuel Level", "Engine Type", "Cargo Capacity", "Mileage"};

            for (int i = 0; i < properties.length; i++) {
                System.out.printf("%d. %s\n", (i + 1), properties[i]);
            }

            int propertyChoice = scanner.nextInt();
            scanner.nextLine();

            if (propertyChoice < 1 || propertyChoice > properties.length) {
                System.out.println(messages.getString("invalid.property"));
                return;
            }

            String property = properties[propertyChoice - 1];
            System.out.println(MessageFormat.format(messages.getString("new.value.prompt"), property));
            String newValue = scanner.nextLine();

            //Apply updates based on choice
            if (propertyChoice == properties.length) { //Last property is Mileage
                vehicle.setMileage(Double.parseDouble(newValue));
            } else {
                updateVehicleProperty(vehicle, propertyChoice, newValue);
            }

            if (vehicle instanceof Car) {
                vehicleService.updateCar((Car) vehicle);
            } else if (vehicle instanceof Truck) {
                vehicleService.updateTruck((Truck) vehicle);
            }

            System.out.println(messages.getString("vehicle.updated") + vehicle);
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

    public void allocateVehicle() {
        Vehicle vehicle = vehicleService.allocateVehicle();
        if (vehicle != null) {
            System.out.println("Allocated Vehicle: " + vehicle);
        } else {
            System.out.println("No vehicle available that fits the criteria.");
        }
    }

    public void releaseVehicle() {
        if (userRole != UserRole.DRIVER) {
            System.out.println(messages.getString("invalid.role"));
            return;
        }

        System.out.println(messages.getString("release.vehicle.prompt"));
        System.out.println(messages.getString("vehicle.type.prompt"));

        int choice = scanner.nextInt();
        scanner.nextLine();

        if (choice != 1 && choice != 2) {
            System.out.println(messages.getString("invalid.option"));
            return;
        }

        viewAllVehicles();
        System.out.println(messages.getString("vehicle.id.prompt.release"));
        int vehicleId = scanner.nextInt();
        scanner.nextLine();

        Optional<? extends Vehicle> vehicleOptional;
        if (choice == 1) {
            vehicleOptional = vehicleService.findCarById(vehicleId);
        } else {
            vehicleOptional = vehicleService.findTruckById(vehicleId);
        }

        if (!vehicleOptional.isPresent() || vehicleOptional.get().getVehicleStatus() != VehicleStatus.IN_USE) {
            System.out.println(messages.getString("vehicle.no.use"));
            return;
        }

        Vehicle vehicle = vehicleOptional.get();
        System.out.println("Current mileage: " + vehicle.getMileage());
        System.out.println(messages.getString("enter.new.mileage"));
        double newMileage = scanner.nextDouble();
        scanner.nextLine();

        if (newMileage < vehicle.getMileage()) {
            System.out.println(messages.getString("invalid.mileage"));
            return;
        }

        vehicle.setMileage(newMileage);
        vehicle.setVehicleStatus(VehicleStatus.AVAILABLE); //Change status to available
        System.out.println(messages.getString("vehicle.released") + newMileage);
    }
}

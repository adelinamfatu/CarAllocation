package org.car.allocation.handler;

import org.car.allocation.abstract_factory.CarFactory;
import org.car.allocation.abstract_factory.TruckFactory;
import org.car.allocation.model.Car;
import org.car.allocation.model.Truck;
import org.car.allocation.model.Vehicle;
import org.car.allocation.service.UserService;
import org.car.allocation.service.VehicleService;
import org.car.allocation.util.*;

import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * The VehicleHandler class does all the necessary operations on vehicles: adding, deleting, updating.
 */
public class VehicleHandler {
    private final Scanner scanner;
    private UserRole userRole;
    private final VehicleService<Vehicle> vehicleService = new VehicleService<>();
    private final UserService userService = new UserService();
    private static final ResourceBundle messages = ResourceBundle.getBundle("messages");

    public VehicleHandler(Scanner scanner, UserRole role) {
        this.scanner = scanner;
        this.userRole = role;
    }

    public void addNewVehicle() {
        System.out.println(messages.getString("add.vehicle.prompt"));
        System.out.println(messages.getString("vehicle.type.prompt"));

        int typeChoice = scanner.nextInt();
        scanner.nextLine();

        if (typeChoice != 1 && typeChoice != 2) {
            System.out.println(messages.getString("returning.menu"));
            return; //Exit the method to stop further processing
        }

        //Common vehicle properties
        System.out.println(messages.getString("enter.license"));
        String licensePlate = scanner.nextLine();

        System.out.println(messages.getString("enter.model"));
        String model = scanner.nextLine();

        System.out.println(messages.getString("enter.fuel.level"));
        double fuelLevel = scanner.nextDouble();
        scanner.nextLine();

        System.out.println(messages.getString("enter.max.speed"));
        double maxSpeed = scanner.nextDouble();
        scanner.nextLine();

        System.out.println(messages.getString("enter.engine.type"));
        String engineType = scanner.nextLine().toUpperCase();

        System.out.println(messages.getString("enter.mileage"));
        double mileage = scanner.nextDouble();
        scanner.nextLine();

        Vehicle vehicle;

        switch (typeChoice) {
            case 1: //Car
                System.out.println(messages.getString("enter.passenger.capacity"));
                int passengerCapacity = scanner.nextInt();
                scanner.nextLine();

                System.out.println(messages.getString("enter.comfort.level"));
                int comfortLevel = scanner.nextInt();
                scanner.nextLine();

                CarFactory carFactory = new CarFactory(passengerCapacity, comfortLevel);
                vehicle = carFactory.createVehicle(licensePlate, model, fuelLevel, maxSpeed, EngineType.valueOf(engineType), mileage);
                vehicleService.addCar((Car) vehicle);
                System.out.println(messages.getString("car.added.successfully"));
                break;

            case 2: //Truck
                System.out.println(messages.getString("enter.cargo.capacity"));
                double cargoCapacity = scanner.nextDouble();
                scanner.nextLine();

                System.out.println(messages.getString("enter.hasRefrigerationUnit"));
                String input = scanner.nextLine().trim().toLowerCase();
                boolean hasRefrigerationUnit;

                if (input.equals("yes") || input.equals("da")) {
                    hasRefrigerationUnit = true;
                } else if (input.equals("no") || input.equals("nu")) {
                    hasRefrigerationUnit = false;
                } else {
                    System.out.println(messages.getString("invalid.input"));
                    hasRefrigerationUnit = false;
                }

                TruckFactory truckFactory = new TruckFactory(cargoCapacity,hasRefrigerationUnit);
                vehicle = truckFactory.createVehicle(licensePlate, model, fuelLevel, maxSpeed, EngineType.valueOf(engineType), mileage);
                vehicleService.addTruck((Truck) vehicle);
                System.out.println(messages.getString("truck.added.successfully"));
                break;
        }

        pause();
    }

    public void viewAllVehicles() {
        System.out.println(messages.getString("all.vehicles"));

        //Display all cars grouped by engine type
        System.out.println(messages.getString("cars.section"));
        List<Car> cars = vehicleService.getAllCars();
        if (cars.isEmpty()) {
            System.out.println(messages.getString("no.cars.available"));
        } else {
            Map<EngineType, List<Car>> groupedCars = cars.stream()
                    .collect(Collectors.groupingBy(Car::getEngineType));
            groupedCars.forEach((engineType, carList) -> {
                System.out.println("- Engine Type: " + engineType + ":");
                carList.forEach(car -> {
                    System.out.println("  > ID: " + car.getId() + " - License Plate: " + car.getLicensePlate() +
                            ", Model: " + car.getModel() +
                            ", Fuel Level: " + car.getFuelLevel() +
                            ", Passenger Capacity: " + car.getPassengerCapacity() +
                            ", Mileage: " + car.getMileage() +
                            ", Status: " + car.getVehicleStatus());
                });
            });
        }

        //Display all trucks grouped by engine type
        System.out.println(messages.getString("trucks.section"));
        List<Truck> trucks = vehicleService.getAllTrucks();
        if (trucks.isEmpty()) {
            System.out.println(messages.getString("no.trucks.available"));
        } else {
            Map<EngineType, List<Truck>> groupedTrucks = trucks.stream()
                    .collect(Collectors.groupingBy(Truck::getEngineType));
            groupedTrucks.forEach((engineType, truckList) -> {
                System.out.println("- Engine Type: " + engineType + ":");
                truckList.forEach(truck -> {
                    System.out.println("  > ID: " + truck.getId() + " - License Plate: " + truck.getLicensePlate() +
                            ", Model: " + truck.getModel() +
                            ", Fuel Level: " + truck.getFuelLevel() +
                            ", Cargo Capacity: " + truck.getCargoCapacity() +
                            ", Mileage: " + truck.getMileage() +
                            ", Has refrigeration unit: " + truck.hasRefrigerationUnit() +
                            ", Status: " + truck.getVehicleStatus());
                });
            });
        }

        pause();
    }

    public void viewVehiclesByStatus() {
        Scanner scanner = new Scanner(System.in);
        System.out.println(messages.getString("vehicle.status.enter"));
        String statusInput = scanner.nextLine().trim().toUpperCase();

        try {
            VehicleStatus status = VehicleStatus.valueOf(statusInput);
            List<Vehicle> vehicles = vehicleService.getVehiclesByStatus(status);
            if (vehicles.isEmpty()) {
                System.out.println(messages.getString("no.vehicles.status") + status);
            } else {
                System.out.println("Vehicles with status " + status + ":");
                vehicles.forEach(vehicle -> System.out.println(vehicle));
            }
        } catch (IllegalArgumentException e) {
            System.out.println(messages.getString("invalid.status"));
        }

        pause();
    }

    public void viewAvailableVehicles() {
        List<Vehicle> availableVehicles = vehicleService.getAvailableVehicles();
        if (availableVehicles.isEmpty()) {
            System.out.println(messages.getString("no.vehicles.available"));
        } else {
            System.out.println(messages.getString("available.vehicles"));
            for (Vehicle vehicle : availableVehicles) {
                System.out.println(vehicle);
            }
        }

        pause();
    }

    public void deleteVehicle() {
        viewAllVehicles();
        System.out.println(messages.getString("delete.vehicle.prompt"));
        System.out.println(messages.getString("vehicle.type.prompt"));

        int typeChoice = scanner.nextInt();
        scanner.nextLine();

        if (typeChoice == 1 || typeChoice == 2) {
            System.out.println(messages.getString("vehicle.id.prompt.delete"));
            int vehicleId = scanner.nextInt();
            scanner.nextLine();

            boolean success = (typeChoice == 1) ? vehicleService.deleteCarById(vehicleId) : vehicleService.deleteTruckById(vehicleId);

            if (success) {
                System.out.println(messages.getString("vehicle.deleted.successfully"));
                viewAllVehicles();
            } else {
                System.out.println("No vehicle found with ID: " + vehicleId + " or error occurred during deletion.");
            }
        } else {
            System.out.println(messages.getString("invalid.option"));
        }

        pause();
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
            scanner.next();
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
                    new String[]{"License Plate", "Model", "Fuel Level", "Engine Type", "Passenger Capacity", "Mileage", "Put in Maintenance"} :
                    new String[]{"License Plate", "Model", "Fuel Level", "Engine Type", "Cargo Capacity", "Mileage", "Put in Maintenance"};

            for (int i = 0; i < properties.length; i++) {
                System.out.printf("%d. %s\n", (i + 1), properties[i]);
            }

            int propertyChoice = scanner.nextInt();
            scanner.nextLine();

            if (propertyChoice < 1 || propertyChoice > properties.length) {
                System.out.println(messages.getString("invalid.property"));
                return;
            }

            if (propertyChoice == properties.length) {
                System.out.println(messages.getString("maintenance.confirm"));
                String confirmation = scanner.nextLine().trim().toLowerCase();

                if ("yes".equals(confirmation)) {
                    putVehicleInMaintenanceByLicensePlate(vehicle.getLicensePlate(), userRole);
                }
            } else {
                String property = properties[propertyChoice - 1];
                System.out.println(MessageFormat.format(messages.getString("new.value.prompt"), property));
                String newValue = scanner.nextLine();

                if (propertyChoice == properties.length - 1) {
                    putVehicleInMaintenanceByLicensePlate(vehicle.getLicensePlate(), userRole);
                } else if (propertyChoice == properties.length) {
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
            }
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
            e.printStackTrace();
        }

        pause();
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
            System.out.println(messages.getString("vehicle.allocate") + vehicle);
        } else {
            System.out.println(messages.getString("vehicle.allocate.error"));
        }

        pause();
    }

    public void releaseVehicle() {
        var loggedInUser = LoggedInUserContext.getLoggedInUser();
        if (userRole != UserRole.DRIVER) {
            System.out.println(messages.getString("invalid.role"));
            return;
        }

        //Check if the user has a vehicle assigned (either car or truck)
        Vehicle vehicleToRelease = null;
        if (loggedInUser.getCar() != null) {
            vehicleToRelease = loggedInUser.getCar();
            loggedInUser.setCar(null); //Remove the car assignment
        } else if (loggedInUser.getTruck() != null) {
            vehicleToRelease = loggedInUser.getTruck();
            loggedInUser.setTruck(null); //Remove the truck assignment
        }

        if (vehicleToRelease == null) {
            System.out.println(messages.getString("vehicle.no.assignment"));
            return;
        }

        //Proceed with releasing the vehicle (same as before)
        System.out.println(messages.getString("release.vehicle.prompt"));
        String input = scanner.nextLine().trim().toLowerCase();

        if (input.equals("yes") || input.equals("da")) {
            Optional<? extends Vehicle> vehicleOptional = Optional.empty();
            if (vehicleToRelease instanceof Car) {
                vehicleOptional = vehicleService.findCarById(vehicleToRelease.getId());
            } else if (vehicleToRelease instanceof Truck) {
                vehicleOptional = vehicleService.findTruckById(vehicleToRelease.getId());
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
            vehicleService.updateVehicle(vehicle);

            //Update the logged-in user in the database to reflect that the vehicle was released
            userService.updateUser(loggedInUser);

            System.out.println(messages.getString("vehicle.released") + newMileage);
        } else {
            System.out.println(messages.getString("release.cancelled"));
        }

        pause();
    }

    private void pause() {
        try {
            Thread.sleep(3000); //3 seconds
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            System.out.println(messages.getString("thread.error"));
        }
    }

    public void putVehicleInMaintenanceByLicensePlate(String licensePlate, UserRole userRole) {
        List<Vehicle> allVehicles = vehicleService.getAllVehicles();
        Optional<Vehicle> vehicleOpt = allVehicles.stream()
                .filter(vehicle -> vehicle.getLicensePlate().equalsIgnoreCase(licensePlate))
                .findFirst();

        if (vehicleOpt.isPresent()) {
            Vehicle vehicle = vehicleOpt.get();

            if (PermissionManager.isActionAllowed(userRole, vehicle.getVehicleStatus(), "PUT_IN_MAINTENANCE")) {
                vehicle.putInMaintenance();
                vehicleService.updateVehicle(vehicle);
            } else {
                System.out.println(messages.getString("maintenance.permission.invalid"));
            }
        } else {
            System.out.println("Vehicle with license plate " + licensePlate + " was not found.");
        }
    }
}

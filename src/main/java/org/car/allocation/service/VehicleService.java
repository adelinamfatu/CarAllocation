package org.car.allocation.service;
import org.car.allocation.handler.VehicleAllocationHandler;
import org.car.allocation.model.User;
import org.car.allocation.specification.OperationableSpecification;
import org.car.allocation.specification.Specification;
import org.car.allocation.specification.VehicleStatusSpecification;
import org.car.allocation.strategy.*;

import org.car.allocation.model.Car;
import org.car.allocation.model.Truck;
import org.car.allocation.model.Vehicle;
import org.car.allocation.repository.VehicleRepository;
import org.car.allocation.util.PermissionManager;
import org.car.allocation.util.UserRole;
import org.car.allocation.util.VehicleStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

public class VehicleService<T extends Vehicle> {
    private final VehicleRepository<Car> carRepository = new VehicleRepository<>(Car.class);
    private final VehicleRepository<Truck> truckRepository = new VehicleRepository<>(Truck.class);
    private final VehicleRepository<Vehicle> vehicleRepository = new VehicleRepository<>(Vehicle.class);

    private final UserService userService;
    public VehicleService() {
        this.userService = new UserService();
    }

    //Utility method to gather all vehicles from the repository
    public List<Vehicle> getAllVehicles() {
        List<Vehicle> vehicles = new ArrayList<>();
        vehicles.addAll(carRepository.findAll());
        vehicles.addAll(truckRepository.findAll());
        return vehicles;
    }

    //Get all available vehicles
    public List<Vehicle> getAvailableVehicles() {
        Specification<Vehicle> specification = new VehicleStatusSpecification(VehicleStatus.AVAILABLE);

        List<Vehicle> allVehicles = getAllVehicles();
        return allVehicles.stream()
                .filter(specification::isSatisfiedBy)
                .collect(Collectors.toList());
    }

    public List<Vehicle> getVehiclesByStatus(VehicleStatus status) {
        Specification<Vehicle> specification = new VehicleStatusSpecification(status);

        List<Vehicle> allVehicles = getAllVehicles();
        return allVehicles.stream()
                .filter(specification::isSatisfiedBy)
                .collect(Collectors.toList());
    }

    public void addCar(Car car) {
        carRepository.save(car);
    }

    public void addTruck(Truck truck) {
        truckRepository.save(truck);
    }

    public Optional<Car> findCarById(int id) {
        return carRepository.findById(id);
    }

    public Optional<Truck> findTruckById(int id) {
        return truckRepository.findById(id);
    }

    public List<Car> getAllCars() {
        return carRepository.findAll();
    }

    public List<Truck> getAllTrucks() { return truckRepository.findAll(); }

    public boolean deleteCarById(int id) {
        Optional<Car> car = carRepository.findById(id);
        if (car.isPresent()) {
            carRepository.delete(car.get());
            return true;
        }
        return false;
    }

    public boolean deleteTruckById(int id) {
        Optional<Truck> truck = truckRepository.findById(id);
        if (truck.isPresent()) {
            truckRepository.delete(truck.get());
            return true;
        }
        return false;
    }

    public void updateCar(Car car) { carRepository.update(car); }

    public void updateTruck(Truck truck) { truckRepository.update(truck); }
    public void updateVehicle(Vehicle vehicle) { vehicleRepository.update(vehicle); }

    public Vehicle allocateVehicle() {
        //Gather all vehicles
        List<Vehicle> allVehicles = getAllVehicles();

        //Create the specification for operational vehicles (e.g., minimum fuel level)
        Specification<Vehicle> operationalSpec = new OperationableSpecification(50.0);

        // Initialize the allocation handler with the chosen specification (filter) and the appropriate strategy
        VehicleAllocationHandler allocationHandler = new VehicleAllocationHandler(selectStrategy(allVehicles), operationalSpec);

        //Use the handler to filter and allocate the vehicle
        Vehicle allocatedVehicle = allocationHandler.allocateVehicle(allVehicles);

        if (allocatedVehicle == null) {
            System.out.println("No vehicle could be allocated with the selected strategies.");
            return null;
        }

        // Check if there is an available driver
        Optional<User> availableDriver = userService.findAvailableDriver();
        if (!availableDriver.isPresent()) {
            System.out.println("No available driver to allocate to vehicle.");
            return null;
        }

        // If a driver is available, proceed to allocate the vehicle and update statuses
        userService.allocateVehicleToDriver(availableDriver.get(), allocatedVehicle);
        allocatedVehicle.setVehicleStatus(VehicleStatus.IN_USE);
        updateVehicle(allocatedVehicle);

        System.out.println("Vehicle with ID " + allocatedVehicle.getId() + " has been allocated to driver " + availableDriver.get().getUsername() + " (" + availableDriver.get().getFirstName() + " " + availableDriver.get().getLastName() + ")");
        return allocatedVehicle;

    }

    private AllocationStrategy selectStrategy(List<Vehicle> availableVehicles) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Do you need to transport cargo? (yes/no)");
        String cargoResponse = scanner.nextLine().trim().toLowerCase();

        if ("yes".equals(cargoResponse)) {
            return handleCargoRequest(scanner);
        } else {
            return handlePassengerRequest(scanner);
        }
    }

    private AllocationStrategy handleCargoRequest(Scanner scanner) {
        System.out.println("Do you need a refrigerated truck? (yes/no)");
        String refrigerationResponse = scanner.nextLine().trim().toLowerCase();

        if ("yes".equals(refrigerationResponse)) {
            return new RefrigerationStrategy();
        } else {
            System.out.println("Enter the minimum cargo capacity required (or -1 for no specific requirement):");
            double minCargoCapacity = scanner.nextDouble();
            if (minCargoCapacity > 0) {
                return new CargoPriorityStrategy(minCargoCapacity);
            } else {
                return new NonRefrigeratedHighSpeedStrategy();
            }
        }
    }

    private AllocationStrategy handlePassengerRequest(Scanner scanner) {
        System.out.println("Are you looking for a comfortable car for passengers? (yes/no)");
        String comfortResponse = scanner.nextLine().trim().toLowerCase();

        if ("yes".equals(comfortResponse)) {
            System.out.println("Enter the minimum passenger capacity required:");
            int minPassengerCapacity = scanner.nextInt();
            return new ComfortPriorityStrategy(minPassengerCapacity);
        } else {
            return new FuelEfficientStrategy();
        }
    }

}

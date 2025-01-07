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
import org.car.allocation.util.VehicleStatus;

import java.util.*;
import java.util.stream.Collectors;
/**
 * Service layer responsible for managing vehicles (Cars and Trucks) and their allocation to drivers.
 * Handles vehicle status, allocation strategies, and vehicle updates.
 * @param <T> the type of vehicle (Car or Truck).
 */
public class VehicleService<T extends Vehicle> {
    private final VehicleRepository<Car> carRepository = new VehicleRepository<>(Car.class);
    private final VehicleRepository<Truck> truckRepository = new VehicleRepository<>(Truck.class);
    private final VehicleRepository<Vehicle> vehicleRepository = new VehicleRepository<>(Vehicle.class);
    private static final ResourceBundle messages = ResourceBundle.getBundle("messages");

    private final UserService userService;
    public VehicleService() {
        this.userService = new UserService();
    }

    /**
     * Retrieves a list of all vehicles (both Cars and Trucks).
     *
     * @return a list of all vehicles.
     */
    public List<Vehicle> getAllVehicles() {
        List<Vehicle> vehicles = new ArrayList<>();
        vehicles.addAll(carRepository.findAll());
        vehicles.addAll(truckRepository.findAll());
        return vehicles;
    }

    /**
     * Retrieves a list of vehicles that are available for use.
     * Filters vehicles based on their status (available).
     *
     * @return a list of available vehicles.
     */
    public List<Vehicle> getAvailableVehicles() {
        Specification<Vehicle> specification = new VehicleStatusSpecification(VehicleStatus.AVAILABLE);

        List<Vehicle> allVehicles = getAllVehicles();
        return allVehicles.stream()
                .filter(specification::isSatisfiedBy)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a list of vehicles filtered by a specific status.
     *
     * @param status the vehicle status to filter by (e.g., AVAILABLE, IN_USE).
     * @return a list of vehicles with the specified status.
     */
    public List<Vehicle> getVehiclesByStatus(VehicleStatus status) {
        Specification<Vehicle> specification = new VehicleStatusSpecification(status);

        List<Vehicle> allVehicles = getAllVehicles();
        return allVehicles.stream()
                .filter(specification::isSatisfiedBy)
                .collect(Collectors.toList());
    }

    /**
     * Adds a car to the repository.
     *
     * @param car the car to be added.
     */
    public void addCar(Car car) {
        carRepository.save(car);
    }

    /**
     * Adds a truck to the repository.
     *
     * @param truck the truck to be added.
     */
    public void addTruck(Truck truck) {
        truckRepository.save(truck);
    }

    /**
     * Finds a car by its ID.
     *
     * @param id the ID of the car.
     * @return an Optional containing the car, if found.
     */
    public Optional<Car> findCarById(int id) {
        return carRepository.findById(id);
    }

    /**
     * Finds a truck by its ID.
     *
     * @param id the ID of the truck.
     * @return an Optional containing the truck, if found.
     */
    public Optional<Truck> findTruckById(int id) {
        return truckRepository.findById(id);
    }

    /**
     * Retrieves a list of all cars.
     *
     * @return a list of all cars.
     */
    public List<Car> getAllCars() {
        return carRepository.findAll();
    }

    /**
     * Retrieves a list of all trucks.
     *
     * @return a list of all trucks.
     */
    public List<Truck> getAllTrucks() { return truckRepository.findAll(); }

    /**
     * Deletes a car by its ID.
     *
     * @param id the ID of the car to delete.
     * @return true if the car was deleted, false otherwise.
     */
    public boolean deleteCarById(int id) {
        Optional<Car> car = carRepository.findById(id);
        if (car.isPresent()) {
            carRepository.delete(car.get());
            return true;
        }
        return false;
    }

    /**
     * Deletes a truck by its ID.
     *
     * @param id the ID of the truck to delete.
     * @return true if the truck was deleted, false otherwise.
     */
    public boolean deleteTruckById(int id) {
        Optional<Truck> truck = truckRepository.findById(id);
        if (truck.isPresent()) {
            truckRepository.delete(truck.get());
            return true;
        }
        return false;
    }

    /**
     * Updates the details of a car in the repository.
     *
     * @param car the car with updated details.
     */
    public void updateCar(Car car) { carRepository.update(car); }

    /**
     * Updates the details of a truck in the repository.
     *
     * @param truck the truck with updated details.
     */
    public void updateTruck(Truck truck) { truckRepository.update(truck); }

    /**
     * Updates the details of a vehicle (generic type).
     *
     * @param vehicle the vehicle with updated details.
     */
    public void updateVehicle(Vehicle vehicle) { vehicleRepository.update(vehicle); }

    /**
     * Allocates a vehicle to an available driver based on the user's input.
     * The allocation strategy depends on whether the vehicle is for cargo or passenger use.
     *
     * @return the allocated vehicle, or null if no vehicle was allocated.
     */
    public Vehicle allocateVehicle() {
        List<Vehicle> allVehicles = getAllVehicles();
        Specification<Vehicle> operationalSpec = new OperationableSpecification(50.0);
        VehicleAllocationHandler allocationHandler = new VehicleAllocationHandler(selectStrategy(), operationalSpec);
        Vehicle allocatedVehicle = allocationHandler.allocateVehicle(allVehicles);

        if (allocatedVehicle == null) {
            System.out.println(messages.getString("vehicle.allocate.strategy.error"));
            return null;
        }

        Optional<User> availableDriver = userService.findAvailableDriver();
        if (!availableDriver.isPresent()) {
            System.out.println(messages.getString("no.available.driver"));
            return null;
        }

        userService.allocateVehicleToDriver(availableDriver.get(), allocatedVehicle);
        allocatedVehicle.setVehicleStatus(VehicleStatus.IN_USE);
        updateVehicle(allocatedVehicle);

        System.out.println("Vehicle with ID " + allocatedVehicle.getId() + " has been allocated to driver " + availableDriver.get().getUsername() + " (" + availableDriver.get().getFirstName() + " " + availableDriver.get().getLastName() + ")");
        return allocatedVehicle;
    }

    /**
     * Selects the appropriate allocation strategy based on user input.
     *
     * @return the selected allocation strategy.
     */
    private AllocationStrategy selectStrategy() {
        Scanner scanner = new Scanner(System.in);

        System.out.println(messages.getString("vehicle.allocate.cargo"));
        String cargoResponse = scanner.nextLine().trim().toLowerCase();

        if ("yes".equals(cargoResponse)) {
            return handleCargoRequest(scanner);
        } else {
            return handlePassengerRequest(scanner);
        }
    }

    /**
     * Handles the cargo allocation request by prompting the user for additional details.
     *
     * @param scanner the scanner to read user input.
     * @return the selected cargo allocation strategy.
     */
    private AllocationStrategy handleCargoRequest(Scanner scanner) {
        System.out.println(messages.getString("vehicle.allocate.refrigeration"));
        String refrigerationResponse = scanner.nextLine().trim().toLowerCase();

        if ("yes".equals(refrigerationResponse)) {
            return new RefrigerationStrategy();
        } else {
            System.out.println(messages.getString("vehicle.allocate.capacity"));
            double minCargoCapacity = scanner.nextDouble();
            if (minCargoCapacity > 0) {
                return new CargoPriorityStrategy(minCargoCapacity);
            } else {
                return new NonRefrigeratedHighSpeedStrategy();
            }
        }
    }

    /**
     * Handles the passenger allocation request by prompting the user for additional details.
     *
     * @param scanner the scanner to read user input.
     * @return the selected passenger allocation strategy.
     */
    private AllocationStrategy handlePassengerRequest(Scanner scanner) {
        System.out.println(messages.getString("vehicle.allocate.comfort"));
        String comfortResponse = scanner.nextLine().trim().toLowerCase();

        if ("yes".equals(comfortResponse)) {
            System.out.println(messages.getString("vehicle.allocate.passenger_capacity"));
            int minPassengerCapacity = scanner.nextInt();
            return new ComfortPriorityStrategy(minPassengerCapacity);
        } else {
            return new FuelEfficientStrategy();
        }
    }
}

package org.car.allocation.service;
import org.car.allocation.specification.OperationableSpecification;
import org.car.allocation.specification.Specification;
import org.car.allocation.strategy.AllocationStrategy;

import org.car.allocation.model.Car;
import org.car.allocation.model.Truck;
import org.car.allocation.model.Vehicle;
import org.car.allocation.repository.VehicleRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class VehicleService<T extends Vehicle> {
    private final VehicleRepository<Car> carRepository = new VehicleRepository<>(Car.class);
    private final VehicleRepository<Truck> truckRepository = new VehicleRepository<>(Truck.class);

    public List<Vehicle> getAllVehicles() {
        List<Vehicle> vehicles = new ArrayList<>();
        vehicles.addAll(carRepository.findAll());
        vehicles.addAll(truckRepository.findAll());
        return vehicles;
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

    public Vehicle allocateVehicle() {
        //Gather all vehicles
        List<Vehicle> allVehicles = getAllVehicles();

        //Filter available vehicles
        Specification<Vehicle> operationalSpec = new OperationableSpecification(50.0);
        List<Vehicle> availableVehicles = allVehicles.stream()
                .filter(operationalSpec::isSatisfiedBy)
                .collect(Collectors.toList());

        if (availableVehicles.isEmpty()) {
            System.out.println("No available vehicles that meet the operational requirements.");
            return null;
        }


        System.out.println("No vehicle could be allocated with the selected strategies.");
        return null;
    }
}

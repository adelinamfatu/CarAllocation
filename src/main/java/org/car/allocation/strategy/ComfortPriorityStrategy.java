package org.car.allocation.strategy;

import org.car.allocation.model.Car;
import org.car.allocation.model.Vehicle;
import org.car.allocation.specification.PassengerCapacitySpecification;
import org.car.allocation.specification.Specification;

import java.util.List;
import java.util.stream.Collectors;

public class ComfortPriorityStrategy implements AllocationStrategy {
    private final Specification<Vehicle> passengerCapacitySpec;

    public ComfortPriorityStrategy(int minPassengerCapacity) {
        this.passengerCapacitySpec = new PassengerCapacitySpecification(minPassengerCapacity);
    }

    @Override
    public Vehicle allocate(List<Vehicle> vehicles) {
        //Filter vehicles based on passenger capacity
        List<Vehicle> filteredVehicles = vehicles.stream()
                .filter(passengerCapacitySpec::isSatisfiedBy)
                .collect(Collectors.toList());

        //Find the car with the highest comfort level and lowest max speed
        return filteredVehicles.stream()
                .filter(vehicle -> vehicle instanceof Car) //Ensure it's a car
                .map(vehicle -> (Car) vehicle) //Cast to Car
                .max((car1, car2) -> {
                    //Compare by comfort level, then by max speed (inverted for "lowest")
                    int comfortComparison = Double.compare(car1.getComfortLevel(), car2.getComfortLevel());
                    if (comfortComparison != 0) {
                        return comfortComparison; //Higher comfort level wins
                    }
                    return Double.compare(car2.getMaxSpeed(), car1.getMaxSpeed()); //Lower max speed wins
                })
                .orElse(null); //Return null if no suitable car is found
    }
}

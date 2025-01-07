package org.car.allocation.strategy;

import org.car.allocation.model.Car;
import org.car.allocation.model.Vehicle;
import org.car.allocation.specification.PassengerCapacitySpecification;
import org.car.allocation.specification.Specification;

import java.util.List;
import java.util.stream.Collectors;
/**
 * Implements the {@link AllocationStrategy} interface to prioritize comfort when allocating a vehicle.
 * This strategy selects a car with the highest comfort level and the lowest max speed from the list
 * of available vehicles, provided it meets the minimum passenger capacity requirement.
 * <p>
 * The strategy ensures that the selected car has at least the minimum required passenger capacity,
 * and then optimizes for the highest comfort level and, in case of a tie, the lowest maximum speed.
 * </p>
 */
public class ComfortPriorityStrategy implements AllocationStrategy {
    private final Specification<Vehicle> passengerCapacitySpec;

    public ComfortPriorityStrategy(int minPassengerCapacity) {
        this.passengerCapacitySpec = new PassengerCapacitySpecification(minPassengerCapacity);
    }

    /**
     * Allocates a car based on the comfort priority strategy.
     * <p>
     * The method filters the available vehicles according to the minimum passenger capacity,
     * then selects the car with the highest comfort level. In case of a tie in comfort level,
     * the car with the lowest maximum speed is chosen.
     * </p>
     *
     * @param vehicles the list of available vehicles.
     * @return the best car based on comfort and speed criteria, or {@code null} if no suitable car is found.
     */
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

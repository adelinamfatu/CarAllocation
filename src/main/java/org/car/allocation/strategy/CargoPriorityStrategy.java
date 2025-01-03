package org.car.allocation.strategy;

import org.car.allocation.model.Vehicle;
import org.car.allocation.model.Truck;
import org.car.allocation.specification.CargoCapacitySpecification;
import org.car.allocation.specification.Specification;

import java.util.List;
import java.util.Optional;

/**
 * Implements the AllocationStrategy interface to prioritize trucks based on their cargo capacity.
 * This strategy selects the truck with the highest cargo capacity from the list of available trucks,
 * optimizing for the ability to transport the largest possible load.
 */
public class CargoPriorityStrategy implements AllocationStrategy {
    private final Specification<Vehicle> specification;

    public CargoPriorityStrategy(double minCargoCapacity) {
        //Cargo capacity: prioritize trucks with greater cargo capacity
        Specification<Vehicle> cargoCapacitySpec = new CargoCapacitySpecification(minCargoCapacity);

        specification = cargoCapacitySpec;
    }

    @Override
    public Vehicle allocate(List<Vehicle> vehicles) {
        //Filter vehicles based on the combined specifications
        List<Vehicle> filteredVehicles = vehicles.stream()
                .filter(specification::isSatisfiedBy) //Filter based on the combined specifications
                .toList();

        //Calculate the truck with the best load efficiency: max(cargoCapacity / fuelLevel)
        Optional<Truck> bestTruck = filteredVehicles.stream()
                .map(vehicle -> (Truck) vehicle)
                .max((truck1, truck2) -> Double.compare(
                        truck1.getCargoCapacity() / truck1.getFuelLevel(),
                        truck2.getCargoCapacity() / truck2.getFuelLevel())) //Compare load efficiency
                .map(Optional::of) //If any truck is found, wrap in Optional
                .orElse(Optional.empty());

        //Return the truck with the best load efficiency, or null if no truck meets the criteria
        return bestTruck.orElse(null);
    }
}

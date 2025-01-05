package org.car.allocation.strategy;

import org.car.allocation.model.Vehicle;
import org.car.allocation.model.Truck;
import org.car.allocation.specification.CargoCapacitySpecification;
import org.car.allocation.specification.RefrigerationUnitSpecification;
import org.car.allocation.specification.Specification;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * Implements the AllocationStrategy interface to prioritize non-refrigerated trucks based on their cargo capacity.
 * This strategy selects the non-refrigerated truck with the highest cargo capacity from the list of available trucks,
 * optimizing for the ability to transport the largest possible load without refrigeration.
 */
public class CargoPriorityStrategy implements AllocationStrategy {
    private final Specification<Vehicle> specification;

    public CargoPriorityStrategy(double minCargoCapacity) {
        Specification<Vehicle> cargoCapacitySpec = new CargoCapacitySpecification(minCargoCapacity);
        Specification<Vehicle> nonRefrigeratedSpec = new RefrigerationUnitSpecification(false);

        // Combine specifications to filter non-refrigerated trucks with at least the minimum cargo capacity
        this.specification = cargoCapacitySpec.and(nonRefrigeratedSpec);
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
                .max(Comparator.comparingDouble(truck ->
                        truck.getCargoCapacity() / truck.getFuelLevel())) //Compare load efficiency
                .map(Optional::of) //If any truck is found, wrap in Optional
                .orElse(Optional.empty());

        //Return the truck with the best load efficiency, or null if no truck meets the criteria
        return bestTruck.orElse(null);
    }
}

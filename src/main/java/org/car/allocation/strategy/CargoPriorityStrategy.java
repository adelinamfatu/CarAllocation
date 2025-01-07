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

    /**
     * Constructs the CargoPriorityStrategy with a minimum cargo capacity requirement.
     *
     * @param minCargoCapacity the minimum cargo capacity required for the truck.
     */
    public CargoPriorityStrategy(double minCargoCapacity) {
        Specification<Vehicle> cargoCapacitySpec = new CargoCapacitySpecification(minCargoCapacity);
        Specification<Vehicle> nonRefrigeratedSpec = new RefrigerationUnitSpecification(false);
        this.specification = cargoCapacitySpec.and(nonRefrigeratedSpec);
    }

    /**
     * Allocates a truck based on the cargo priority strategy.
     *
     * The method filters the available vehicles according to the specified cargo capacity and
     * non-refrigerated conditions, then selects the truck with the highest cargo capacity, optimized
     * by fuel efficiency (cargo capacity per unit of fuel).
     *
     * @param vehicles the list of available vehicles.
     * @return the best truck based on the cargo capacity and non-refrigeration criteria, or null if no suitable truck is found.
     */
    @Override
    public Vehicle allocate(List<Vehicle> vehicles) {
        List<Vehicle> filteredVehicles = vehicles.stream()
                .filter(specification::isSatisfiedBy) //Filter based on the combined specifications
                .toList();
        Optional<Truck> bestTruck = filteredVehicles.stream()
                .map(vehicle -> (Truck) vehicle)
                .max(Comparator.comparingDouble(truck ->
                        truck.getCargoCapacity() / truck.getFuelLevel())) //Compare load efficiency
                .map(Optional::of) //If any truck is found, wrap in Optional
                .orElse(Optional.empty());
        return bestTruck.orElse(null);
    }
}

package org.car.allocation.strategy;

import org.car.allocation.model.Vehicle;
import org.car.allocation.model.Truck;
import org.car.allocation.specification.RefrigerationUnitSpecification;
import org.car.allocation.specification.Specification;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
/**
 * Implements the {@link AllocationStrategy} interface to allocate non-refrigerated trucks based on high speed.
 * <p>
 * This strategy selects the truck that is not equipped with a refrigeration unit and has the highest maximum speed.
 * It first filters trucks that are not refrigerated and then sorts them by their maximum speed in descending order.
 * The truck with the highest speed is allocated.
 * </p>
 */
public class NonRefrigeratedHighSpeedStrategy implements AllocationStrategy {
    private final Specification<Vehicle> specification;

    public NonRefrigeratedHighSpeedStrategy() {
        //Not refrigerated trucks
        Specification<Vehicle> refrigeratedSpec = new RefrigerationUnitSpecification(true);
        this.specification = refrigeratedSpec.not();
    }

    /**
     * Allocates a non-refrigerated truck with the highest maximum speed from the available vehicles.
     * <p>
     * This method first filters the available vehicles to include only non-refrigerated trucks, then sorts them by their
     * maximum speed in descending order. The truck with the highest speed is selected.
     * </p>
     *
     * @param availableVehicles the list of available vehicles.
     * @return the non-refrigerated truck with the highest speed, or {@code null} if no suitable truck is found.
     */
    @Override
    public Vehicle allocate(List<Vehicle> availableVehicles) {
        //Select trucks that are not refrigerated and have high max speed
        List<Truck> suitableTrucks = availableVehicles.stream()
                .filter(specification::isSatisfiedBy)
                .filter(v -> v instanceof Truck) // Ensure it's a truck
                .map(v -> (Truck) v)
                .sorted(Comparator.comparingDouble(Truck::getMaxSpeed).reversed()) // Sort by max speed, highest first
                .collect(Collectors.toList());

        return suitableTrucks.stream()
                .findFirst()
                .orElse(null);
    }
}

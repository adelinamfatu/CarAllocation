package org.car.allocation.strategy;

import org.car.allocation.model.Truck;
import org.car.allocation.model.Vehicle;
import org.car.allocation.specification.RefrigerationUnitSpecification;
import org.car.allocation.specification.Specification;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Strategy to allocate trucks that have a refrigeration unit.
 */
public class RefrigerationStrategy implements AllocationStrategy {
    private final Specification<Vehicle> specification;

    public RefrigerationStrategy() {
        this.specification = new RefrigerationUnitSpecification(true);
    }
    /**
     * Allocates a truck with a refrigeration unit, prioritizing those with the lowest mileage.
     * <p>
     * This method filters the available vehicles to include only trucks with a refrigeration unit, then selects
     * the truck with the lowest mileage from the filtered list.
     * </p>
     *
     * @param availableVehicles the list of available vehicles.
     * @return the truck with a refrigeration unit and the lowest mileage, or {@code null} if no suitable truck is found.
     */
    @Override
    public Vehicle allocate(List<Vehicle> availableVehicles) {
        //Filter trucks with refrigeration
        List<Truck> refrigeratedTrucks = availableVehicles.stream()
                .filter(specification::isSatisfiedBy)
                .map(v -> (Truck) v)
                .collect(Collectors.toList());

        //Prioritize by another factor, like lowest mileage
        return refrigeratedTrucks.stream()
                .min(Comparator.comparingDouble(Truck::getMileage))
                .orElse(null);
    }
}

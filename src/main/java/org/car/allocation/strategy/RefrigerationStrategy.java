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

    @Override
    public Vehicle allocate(List<Vehicle> availableVehicles) {
        // Filter trucks with refrigeration
        List<Truck> refrigeratedTrucks = availableVehicles.stream()
                .filter(specification::isSatisfiedBy)
                .map(v -> (Truck) v)
                .collect(Collectors.toList());

        // Prioritize by another factor, like lowest mileage
        return refrigeratedTrucks.stream()
                .min(Comparator.comparingDouble(Truck::getMileage))
                .orElse(null);
    }
}

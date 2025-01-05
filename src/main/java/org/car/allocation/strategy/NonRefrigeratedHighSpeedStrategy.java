package org.car.allocation.strategy;

import org.car.allocation.model.Vehicle;
import org.car.allocation.model.Truck;
import org.car.allocation.specification.RefrigerationUnitSpecification;
import org.car.allocation.specification.Specification;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class NonRefrigeratedHighSpeedStrategy implements AllocationStrategy {
    private final Specification<Vehicle> specification;

    public NonRefrigeratedHighSpeedStrategy() {
        //Not refrigerated trucks
        Specification<Vehicle> refrigeratedSpec = new RefrigerationUnitSpecification(true);
        this.specification = refrigeratedSpec.not();
    }

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

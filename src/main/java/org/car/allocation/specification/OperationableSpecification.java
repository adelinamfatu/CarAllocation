package org.car.allocation.specification;

import org.car.allocation.model.Vehicle;
import org.car.allocation.util.VehicleStatus;

/**
 * A specification to determine if a vehicle is operational.
 * A vehicle is considered operational if it is available and has a sufficient fuel level.
 */
public class OperationableSpecification implements Specification<Vehicle> {
    private final double minFuelLevel;

    public OperationableSpecification(double minFuelLevel) {
        this.minFuelLevel = minFuelLevel;
    }

    @Override
    public boolean isSatisfiedBy(Vehicle vehicle) {
        return vehicle.getVehicleStatus() == VehicleStatus.AVAILABLE
                && vehicle.getFuelLevel() >= minFuelLevel;
    }

    @Override
    public Specification<Vehicle> and(Specification<Vehicle> other) {
        return new AndSpecification<>(this, other);
    }

    @Override
    public Specification<Vehicle> or(Specification<Vehicle> other) {
        return new OrSpecification<>(this, other);
    }

    @Override
    public Specification<Vehicle> not() {
        return new NotSpecification<>(this);
    }
}

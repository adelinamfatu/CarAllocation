package org.car.allocation.specification;

import org.car.allocation.model.Vehicle;
import org.car.allocation.model.VehicleStatus;

public class OperationableSpecification implements Specification<Vehicle> {
    private final double minFuelLevel;

    public OperationableSpecification(double minFuelLevel) {
        this.minFuelLevel = minFuelLevel;
    }

    @Override
    public boolean isSatisfiedBy(Vehicle vehicle) {
        // Verifici statusul vehiculului folosind enum-ul VehicleStatus
        return vehicle.getVehicleStatus() == VehicleStatus.AVAILABLE
                && vehicle.getVehicleStatus() != VehicleStatus.IN_MAINTENANCE
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

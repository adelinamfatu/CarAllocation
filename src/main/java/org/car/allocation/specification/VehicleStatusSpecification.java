package org.car.allocation.specification;

import org.car.allocation.model.Vehicle;
import org.car.allocation.util.VehicleStatus;

/**
 * A specification that checks whether a vehicle has a specific status.
 *
 * This specification can be used to filter vehicles based on their current status,
 * such as whether the vehicle is available, in use, or requires maintenance.
 */
public class VehicleStatusSpecification implements Specification<Vehicle> {
    private VehicleStatus status;

    public VehicleStatusSpecification(VehicleStatus status) {
        this.status = status;
    }

    @Override
    public boolean isSatisfiedBy(Vehicle vehicle) {
        return vehicle.getVehicleStatus() == status;
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

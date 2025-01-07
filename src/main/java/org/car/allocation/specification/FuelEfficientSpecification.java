package org.car.allocation.specification;

import org.car.allocation.model.Vehicle;
import org.car.allocation.util.EngineType;
/**
 * A specification to filter vehicles that are fuel-efficient based on engine type, mileage, and speed.
 * This specification ensures vehicles meet certain criteria for fuel efficiency and performance.
 */
public class FuelEfficientSpecification implements Specification<Vehicle> {
    private final EngineType preferredEngineType;
    private final double maxMileage;
    private final double minSpeed;

    public FuelEfficientSpecification(EngineType preferredEngineType, double maxMileage, double minSpeed) {
        this.preferredEngineType = preferredEngineType;
        this.maxMileage = maxMileage;
        this.minSpeed = minSpeed;
    }

    @Override
    public boolean isSatisfiedBy(Vehicle vehicle) {
        if (vehicle.getEngineType() == preferredEngineType) {
            return vehicle.getMileage() <= maxMileage && vehicle.getMaxSpeed() >= minSpeed;
        }
        return false; //Not relevant if the vehicle does not match the engine type
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

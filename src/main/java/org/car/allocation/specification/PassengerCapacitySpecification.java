package org.car.allocation.specification;

import org.car.allocation.model.Car;
import org.car.allocation.model.Vehicle;

/**
 * A specification that checks if a vehicle has a minimum passenger capacity.
 * <p>
 * This specification is specifically applicable to {@link Car} objects, as only cars have a defined passenger capacity.
 */
public class PassengerCapacitySpecification implements Specification<Vehicle> {
    private final int minPassengerCapacity;

    public PassengerCapacitySpecification(int minPassengerCapacity) {
        this.minPassengerCapacity = minPassengerCapacity;
    }

    @Override
    public boolean isSatisfiedBy(Vehicle vehicle) {
        if (vehicle instanceof Car) {
            Car car = (Car) vehicle;
            return car.getPassengerCapacity() >= minPassengerCapacity;
        }
        return false; //Only applies to cars
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
package org.car.allocation.specification;

import org.car.allocation.model.Truck;
import org.car.allocation.model.Vehicle;

public class CargoCapacitySpecification implements Specification<Vehicle> {
    private final double minCargoCapacity;

    public CargoCapacitySpecification(double minCargoCapacity) {
        this.minCargoCapacity = minCargoCapacity;
    }

    @Override
    public boolean isSatisfiedBy(Vehicle vehicle) {
        if (vehicle instanceof Truck) {
            Truck truck = (Truck) vehicle;
            return truck.getCargoCapacity() >= minCargoCapacity; //Check if cargo capacity meets the threshold
        }
        return false; //Only apply to trucks
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

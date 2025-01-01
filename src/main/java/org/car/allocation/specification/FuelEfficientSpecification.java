package org.car.allocation.specification;

import org.car.allocation.model.Vehicle;

public class FuelEfficientSpecification implements Specification<Vehicle> {
    private final double minEfficiency;

    public FuelEfficientSpecification(double minEfficiency) {
        this.minEfficiency = minEfficiency;
    }

    @Override
    public boolean isSatisfiedBy(Vehicle vehicle) {
        double efficiency = vehicle.getMileage() / vehicle.getFuelLevel();
        return efficiency >= minEfficiency;
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

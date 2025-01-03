package org.car.allocation.specification;

import org.car.allocation.model.Vehicle;
import org.car.allocation.util.EngineType;

public class EngineTypeSpecification implements Specification<Vehicle> {
    private final EngineType engineType;

    public EngineTypeSpecification(EngineType engineType) {
        this.engineType = engineType;
    }

    @Override
    public boolean isSatisfiedBy(Vehicle vehicle) {
        return vehicle.getEngineType() == engineType;
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

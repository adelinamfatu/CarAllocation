package org.car.allocation.specification;

import org.car.allocation.model.Vehicle;
import org.car.allocation.util.EngineType;

/**
 * A specification to filter vehicles based on their engine type.
 * This specification is applicable to any vehicle with an {@link EngineType}.
 */
public class EngineTypeSpecification implements Specification<Vehicle> {
    private final EngineType engineType;

    public EngineTypeSpecification(EngineType engineType) {
        this.engineType = engineType;
    }

    /**
     * A specification to filter vehicles based on their engine type.
     * This specification is applicable to any vehicle with an {@link EngineType}.
     */
    @Override
    public boolean isSatisfiedBy(Vehicle vehicle) {
        return vehicle.getEngineType() == engineType;
    }

    /**
     * Combines this specification with another using a logical AND operation.
     *
     * @param other the other specification to combine.
     * @return a new AndSpecification that represents the combination.
     */
    @Override
    public Specification<Vehicle> and(Specification<Vehicle> other) {
        return new AndSpecification<>(this, other);
    }

    /**
     * Combines this specification with another using a logical OR operation.
     *
     * @param other the other specification to combine.
     * @return a new OrSpecification that represents the combination.
     */
    @Override
    public Specification<Vehicle> or(Specification<Vehicle> other) {
        return new OrSpecification<>(this, other);
    }

    /**
     * Negates this specification using a logical NOT operation.
     *
     * @return a new NotSpecification that represents the negation of this specification.
     */
    @Override
    public Specification<Vehicle> not() {
        return new NotSpecification<>(this);
    }
}

package org.car.allocation.specification;

import org.car.allocation.model.Truck;
import org.car.allocation.model.Vehicle;
/**
 * A specification to filter vehicles based on their cargo capacity.
 * This specification only applies to vehicles of type {@link Truck}.
 */
public class CargoCapacitySpecification implements Specification<Vehicle> {
    private final double minCargoCapacity;

    public CargoCapacitySpecification(double minCargoCapacity) {
        this.minCargoCapacity = minCargoCapacity;
    }

    /**
     * Checks whether the specified vehicle satisfies the cargo capacity requirement.
     *
     * @param vehicle the vehicle to test.
     * @return true if the vehicle is a truck and its cargo capacity meets or exceeds the specified minimum, false otherwise.
     */
    @Override
    public boolean isSatisfiedBy(Vehicle vehicle) {
        if (vehicle instanceof Truck) {
            Truck truck = (Truck) vehicle;
            return truck.getCargoCapacity() >= minCargoCapacity; //Check if cargo capacity meets the threshold
        }
        return false; //Only apply to trucks
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

package org.car.allocation.specification;

import org.car.allocation.model.Truck;
import org.car.allocation.model.Vehicle;

public class RefrigerationUnitSpecification implements Specification<Vehicle> {
    private final boolean requiresRefrigeration;

    public RefrigerationUnitSpecification(boolean requiresRefrigeration) {
        this.requiresRefrigeration = requiresRefrigeration;
    }

    @Override
    public boolean isSatisfiedBy(Vehicle vehicle) {
        if (vehicle instanceof Truck) {
            Truck truck = (Truck) vehicle;
            return truck.hasRefrigerationUnit() == requiresRefrigeration; //Check if the truck has a refrigeration unit
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

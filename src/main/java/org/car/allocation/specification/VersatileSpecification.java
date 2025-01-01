package org.car.allocation.specification;

import org.car.allocation.model.Car;
import org.car.allocation.model.EngineType;
import org.car.allocation.model.Truck;
import org.car.allocation.model.Vehicle;

public class VersatileSpecification implements Specification<Vehicle> {
    private final EngineType requiredEngineType;
    private final int minimumPassengerCapacity;
    private final double minimumCargoCapacity;

    public VersatileSpecification(EngineType requiredEngineType, int minimumPassengerCapacity, double minimumCargoCapacity) {
        this.requiredEngineType = requiredEngineType;
        this.minimumPassengerCapacity = minimumPassengerCapacity;
        this.minimumCargoCapacity = minimumCargoCapacity;
    }

    @Override
    public boolean isSatisfiedBy(Vehicle vehicle) {
        if (vehicle.getEngineType() != requiredEngineType) {
            return false;
        }

        if (vehicle instanceof Car) {
            Car car = (Car) vehicle;
            return car.getPassengerCapacity() >= minimumPassengerCapacity;
        }

        if (vehicle instanceof Truck) {
            Truck truck = (Truck) vehicle;
            return truck.getCargoCapacity() >= minimumCargoCapacity;
        }

        return false;
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

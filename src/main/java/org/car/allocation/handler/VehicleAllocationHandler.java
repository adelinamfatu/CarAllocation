package org.car.allocation.handler;

import org.car.allocation.model.Vehicle;
import org.car.allocation.specification.Specification;
import org.car.allocation.strategy.AllocationStrategy;

import java.util.List;
import java.util.stream.Collectors;

/**
 * The VehicleAllocationHandler handles the vehicle allocation based on a certain strategy and specification
 */
public class VehicleAllocationHandler {
    private AllocationStrategy strategy;
    private Specification<Vehicle> filterSpecification;

    public VehicleAllocationHandler(AllocationStrategy strategy, Specification<Vehicle> filterSpecification) {
        this.strategy = strategy;
        this.filterSpecification = filterSpecification;
    }

    public void setStrategy(AllocationStrategy strategy) {
        this.strategy = strategy;
    }

    /**
     * Allocates a vehicle by first filtering with the specification, then applying the strategy.
     * @param allVehicles List of all vehicles (not pre-filtered).
     * @return Allocated vehicle or null if none satisfies the criteria.
     */
    public Vehicle allocateVehicle(List<Vehicle> allVehicles) {
        //Filter vehicles based on the specification
        List<Vehicle> filteredVehicles = allVehicles.stream()
                .filter(filterSpecification::isSatisfiedBy)
                .collect(Collectors.toList());

        //Apply the allocation strategy on the filtered list
        return strategy.allocate(filteredVehicles);
    }
}

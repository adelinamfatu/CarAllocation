package org.car.allocation.strategy;

import org.car.allocation.model.Vehicle;
import org.car.allocation.specification.EngineTypeSpecification;
import org.car.allocation.specification.FuelEfficientSpecification;
import org.car.allocation.specification.Specification;
import org.car.allocation.util.EngineType;

import java.util.List;

/**
 * Implements the AllocationStrategy interface to allocate vehicles based on fuel efficiency.
 * It selects the vehicle that TO BE COMPLETED
 */
public class FuelEfficientStrategy implements AllocationStrategy {
    /**
     * Allocates a vehicle based on fuel efficiency.
     * <p>
     * This method first filters the available vehicles based on whether they have an eco-friendly engine
     * type (electric or hybrid). If no eco-friendly vehicles are found, it will then check for fuel-efficient
     * vehicles that meet the specified criteria (petrol engine with a mileage under 150,000 and minimum speed of 100).
     * </p>
     *
     * @param availableVehicles the list of available vehicles.
     * @return the most fuel-efficient vehicle that meets the criteria, or {@code null} if no suitable vehicle is found.
     */
    @Override
    public Vehicle allocate(List<Vehicle> availableVehicles) {
        Specification<Vehicle> ecoFriendlySpec = new EngineTypeSpecification(EngineType.ELECTRIC)
                .or(new EngineTypeSpecification(EngineType.HYBRID));

        Specification<Vehicle> nonEcoSpec = new FuelEfficientSpecification(EngineType.PETROL, 150000, 100);

        var specification = ecoFriendlySpec.or(nonEcoSpec);
        List<Vehicle> filteredVehicles = availableVehicles.stream()
                .filter(specification::isSatisfiedBy)
                .toList();

        return filteredVehicles.stream()
                .findFirst()
                .orElse(null);
    }
}

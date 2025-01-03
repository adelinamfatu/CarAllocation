package org.car.allocation.strategy;

import org.car.allocation.model.Vehicle;
import org.car.allocation.specification.EngineTypeSpecification;
import org.car.allocation.specification.FuelEfficientSpecification;
import org.car.allocation.specification.Specification;
import org.car.allocation.util.EngineType;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implements the AllocationStrategy interface to allocate vehicles based on fuel efficiency.
 * It selects the vehicle that TO BE COMPLETED
 */
public class FuelEfficientStrategy implements AllocationStrategy {
    @Override
    public Vehicle allocate(List<Vehicle> availableVehicles) {
        //Specification for Electric or Hybrid vehicles (engine type)
        Specification<Vehicle> ecoFriendlySpec = new EngineTypeSpecification(EngineType.ELECTRIC)
                .or(new EngineTypeSpecification(EngineType.HYBRID));  //Vehicles with electric or hybrid engines

        //Specification for non-Electric/Hybrid vehicles with certain mileage and speed
        Specification<Vehicle> nonEcoSpec = new FuelEfficientSpecification(EngineType.PETROL, 150000, 100);

        //Combine both specifications using OR: prioritize either eco-friendly or non-eco-friendly vehicles with high fuel efficiency
        var specification = ecoFriendlySpec.or(nonEcoSpec);

        List<Vehicle> filteredVehicles = availableVehicles.stream()
                .filter(specification::isSatisfiedBy) //Filter based on the specification
                .collect(Collectors.toList());

        //Allocate the first available vehicle from the filtered list
        return filteredVehicles.stream()
                .findFirst()
                .orElse(null);
    }
}

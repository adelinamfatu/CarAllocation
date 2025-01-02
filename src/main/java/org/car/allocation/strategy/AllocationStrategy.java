package org.car.allocation.strategy;

import org.car.allocation.model.Vehicle;

import java.util.List;

/**
 * Interface for vehicle allocation strategies.
 * Defines a method to allocate a vehicle based on specific criteria.
 */

public interface AllocationStrategy {
    /**
     * Allocates a vehicle based on the implemented strategy.
     * @param availableVehicles List of vehicles that are available for allocation.
     * @return The vehicle that fits the strategy criteria.
     */
    Vehicle allocate(List<Vehicle> availableVehicles);
}

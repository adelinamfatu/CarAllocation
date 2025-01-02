package org.car.allocation.strategy;

import org.car.allocation.model.Vehicle;
import org.car.allocation.model.Truck;

import java.util.List;

/**
 * This class implements the AllocationStrategy interface to provide
 * a load capacity-based vehicle allocation strategy.
 * It allocates trucks based on whether they can meet or exceed a specified cargo capacity.
 */

public class LoadCapacityStrategy implements AllocationStrategy {
    private double requiredCapacity; // Required capacity in kg

    /**
     * Constructor to set the required cargo capacity.
     * @param requiredCapacity the cargo capacity required for the task.
     */
    public LoadCapacityStrategy(double requiredCapacity) {
        this.requiredCapacity = requiredCapacity;
    }

    /**
     * Allocates a truck that meets or exceeds the required cargo capacity.
     * @param availableVehicles List of vehicles to choose from.
     * @return Truck that fits the required capacity or null if no suitable truck is found.
     */
    @Override
    public Vehicle allocate(List<Vehicle> availableVehicles) {
        for (Vehicle vehicle : availableVehicles) {
            if (vehicle instanceof Truck) {
                Truck truck = (Truck) vehicle;
                if (truck.getCargoCapacity() >= requiredCapacity) {
                    return truck;
                }
            }
        }
        return null;
    }
}

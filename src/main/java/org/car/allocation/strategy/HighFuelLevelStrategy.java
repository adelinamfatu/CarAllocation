package org.car.allocation.strategy;

import org.car.allocation.model.Vehicle;
import java.util.List;

/**
 * This class implements the AllocationStrategy interface to provide
 * a high fuel level-based vehicle allocation strategy.
 * It allocates the vehicle with the highest fuel level, aiming to maximize the immediate operational range.
 */
public class HighFuelLevelStrategy implements AllocationStrategy {
    /**
     * Allocates the vehicle with the highest fuel level from the list of available vehicles.
     * @param availableVehicles List of vehicles to choose from.
     * @return Vehicle with the highest fuel level or null if no suitable vehicle is found.
     */
    @Override
    public Vehicle allocate(List<Vehicle> availableVehicles) {
        Vehicle selectedVehicle = null;
        double maxFuelLevel = 0.0;

        for (Vehicle vehicle : availableVehicles) {
            if (vehicle.getFuelLevel() > maxFuelLevel) {
                maxFuelLevel = vehicle.getFuelLevel();
                selectedVehicle = vehicle;
            }
        }
        return selectedVehicle;
    }
}

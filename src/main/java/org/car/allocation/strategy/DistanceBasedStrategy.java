package org.car.allocation.strategy;

import org.car.allocation.model.Vehicle;

import java.util.List;

/**
 * This class implements the AllocationStrategy interface to provide
 * a distance-based vehicle allocation strategy.
 * It allocates vehicles based on the closeness of their current mileage
 * to a specified task distance, aiming to minimize travel distance.
 */

public class DistanceBasedStrategy implements AllocationStrategy {
    private double taskDistance; // Load distance in km

    /**
     * Constructor to set the task distance.
     * @param taskDistance the distance of the task for which a vehicle is needed.
     */
    public DistanceBasedStrategy(double taskDistance) {
        this.taskDistance = taskDistance;
    }

    /**
     * Allocates a vehicle based on the minimum distance difference.
     * @param availableVehicles List of vehicles to choose from.
     * @return Vehicle closest in mileage to the task distance or null if no suitable vehicle is found.
     */
    @Override
    public Vehicle allocate(List<Vehicle> availableVehicles) {
        Vehicle selectedVehicle = null;
        double minDistanceDiff = Double.MAX_VALUE;

        for (Vehicle vehicle : availableVehicles) {
            double distanceDiff = Math.abs(vehicle.getMileage() - taskDistance);
            if (distanceDiff < minDistanceDiff) {
                minDistanceDiff = distanceDiff;
                selectedVehicle = vehicle;
            }
        }
        return selectedVehicle;
    }
}

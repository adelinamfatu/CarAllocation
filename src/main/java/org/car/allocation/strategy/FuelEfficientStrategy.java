package org.car.allocation.strategy;

import org.car.allocation.model.Vehicle;
import java.util.List;

/**
 * Implements the AllocationStrategy interface to allocate vehicles based on fuel efficiency.
 * It selects the vehicle that achieves the highest distance per unit of fuel consumed.
 */
public class FuelEfficientStrategy implements AllocationStrategy {
    @Override
    public Vehicle allocate(List<Vehicle> availableVehicles) {
        Vehicle selectedVehicle = null;
        double maxEfficiency = 0.0; //Presuming efficiency is calculated as distance traveled per unit of fuel

        for (Vehicle vehicle : availableVehicles) {
            double efficiency = calculateFuelEfficiency(vehicle); //Method to calculate efficiency
            if (efficiency > maxEfficiency) {
                maxEfficiency = efficiency;
                selectedVehicle = vehicle;
            }
        }
        return selectedVehicle;
    }

    /**
     * A simple implementation could use data about fuel consumption.
     * @param vehicle The vehicle for which to calculate fuel efficiency.
     * @return The calculated fuel efficiency as mileage per fuel level.
     */
    private double calculateFuelEfficiency(Vehicle vehicle) {
        //This could be calculated based on known data about vehicle's fuel consumption
        return vehicle.getMileage() / vehicle.getFuelLevel();
    }
}

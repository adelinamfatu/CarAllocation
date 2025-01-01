package org.car.allocation.abstract_factory;

import org.car.allocation.model.Vehicle;
/**
 * VehicleFactory is an interface that defines the contract for creating
 * different types of vehicles. Any class implementing this interface
 * must provide an implementation for the method to create a vehicle.
 */
public interface VehicleFactory {
    /**
     * Creates a new vehicle with the specified details.
     *
     * @param licensePlate The license plate of the vehicle to be created.
     * @param model The model of the vehicle to be created.
     * @param fuelLevel The fuel level of the vehicle to be created.
     * @return A new Vehicle object with the specified license plate, model,
     *         and fuel level.
     */
    Vehicle createVehicle(String licensePlate, String model, double fuelLevel);
}

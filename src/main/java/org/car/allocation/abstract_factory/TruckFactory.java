package org.car.allocation.abstract_factory;

import org.car.allocation.model.Truck;
import org.car.allocation.model.Vehicle;
import org.car.allocation.util.EngineType;

/**
 * TruckFactory implements the VehicleFactory interface and is responsible
 * for creating Truck objects. This class utilizes the cargo capacity
 * to configure the created truck objects.
 */
public class TruckFactory implements VehicleFactory {
    private double cargoCapacity;
    private boolean hasRefrigerationUnit;

    public TruckFactory(double cargoCapacity,boolean hasRefrigerationUnit) {
        this.cargoCapacity = cargoCapacity;
        this.hasRefrigerationUnit = hasRefrigerationUnit;
    }

    /**
     * Creates a new Truck object with the provided details.
     * This method implements the createVehicle method from the VehicleFactory interface.
     * @param licensePlate The license plate of the truck to be created.
     * @param model The model of the truck to be created.
     * @param fuelLevel The fuel level of the truck to be created.
     * @return A new Truck object with the specified license plate, model,
     *         fuel level, and cargo capacity.
     */
    @Override
    public Vehicle createVehicle(String licensePlate, String model, double fuelLevel, double maxSpeed, EngineType engineType, double mileage) {
        return new Truck(licensePlate, model, fuelLevel, maxSpeed, engineType, cargoCapacity, hasRefrigerationUnit, mileage);
    }
}

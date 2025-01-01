package org.car.allocation.abstract_factory;

import org.car.allocation.model.Truck;
import org.car.allocation.model.Vehicle;

public class TruckFactory implements VehicleFactory {
    private double cargoCapacity;

    public TruckFactory(double cargoCapacity) {
        this.cargoCapacity = cargoCapacity;
    }

    @Override
    public Vehicle createVehicle(String licensePlate, String model, double fuelLevel) {
        return new Truck(licensePlate, model, fuelLevel, cargoCapacity);
    }
}

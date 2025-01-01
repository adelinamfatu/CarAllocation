package org.car.allocation.abstract_factory;

import org.car.allocation.model.Vehicle;

public interface VehicleFactory {
    Vehicle createVehicle(String licensePlate, String model, double fuelLevel);
}

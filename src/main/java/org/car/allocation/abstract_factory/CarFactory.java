package org.car.allocation.abstract_factory;

import org.car.allocation.model.Car;
import org.car.allocation.model.Vehicle;

public class CarFactory implements VehicleFactory {
    private int passengerCapacity;

    public CarFactory(int passengerCapacity) {
        this.passengerCapacity = passengerCapacity;
    }

    @Override
    public Vehicle createVehicle(String licensePlate, String model, double fuelLevel) {
        return new Car(licensePlate, model, fuelLevel, passengerCapacity);
    }
}

package org.car.allocation.abstract_factory;

import org.car.allocation.model.Car;
import org.car.allocation.model.Vehicle;
import org.car.allocation.util.EngineType;

/**
 * CarFactory is responsible for creating Car objects.
 * It implements the VehicleFactory interface and provides
 * the specific implementation to create a car based on the given
 * parameters, including the passenger capacity.
 */
public class CarFactory implements VehicleFactory {
    private int passengerCapacity;
    private int comfortLevel;

    public CarFactory(int passengerCapacity, int comfortLevel) {
        this.passengerCapacity = passengerCapacity;
        this.comfortLevel = comfortLevel;
    }

    /**
     * Creates a new Car object with the provided details.
     ** This method implements the createVehicle method from the VehicleFactory interface.
     * @param licensePlate The license plate of the car to be created.
     * @param model The model of the car to be created.
     * @param fuelLevel The fuel level of the car to be created.
     * @return A new Car object with the specified license plate, model,
     *         fuel level, and passenger capacity.
     */
    @Override
    public Vehicle createVehicle(String licensePlate, String model, double fuelLevel, double maxSpeed, EngineType engineType) {
        return new Car(licensePlate, model, fuelLevel, maxSpeed, engineType, passengerCapacity, comfortLevel);
    }
}

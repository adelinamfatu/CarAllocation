package org.car.allocation.model;

import jakarta.persistence.*;
import org.car.allocation.util.EngineType;

/**
 * The Car class represents a car object in the system. It extends the Vehicle class
 * and adds specific attributes for a car, such as passenger capacity.
 */
@Entity
@Table(name = "cars")
public class Car extends Vehicle {
    @Column(nullable = false)
    private int passengerCapacity;

    public Car() {}

    public Car(String licensePlate, String model, double fuelLevel, EngineType engineType, int passengerCapacity) {
        super(licensePlate, model, fuelLevel, engineType);
        this.passengerCapacity = passengerCapacity;
    }

    /**
     * Retrieves the passenger capacity of the car.
     * @return The passenger capacity of the car.
     */
    public int getPassengerCapacity() {
        return passengerCapacity;
    }

    /**
     * Sets the passenger capacity of the car.
     * @param passengerCapacity The new passenger capacity of the car.
     */
    public void setPassengerCapacity(int passengerCapacity) {
        this.passengerCapacity = passengerCapacity;
    }

    @Override
    public String toString() {
        return String.format("Car [ID: %d, License Plate: %s, Model: %s, Fuel Level: %.1f, Engine Type: %s, Passenger Capacity: %d]",
                getId(), getLicensePlate(), getModel(), getFuelLevel(), getEngineType(), passengerCapacity);
    }
}

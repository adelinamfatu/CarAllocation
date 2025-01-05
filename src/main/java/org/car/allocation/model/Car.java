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

    @Column(nullable = false, columnDefinition = "TINYINT CHECK (comfortLevel BETWEEN 1 AND 10)")
    private int comfortLevel;

    public Car() {}

    public Car(String licensePlate, String model, double fuelLevel, double maxSpeed, EngineType engineType, int passengerCapacity, int comfortLevel, double mileage) {
        super(licensePlate, model, fuelLevel, maxSpeed, engineType, mileage);
        this.passengerCapacity = passengerCapacity;
        this.comfortLevel = comfortLevel;
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

    public int getComfortLevel() {
        return comfortLevel;
    }

    public void setComfortLevel(int comfortLevel) {
        if (comfortLevel < 1 || comfortLevel > 10) {
            throw new IllegalArgumentException("Comfort level must be between 1 and 10.");
        }
        this.comfortLevel = comfortLevel;
    }

    @Override
    public String toString() {
        return String.format("Car [ID: %d, License Plate: %s, Model: %s, Fuel Level: %.1f, Engine Type: %s, Passenger Capacity: %d, Mileage: %.1f]",
                getId(), getLicensePlate(), getModel(), getFuelLevel(), getEngineType(), passengerCapacity, getMileage());
    }
}

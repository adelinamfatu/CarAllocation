package org.car.allocation.model;

import jakarta.persistence.*;

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

    public Car(String licensePlate, String model, double fuelLevel, int passengerCapacity) {
        super(licensePlate, model, fuelLevel);
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
}

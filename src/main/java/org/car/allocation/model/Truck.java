package org.car.allocation.model;

import jakarta.persistence.*;

/**
 * The Truck class represents a truck object in the system. It extends the Vehicle class
 * and adds specific attributes for a truck, such as cargo capacity.
 */
@Entity
@Table(name = "trucks")
public class Truck extends Vehicle {
    /**
     * The cargo capacity of the truck, representing the maximum weight it can carry.
     */
    @Column(nullable = false)
    private double cargoCapacity;

    /**
     * Default constructor for Truck.
     * Initializes a new instance of the Truck class with default values.
     */
    public Truck() {}

    public Truck(String licensePlate, String model, double fuelLevel, double cargoCapacity) {
        super(licensePlate, model, fuelLevel);
        this.cargoCapacity = cargoCapacity;
    }

    /**
     * Retrieves the cargo capacity of the truck.
     *
     * @return The cargo capacity of the truck.
     */
    public double getCargoCapacity() {
        return cargoCapacity;
    }

    /**
     * Sets the cargo capacity of the truck.
     *
     * @param cargoCapacity The new cargo capacity of the truck.
     */
    public void setCargoCapacity(double cargoCapacity) {
        this.cargoCapacity = cargoCapacity;
    }
}

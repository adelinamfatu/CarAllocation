package org.car.allocation.model;

import jakarta.persistence.*;
import org.car.allocation.util.EngineType;

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

    @Column(nullable = false)
    private boolean hasRefrigerationUnit;

    /**
     * Default constructor for Truck.
     * Initializes a new instance of the Truck class with default values.
     */
    public Truck() {}

    public Truck(String licensePlate, String model, double fuelLevel, double maxSpeed, EngineType engineType, double cargoCapacity, boolean hasRefrigerationUnit, double mileage) {
        super(licensePlate, model, fuelLevel, maxSpeed, engineType, mileage);
        this.cargoCapacity = cargoCapacity;
        this.hasRefrigerationUnit = hasRefrigerationUnit;
    }

    /**
     * Retrieves the cargo capacity of the truck.
     * @return The cargo capacity of the truck.
     */
    public double getCargoCapacity() {
        return cargoCapacity;
    }

    /**
     * Sets the cargo capacity of the truck.
     * @param cargoCapacity The new cargo capacity of the truck.
     */
    public void setCargoCapacity(double cargoCapacity) {
        this.cargoCapacity = cargoCapacity;
    }

    public boolean hasRefrigerationUnit() {
        return hasRefrigerationUnit;
    }

    @Override
    public String toString() {
        return String.format("Truck [ID: %d, License Plate: %s, Model: %s, Fuel Level: %.1f, Engine Type: %s, Cargo Capacity: %.1f, Mileage: %.1f, Has refrigeration unit: %b]",
                getId(), getLicensePlate(), getModel(), getFuelLevel(), getEngineType(), cargoCapacity, getMileage(), hasRefrigerationUnit);
    }
}

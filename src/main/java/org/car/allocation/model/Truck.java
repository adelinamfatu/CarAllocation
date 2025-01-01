package org.car.allocation.model;

import jakarta.persistence.*;

@Entity
@Table(name = "trucks")
public class Truck extends Vehicle {
    @Column(nullable = false)
    private double cargoCapacity;

    public Truck() {}

    public Truck(String licensePlate, String model, double fuelLevel, double cargoCapacity) {
        super(licensePlate, model, fuelLevel);
        this.cargoCapacity = cargoCapacity;
    }

    public double getCargoCapacity() {
        return cargoCapacity;
    }

    public void setCargoCapacity(double cargoCapacity) {
        this.cargoCapacity = cargoCapacity;
    }
}

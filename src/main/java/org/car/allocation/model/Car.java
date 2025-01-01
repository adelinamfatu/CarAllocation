package org.car.allocation.model;

import jakarta.persistence.*;

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

    public int getPassengerCapacity() {
        return passengerCapacity;
    }

    public void setPassengerCapacity(int passengerCapacity) {
        this.passengerCapacity = passengerCapacity;
    }
}

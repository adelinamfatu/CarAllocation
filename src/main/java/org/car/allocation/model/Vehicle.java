package org.car.allocation.model;

import jakarta.persistence.*;

import org.car.allocation.observer.VehicleObserver;
import org.car.allocation.observer.VehicleStatusNotifier;

@MappedSuperclass
public abstract class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected int id;

    @Column(nullable = false, unique = true)
    protected String licensePlate;

    @Column(nullable = false)
    protected String model;

    @Column(nullable = false)
    protected boolean isAvailable;

    @Column(nullable = false)
    protected double fuelLevel;

    @Column(nullable = false)
    protected boolean isInMaintenance;

    @Column(nullable = false)
    protected double mileage;

    @Transient
    private VehicleStatusNotifier statusNotifier = new VehicleStatusNotifier();

    public Vehicle () {}

    public Vehicle(String licensePlate, String model, double fuelLevel) {
        this.licensePlate = licensePlate;
        this.model = model;
        this.fuelLevel = fuelLevel;
        this.isAvailable = true;
        this.isInMaintenance = false;
        this.mileage = 0.0;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
        statusNotifier.notifyObservers("Availability changed to: " + available);
    }

    public double getFuelLevel() {
        return fuelLevel;
    }

    public void setFuelLevel(double fuelLevel) {
        this.fuelLevel = fuelLevel;
    }

    public boolean isInMaintenance() {
        return isInMaintenance;
    }

    public void setInMaintenance(boolean inMaintenance) {
        isInMaintenance = inMaintenance;
    }

    public double getMileage() {
        return mileage;
    }

    public void setMileage(double mileage) {
        this.mileage = mileage;
    }

    public void addObserver(VehicleObserver observer) {
        statusNotifier.addObserver(observer);
    }

    public void removeObserver(VehicleObserver observer) {
        statusNotifier.removeObserver(observer);
    }
}

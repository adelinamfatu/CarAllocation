package org.car.allocation.model;

import jakarta.persistence.*;
import org.car.allocation.observer.VehicleObserver;
import org.car.allocation.observer.VehicleStatusNotifier;
import org.car.allocation.util.EngineType;
import org.car.allocation.util.VehicleStatus;

/**
 * The abstract Vehicle class represents a generic vehicle with basic attributes such as
 * license plate, model, fuel level, availability, and maintenance status. It also
 * supports the observer pattern to notify when the vehicle's availability changes.
 */
@MappedSuperclass
public abstract class Vehicle {
    /**
     * The unique identifier for the vehicle.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected int id;

    /**
     * The license plate of the vehicle, which must be unique.
     */
    @Column(nullable = false, unique = true)
    protected String licensePlate;

    @Column(nullable = false)
    protected String model;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    protected VehicleStatus vehicleStatus;

    @Column(nullable = false)
    protected double fuelLevel;

    @Column(nullable = false)
    protected double mileage;

    @Column(nullable = false)
    private double maxSpeed;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    protected EngineType engineType;

    /**
     * A transient notifier for vehicle status changes, used to implement the observer pattern.
     */
    @Transient
    private VehicleStatusNotifier statusNotifier = new VehicleStatusNotifier();

    public Vehicle () {}

    public Vehicle(String licensePlate, String model, double fuelLevel, double maxSpeed, EngineType engineType, double mileage) {
        this.licensePlate = licensePlate;
        this.model = model;
        this.fuelLevel = fuelLevel;
        this.engineType = engineType;
        this.maxSpeed = maxSpeed;
        this.vehicleStatus = VehicleStatus.AVAILABLE; //Default status
        this.mileage = mileage;
    }

    public int getId() { return id; }

    /**
     * Retrieves the license plate of the vehicle.
     * @return The license plate of the vehicle.
     */
    public String getLicensePlate() {
        return licensePlate;
    }

    /**
     * Sets the license plate of the vehicle.
     * @param licensePlate The new license plate of the vehicle.
     */
    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public double getFuelLevel() {
        return fuelLevel;
    }

    public void setFuelLevel(double fuelLevel) {
        this.fuelLevel = fuelLevel;
    }

    public double getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(double maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public void setId(int id) {
        this.id = id;
    }

    public VehicleStatus getVehicleStatus() {
        return vehicleStatus;
    }

    public void setVehicleStatus(VehicleStatus vehicleStatus) {
        this.vehicleStatus = vehicleStatus;
        statusNotifier.notifyObservers("Vehicle status changed to: " + vehicleStatus);
    }

    public double getMileage() {
        return mileage;
    }

    public void setMileage(double mileage) {
        this.mileage = mileage;
    }

    public EngineType getEngineType() {
        return engineType;
    }

    public void setEngineType(EngineType engineType) {
        this.engineType = engineType;
    }

    /**
     * Adds an observer to monitor the vehicle's status changes.
     * @param observer The observer to be added.
     */
    public void addObserver(VehicleObserver observer) {
        statusNotifier.addObserver(observer);
    }

    /**
     * Removes an observer from monitoring the vehicle's status changes.
     * @param observer The observer to be removed.
     */
    public void removeObserver(VehicleObserver observer) {
        statusNotifier.removeObserver(observer);
    }

    public void releaseVehicle(double newMileage) {
        setMileage(newMileage);
        setVehicleStatus(VehicleStatus.AVAILABLE);
        statusNotifier.notifyObservers("Vehicle released and available with new mileage: " + newMileage);
    }

/**
 * Puts the vehicle into maintenance if it is not already in maintenance.

 * This method checks the current status of the vehicle. If the vehicle's status is not already
 * set to "IN_MAINTENANCE", it updates the status to "IN_MAINTENANCE" and notifies observers
 * of the change. A message is printed to indicate that the vehicle has been placed in maintenance.
 * If the vehicle is already in maintenance, a message is printed stating that the vehicle is already
 * in maintenance.
 * @throws IllegalStateException if the status change cannot be made (e.g., due to permissions).
 */
public void putInMaintenance() {
    if (this.vehicleStatus != VehicleStatus.IN_MAINTENANCE) {
        setVehicleStatus(VehicleStatus.IN_MAINTENANCE);
        System.out.println("Vehiculul a fost pus în mentenanță.");
    } else {
        System.out.println("Vehiculul este deja în mentenanță.");
    }
}
}

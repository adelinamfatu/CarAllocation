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

    /**
     * A transient notifier for vehicle status changes, used to implement the observer pattern.
     */
    @Transient
    private VehicleStatusNotifier statusNotifier = new VehicleStatusNotifier();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    protected EngineType engineType;

    public Vehicle () {}

    public Vehicle(String licensePlate, String model, double fuelLevel, EngineType engineType) {
        this.licensePlate = licensePlate;
        this.model = model;
        this.fuelLevel = fuelLevel;
        this.engineType = engineType;
        this.vehicleStatus = VehicleStatus.AVAILABLE; //Default status
        this.mileage = 0.0;
    }

    public Vehicle(String licensePlate, String model, double fuelLevel) {
        this.licensePlate = licensePlate;
        this.model = model;
        this.fuelLevel = fuelLevel;
    }

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

    /**
     * Retrieves the model of the vehicle.
     * @return The model of the vehicle.
     */
    public String getModel() {
        return model;
    }

    /**
     * Sets the model of the vehicle.
     * @param model The new model of the vehicle.
     */
    public void setModel(String model) {
        this.model = model;
    }


    /**
     * Retrieves the current fuel level of the vehicle.
     * @return The fuel level of the vehicle.
     */
    public double getFuelLevel() {
        return fuelLevel;
    }

    /**
     * Sets the fuel level of the vehicle.
     * @param fuelLevel The new fuel level of the vehicle.
     */
    public void setFuelLevel(double fuelLevel) {
        this.fuelLevel = fuelLevel;
    }

    /**
     * Retrieves the current status of the vehicle.
     * @return The current status (VehicleStatus) of the vehicle.
     */
    public VehicleStatus getVehicleStatus() {
        return vehicleStatus;
    }

    /**
     * Sets the status of the vehicle and notifies observers of the change.
     * @param vehicleStatus The new status of the vehicle.
     */
    public void setVehicleStatus(VehicleStatus vehicleStatus) {
        this.vehicleStatus = vehicleStatus;
        statusNotifier.notifyObservers("Vehicle status changed to: " + vehicleStatus);
    }

    /**
     * Retrieves the total mileage of the vehicle.
     * @return The mileage of the vehicle.
     */
    public double getMileage() {
        return mileage;
    }

    /**
     * Sets the mileage of the vehicle.
     * @param mileage The new mileage of the vehicle.
     */
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
}

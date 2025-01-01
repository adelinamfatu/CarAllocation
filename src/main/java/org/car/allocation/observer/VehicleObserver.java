package org.car.allocation.observer;

/**
 * The VehicleObserver interface defines the contract for classes that want
 * to observe changes in the status of a vehicle. Implementing classes will
 * be notified when the status of a vehicle changes.
 */
public interface VehicleObserver {
    /**
     * This method is called to notify the observer about the change in the
     * vehicle's status.
     *
     * @param vehicleStatus The new status of the vehicle.
     */
    void update(String vehicleStatus);
}

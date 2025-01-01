package org.car.allocation.observer;

/**
 * The User class represents a user who is notified of vehicle status changes.
 * This class implements the VehicleObserver interface and defines the
 * behavior for receiving notifications when the vehicle's status changes.
 */
public class User implements VehicleObserver {
    /**
     * The name of the user who will be notified.
     */
    private String name;

    public User(String name) {
        this.name = name;
    }

    /**
     * This method is called when the vehicle's status changes.
     * It notifies the user with the updated status of the vehicle.
     * @param vehicleStatus The new status of the vehicle.
     */
    @Override
    public void update(String vehicleStatus) {
        System.out.println(name + " notified: Vehicle status changed to " + vehicleStatus);
    }
}

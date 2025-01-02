package org.car.allocation.observer;

import org.car.allocation.model.UserRole;
import org.car.allocation.model.Vehicle;
import org.car.allocation.model.VehicleStatus;

/**
 * The User class represents a user who is notified of vehicle status changes.
 * This class implements the VehicleObserver interface and defines the
 * behavior for receiving notifications when the vehicle's status changes.
 */
public class User implements VehicleObserver {

    private String username;
    private UserRole role;

    // Constructor to create a user with a specific role
    public User(String username, UserRole role) {
        this.username = username;
        this.role = role;
    }

    // Get the username of the user
    public String getUsername() {
        return username;
    }

    // Get the role of the user
    public UserRole getRole() {
        return role;
    }

    /**
     * This method is called when the vehicle's status changes. It grants or denies access
     * based on the user's role and the vehicle's current status.
     */
    @Override
    public void update(String vehicleStatus) {
        System.out.println("User " + username + " notified: " + vehicleStatus);

        // Logic based on user role and vehicle status
        if (vehicleStatus.contains("Vehicle status changed to: AVAILABLE")) {
            if (role == UserRole.DRIVER) {
                System.out.println("Driver " + username + " can view vehicle details.");
            } else if (role == UserRole.MANAGER) {
                System.out.println("Manager " + username + " can reserve the vehicle.");
            } else if (role == UserRole.ADMIN) {
                System.out.println("Admin " + username + " has full access to the vehicle.");
            }
        } else if (vehicleStatus.contains("Vehicle status changed to: IN_USE")) {
            if (role == UserRole.DRIVER) {
                System.out.println("Driver " + username + " cannot view vehicle details as it is in use.");
            } else if (role == UserRole.MANAGER) {
                System.out.println("Manager " + username + " cannot reserve the vehicle as it is in use.");
            }
        } else if (vehicleStatus.contains("Vehicle status changed to: IN_MAINTENANCE")) {
            if (role == UserRole.DRIVER) {
                System.out.println("Driver " + username + " cannot view vehicle details as it is in maintenance.");
            } else if (role == UserRole.MANAGER) {
                System.out.println("Manager " + username + " cannot reserve the vehicle as it is in maintenance.");
            }
        } else if (vehicleStatus.contains("Vehicle status changed to: RESERVED")) {
            System.out.println("Manager " + username + " cannot reserve the vehicle as it is already reserved.");
        }
    }

    /**
     * User-specific access to reserve a vehicle. Available only if the vehicle is AVAILABLE
     */
    public void reserveVehicle(Vehicle vehicle) {
        if (vehicle.getVehicleStatus() == VehicleStatus.AVAILABLE && role == UserRole.MANAGER) {
            System.out.println("Manager " + username + " has reserved the vehicle.");
            vehicle.setVehicleStatus(VehicleStatus.RESERVED); // Mark the vehicle as reserved
        } else {
            System.out.println("Manager " + username + " cannot reserve the vehicle.");
        }
    }
}

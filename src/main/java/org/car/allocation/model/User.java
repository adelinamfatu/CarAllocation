package org.car.allocation.model;

import org.car.allocation.observer.VehicleObserver;
import org.car.allocation.util.PermissionManager;
import org.car.allocation.util.UserRole;
import org.car.allocation.util.VehicleStatus;
import jakarta.persistence.*;

/**
 * The User class represents a user who is notified of vehicle status changes.
 * This class implements the VehicleObserver interface and defines the
 * behavior for receiving notifications when the vehicle's status changes.
 */
@Entity
@Table(name = "users")
public class User implements VehicleObserver {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private UserRole role;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email")
    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;
    @Column(name = "username", unique = true, nullable = false)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @ManyToOne
    @JoinColumn(name = "car_id", referencedColumnName = "id", nullable = true)
    private Car car;

    @ManyToOne
    @JoinColumn(name = "truck_id", referencedColumnName = "id", nullable = true)
    private Truck truck;

    public User(UserRole role, String firstName, String lastName, String email, String phoneNumber, String username, String password, Car car, Truck truck) {
        this.role = role;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.username = username;
        this.password = password;
        this.car = car;
        this.truck = truck;
    }

    public User(String username, UserRole admin) {}

    public User() {
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public Truck getTruck() {
        return truck;
    }

    public void setTruck(Truck truck) {
        this.truck = truck;
    }

    /**
     * This method is called when the vehicle's status changes. It grants or denies access
     * based on the user's role and the vehicle's current status.
     */
    @Override
    public void update(String vehicleStatus) {
        System.out.println("User " + firstName + " notified: " + vehicleStatus);

        VehicleStatus status = VehicleStatus.valueOf(vehicleStatus.split(": ")[1]);

        if (PermissionManager.isActionAllowed(role, status, "VIEW")) {
            System.out.println(role + " " + firstName + " can view vehicle details.");
        } else if (PermissionManager.isActionAllowed(role, status, "RESERVE")) {
            System.out.println(role + " " + firstName + " can reserve the vehicle.");
        } else if (PermissionManager.isActionAllowed(role, status, "FULL_ACCESS")) {
            System.out.println(role + " " + firstName + " has full access to the vehicle.");
        } else {
            System.out.println(role + " " + firstName + " cannot perform any actions on the vehicle.");
        }
    }

    /**
     * User-specific access to reserve a vehicle. Available only if the vehicle is AVAILABLE
     */
    public void reserveVehicle(Vehicle vehicle) {
        if (PermissionManager.isActionAllowed(role, vehicle.getVehicleStatus(), "RESERVE")) {
            System.out.println(role + " " + firstName + " has reserved the vehicle.");
            vehicle.setVehicleStatus(VehicleStatus.IN_USE);
        } else {
            System.out.println(role + " " + firstName + " cannot reserve the vehicle.");
        }
    }
}

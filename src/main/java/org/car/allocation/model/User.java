package org.car.allocation.model;

import org.car.allocation.observer.VehicleObserver;
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

    public User(String username, UserRole admin) {}

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

        //Logic based on user role and vehicle status
        if (vehicleStatus.contains("Vehicle status changed to: AVAILABLE")) {
            if (role == UserRole.DRIVER) {
                System.out.println("Driver " + firstName + " can view vehicle details.");
            } else if (role == UserRole.MANAGER) {
                System.out.println("Manager " + firstName + " can reserve the vehicle.");
            } else if (role == UserRole.ADMIN) {
                System.out.println("Admin " + firstName + " has full access to the vehicle.");
            }
        } else if (vehicleStatus.contains("Vehicle status changed to: IN_USE")) {
            if (role == UserRole.DRIVER) {
                System.out.println("Driver " + firstName + " cannot view vehicle details as it is in use.");
            } else if (role == UserRole.MANAGER) {
                System.out.println("Manager " + firstName + " cannot reserve the vehicle as it is in use.");
            }
        } else if (vehicleStatus.contains("Vehicle status changed to: IN_MAINTENANCE")) {
            if (role == UserRole.DRIVER) {
                System.out.println("Driver " + firstName + " cannot view vehicle details as it is in maintenance.");
            } else if (role == UserRole.MANAGER) {
                System.out.println("Manager " + firstName + " cannot reserve the vehicle as it is in maintenance.");
            }
        } else if (vehicleStatus.contains("Vehicle status changed to: RESERVED")) {
            System.out.println("Manager " + firstName + " cannot reserve the vehicle as it is already reserved.");
        }
    }

    /**
     * User-specific access to reserve a vehicle. Available only if the vehicle is AVAILABLE
     */
    public void reserveVehicle(Vehicle vehicle) {
        if (vehicle.getVehicleStatus() == VehicleStatus.AVAILABLE && role == UserRole.MANAGER) {
            System.out.println("Manager " + firstName + " has reserved the vehicle.");
            vehicle.setVehicleStatus(VehicleStatus.RESERVED); //Mark the vehicle as reserved
        } else {
            System.out.println("Manager " + firstName + " cannot reserve the vehicle.");
        }
    }
}

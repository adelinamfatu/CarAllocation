package org.car.allocation.observer;


public class User implements VehicleObserver {
    private String name;

    public User(String name) {
        this.name = name;
    }

    @Override
    public void update(String vehicleStatus) {
        System.out.println(name + " notified: Vehicle status changed to " + vehicleStatus);
    }
}

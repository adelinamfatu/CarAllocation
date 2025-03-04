package org.car.allocation.observer;

import org.car.allocation.model.User;

import java.util.ArrayList;
import java.util.List;

/**
 * The VehicleStatusNotifier class is responsible for managing the list of
 * observers and notifying them when the status of a vehicle changes.
 * Observers can be added or removed, and when a status update occurs,
 * all registered observers are notified.
 */
public class VehicleStatusNotifier {
    private List<VehicleObserver> observers = new ArrayList<>();

    /**
     * Adds a new observer to the list of observers.
     * @param observer The observer to be added.
     */
    public void addObserver(VehicleObserver observer) {
        observers.add(observer);
        if (observer instanceof User) {
            User user = (User) observer;
            System.out.println("Added observer: " + user.getFirstName() + " " + user.getLastName());
        } else {
            System.out.println("Added observer: " + observer);
        }
    }

    /**
     * Removes an observer from the list of observers.
     * @param observer The observer to be removed.
     */
    public void removeObserver(VehicleObserver observer) {
        if (observer instanceof User) {
            User user = (User) observer;
            System.out.println("Remove observer: " + user.getFirstName() + " " + user.getLastName());
        } else {
            System.out.println("Added observer: " + observer);
        }
        observers.remove(observer);
    }

    /**
     * Notifies all registered observers about a change in the vehicle's
     * status by calling their update method.
     * @param status The new status of the vehicle.
     */
    public void notifyObservers(String status) {
        System.out.println("Notifying observers about the status change: " + status);
        for (VehicleObserver observer : observers) {
            observer.update(status);
        }
    }

    public List<VehicleObserver> getObservers() {
        return observers;
    }
}

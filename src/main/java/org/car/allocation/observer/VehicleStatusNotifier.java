package org.car.allocation.observer;


import java.util.ArrayList;
import java.util.List;

public class VehicleStatusNotifier {
    private List<VehicleObserver> observers = new ArrayList<>();

    public void addObserver(VehicleObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(VehicleObserver observer) {
        observers.remove(observer);
    }

    public void notifyObservers(String status) {
        for (VehicleObserver observer : observers) {
            observer.update(status);
        }
    }
}

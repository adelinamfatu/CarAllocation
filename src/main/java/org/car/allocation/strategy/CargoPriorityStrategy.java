package org.car.allocation.strategy;

import org.car.allocation.model.Vehicle;
import org.car.allocation.model.Truck;
import java.util.List;

/**
 * Implements the AllocationStrategy interface to prioritize trucks based on their cargo capacity.
 * This strategy selects the truck with the highest cargo capacity from the list of available trucks,
 * optimizing for the ability to transport the largest possible load.
 */
public class CargoPriorityStrategy implements AllocationStrategy {
    @Override
    public Vehicle allocate(List<Vehicle> availableVehicles) {
        Truck selectedTruck = null;
        double maxCargoCapacity = 0.0;

        for (Vehicle vehicle : availableVehicles) {
            if (vehicle instanceof Truck) {
                Truck truck = (Truck) vehicle;
                if (truck.getCargoCapacity() > maxCargoCapacity) {
                    maxCargoCapacity = truck.getCargoCapacity();
                    selectedTruck = truck;
                }
            }
        }
        return selectedTruck;
    }
}

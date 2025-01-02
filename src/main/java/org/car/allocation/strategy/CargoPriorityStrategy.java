package org.car.allocation.strategy;

import org.car.allocation.model.Vehicle;
import org.car.allocation.model.Truck;
import java.util.List;

/**
 * Implements the AllocationStrategy interface to prioritize trucks for cargo transport tasks.
 * It allocates a truck that has a positive cargo capacity, suitable for freight tasks.
 */
public class CargoPriorityStrategy implements AllocationStrategy {
    @Override
    public Vehicle allocate(List<Vehicle> availableVehicles) {
        for (Vehicle vehicle : availableVehicles) {
            if (vehicle instanceof Truck) {
                Truck truck = (Truck) vehicle;
                // Assume that only trucks with positive cargo capacity are eligible
                if (truck.getCargoCapacity() > 0) {
                    return truck;
                }
            }
        }
        // Return null if no suitable truck is available
        return null;
    }
}

package org.car.allocation.util;

import java.util.*;

public class PermissionManager {
    private static final Map<UserRole, Map<VehicleStatus, List<String>>> permissions = new HashMap<>();

    static {
        //Define permissions
        Map<VehicleStatus, List<String>> driverPermissions = new HashMap<>();
        driverPermissions.put(VehicleStatus.AVAILABLE, Arrays.asList("VIEW", "RESERVE"));
        driverPermissions.put(VehicleStatus.IN_USE, Arrays.asList("DENY_VIEW"));
        driverPermissions.put(VehicleStatus.IN_MAINTENANCE, Arrays.asList("DENY_VIEW"));

        Map<VehicleStatus, List<String>> managerPermissions = new HashMap<>();
        managerPermissions.put(VehicleStatus.AVAILABLE, Arrays.asList("RESERVE", "PUT_IN_MAINTENANCE"));
        managerPermissions.put(VehicleStatus.IN_USE, Arrays.asList("DENY_RESERVE", "DENY_PUT_IN_MAINTENANCE"));
        managerPermissions.put(VehicleStatus.IN_MAINTENANCE, Arrays.asList("RESERVE", "DENY_PUT_IN_MAINTENANCE"));
        managerPermissions.put(VehicleStatus.RESERVED, Arrays.asList("DENY_RESERVE", "DENY_PUT_IN_MAINTENANCE"));


        Map<VehicleStatus, List<String>> adminPermissions = new HashMap<>();
        adminPermissions.put(VehicleStatus.AVAILABLE, Arrays.asList("FULL_ACCESS"));
        adminPermissions.put(VehicleStatus.IN_USE, Arrays.asList("FULL_ACCESS"));
        adminPermissions.put(VehicleStatus.IN_MAINTENANCE, Arrays.asList("FULL_ACCESS"));

        permissions.put(UserRole.DRIVER, driverPermissions);
        permissions.put(UserRole.MANAGER, managerPermissions);
        permissions.put(UserRole.ADMIN, adminPermissions);
    }

    /**
     * Check if a role has a specific action for a vehicle status.
     * @param role The user role.
     * @param status The current vehicle status.
     * @param action The action to check.
     * @return true if the action is allowed, false otherwise.
     */
    public static boolean isActionAllowed(UserRole role, VehicleStatus status, String action) {
        Map<VehicleStatus, List<String>> rolePermissions = permissions.get(role);
        if (rolePermissions == null) {
            return false;
        }
        List<String> actions = rolePermissions.get(status);
        return actions != null && actions.contains(action);
    }

    /**
     * Get all actions allowed for a role and a vehicle status.
     * @param role The user role.
     * @param status The current vehicle status.
     * @return List of allowed actions.
     */
    public static List<String> getAllowedActions(UserRole role, VehicleStatus status) {
        Map<VehicleStatus, List<String>> rolePermissions = permissions.get(role);
        if (rolePermissions == null) {
            return Collections.emptyList();
        }
        return rolePermissions.getOrDefault(status, Collections.emptyList());
    }
}
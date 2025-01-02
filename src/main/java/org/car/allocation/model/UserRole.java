package org.car.allocation.model;


/**
 * Enum for the user roles. Defines different types of users and their associated permissions.
 */
public enum UserRole {
    ADMIN,    // Full access to all operations
    MANAGER,  // Can reserve vehicles, but cannot perform maintenance
    DRIVER    // Can only view vehicle status
}

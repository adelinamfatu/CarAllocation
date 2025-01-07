package org.car.allocation.util;

import org.car.allocation.model.User;
/**
 * This class provides a context for accessing the currently logged-in user.
 * <p>
 * The {@link LoggedInUserContext} class maintains a static reference to the user
 * who is currently logged in, allowing the system to retrieve or set the logged-in user
 * across different parts of the application.
 * </p>
 * <p>
 * It is important to ensure that the logged-in user is properly set before accessing it.
 * </p>
 */
public class LoggedInUserContext {
    private static User loggedInUser;

    public static User getLoggedInUser() {
        return loggedInUser;
    }

    public static void setLoggedInUser(User user) {
        loggedInUser = user;
    }
}

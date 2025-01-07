package org.car.allocation.util;

import org.car.allocation.model.User;

public class LoggedInUserContext {
    private static User loggedInUser;

    public static User getLoggedInUser() {
        return loggedInUser;
    }

    public static void setLoggedInUser(User user) {
        loggedInUser = user;
    }
}

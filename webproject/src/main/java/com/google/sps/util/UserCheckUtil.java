package com.google.sps.util;

import com.google.sps.authentication.AuthenticationHandler;
import com.google.sps.dataManagers.UserRoomManager;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.ServletException;

/**
 * A utility class for checking if a User is a member of a Room.
 */
@Singleton
public class UserCheckUtil {
    private static AuthenticationHandler authenticationHandler;
    private static UserRoomManager userRoomManager;

    @Inject
    public UserCheckUtil(AuthenticationHandler authenticationHandler, UserRoomManager userRoomManager) {
        this.authenticationHandler = authenticationHandler;
        this.userRoomManager = userRoomManager;
    }

    public static boolean isRoomMember(String roomId) throws IOException, ServletException {
        String userEmail = authenticationHandler.getCurrentUser().getEmail();

        if (userRoomManager.getUserRoom(userEmail, roomId) == null) {
            return false;
        }

        return true;
    }
}

package com.google.sps.servlets;

import java.io.IOException;

import com.google.sps.proto.UserRoomStatusProto.UserRoomStatusResponse;
import com.google.sps.proto.UserRoomStatusProto.UserRoomStatusRequest;
import com.google.sps.services.interfaces.UserRoomStatusService;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * A servlet which manages fetching whether a user has joined a room.
 */
@Singleton
public class UserRoomStatusServlet extends HttpServlet {
    private UserRoomStatusService userRoomStatusService;

    @Inject
    public UserRoomStatusServlet(UserRoomStatusService userRoomStatusService) {
        this.userRoomStatusService = userRoomStatusService;
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String roomId = request.getParameter("roomId");

        UserRoomStatusRequest userRoomJoinRequest = UserRoomStatusRequest.newBuilder().setRoomId(roomId).build();
        UserRoomStatusResponse userRoomStatusResponse = userRoomStatusService.execute(userRoomJoinRequest);

        response.setContentType("text/plain");
        if (userRoomStatusResponse.getIsJoined()) {
            response.getWriter().print("Chat");
        } else {
            response.getWriter().print("Join");
        }
    }
}

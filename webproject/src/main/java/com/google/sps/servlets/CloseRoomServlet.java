package com.google.sps.servlets;

import com.google.protobuf.util.JsonFormat;
import com.google.sps.firebase.Firebase;
import com.google.sps.protoc.CloseRoomProtoc.CloseRoomRequest;
import com.google.sps.protoc.CloseRoomProtoc.CloseRoomResponse;
import com.google.sps.services.interfaces.CloseRoomService;

import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * A servlet which manages room closure.
 */
@Singleton
public class CloseRoomServlet extends HttpServlet {
    public CloseRoomService closeRoomService;

    @Inject
    public CloseRoomServlet(CloseRoomService closeRoomService) {
        this.closeRoomService = closeRoomService;
    }
    /**
     * Called by the server close a room.
     * @param request An HttpServletRequest object that contains the request the client has made of the servlet.
     * @param response An HttpServletResponse object that contains the response the servlet sends to the client.
     * @throws IOException If an input or output error is detected when the servlet handles the server's request.
     */
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String roomId = request.getParameter("roomId");

        CloseRoomRequest.Builder closeRoomRequest = CloseRoomRequest.newBuilder();
        closeRoomRequest.setRoomId(roomId);
        CloseRoomResponse closeRoomResponse = closeRoomService.execute(closeRoomRequest.build());

        response.setContentType("application/json; charset=UTF-8;");
        response.getWriter().println(JsonFormat.printer().print(closeRoomResponse));
        response.sendRedirect("/");
    }
}

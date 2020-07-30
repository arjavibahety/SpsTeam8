package com.google.sps.servlets;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.protobuf.util.JsonFormat;
import com.google.sps.authentication.AuthenticationHandler;
import com.google.sps.services.interfaces.JoinService;
import com.google.sps.protoc.JoinProtoc.JoinRequest;
import com.google.sps.protoc.JoinProtoc.JoinResponse;

import java.io.*;
import java.util.Collections;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.users.User;
import com.google.firebase.database.FirebaseDatabase;
import com.google.sps.data.UserRoom;

@Singleton
public class JoinServlet extends HttpServlet {
    private JoinService joinService;

    @Inject
    public JoinServlet(JoinService joinService) {
        this.joinService = joinService;
    }

    @Override
    public void init() {
        try {
            FirebaseOptions options = joinService.getFirebaseOptions();
            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
            }
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        AuthenticationHandler auth = new AuthenticationHandler();
        if (!auth.isUserLoggedIn()) {
            response.setStatus(400);
            return;
        }

        User user = (new AuthenticationHandler()).getCurrentUser();
        String userEmail = user.getEmail();
        String roomId = request.getParameter("roomId");
        for (String name: Collections.list(request.getParameterNames())) {
            System.out.println(name);
        }
        FirebaseDatabase.getInstance().getReference("UserRoom").push().setValueAsync(new UserRoom(userEmail, roomId));

        response.setStatus(200);
        response.getWriter().println(roomId);
        
        
        // AuthenticationHandler auth = joinService.getAuthenticationHandler();
        // if (!auth.isUserLoggedIn()) {
        //     response.setStatus(400);
        //     return;
        // }

        // String roomId = request.getParameter("roomId");

        // JoinRequest.Builder joinRequest = JoinRequest.newBuilder();
        // joinRequest.setRoomId(roomId);

        // // JoinResponse joinResponse = joinService.executePost(joinRequest.build());
        // // joinService.executePost(joinRequest.build());

        // User user = joinService.getCurrentUser();
        // String userEmail = user.getEmail();
        
        // FirebaseDatabase.getInstance()
        //         .getReference("UserRoom")
        //         .push()
        //         .setValueAsync(new UserRoom(userEmail, roomId));

        // // response.setContentType("application/json; charset=UTF-8;");
        // // response.getWriter().println(JsonFormat.printer().print(joinResponse));
        // response.setStatus(200);
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String roomId = request.getParameter("roomId");
        JoinRequest.Builder joinRequest = JoinRequest.newBuilder();
        joinRequest.setRoomId(roomId);

        String joinResponse = joinService.executeGet(joinRequest.build());
        response.setContentType("text/plain");
        if (joinResponse.substring(0, 2).equals("{}")) {
            response.getWriter().print("Join");
        } else {
            response.getWriter().print("Chat");
        }
    }
}

package com.google.sps.servlets;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.sps.services.interfaces.GetJoinService;
import com.google.sps.protoc.GetJoinProtoc.GetJoinRequest;

import java.io.*;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Singleton
public class GetJoinServlet extends HttpServlet {
    private GetJoinService getJoinService;

    @Inject
    public GetJoinServlet(GetJoinService getJoinService) {
        this.getJoinService = getJoinService;
    }

    @Override
    public void init() {
        try {
            FirebaseOptions options = getJoinService.getFirebaseOptions();
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
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String roomId = request.getParameter("roomId");
        GetJoinRequest.Builder getJoinRequest = GetJoinRequest.newBuilder();
        getJoinRequest.setRoomId(roomId);

        String getJoinResponse = getJoinService.execute(getJoinRequest.build());
        response.setContentType("text/plain");
        if (getJoinResponse.substring(0, 2).equals("{}")) {
            response.getWriter().print("Join");
        } else {
            response.getWriter().print("Chat");
        }
    }
}

package com.google.sps.servlets;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.protobuf.util.JsonFormat;
import com.google.sps.authentication.AuthenticationHandler;
import com.google.sps.services.interfaces.PostJoinService;
import com.google.sps.protoc.PostJoinProtoc.PostJoinRequest;
import com.google.sps.protoc.PostJoinProtoc.PostJoinResponse;

import java.io.*;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Singleton
public class PostJoinServlet extends HttpServlet {
    private PostJoinService postJoinService;

    @Inject
    public PostJoinServlet(PostJoinService postJoinService) {
        this.postJoinService = postJoinService;
    }

    @Override
    public void init() {
        try {
            FirebaseOptions options = postJoinService.getFirebaseOptions();
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
        AuthenticationHandler auth = postJoinService.getAuthenticationHandler();
        if (!auth.isUserLoggedIn()) {
            response.setStatus(400);
            return;
        }

        String roomId = request.getParameter("roomId");

        PostJoinRequest.Builder postJoinRequest = PostJoinRequest.newBuilder();
        postJoinRequest.setRoomId(roomId);

        PostJoinResponse postJoinResponse = postJoinService.execute(postJoinRequest.build());

        response.setContentType("application/json; charset=UTF-8;");
        response.getWriter().println(JsonFormat.printer().print(postJoinResponse));
        response.setStatus(200);
    }
}

package com.google.sps.servlets;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.protobuf.util.JsonFormat;
import com.google.sps.authentication.AuthenticationHandler;
import com.google.sps.services.interfaces.PostJoinService;
import com.google.sps.proto.PostJoinProto.PostJoinRequest;
import com.google.sps.proto.PostJoinProto.PostJoinResponse;
import com.google.sps.util.AuthHandlerUtil;
import com.google.sps.util.FirebaseUtil;

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
        FirebaseUtil.initializeFirebase();
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        AuthenticationHandler auth = AuthHandlerUtil.getAuthenticationHandler();
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

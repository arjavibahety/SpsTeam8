package com.google.sps.servlets;

import com.google.inject.Inject;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.protobuf.util.JsonFormat;
import com.google.sps.protoc.TextMessageProtoc.TextMessageRequest;
import com.google.sps.protoc.TextMessageProtoc.TextMessageResponse;
import com.google.sps.services.interfaces.TextMessageService;

import java.io.FileNotFoundException;
import java.io.IOException;

import javax.inject.Singleton;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Singleton
public class TextMessageServlet extends HttpServlet {
    private TextMessageService textMessageService;

    @Inject
    public TextMessageServlet(TextMessageService textMessageService) {
        this.textMessageService = textMessageService;
    }

    @Override
    public void init() {
        try {
            // Initialize the app with a service account, granting admin privileges
            FirebaseOptions options = textMessageService.getFirebaseOptions();
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
        String message = request.getParameter("textmessage");
        String referrer = request.getHeader("referer");
        String[] array = referrer.split("\\?");
        String roomID = array[1];

        TextMessageRequest.Builder textMessageRequest = TextMessageRequest.newBuilder();
        textMessageRequest.setReferrer(referrer);
        textMessageRequest.setMessage(message);
        textMessageRequest.setRoomId(roomID);

        TextMessageResponse textMessageResponse = textMessageService.handleRequest(textMessageRequest.build());

        response.setContentType("application/json; charset=UTF-8;");
        response.getWriter().println(JsonFormat.printer().print(textMessageResponse));
        response.sendRedirect(referrer);
    }
}

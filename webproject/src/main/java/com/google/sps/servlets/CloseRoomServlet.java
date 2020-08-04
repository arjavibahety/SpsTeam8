package com.google.sps.servlets;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.DatabaseError;

import com.google.sps.authentication.AuthenticationHandler;
import com.google.sps.firebase.FirebaseHelper;

import java.io.IOException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * A servlet which manages room closure.
 */
public class CloseRoomServlet extends HttpServlet {
    /**
     * Called by the server close a room.
     * @param request An HttpServletRequest object that contains the request the client has made of the servlet.
     * @param response An HttpServletResponse object that contains the response the servlet sends to the client.
     * @throws IOException If an input or output error is detected when the servlet handles the server's request.
     */
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        FirebaseHelper.initialiseFirebaseApp();
        String roomID = request.getParameter("roomId");

        FirebaseDatabase.getInstance()
            .getReference("rooms")
            .child(roomID)
            .removeValue(new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    if (databaseError != null) {
                        System.out.println(databaseError.getMessage());
                    }
                }
            });

        FirebaseDatabase.getInstance()
            .getReference("UserRoom")
            .child(roomID)
            .removeValue(new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    if (databaseError != null) {
                        System.out.println(databaseError.getMessage());
                    }
                }
            });

        response.sendRedirect("/");
    }
}

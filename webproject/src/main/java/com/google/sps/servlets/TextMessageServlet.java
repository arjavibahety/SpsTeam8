package com.google.sps.servlets;

import com.google.gson.Gson;
import com.google.sps.firebase.Firebase;
import com.google.sps.authentication.AuthenticationHandler;
import com.google.appengine.api.users.User;
import com.google.sps.data.Message;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.database.FirebaseDatabase;
import com.google.appengine.api.blobstore.BlobInfo;
import com.google.appengine.api.blobstore.BlobInfoFactory;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.api.images.ServingUrlOptions;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TextMessageServlet extends HttpServlet {
    private static String username;
    private static Gson gson = new Gson();

    @Override
    public void init() {
        AuthenticationHandler handler = new AuthenticationHandler();
        User curr = handler.getCurrentUser();
        username = curr.getNickname();
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Get the URL of the image that the user uploaded to Blobstore.
        String message = request.getParameter("textmessage");

        String referrer = request.getHeader("referer");
        String[] array = referrer.split("\\?");
        String roomID = array[1];

        String messageJson = gson.toJson(new Message(username, message, "text"));
        Firebase.sendRequest("https://summer20-sps-47.firebaseio.com/messages/" + roomID + ".json", "POST", messageJson);

        response.sendRedirect(referrer);
    }
}

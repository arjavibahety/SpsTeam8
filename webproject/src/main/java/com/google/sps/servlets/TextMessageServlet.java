package com.google.sps.servlets;

import com.google.sps.data.Message;
import com.google.sps.firebase.FirebaseHelper;
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

    @Override
    public void init() {
        username = (new FirebaseHelper()).getUserName();
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Get the URL of the image that the user uploaded to Blobstore.
        String message = request.getParameter("textmessage");

        String referrer = request.getHeader("referer");
        String[] array = referrer.split("\\?");
        String roomID = array[1];
        FirebaseDatabase.getInstance()
            .getReference("messages")
            .child(roomID)
            .push()
            .setValueAsync(new Message(username, message, "text"));
        response.sendRedirect(referrer);
    }
}

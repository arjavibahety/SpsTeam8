package com.google.sps.servlets;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.appengine.api.blobstore.BlobInfo;
import com.google.appengine.api.blobstore.BlobInfoFactory;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.images.Image;
import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.api.images.ServingUrlOptions;
import com.google.appengine.api.users.User;
import com.google.firebase.database.FirebaseDatabase;

import com.google.sps.authentication.AuthenticationHandler;
import com.google.sps.data.Message;
import com.google.sps.firebase.FirebaseHelper;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.IllegalArgumentException;
import java.util.List;
import java.util.Map;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class FormHandlerServlet extends HttpServlet {
    private static String username;

    @Override
    public void init() {
        FirebaseHelper.initialiseFirebaseApp();
        AuthenticationHandler handler = new AuthenticationHandler();
        User curr = handler.getCurrentUser();
        username = curr.getNickname();
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Get the URL of the image that the user uploaded to Blobstore.
        String imageUrl = getUploadedFileUrl(request, "image");
        
        String referrer = request.getHeader("referer");

        if (imageUrl == null || imageUrl.equals("")) {
            response.sendRedirect(referrer);
            return;
        }

        String[] array = referrer.split("\\?");
        String roomID = array[1];

        FirebaseDatabase.getInstance()
            .getReference("messages")
            .child(roomID)
            .push()
            .setValueAsync(new Message(username, imageUrl, "image"));
            
        response.sendRedirect(referrer);
    }

    /** Returns a URL that points to the uploaded file, or null if the user didn't upload a file. */
    private String getUploadedFileUrl(HttpServletRequest request, String formInputElementName) {
        BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
        Map<String, List<BlobKey>> blobs = blobstoreService.getUploads(request);
        List<BlobKey> blobKeys = blobs.get("image");

        // User submitted form without selecting a file, so we can't get a URL. (dev server)
        if (blobKeys == null || blobKeys.isEmpty()) {
        return null;
        }

        // Our form only contains a single file input, so get the first index.
        BlobKey blobKey = blobKeys.get(0);

        // User submitted form without selecting a file, so we can't get a URL. (live server)
        BlobInfo blobInfo = new BlobInfoFactory().loadBlobInfo(blobKey);
        if (blobInfo.getSize() == 0) {
            blobstoreService.delete(blobKey);
            return null;
        }

        // Use ImagesService to get a URL that points to the uploaded file.
        ImagesService imagesService = ImagesServiceFactory.getImagesService();
        ServingUrlOptions options = ServingUrlOptions.Builder.withBlobKey(blobKey);
        String url;
        try {
            url = imagesService.getServingUrl(options);
        } catch (IllegalArgumentException e) {
            blobstoreService.delete(blobKey);
            return null;
        }

        // GCS's localhost preview is not actually on localhost,
        // so make the URL relative to the current domain.
        if(url.startsWith("http://localhost:8080/")){
        url = url.replace("http://localhost:8080/", "/");
        }
        return url;
    }
}

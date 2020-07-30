package com.google.sps.services.implementations;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Timestamp;

import com.google.appengine.api.users.User;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.google.sps.authentication.AuthenticationHandler;
import com.google.sps.data.UserRoom;
import com.google.sps.protoc.PostJoinProtoc.PostJoinRequest;
import com.google.sps.protoc.PostJoinProtoc.PostJoinResponse;
import com.google.sps.services.interfaces.PostJoinService;

public class PostJoinServiceImpl implements PostJoinService {
    AuthenticationHandler auth;

    public AuthenticationHandler getAuthenticationHandler() {
        if (auth == null) {
            auth = new AuthenticationHandler();    
        }
        return auth;
    }

    @Override
    public FirebaseOptions getFirebaseOptions() throws IOException {
        // Fetch the service account key JSON file contents
        FileInputStream serviceAccount = new FileInputStream("./key.json");
        
        // Initialize the app with a service account, granting admin privileges
        return new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setDatabaseUrl("https://summer20-sps-47.firebaseio.com")
                .build();
    }

    @Override
    public PostJoinResponse execute(PostJoinRequest postJoinRequest) {
        User user = getCurrentUser();
        String userEmail = user.getEmail();
        
        FirebaseDatabase.getInstance()
                .getReference("UserRoom")
                .push()
                .setValueAsync(new UserRoom(userEmail, postJoinRequest.getRoomId()));

        PostJoinResponse.Builder postJoinResponse = PostJoinResponse.newBuilder();
        postJoinResponse.setRoomId(postJoinRequest.getRoomId());
        postJoinResponse.setTimestamp(getTimestamp());

        return postJoinResponse.build();
    }

    public User getCurrentUser() {
        return getAuthenticationHandler().getCurrentUser();
    }

    public long getTimestamp() {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        return timestamp.getTime();
    }
}

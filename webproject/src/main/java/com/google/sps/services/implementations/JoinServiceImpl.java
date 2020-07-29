package com.google.sps.services.implementations;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Timestamp;

import com.google.appengine.api.users.User;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.google.sps.authentication.AuthenticationHandler;
import com.google.sps.data.UserRoom;
import com.google.sps.protoc.JoinProtoc.JoinRequest;
import com.google.sps.protoc.JoinProtoc.JoinResponse;
import com.google.sps.services.interfaces.JoinService;

public class JoinServiceImpl implements JoinService {
    @Override
    public AuthenticationHandler getAuthenticationHandler() {
        return new AuthenticationHandler();
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
    public JoinResponse executePost(JoinRequest joinRequest) {
        User user = getCurrentUser();
        String userEmail = user.getEmail();

        FirebaseDatabase.getInstance().
                getReference("UserRoom")
                .push().
                setValueAsync(new UserRoom(userEmail, joinRequest.getRoomId()));

        JoinResponse.Builder joinResponse = JoinResponse.newBuilder();
        joinResponse.setRoomId(joinRequest.getRoomId());
        joinResponse.setTimestamp(getTimestamp());

        return joinResponse.build();
    }

    @Override
    public String executeGet(JoinRequest joinRequest) throws IOException {
        String roomId = joinRequest.getRoomId();
        User user = getCurrentUser();
        String userEmail = user.getEmail();

        String url = "https://summer20-sps-47.firebaseio.com/UserRoom.json?orderBy=%22userEmailRoom%22&equalTo=%22"
                + userEmail + "_" + roomId + "%22";
        //Use Firebase class once merged
        HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("Accept", "application/json");
        con.setDoOutput(true);

        String jsonResponse = "";
        try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
            StringBuilder firebaseResponse = new StringBuilder();
            String line;

            while ((line = in.readLine()) != null) {
                firebaseResponse.append(line);
            }

            jsonResponse = firebaseResponse.toString();
            return jsonResponse;
        }
    }

    public User getCurrentUser() {
        return (new AuthenticationHandler()).getCurrentUser();
    }

    public long getTimestamp() {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        return timestamp.getTime();
    }
}

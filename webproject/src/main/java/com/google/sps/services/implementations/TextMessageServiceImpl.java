package com.google.sps.services.implementations;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Timestamp;

import com.google.appengine.api.users.User;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.google.sps.authentication.AuthenticationHandler;
import com.google.sps.data.Message;
import com.google.sps.services.interfaces.TextMessageService;
import com.google.sps.protoc.TextMessageProtoc.TextMessageRequest;
import com.google.sps.protoc.TextMessageProtoc.TextMessageResponse;

public class TextMessageServiceImpl implements TextMessageService {

    @Override
    public FirebaseOptions getFirebaseOptions() throws IOException {
        FileInputStream serviceAccount = new FileInputStream("./key.json");
        return new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setDatabaseUrl("https://summer20-sps-47.firebaseio.com")
                .build();
    }

    public String getUsername() {
        AuthenticationHandler handler = new AuthenticationHandler();
        User curr = handler.getCurrentUser();
        return curr.getNickname();
    }

    @Override
    public TextMessageResponse execute(TextMessageRequest textMessageRequest) {
        String message = textMessageRequest.getMessage();
        String roomID = textMessageRequest.getRoomId();
        String referrer = textMessageRequest.getReferrer();
        FirebaseDatabase.getInstance()
                .getReference("messages")
                .child(roomID)
                .push()
                .setValueAsync(new Message(getUsername(), message, "text"));

        TextMessageResponse.Builder textMessageResponse = TextMessageResponse.newBuilder();
        textMessageResponse.setReferrer(referrer);
        textMessageResponse.setMessage(message);
        textMessageResponse.setRoomId(roomID);
        textMessageResponse.setTimestamp(getTimestamp());

        return textMessageResponse.build();
    }

    public long getTimestamp() {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        return timestamp.getTime();
    }
}

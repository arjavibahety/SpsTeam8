package com.google.sps.dataManagers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.ServletException;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.sps.data.MessageProto.Message;
import com.google.sps.util.FirebaseUtil;
import com.google.sps.util.TimestampUtil;

@Singleton
public class MessagesManager {
    private FirebaseUtil firebaseUtil;
    private UserRoomManager userRoomManager;

    @Inject
    public MessagesManager(FirebaseUtil firebaseUtil, UserRoomManager userRoomManager) {
        this.firebaseUtil = firebaseUtil;
        this.userRoomManager = userRoomManager;
    }

    public List<Message> getMessages(String roomId) throws ServletException {
        if (!userRoomManager.isRoomIdValid(roomId)) {
            throw new ServletException("Invalid roomId");
        }

        DatabaseReference ref = firebaseUtil.getMessagesReference().child(roomId);
        List<DataSnapshot> dataSnapshots = firebaseUtil.getAllSnapshotsFromReference(ref);
        List<Message> messages = dataSnapshots.stream().map(this::toMessage).collect(Collectors.toList());
        return messages;
    }


    public Message addMessage(String user, String message, String roomId) throws ServletException {
        String timestamp = String.valueOf(TimestampUtil.getTimestamp());
        String type = "text";

        Map<String, String> messageMapping = new HashMap<>();
        messageMapping.put("user", user);
        messageMapping.put("message", message);
        messageMapping.put("type", type);
        messageMapping.put("time", timestamp);

        DatabaseReference databaseReference = firebaseUtil.getMessagesReference().child(roomId);
        firebaseUtil.addToDatabase(databaseReference, messageMapping);
        return Message.newBuilder().setMessage(message).setUser(user).setType(type).setTime(timestamp).build();
    }

    private Message toMessage(DataSnapshot dataSnapshot) {
        String user = dataSnapshot.child("user").getValue(String.class);
        String time = dataSnapshot.child("time").getValue(String.class);
        String message = dataSnapshot.child("message").getValue(String.class);
        String type = dataSnapshot.child("type").getValue(String.class);
        Message messageObject =
                Message.newBuilder().setMessage(message).setTime(time).setUser(user).setType(type).build();
        return messageObject;
    }
}

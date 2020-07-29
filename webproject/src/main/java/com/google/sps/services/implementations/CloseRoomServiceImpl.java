package com.google.sps.services.implementations;

import java.io.IOException;
import java.sql.Timestamp;

import com.google.sps.firebase.Firebase;
import com.google.sps.protoc.CloseRoomProtoc.CloseRoomRequest;
import com.google.sps.protoc.CloseRoomProtoc.CloseRoomResponse;
import com.google.sps.services.interfaces.CloseRoomService;

public class CloseRoomServiceImpl implements CloseRoomService {
    @Override
    public CloseRoomResponse execute(CloseRoomRequest closeRoomRequest) throws IOException {
        StringBuilder roomsUrlString = new StringBuilder("https://summer20-sps-47.firebaseio.com/rooms/");
        roomsUrlString.append(closeRoomRequest.getRoomId());
        roomsUrlString.append(".json");
        Firebase.sendRequest(roomsUrlString.toString(), "DELETE", "");

        StringBuilder userRoomUrlString = new StringBuilder("https://summer20-sps-47.firebaseio.com/UserRoom/");
        userRoomUrlString.append(closeRoomRequest.getRoomId());
        userRoomUrlString.append(".json");
        Firebase.sendRequest(userRoomUrlString.toString(), "DELETE", "");

        CloseRoomResponse.Builder closeRoomResponse = CloseRoomResponse.newBuilder();
        closeRoomResponse.setRoomId(closeRoomRequest.getRoomId());
        closeRoomResponse.setTimestamp(getTimestamp());

        return closeRoomResponse.build();
    }

    public long getTimestamp() {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        return timestamp.getTime();
    }
}

package com.google.sps.services.interfaces;

import java.io.IOException;

import com.google.sps.protoc.CloseRoomProtoc.CloseRoomResponse;
import com.google.sps.protoc.CloseRoomProtoc.CloseRoomRequest;

public interface CloseRoomService {
    public CloseRoomResponse execute(CloseRoomRequest closeRoomRequest) throws IOException;
}

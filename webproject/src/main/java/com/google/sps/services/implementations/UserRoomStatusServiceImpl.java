package com.google.sps.services.implementations;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.google.sps.authentication.AuthenticationHandler;
import com.google.sps.dataManagers.UserRoomManager;
import com.google.sps.proto.UserRoomStatusProto.UserRoomStatusResponse;
import com.google.sps.proto.UserRoomStatusProto.UserRoomStatusRequest;
import com.google.sps.services.interfaces.UserRoomStatusService;
import com.google.sps.util.TimestampUtil;

@Singleton
public class UserRoomStatusServiceImpl implements UserRoomStatusService {
    private AuthenticationHandler authenticationHandler;
    private UserRoomManager userRoomManager;

    @Inject
    public UserRoomStatusServiceImpl(AuthenticationHandler authenticationHandler, UserRoomManager userRoomManager) {
        this.userRoomManager = userRoomManager;
        this.authenticationHandler = authenticationHandler;
    }

    @Override
    public UserRoomStatusResponse execute(UserRoomStatusRequest getJoinRequest) throws InterruptedException {
        String roomId = getJoinRequest.getRoomId();
        String userEmail = authenticationHandler.getCurrentUser().getEmail();

        boolean isJoined = userRoomManager.hasUserJoinedRoom(userEmail, roomId);
        return UserRoomStatusResponse.newBuilder()
                .setIsJoined(isJoined)
                .setRoomId(roomId)
                .setTimestamp(TimestampUtil.getTimestamp())
                .build();
    }
}
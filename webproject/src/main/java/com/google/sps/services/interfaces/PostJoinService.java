package com.google.sps.services.interfaces;

import java.io.IOException;

import com.google.firebase.FirebaseOptions;
import com.google.sps.authentication.AuthenticationHandler;
import com.google.sps.protoc.PostJoinProtoc.PostJoinResponse;
import com.google.sps.protoc.PostJoinProtoc.PostJoinRequest;

public interface PostJoinService {
    public FirebaseOptions getFirebaseOptions() throws IOException;
    public AuthenticationHandler getAuthenticationHandler();
    public PostJoinResponse execute(PostJoinRequest joinRequest);
}

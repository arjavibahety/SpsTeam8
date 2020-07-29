package com.google.sps.services.interfaces;

import java.io.IOException;

import com.google.appengine.api.users.User;
import com.google.firebase.FirebaseOptions;
import com.google.sps.authentication.AuthenticationHandler;
import com.google.sps.protoc.JoinProtoc.JoinResponse;
import com.google.sps.protoc.JoinProtoc.JoinRequest;

public interface JoinService {
    public FirebaseOptions getFirebaseOptions() throws IOException;
    public AuthenticationHandler getAuthenticationHandler();
    public JoinResponse executePost(JoinRequest joinRequest);
    public String executeGet(JoinRequest joinRequest) throws IOException;
}

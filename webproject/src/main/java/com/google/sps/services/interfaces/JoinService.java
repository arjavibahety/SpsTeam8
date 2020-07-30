package com.google.sps.services.interfaces;

import java.io.IOException;

import com.google.firebase.FirebaseOptions;
import com.google.sps.authentication.AuthenticationHandler;
import com.google.sps.protoc.JoinProtoc.JoinResponse;
import com.google.sps.protoc.JoinProtoc.JoinRequest;
import com.google.appengine.api.users.User;

public interface JoinService {
    public FirebaseOptions getFirebaseOptions() throws IOException;
    public AuthenticationHandler getAuthenticationHandler();
    public void executePost(JoinRequest joinRequest);
    public String executeGet(JoinRequest joinRequest) throws IOException;
    public User getCurrentUser();
}

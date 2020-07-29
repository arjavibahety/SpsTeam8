package com.google.sps.services.interfaces;

import java.io.IOException;

import com.google.firebase.FirebaseOptions;
import com.google.sps.protoc.TextMessageProtoc.TextMessageRequest;
import com.google.sps.protoc.TextMessageProtoc.TextMessageResponse;

public interface TextMessageService {
    public FirebaseOptions getFirebaseOptions() throws IOException;
    public TextMessageResponse execute(TextMessageRequest textMessageRequest);
}

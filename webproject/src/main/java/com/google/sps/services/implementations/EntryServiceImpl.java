package com.google.sps.services.implementations;

import com.google.sps.authentication.AuthenticationHandler;
import com.google.sps.services.interfaces.EntryService;

public class EntryServiceImpl implements EntryService {
    @Override
    public AuthenticationHandler getAuthenticationHandler() {
        return new AuthenticationHandler();
    }
}

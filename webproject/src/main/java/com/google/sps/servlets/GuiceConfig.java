package com.google.sps.servlets;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletModule;
import com.google.sps.services.implementations.BlobstoreServiceImpl;
import com.google.sps.services.implementations.CloseRoomServiceImpl;
import com.google.sps.services.implementations.EntryServiceImpl;
import com.google.sps.services.implementations.JoinServiceImpl;
import com.google.sps.services.implementations.TextMessageServiceImpl;
import com.google.sps.services.interfaces.BlobstoreService;
import com.google.sps.services.interfaces.CloseRoomService;
import com.google.sps.services.interfaces.EntryService;
import com.google.sps.services.interfaces.JoinService;
import com.google.sps.services.interfaces.LandingService;
import com.google.sps.services.implementations.LandingServiceImpl;
import com.google.sps.services.interfaces.TextMessageService;

public class GuiceConfig extends GuiceServletContextListener {

  @Override
  protected Injector getInjector() {
    return Guice.createInjector(new ServletModule() {
      @Override
      protected void configureServlets() {
        bind(BlobstoreService.class).to(BlobstoreServiceImpl.class);
        bind(LandingService.class).to(LandingServiceImpl.class);
        bind(TextMessageService.class).to(TextMessageServiceImpl.class);
        bind(EntryService.class).to(EntryServiceImpl.class);
        bind(CloseRoomService.class).to(CloseRoomServiceImpl.class);
        bind(JoinService.class).to(JoinServiceImpl.class);

        serve("/landing").with(LandingServlet.class);
        serve("/blobstore").with(BlobstoreServlet.class);
        serve("/closeRoom").with(CloseRoomServlet.class);
        // serve("/text-message").with(TextMessageServlet.class);
        // serve("/join").with(JoinServlet.class);
        serve("/").with(EntryServlet.class);
      }
    });
  }
}

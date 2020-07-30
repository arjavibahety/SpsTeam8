package com.google.sps.servlets;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletModule;
import com.google.sps.services.implementations.BlobstoreServiceImpl;
import com.google.sps.services.implementations.CloseRoomServiceImpl;
import com.google.sps.services.implementations.EntryServiceImpl;
import com.google.sps.services.implementations.GetJoinServiceImpl;
import com.google.sps.services.implementations.PostJoinServiceImpl;
import com.google.sps.services.implementations.TextMessageServiceImpl;
import com.google.sps.services.interfaces.BlobstoreService;
import com.google.sps.services.interfaces.CloseRoomService;
import com.google.sps.services.interfaces.EntryService;
import com.google.sps.services.interfaces.GetJoinService;
import com.google.sps.services.interfaces.PostJoinService;
import com.google.sps.services.interfaces.LandingService;
import com.google.sps.services.implementations.LandingServiceImpl;
import com.google.sps.services.interfaces.TextMessageService;

public class GuiceConfig extends GuiceServletContextListener {

  @Override
  protected Injector getInjector() {
    return Guice.createInjector(new ServletModule() {
      @Override
      protected void configureServlets() {
        super.configureServlets();

        bind(BlobstoreService.class).to(BlobstoreServiceImpl.class);
        bind(LandingService.class).to(LandingServiceImpl.class);
        bind(TextMessageService.class).to(TextMessageServiceImpl.class);
        bind(EntryService.class).to(EntryServiceImpl.class);
        bind(CloseRoomService.class).to(CloseRoomServiceImpl.class);
        bind(PostJoinService.class).to(PostJoinServiceImpl.class);
        bind(GetJoinService.class).to(GetJoinServiceImpl.class);

        serve("/landing").with(LandingServlet.class);
        // serve("/listings").with(LandingServlet.class);
        // serve("/myRooms").with(MyRoomsServlet.class);
        // serve("/my-form-handler").with(FormHandlerServlet.class);
        // serve("/login").with(LoginServlet.class);
        // serve("/logout").with(LogoutServlet.class);
        // serve("/username").with(UsernameServlet.class);
        serve("/blobstore").with(BlobstoreServlet.class);
        serve("/closeRoom").with(CloseRoomServlet.class);
        serve("/text-message").with(TextMessageServlet.class);
        serve("/postJoin").with(PostJoinServlet.class);
        serve("/getJoin").with(GetJoinServlet.class);
        // serve("/myOrder").with(MyOrderServlet.class);
        // serve("/order").with(OrderServlet.class);
        serve("/").with(EntryServlet.class);
      }
    });
  }
}

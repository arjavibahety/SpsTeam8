package com.google.sps.servlets;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletModule;
import com.google.sps.services.implementations.TextMessageServiceImpl;
import com.google.sps.services.interfaces.LandingService;
import com.google.sps.services.implementations.LandingServiceImpl;
import com.google.sps.services.interfaces.TextMessageService;

public class GuiceConfig extends GuiceServletContextListener {

  @Override
  protected Injector getInjector() {
    return Guice.createInjector(new ServletModule() {
      @Override
      protected void configureServlets() {
        bind(LandingService.class).to(LandingServiceImpl.class);
        bind(TextMessageService.class).to(TextMessageServiceImpl.class);
        serve("/landing").with(LandingServlet.class);
      }
    });
  }
}

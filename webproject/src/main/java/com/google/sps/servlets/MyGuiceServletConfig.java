package com.google.sps.servlets;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletModule;
import com.google.sps.services.LandingService;
import com.google.sps.services.LandingServiceImpl;

public class MyGuiceServletConfig extends GuiceServletContextListener {

  @Override
  protected Injector getInjector() {
    return Guice.createInjector(new ServletModule() {
      @Override
      protected void configureServlets() {
        bind(LandingService.class).to(LandingServiceImpl.class);
        serve("/landing").with(LandingServlet.class);
      }
    });
  }
}

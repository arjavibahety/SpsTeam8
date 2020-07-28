package com.google.sps.servlets;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.google.sps.util.HtmlParser;
import java.io.IOException;

import javax.inject.Named;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * A servlet which renders the website's landing page.
 */
@Singleton
public class LandingServlet extends HttpServlet {
    @Inject LandingService parsedHtmlString;

    /**
     * Called by the server to allow this servlet to handle a GET request from the landing page.
     * @param request An HttpServletRequest object that contains the request the client has made of the servlet.
     * @param response An HttpServletResponse object that contains the response the servlet sends to the client.
     * @throws IOException If an input or output error is detected when the servlet handles the GET request.
     */
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        response.getWriter().println(parsedHtmlString);
    }
}

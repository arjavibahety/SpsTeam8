package com.google.sps.servlets;

import com.google.sps.authentication.AuthenticationHandler;
import com.google.sps.util.HtmlParser;
import java.io.IOException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;

/**
 * A servlet which parses the html file for create-room.
 */
public class MyRoomsServlet extends RedirectServlet {
    public void doGetAuthenticated(HttpServletRequest request, 
                            HttpServletResponse response) throws IOException, ServletException {        
        response.setContentType("text/html");
        response.getWriter().println(HtmlParser.parseHtmlFromFile("my-rooms.html"));
    };
}

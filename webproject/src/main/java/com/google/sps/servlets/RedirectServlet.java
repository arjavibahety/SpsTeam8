package com.google.sps.servlets;

import com.google.sps.authentication.AuthenticationHandler;
import java.io.IOException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;

/**
 * A servlet which manages redirecting to landing page.
 */
public abstract class RedirectServlet extends HttpServlet {
    public final AuthenticationHandler authenticationHandler;
    
    /**
     * Constructs an instance of the LogoutServlet class.
     */
    public RedirectServlet() {
        authenticationHandler = new AuthenticationHandler();
    }

    public abstract void doGetAuthenticated(HttpServletRequest request, HttpServletResponse response) 
                                            throws IOException, ServletException;

    /**
     * Called by the server to manage access to webpage.
     * Redirects the user to the landing page if the user is not logged in.
     * @param request An HttpServletRequest object that contains the request the client has made of the servlet.
     * @param response An HttpServletResponse object that contains the response the servlet sends to the client.
     * @throws IOException If an input or output error is detected when the servlet handles the server's request.
     */
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        if (authenticationHandler.isUserLoggedIn()) {
            doGetAuthenticated(request, response);
        } else {
            response.sendRedirect("/");
        }
    }
}

package com.google.sps.servlets;

import com.google.sps.util.UserCheckUtil;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.ServletException;

/**
 * A servlet which manages redirecting to landing page.
 */
@Singleton
public class UserCheckServlet extends HttpServlet {
    private UserCheckUtil userCheckUtil;

    @Inject
    public UserCheckServlet(UserCheckUtil userCheckUtil) {
        this.userCheckUtil = userCheckUtil;
    }
    
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) 
                        throws IOException, ServletException {
        String roomId = request.getParameter("roomId");

        response.setContentType("text/html");
        response.getWriter().print(userCheckUtil.isRoomMember(roomId));
    }
}

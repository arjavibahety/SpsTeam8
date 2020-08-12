package com.google.sps.servlets;

import com.google.sps.authentication.AuthenticationHandler;
import com.google.sps.util.HtmlParser;
import com.google.sps.util.UserCheckUtil;

import java.io.IOException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.ServletException;

/**
 * A servlet which parses the html file for RoomChat.
 */
 @Singleton
public class RoomChatServlet extends RedirectServlet {
    private UserCheckUtil userCheckUtil;

    @Inject
    public RoomChatServlet(UserCheckUtil userCheckUtil) {
        this.userCheckUtil = userCheckUtil;
    }

    public void doGetAuthenticated(HttpServletRequest request, 
                            HttpServletResponse response) throws IOException, ServletException {
        String roomId = request.getParameter("roomId");

        if (UserCheckUtil.isRoomMember(roomId)) {
            response.setContentType("text/html");
            response.getWriter().println(HtmlParser.parseHtmlFromFile("roomChat.html"));
        } else {
            response.sendRedirect("/listings");
        }
    };
}

package com.google.sps.servlets;

import com.google.gson.Gson;
import com.google.sps.firebase.Firebase;

import com.google.appengine.api.users.User;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.google.sps.authentication.AuthenticationHandler;
import com.google.sps.data.Message;
import com.google.sps.data.UserRoom;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class JoinServlet extends HttpServlet {
    private static Gson gson = new Gson();

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        AuthenticationHandler auth = new AuthenticationHandler();
        if (!auth.isUserLoggedIn()) {
            response.setStatus(400);
            return;
        }

        User user = (new AuthenticationHandler()).getCurrentUser();
        String userEmail = user.getEmail();
        String roomId = request.getParameter("roomId");
        String userRoomJson = gson.toJson(new UserRoom(userEmail, roomId));
        Firebase.sendRequest("https://summer20-sps-47.firebaseio.com/UserRoom.json", "POST", userRoomJson);

        response.setStatus(200);
        response.getWriter().println(roomId);
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String roomId = request.getParameter("roomId");
        User user = (new AuthenticationHandler()).getCurrentUser();
        String userEmail = user.getEmail();

        String url = "https://summer20-sps-47.firebaseio.com/UserRoom.json?orderBy=%22userEmailRoom%22&equalTo=%22" + userEmail + "_" + roomId + "%22";
        //Use Firebase class once merged
        HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("Accept", "application/json");
        con.setDoOutput(true);

        String jsonResponse = "";
        try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
            StringBuilder firebaseResponse = new StringBuilder();
            String line;

            while ((line = in.readLine()) != null) {
                firebaseResponse.append(line);
            }

            jsonResponse = firebaseResponse.toString();
        }

        response.setContentType("text/plain");

        if (jsonResponse.substring(0, 2).equals("{}")) {
            response.getWriter().print("Join");
        } else {
            response.getWriter().print("Chat");
        }
    }
}

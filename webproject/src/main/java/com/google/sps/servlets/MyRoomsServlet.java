package com.google.sps.servlets;

import com.google.appengine.api.users.User;
import com.google.sps.authentication.AuthenticationHandler;
import com.google.sps.data.Room;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public class MyRoomsServlet extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        User user = (new AuthenticationHandler()).getCurrentUser();
        String userEmail = user.getEmail();

        String url = "https://summer20-sps-47.firebaseio.com/UserRoom.json?orderBy=%22userEmail%22&equalTo=%22" + userEmail + "%22";
        HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("Accept", "application/json");
        con.setDoOutput(true);

        String jsonResponse = "";
        try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {            
            String line;
            String firebaseResponse;
            while ((line = in.readLine()) != null) {
                String[] rooms = line.split("}");
                for (String room : rooms) {
                    String roomID = ((room.split("userEmail")[0]).split("roomId")[1]);
                    roomID = roomID.substring(3, roomID.length() - 3);
                    jsonResponse += roomID + " ";
                }
            }
            jsonResponse = jsonResponse.substring(0, jsonResponse.length() - 1);
            System.out.println(jsonResponse);
        }

        response.setContentType("html/text");
        response.getWriter().print(jsonResponse);
    }
}

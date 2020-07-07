package servlets;

import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.gson.Gson;
import data.Room;
import data.RoomCondition;
import data.RoomType;


@WebServlet("/room")
public class RoomServlet extends HttpServlet {
  private static DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
  private static Gson gson = new Gson();

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    Query query = new Query("Room");
    PreparedQuery results = datastore.prepare(query);
    List<Entity> rooms =  results.asList(FetchOptions.Builder.withDefaults());

    response.setContentType("text/json");
    String jsonResponse = gson.toJson(rooms);
    response.getWriter().println(jsonResponse);
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    Room room = new Room.Builder(request.getParameter("id"))
            .setTitle(request.getParameter("title"))
            .setLink(request.getParameter("link"))
            .setDescription(request.getParameter("description"))
            .setDeliveryLocation(Integer.parseInt(request.getParameter("deliveryLocation")))
            .setPhoneNumber(Integer.parseInt(request.getParameter("phoneNumber")))
            .setCategory(request.getParameter("category"))
            .setImagePath(request.getParameter("imagePath"))
            .setRoomType(gson.fromJson(request.getParameter("roomType"), RoomType.class))
            .setRoomCondition(gson.fromJson(request.getParameter("roomCondition"), RoomCondition.class))
            .build();

    datastore.put(room.toEntity());
  }
}

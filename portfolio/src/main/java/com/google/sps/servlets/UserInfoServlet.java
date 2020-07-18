package com.google.sps.servlets;

import com.google.gson.*;
import com.google.appengine.api.datastore.*;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.io.IOException;

@WebServlet("/userinfo")
public class UserInfoServlet extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    Map<String, String> userInfo = new HashMap<String, String>();

    // Gets user's info.
    UserService userService = UserServiceFactory.getUserService();
    if (!userService.isUserLoggedIn()) {
      userInfo.put("isLoggedIn", "no");
      userInfo.put("loginUrl", userService.createLoginURL("/index.html"));
    }else {
      userInfo.put("isLoggedIn", "yes");
      userInfo.put("logoutUrl", userService.createLogoutURL("/index.html"));

      // Gets user's info from datastore.
      DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
      String userId = userService.getCurrentUser().getUserId();
      Query query =
          new Query("userInfo")
              .setFilter(new Query.FilterPredicate("id", Query.FilterOperator.EQUAL, userId));
      PreparedQuery results = datastore.prepare(query);
      Entity entity = results.asSingleEntity();
      if (entity != null) {
        userInfo.put("email", (String) entity.getProperty("email"));
        userInfo.put("userName", (String) entity.getProperty("userName"));
      }
    }

    // Converts data to json
    Gson gson = new GsonBuilder().disableHtmlEscaping().create();
    String json = gson.toJson(userInfo);

    // Generates response
    response.setContentType("application/json;");
    response.getWriter().println(json);
  }
}

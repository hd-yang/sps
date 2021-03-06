// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps.servlets;

import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import com.google.gson.Gson;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;

/** Servlet that returns some example content. TODO: modify this file to handle comments data */
@WebServlet("/data")
public class DataServlet extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    ArrayList<String> data = loadData();
    
    // Converts data to json
    Gson gson = new Gson();
    String json = gson.toJson(data);

    // Generates response
    response.setContentType("application/json;");
    response.getWriter().println(json);
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    // Gets the input from the form.
    String comment = getParameter(request, "comment-input", "");
    String name = getParameter(request, "name-input", "");
    if (name.length() == 0) {
      name = "anonymous";
    }
    if (comment.length() != 0) {
      storeData(name, comment);
    }

    // Redirects back to the HTML page.
    response.sendRedirect("/index.html");
  }

  /**
   * @return the request parameter, or the default value if the parameter
   *         was not specified by the client
   */
  private String getParameter(HttpServletRequest request, String name, String defaultValue) {
    String value = request.getParameter(name);
    if (value == null) {
      return defaultValue;
    }
    return value;
  }

  private void storeData(String name, String comment) {
    long timestamp = System.currentTimeMillis();
    Entity commentEntity = new Entity("comment");
    commentEntity.setProperty("name", name);
    commentEntity.setProperty("text", comment);
    commentEntity.setProperty("timestamp", timestamp);

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    datastore.put(commentEntity);
  }

  private ArrayList<String> loadData() {
    Query query = new Query("comment").addSort("timestamp", SortDirection.DESCENDING);;

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    PreparedQuery results = datastore.prepare(query);

    ArrayList<String> data = new ArrayList<String>();
    for (Entity entity : results.asIterable()) {
      String name = (String) entity.getProperty("name");
      String text = (String) entity.getProperty("text");

      data.add(name + ": " + text);
    }

    return data;
  }
}

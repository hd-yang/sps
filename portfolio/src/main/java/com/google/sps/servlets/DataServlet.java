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

/** Servlet that returns some example content. TODO: modify this file to handle comments data */
@WebServlet("/data")
public class DataServlet extends HttpServlet {

  private ArrayList<String> data = new ArrayList<String>();

  // @Override
  // public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
  //   response.setContentType("text/html;");
  //   response.getWriter().println("Hello Huidong Yang!");
  // }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    // generate data
    // ArrayList<String> data = new ArrayList<String>();
    // data.add("Hello!");
    // data.add("How are you?");
    // data.add("cool!");
    
    // convert data to json
    Gson gson = new Gson();
    String json = gson.toJson(data);

    // generate response
    response.setContentType("application/json;");
    response.getWriter().println(json);
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    // Get the input from the form.
    String comment = getParameter(request, "comment-input", "");
    String name = getParameter(request, "name-input", "");
    if (name.length() == 0) {
      name = "anonymous";
    }
    if (comment.length() != 0) {
      data.add(0, name + ": " + comment);
    }

    // Redirect back to the HTML page.
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
}

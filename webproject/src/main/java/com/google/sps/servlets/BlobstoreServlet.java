package com.google.sps.servlets;

import com.google.protobuf.util.JsonFormat;
import com.google.sps.protoc.BlobstoreProtoc.BlobstoreResponse;
import com.google.sps.services.interfaces.BlobstoreService;
import com.google.inject.Inject;

import java.io.IOException;
import javax.inject.Singleton;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Singleton
public class BlobstoreServlet extends HttpServlet {
  private BlobstoreService blobstoreService;

  @Inject
  public BlobstoreServlet(BlobstoreService blobstoreService) {
    this.blobstoreService = blobstoreService;
  }

  /**
   * The form submits to Blobstore, which redirects to our
   * /my-form-handler, which is handled by FormHandlerServlet.
   */
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    BlobstoreResponse blobstoreResponse = blobstoreService.execute();
    response.setContentType("application/json; charset=UTF-8;");
    response.getWriter().println(JsonFormat.printer().print(blobstoreResponse));
  }
}

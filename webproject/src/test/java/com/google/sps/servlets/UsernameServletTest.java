package com.google.sps.servlets;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.appengine.tools.development.testing.LocalUserServiceTestConfig;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for UsernameServlet.
 */
public class UsernameServletTest {
    private UsernameServlet usernameServlet;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private PrintWriter printWriter;

    @Before
    public void setUp() throws IOException {
        usernameServlet = new UsernameServlet();
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        printWriter = mock(PrintWriter.class);
        when(response.getWriter()).thenReturn(printWriter);
    }

    @Test
    public void doGet_loggedIn_redirectsToLoggedInPath() throws IOException {

        // Arrange
        LocalServiceTestHelper helper = new LocalServiceTestHelper(new LocalUserServiceTestConfig())
            .setEnvIsLoggedIn(true).setEnvEmail("test@mail.com").setEnvAuthDomain("test");
        helper.setUp();
        response.setContentType("text/html");

        // Act
        usernameServlet.doGet(request, response);

        // Assert
        verify(response.getWriter()).print("test@mail.com");
        helper.tearDown();
    }
}

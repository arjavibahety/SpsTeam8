package com.google.sps.servlets;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.appengine.tools.development.testing.LocalUserServiceTestConfig;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for EntryServlet.
 */
public class EntryServletTest {
    private EntryServlet entryServlet;
    private HttpServletRequest request;
    private HttpServletResponse response;

    @Before
    public void setUp() {
        entryServlet = new EntryServlet();
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
    }

    @Test
    public void doGet_loggedIn_redirectsToLoggedInPath() throws IOException, ServletException {
        LocalServiceTestHelper helper = new LocalServiceTestHelper(new LocalUserServiceTestConfig())
            .setEnvIsLoggedIn(true);
        helper.setUp();

        entryServlet.doGet(request, response);

        verify(response).sendRedirect(EntryServlet.LOGGED_IN_REDIRECT_PATH);
        helper.tearDown();
    }

    @Test
    public void doGet_loggedOut_redirectsToLoggedOutPath() throws IOException, ServletException {
        LocalServiceTestHelper helper = new LocalServiceTestHelper(new LocalUserServiceTestConfig());
        helper.setUp();

        entryServlet.doGet(request, response);

        verify(response).sendRedirect(EntryServlet.LOGGED_OUT_REDIRECT_PATH);
        helper.tearDown();
    }
}

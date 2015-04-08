package se.fearless.spacedweb.web.authentication;

import org.junit.Before;
import org.junit.Test;

import org.springframework.ui.Model;
import se.fearless.spacedweb.model.UserAccount;
import se.fearless.spacedweb.services.UserAccountService;
import se.fearlessgames.common.util.uuid.UUID;
import se.mockachino.annotations.Mock;

import javax.servlet.http.HttpServletResponse;

import static junit.framework.Assert.assertEquals;
import static se.mockachino.Mockachino.*;
import static se.mockachino.matchers.Matchers.*;

public class SpacedAuthenticationControllerTest {
    @Mock
    private UserAccountService userAccountService;
    @Mock
    private Model model;
    @Mock
    private HttpServletResponse response;


    @Before
    public void setUp() {
        setupMocks(this);
    }

    @Test
    public void testAuthenticateAndAuthorizeSuccessShouldAddUUIDToModel() throws Exception {
        SpacedAuthenticationController ac = new SpacedAuthenticationController(userAccountService, "omagahd");
        UUID uuid = new UUID(1l, 1l);
        UserAccount userAccount = new UserAccount(uuid, "a", "b", "c");

        when(userAccountService.authenticate(any(String.class), any(String.class))).thenReturn(userAccount);
        when(userAccountService.isAccountAuthorizedForFeature(userAccount, "spaced")).thenReturn(Boolean.TRUE);
        String viewName = ac.authenticateSpaced(response,model,"blah","blah","omagahd");
        verifyOnce().on(model).addAttribute("userAccount", userAccount);
        assertEquals("authSpaced", viewName);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIncorrectServiceKeyThrowsException() {
        SpacedAuthenticationController ac = new SpacedAuthenticationController(userAccountService, "omagahd");
        UUID uuid = new UUID(1l, 1l);
        UserAccount userAccount = new UserAccount(uuid, "a", "b", "c");

        when(userAccountService.authenticate(any(String.class), any(String.class))).thenReturn(userAccount);
        when(userAccountService.isAccountAuthorizedForFeature(userAccount, "spaced")).thenReturn(Boolean.TRUE);
        String viewName = ac.authenticateSpaced(response, model, "blah", "blah", "poopyface");
        verifyOnce().on(model).addAttribute("userAccount", userAccount);
        assertEquals("authForum", viewName);
    }


    @Test
    public void testAuthenticateFailShouldNotAddUUIDToModelAndAddErrorResponse() throws Exception {
        SpacedAuthenticationController ac = new SpacedAuthenticationController(userAccountService, "omagahd");
        when(userAccountService.authenticate(any(String.class), any(String.class))).thenReturn(null);
        String viewName = ac.authenticateSpaced(response, model, "blah", "blah", "omagahd");
        verifyNever().on(model).addAttribute(any(String.class), any(Object.class));
        verifyOnce().on(response).setStatus(eq(HttpServletResponse.SC_UNAUTHORIZED));
        assertEquals("authSpaced", viewName);
    }

    @Test
    public void testAuthenticateSuccessButAuthorizeFailureShouldNotAddUUIDToModelAndAddErrorResponse() throws Exception {
        SpacedAuthenticationController ac = new SpacedAuthenticationController(userAccountService, "omagahd");
        UUID uuid = new UUID(1l, 1l);
        UserAccount userAccount = new UserAccount(uuid, "a", "b", "c");
        when(userAccountService.authenticate(any(String.class), any(String.class))).thenReturn(userAccount);
        when(userAccountService.isAccountAuthorizedForFeature(userAccount, "spaced")).thenReturn(Boolean.FALSE);
        String viewName = ac.authenticateSpaced(response, model, "blah", "blah", "omagahd");
        verifyNever().on(model).addAttribute(any(String.class), any(Object.class));
        verifyOnce().on(response).setStatus(eq(HttpServletResponse.SC_UNAUTHORIZED));
        assertEquals("authSpaced", viewName);
    }
}

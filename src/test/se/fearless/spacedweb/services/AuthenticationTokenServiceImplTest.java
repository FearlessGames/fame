package se.fearless.spacedweb.services;

import org.junit.Before;
import org.junit.Test;

import se.fearless.spacedweb.model.Salts;
import se.fearless.spacedweb.model.UserAccount;
import se.fearless.spacedweb.persistance.dao.UserAccountDao;
import se.fearlessgames.common.util.uuid.UUID;
import se.fearlessgames.common.util.uuid.UUIDFactory;
import se.mockachino.annotations.Mock;

import java.util.ArrayList;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import static se.mockachino.Mockachino.*;

public class AuthenticationTokenServiceImplTest {
    @Mock
    private UUIDFactory uuidfactory;
    @Mock
    private UserAccountDao userAccountDao;

    @Before
    public void setUp() {
        setupMocks(this);
    }

    @Test
    public void testRequestSalts(){
        AuthenticationSaltService service = new AuthenticationSaltServiceImpl(uuidfactory,userAccountDao);
        UUID uuid = new UUID(1l, 1l);
        String oneTimeSalt = uuid.toString();
        when(uuidfactory.randomUUID()).thenReturn(uuid);
        String userSalt = "usersalt";
        Salts expectedSalts = new Salts(userSalt,oneTimeSalt);
        UserAccount wantedUserAccount = new UserAccount(new UUID(1l,1l),"user","password", userSalt,"olle.back@poopshop.com", new ArrayList<String>());
        when(userAccountDao.findByUsername("username")).thenReturn(wantedUserAccount);
        Salts salts = service.requestSalts("username");
        assertEquals(expectedSalts,salts);
    }

    @Test
    public void testGetSaltByUsername() {
        AuthenticationSaltService service = new AuthenticationSaltServiceImpl(uuidfactory,userAccountDao);
        UUID uuid = new UUID(1l, 1l);
        when(uuidfactory.randomUUID()).thenReturn(uuid);
        service.requestSalts("user");
        assertEquals(uuid.toString(), service.getOneTimeSaltForUsername("user"));
    }



}

package se.fearless.spacedweb.services;

import org.junit.Before;
import org.junit.Test;
import se.fearless.spacedweb.model.Salts;
import se.fearless.spacedweb.model.UserAccount;
import se.fearless.spacedweb.persistance.dao.UserAccountDao;
import se.fearlessgames.common.uuid.UUID;
import se.fearlessgames.common.uuid.UUIDFactory;
import se.mockachino.annotations.Mock;

import java.util.ArrayList;

import static junit.framework.Assert.assertEquals;
import static se.mockachino.Mockachino.setupMocks;
import static se.mockachino.Mockachino.when;

public class AuthenticationTokenServiceImplTest {
    public static final String USERNAME = "username";
    @Mock
    private UUIDFactory uuidfactory;
    @Mock
    private UserAccountDao userAccountDao;
    private AuthenticationSaltService service;
    private UUID uuid;
    private String userSalt;

    @Before
    public void setUp() {
        setupMocks(this);
        service = new AuthenticationSaltServiceImpl(uuidfactory, userAccountDao);
        uuid = new UUID(1l, 1l);
        when(uuidfactory.randomUUID()).thenReturn(uuid);
        userSalt = "usersalt";
        UserAccount wantedUserAccount = new UserAccount(new UUID(1l, 1l), "user", "password", userSalt, "olle.back@poopshop.com", new ArrayList<String>());
        when(userAccountDao.findByUsername(USERNAME)).thenReturn(wantedUserAccount);
    }

    @Test
    public void testRequestSalts(){


        String oneTimeSalt = uuid.toString();


        Salts expectedSalts = new Salts(userSalt,oneTimeSalt);

        Salts salts = service.requestSalts(USERNAME);
        assertEquals(expectedSalts, salts);
    }

    @Test
    public void testGetSaltByUsername() {

        service.requestSalts(USERNAME);
        assertEquals(uuid.toString(), service.getOneTimeSaltForUsername(USERNAME));
    }



}

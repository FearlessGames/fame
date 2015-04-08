package se.fearless.spacedweb.services;

import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mindrot.jbcrypt.BCrypt;
import se.fearless.spacedweb.model.UserAccount;
import se.fearless.spacedweb.persistance.dao.UserAccountDao;
import se.fearless.spacedweb.utils.UserAccountDigester;
import se.fearlessgames.common.util.uuid.UUID;
import se.fearlessgames.common.util.uuid.UUIDFactoryImpl;
import se.mockachino.Mockachino;
import se.mockachino.annotations.Mock;

import java.util.ArrayList;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static se.mockachino.Mockachino.*;
import static se.mockachino.matchers.Matchers.any;


public class UserAccountServiceImplTest {
    @Mock
    private UserAccountDao dao;
    @Mock
    private AuthenticationSaltService authSaltService;
    private UserAccountDigester digester = new UserAccountDigester();

    @Before
    public void setUp() {
        setupMocks(this);
    }

    @Test
    public void testSuccessOnNameNotTaken() throws UsernameOccupiedException, EmailOccupiedException {
        UserAccountServiceImpl userAccountService = new UserAccountServiceImpl(dao, digester, authSaltService);
        when(dao.findByUsername(any(String.class))).thenReturn(null);
        userAccountService.createAccount("olle", "bolle", "knolle");
        verifyOnce().on(dao).persist(any(UserAccount.class));
    }

    @Test(expected = UsernameOccupiedException.class)
    public void testExceptionOnNameTaken() throws UsernameOccupiedException, EmailOccupiedException {
        UserAccountServiceImpl userAccountService = new UserAccountServiceImpl(dao, digester, authSaltService);
        when(dao.findByUsername(any(String.class))).thenReturn(new UserAccount(UUIDFactoryImpl.INSTANCE.randomUUID(), "olle", "bolle", "knolle"));
        userAccountService.createAccount("olle", "bolle", "knolle");
        verifyNever().on(dao).persist(any(UserAccount.class));
    }


    @Test
    public void testHappyPathAuth() {
        UserAccountServiceImpl userAccountService = new UserAccountServiceImpl(dao, digester, authSaltService);
        String userSalt = "salt";
        String username = "username";
        String bcryptUserPasswordAndUserSalt = "password";
        UserAccount userAccount = new UserAccount(new UUID(1l, 1l), username, bcryptUserPasswordAndUserSalt, userSalt, "olle.back@poopshop.com", new ArrayList<String>());
        when(dao.findByUsername(username)).thenReturn(userAccount);
        String oneTimeSalt = "salt";
        when(authSaltService.getOneTimeSaltForUsername(username)).thenReturn(oneTimeSalt);

        String hashFromClient = digester.sha512Hex(bcryptUserPasswordAndUserSalt + oneTimeSalt);
        UserAccount account = userAccountService.authenticate(username, hashFromClient);
        assertNotNull(account);
    }

    @Test
    public void testFailAuthDueToWrongUserSaltButCorrectPassword() {
        UserAccountServiceImpl userAccountService = new UserAccountServiceImpl(dao, digester, authSaltService);
        String correctUserSalt = BCrypt.gensalt(5);
        String username = "username";
        String correctPassword = "password";
        String correctBCryptHash = digester.bcrypt(correctPassword, correctUserSalt);
        UserAccount userAccount = new UserAccount(new UUID(1l, 1l), username, correctBCryptHash, correctUserSalt, "olle.back@poopshop.com", new ArrayList<String>());
        when(dao.findByUsername(username)).thenReturn(userAccount);
        String oneTimeSalt = "salt";
        when(authSaltService.getOneTimeSaltForUsername(username)).thenReturn(oneTimeSalt);
        String wrongUserSalt = BCrypt.gensalt(5);
        String wrongBCryptHashDueToWrongSalt = digester.bcrypt(correctPassword, wrongUserSalt);
        String hashFromClient = digester.sha512Hex(wrongBCryptHashDueToWrongSalt + oneTimeSalt);
        UserAccount account = userAccountService.authenticate(username, hashFromClient);
        assertNull(account);
    }

    @Test
    public void testFailAuthDueToWrongOneTimeSalt() {
        UserAccountServiceImpl userAccountService = new UserAccountServiceImpl(dao, digester, authSaltService);
        String correctUserSalt = BCrypt.gensalt(5);
        String username = "username";
        String correctPassword = "password";
        String correctBCryptHash = digester.bcrypt(correctPassword, correctUserSalt);
        UserAccount userAccount = new UserAccount(new UUID(1l, 1l), username, correctBCryptHash, correctUserSalt, "olle.back@poopshop.com", new ArrayList<String>());
        when(dao.findByUsername(username)).thenReturn(userAccount);
        String oneTimeSalt = "salt";
        when(authSaltService.getOneTimeSaltForUsername(username)).thenReturn(oneTimeSalt);
        String wrongUserSalt = BCrypt.gensalt(5);
        String wrongBCryptHashDueToWrongSalt = digester.bcrypt(correctPassword, wrongUserSalt);
        String hashFromClient = digester.sha512Hex(wrongBCryptHashDueToWrongSalt + oneTimeSalt);
        UserAccount account = userAccountService.authenticate(username, hashFromClient);
        assertNull(account);
    }

    @Test
    public void testFailDueToWrongUsernamePasswordButCorrectSalts() {
        UserAccountServiceImpl userAccountService = new UserAccountServiceImpl(dao, digester, authSaltService);
        String correctUserSalt = BCrypt.gensalt(5);
        String username = "username";
        String correctPassword = "password";
        String correctBCryptHash = digester.bcrypt(correctPassword, correctUserSalt);
        String incorrectPassword = "incorrectmutherbitches";
        UserAccount userAccount = new UserAccount(new UUID(1l, 1l), username, correctBCryptHash, correctUserSalt, "olle.back@poopshop.com", new ArrayList<String>());
        when(dao.findByUsername(username)).thenReturn(userAccount);
        String oneTimeSalt = "salt";
        when(authSaltService.getOneTimeSaltForUsername(username)).thenReturn(oneTimeSalt);
        String wrongBCryptHashDueToWrongSalt = digester.bcrypt(incorrectPassword, correctUserSalt);
        String hashFromClient = digester.sha512Hex(wrongBCryptHashDueToWrongSalt + oneTimeSalt);
        UserAccount account = userAccountService.authenticate(username, hashFromClient);
        assertNull(account);
    }

    @Test(expected = RuntimeException.class)
    public void testFailedByUnexistingOneTimeHash() {
        UserAccountServiceImpl userAccountService = new UserAccountServiceImpl(dao, digester, authSaltService);
        String userSalt = "salt";
        String username = "username";
        String bcryptUserPasswordAndUserSalt = "password";
        UserAccount userAccount = new UserAccount(new UUID(1l, 1l), username, bcryptUserPasswordAndUserSalt, userSalt, "olle.back@poopshop.com", new ArrayList<String>());
        when(dao.findByUsername(username)).thenReturn(userAccount);
        String oneTimeSalt = "salt";
        when(authSaltService.getOneTimeSaltForUsername(username)).thenReturn(null);
        String hashFromClient = digester.sha512Hex(bcryptUserPasswordAndUserSalt + oneTimeSalt);
        userAccountService.authenticate(username, hashFromClient);
    }

    @Test
    public void testCreateUserWithBcrypt() throws UsernameOccupiedException, EmailOccupiedException {
        UserAccountServiceImpl userAccountService = new UserAccountServiceImpl(dao, digester, authSaltService);
        userAccountService.createAccount("olle", "olle", "olle@bolle.se");
        verifyOnce().on(dao).persist(any(UserAccount.class));
    }

    @Test
    public void testAuthWithBCrypt() {
        String userSalt = "userSalt";
        String oneTimeSalt = "oneTimeSalt";
        UserAccountDigester uad = Mockachino.mock(UserAccountDigester.class);
        UserAccount wantedUserAccount = new UserAccount(new UUID(1l, 1l), "user", "password", userSalt, "olle.back@poopshop.com", new ArrayList<String>());
        when(dao.findByUsername("user")).thenReturn(wantedUserAccount);
        when(authSaltService.getOneTimeSaltForUsername("user")).thenReturn(oneTimeSalt);
        when(uad.sha512Hex(any(String.class))).thenReturn("hash");
        UserAccountServiceImpl userAccountService = new UserAccountServiceImpl(dao, uad, authSaltService);
        UserAccount actualUserAccount = userAccountService.authenticate("user", "hash");
        Assert.assertEquals(wantedUserAccount, actualUserAccount);
    }


}

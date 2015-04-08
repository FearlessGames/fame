package se.fearless.spacedweb.web.authentication;

import org.junit.Ignore;
import org.springframework.security.core.codec.Base64;
import se.fearless.spacedweb.model.Salts;
import se.fearless.spacedweb.utils.UserAccountDigester;
import se.fearlessgames.common.util.Digester;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

@Ignore
public class AuthTestTool {
    private static final String HOST = "localhost:8080";
    private final static String REQUEST_TOKEN_URL = "http://" + HOST + "/fame/api/auth/requestToken.html";
    private final static String REQUEST_SALTS_URL = "http://" + HOST + "/fame/api/auth/requestSalts.html";
    private final static String SPACED_URL = "http://" + HOST + "/fame/api/auth/spaced.html";
    private final static String FORUM_URL = "http://" + HOST + "/fame/api/auth/forum.html";

    private Digester dig = new UserAccountDigester();


    private String username = "apaman";
    private String password = "apaman123";

    private String serviceKeySpaced = "omagahd";
    private String serviceKeyForum = "gonzales";

    private String tokenId;
    private String token;

    public AuthTestTool() {

    }

    private void authForum() throws IOException {
        fetchToken();
        String hash = createHash();
        String url = buildUrl(FORUM_URL, hash, serviceKeyForum);
        String content = tinyGET(url);
        System.out.println("AuthForums result: [" + content + "]");

        //TODO: move to bcrypt styled auth
    }

    private String buildUrl(String url, String hash, String serviceKey) {
        return new StringBuilder(url).append("?").
                append("userName=").append(username).
                append("&hash=").append(hash).
                append("&tokenId=").append(tokenId).
                append("&serviceKey=").append(serviceKey).
                toString();
    }

    private String createHash() {
        return (dig.sha512Hex(token + dig.sha512Hex(username + password)));
    }

    private void fetchToken() throws IOException {
        String result = tinyGET(REQUEST_TOKEN_URL);
        String[] strings = result.split(":");
        tokenId = strings[0];
        token = strings[1];
    }

    private Salts fetchSalts(String username) throws IOException {
        String result = tinyGET(REQUEST_SALTS_URL+"?userName="+username);
        String[] strings = result.split(":");
        return new Salts(strings[0], strings[1]);
    }

    private String tinyGET(String urlString) throws IOException {
        URL url = new URL(urlString);
        URLConnection urlConnection = url.openConnection();
        String auth = new String(Base64.encode("remote:remote".getBytes()));
        urlConnection.addRequestProperty("Authorization", "Basic " + auth);

        InputStream is = urlConnection.getInputStream();

        byte[] buffer = new byte[2048];
        int len = is.read(buffer);
        is.close();
        return new String(buffer, 0, len);
    }


    public static void main(String[] args) throws IOException {
        AuthTestTool authTestTool = new AuthTestTool();
     //   authTestTool.authForum();
        authTestTool.authSpacedBC();

    }

    private void authSpacedBC() throws IOException {
        Salts salts = fetchSalts("bronzon");
        System.out.println("got salts: "+salts);
        String ots = salts.getOneTimeSalt();
        String userSalt = salts.getUserSalt();
        String hash = dig.sha512Hex(dig.bcrypt("bronzon" + "bronzon", userSalt) + ots);
        String uri = new StringBuilder(SPACED_URL).append("?").
                append("userName=").append("bronzon").
                append("&hash=").append(hash).
                append("&serviceKey=").append(serviceKeySpaced).
                toString();
        System.out.println(uri);
        String content = tinyGET(uri);
        System.out.println("AuthSpaced result: [" + content + "]");
    }


}

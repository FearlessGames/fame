package se.fearless.spacedweb.web.api;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import se.fearless.spacedweb.FameConfig;

import java.nio.charset.Charset;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = FameConfig.class)
@WebAppConfiguration
@TestPropertySource(value = "classpath:/test.properties")
public class UserApiTest {
	private MockMvc mockMvc;

	private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
			MediaType.APPLICATION_JSON.getSubtype(),
			Charset.forName("utf8"));

	@Autowired
	private WebApplicationContext webApplicationContext;

	@Autowired
	private UserApi userApi;


	@Before
	public void setUp() throws Exception {
		this.mockMvc = MockMvcBuilders.standaloneSetup(userApi).build();
	}

	@Test
	public void createUserUsingPrivateApi() throws Exception {

		String createUserJson = "{ \"username\": \"Foobar\", \"email\": \"foobar@gmail.com\", \"password\": \"qwerty\" }";
		this.mockMvc.perform(post("/api/private/users")
				.contentType(contentType)
				.content(createUserJson))
				.andExpect(status().isCreated());
	}

}
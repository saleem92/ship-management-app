package com.shipping.logistics;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shipping.logistics.repository.ShipRepository;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.persistence.EntityManager;
import java.io.UnsupportedEncodingException;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
		classes = LogisticsApplication.class)
public class AbstractIT {

	@Autowired
	protected WebApplicationContext webApplicationContext;

	@Autowired
	protected ShipRepository repository;

	@Autowired
	protected EntityManager entityManager;

	@Autowired
	protected ObjectMapper mapper;

	protected MockMvc mvc;

	protected final String url = "/ships";
	protected final String urlWithId = url + "/{id}";

	@BeforeEach
	void setUp() {
		mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
		repository.deleteAll();
	}

	protected String[] getErrorMessages(MvcResult mvcResult) throws UnsupportedEncodingException, JSONException {
		String response = mvcResult.getResponse().getContentAsString();
		JSONObject json = new JSONObject(response);
		json.remove("errorCode");
		return json.get("message").toString().split(",");
	}

}

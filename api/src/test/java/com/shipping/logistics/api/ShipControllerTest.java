package com.shipping.logistics.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shipping.logistics.api.mapper.ShipMapper;
import com.shipping.logistics.exception.NotFoundException;
import com.shipping.logistics.model.ShipRequestDTO;
import com.shipping.logistics.model.ShipResponseDTO;
import com.shipping.logistics.repository.entity.Ship;
import com.shipping.logistics.service.ManageShipsService;
import org.hamcrest.CoreMatchers;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
class ShipControllerTest {

	@Autowired
	private MockMvc mvc;

	@MockBean
	private ManageShipsService service;

	@Autowired
	private ObjectMapper mapper;

	private static final ShipMapper INSTANCE = ShipMapper.INSTANCE;

	private static final String name1 = "name 1";
	private static final String code1 = "xxxx-0000-x0";
	private static final String name2 = "name 2";
	private static final String code2 = "code 2";

	@Nested
	class GetAllShips {
		@Test
		void shouldReturnAllShips() throws Exception {
			Ship s1 = Ship.builder().id(1L).name(name1).code(code1).length(0.1).width(1.1).build();
			Ship s2 = Ship.builder().id(2L).name(name2).code(code2).length(0.2).width(1.2).build();

			when(service.getShips()).thenReturn(Arrays.asList(s1, s2));

			// WHEN
			mvc.perform(get("/ships")
					.contentType(MediaType.APPLICATION_JSON))
					// THEN
					.andExpect(status().isOk())
					.andExpect(jsonPath("$[0].id", CoreMatchers.is(1)))
					.andExpect(jsonPath("$[0].name", CoreMatchers.is(name1)))
					.andExpect(jsonPath("$[0].code", CoreMatchers.is(code1)))
					.andExpect(jsonPath("$[0].length", CoreMatchers.is(0.1)))
					.andExpect(jsonPath("$[0].width", CoreMatchers.is(1.1)))
					.andExpect(jsonPath("$[1].id", CoreMatchers.is(2)))
					.andExpect(jsonPath("$[1].name", CoreMatchers.is(name2)))
					.andExpect(jsonPath("$[1].code", CoreMatchers.is(code2)))
					.andExpect(jsonPath("$[1].length", CoreMatchers.is(0.2)))
					.andExpect(jsonPath("$[1].width", CoreMatchers.is(1.2)));
		}
	}

	@Nested
	class GetShipById {

		private final String url = "/ships/{id}";

		@Test
		void shouldThrowErrorWhenIdNotFound() throws Exception {
			// GIVEN
			int id = 1;
			String errorMessage = String.format("Ship with id {%d} not found", id);

			when(service.getShipById(id)).thenThrow(new NotFoundException(errorMessage));

			// WHEN
			mvc.perform(get(url, id)
					.contentType(MediaType.APPLICATION_JSON))
					// THEN
					.andExpect(status().isNotFound())
					.andExpect(jsonPath("$.errorCode", CoreMatchers.is(404)))
					.andExpect(jsonPath("$.message", CoreMatchers.is(errorMessage)));
		}

		@Test
		void shouldReturnResponseWhenIdIsValid() throws Exception {
			// GIVEN
			int id = 1;
			Ship s1 = Ship.builder().id(1L).name(name1).code(code1).length(0.1).width(1.1).build();

			when(service.getShipById(id)).thenReturn(s1);

			// WHEN
			mvc.perform(get(url, id)
					.contentType(MediaType.APPLICATION_JSON))
					// THEN
					.andExpect(status().isOk())
					.andExpect(jsonPath("$.id", CoreMatchers.is(1)))
					.andExpect(jsonPath("$.name", CoreMatchers.is(name1)))
					.andExpect(jsonPath("$.code", CoreMatchers.is(code1)))
					.andExpect(jsonPath("$.length", CoreMatchers.is(0.1)));
		}
	}

	@Nested
	class CreateShip {

		private final String url = "/ships";

		@Test
		void shouldThrowErrorWhenInputIsNotValid() throws Exception {
			// GIVEN
			ShipRequestDTO dto = ShipRequestDTO.builder().build();

			// WHEN
			MvcResult mvcResult = mvc.perform(post(url)
					.content(mapper.writeValueAsString(dto))
					.contentType(MediaType.APPLICATION_JSON))
					// THEN
					.andExpect(status().isBadRequest())
					.andReturn();

			String[] messages = getErrorMessages(mvcResult);

			assertThat(messages).containsExactlyInAnyOrder("name cannot be null",
					"code cannot be null", "length cannot not be null or should be greater than 0",
					"width cannot not be null or should be greater than 0");

			verify(service, never()).createShip(INSTANCE.map(dto));
		}

		@Test
		@DisplayName("should create ship")
		void shouldCreateShip() throws Exception {
			// GIVEN
			ShipRequestDTO dto = ShipRequestDTO.builder().name(name1).code(code1).width(1d).length(2d).build();

			// WHEN
			mvc.perform(post(url)
					.content(mapper.writeValueAsString(dto))
					.contentType(MediaType.APPLICATION_JSON))
					// THEN
					.andExpect(status().isCreated());
		}
	}

	@Nested
	class PatchShip {

		private final String url = "/ships/{id}";

		@Test
		void shouldThrowErrorWhenInputIsNotValid() throws Exception {
			// GIVEN
			ShipRequestDTO dto = ShipRequestDTO.builder().code("test").build();

			// WHEN
			MvcResult mvcResult = mvc.perform(patch(url, 1)
					.content(mapper.writeValueAsString(dto))
					.contentType(MediaType.APPLICATION_JSON))
					// THEN
					.andExpect(status().isBadRequest())
					.andReturn();

			String[] messages = getErrorMessages(mvcResult);

			assertThat(messages).contains("code format not valid");

			verify(service, never()).createShip(INSTANCE.map(dto));
		}

		@Test
		void shouldPatchShip() throws Exception {
			// GIVEN
			String name = "test 1";
			String code = "xxxx-0000-x9";
			Ship dto = Ship.builder().name(name).code(code).width(1d).length(2d).build();

			when(service.patchShip(anyLong(), any())).thenReturn(Ship.builder()
					.name(name).code(code).width(1d).length(2d).build());

			// WHEN
			MvcResult mvcResult = mvc.perform(patch(url, 1)
					.content(mapper.writeValueAsString(dto))
					.contentType(MediaType.APPLICATION_JSON))
					// THEN
					.andExpect(status().isOk())
					.andReturn();

			ShipResponseDTO response = mapper.readValue(mvcResult.getResponse().getContentAsString(), ShipResponseDTO.class);

			assertThat(response).extracting(ShipResponseDTO::getName,
					ShipResponseDTO::getCode,
					ShipResponseDTO::getLength,
					ShipResponseDTO::getWidth)
					.contains(name, code, 1d, 2d);
		}
	}

	@Nested
	class DeleteShip {

		private final String url = "/ships/{id}";


		@Test
		void shouldThrowErrorWhenIdNotFound() throws Exception {
			// GIVEN

			doThrow(new NotFoundException("not found"))
					.when(service).deleteShip(1);

			// WHEN
			mvc.perform(delete(url, 1)
					.contentType(MediaType.APPLICATION_JSON))
					// THEN
					.andExpect(status().isNotFound());
		}

		@Test
		void shouldDeleteShip() throws Exception {
			// GIVEN
			ShipRequestDTO dto = ShipRequestDTO.builder().build();

			// WHEN
			mvc.perform(delete(url, 1)
					.contentType(MediaType.APPLICATION_JSON))
					// THEN
					.andExpect(status().isNoContent());
		}

	}

	/**
	 * Consolidates the error messages and returns it
	 *
	 * @param mvcResult the mvcResult object
	 * @return String array with error messages
	 * @throws UnsupportedEncodingException when the string's encoding is not valid
	 * @throws JSONException                when there's exception while parsing
	 */
	private String[] getErrorMessages(MvcResult mvcResult) throws UnsupportedEncodingException, JSONException {
		String response = mvcResult.getResponse().getContentAsString();
		JSONObject json = new JSONObject(response);
		json.remove("errorCode");
		return json.get("message").toString().split(",");
	}
}
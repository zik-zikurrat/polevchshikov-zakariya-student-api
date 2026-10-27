package com.polevchshikov.studentapi;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class StudentControllerTest {

	private static final String STUDENT_JSON = """
			{"firstName":"Zakariya","lastName":"Polevchshikov",
			 "email":"zakariya.test@student.example","groupName":"IT2-2312"}
			""";

	@Autowired
	private MockMvc mockMvc;

	@Test
	void rootShowsStudentName() throws Exception {
		mockMvc.perform(get("/"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.student").value("Polevchshikov Zakariya"));
	}

	@Test
	void createAndGetStudentById() throws Exception {
		long id = createStudent();
		mockMvc.perform(get("/api/students/{id}", id))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.lastName").value("Polevchshikov"));
	}

	@Test
	void getAllStudentsContainsTestData() throws Exception {
		mockMvc.perform(get("/api/students"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$[*].lastName", hasItem("Polevchshikov")));
	}

	@Test
	void updateStudent() throws Exception {
		long id = createStudent();
		mockMvc.perform(put("/api/students/{id}", id)
				.contentType(MediaType.APPLICATION_JSON)
				.content("""
						{"firstName":"Zakariya","lastName":"Polevchshikov",
						 "email":"zakariya.updated@student.example","groupName":"IT2-2312"}
						"""))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.email").value("zakariya.updated@student.example"));
	}

	@Test
	void updateNonexistentStudentReturns404() throws Exception {
		mockMvc.perform(put("/api/students/{id}", 999999)
				.contentType(MediaType.APPLICATION_JSON)
				.content(STUDENT_JSON))
			.andExpect(status().isNotFound());
	}

	@Test
	void deleteStudent() throws Exception {
		long id = createStudent();
		mockMvc.perform(delete("/api/students/{id}", id)).andExpect(status().isNoContent());
		mockMvc.perform(get("/api/students/{id}", id)).andExpect(status().isNotFound());
	}

	@Test
	void nonexistentStudentReturns404() throws Exception {
		mockMvc.perform(get("/api/students/{id}", 999999))
			.andExpect(status().isNotFound())
			.andExpect(jsonPath("$.message").value("Student with id 999999 not found"));
	}

	@Test
	void invalidStudentReturns400() throws Exception {
		mockMvc.perform(post("/api/students")
				.contentType(MediaType.APPLICATION_JSON)
				.content("""
						{"firstName":"","lastName":"Polevchshikov","email":"not-an-email"}
						"""))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.fields.firstName").value("First name is required"))
			.andExpect(jsonPath("$.fields.email").value("Email must be valid"));
	}

	private long createStudent() throws Exception {
		MvcResult result = mockMvc.perform(post("/api/students")
				.contentType(MediaType.APPLICATION_JSON)
				.content(STUDENT_JSON))
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.id").exists())
			.andReturn();
		String body = result.getResponse().getContentAsString();
		return Long.parseLong(body.replaceAll(".*\"id\":(\\d+).*", "$1"));
	}

}

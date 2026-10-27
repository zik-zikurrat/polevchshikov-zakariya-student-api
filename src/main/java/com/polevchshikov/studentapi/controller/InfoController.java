package com.polevchshikov.studentapi.controller;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.info.BuildProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Root endpoint that shows who owns this application and which version is running.
 */
@RestController
public class InfoController {

	private final String studentName;

	private final String studentGroup;

	private final String studentId;

	private final String version;

	public InfoController(@Value("${student.name}") String studentName,
			@Value("${student.group}") String studentGroup,
			@Value("${student.id}") String studentId,
			ObjectProvider<BuildProperties> buildProperties) {
		this.studentName = studentName;
		this.studentGroup = studentGroup;
		this.studentId = studentId;
		BuildProperties build = buildProperties.getIfAvailable();
		this.version = (build != null) ? build.getVersion() : "dev";
	}

	@GetMapping("/")
	public Map<String, Object> info() {
		Map<String, Object> info = new LinkedHashMap<>();
		info.put("application", "Student CRUD API");
		info.put("student", studentName);
		info.put("group", studentGroup);
		info.put("studentId", studentId);
		info.put("version", version);
		info.put("students", "/polevchshikov-zakariya/api/students");
		return info;
	}

}

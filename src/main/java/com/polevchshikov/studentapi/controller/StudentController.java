package com.polevchshikov.studentapi.controller;

import java.util.List;

import com.polevchshikov.studentapi.model.Student;
import com.polevchshikov.studentapi.repository.StudentRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/students")
public class StudentController {

	private final StudentRepository repository;

	public StudentController(StudentRepository repository) {
		this.repository = repository;
	}

	@GetMapping
	public List<Student> findAll() {
		return repository.findAll();
	}

	@GetMapping("/{id}")
	public Student findById(@PathVariable Long id) {
		return repository.findById(id)
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student with id " + id + " not found"));
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Student create(@RequestBody Student student) {
		return repository.save(student);
	}

	@PutMapping("/{id}")
	public Student update(@PathVariable Long id, @RequestBody Student changes) {
		Student student = repository.findById(id)
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student with id " + id + " not found"));
		student.setFirstName(changes.getFirstName());
		student.setLastName(changes.getLastName());
		student.setEmail(changes.getEmail());
		student.setGroupName(changes.getGroupName());
		return repository.save(student);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable Long id) {
		if (!repository.existsById(id)) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Student with id " + id + " not found");
		}
		repository.deleteById(id);
	}

}

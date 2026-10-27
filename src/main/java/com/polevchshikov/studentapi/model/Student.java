package com.polevchshikov.studentapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "students")
public class Student {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private Long id;

	@NotBlank(message = "First name is required")
	@Column(nullable = false)
	private String firstName;

	@NotBlank(message = "Last name is required")
	@Column(nullable = false)
	private String lastName;

	@NotBlank(message = "Email is required")
	@Email(message = "Email must be valid")
	@Column(nullable = false)
	private String email;

	private String groupName;

	protected Student() {
	}

	public Student(String firstName, String lastName, String email, String groupName) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.groupName = groupName;
	}

	public Long getId() {
		return id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

}

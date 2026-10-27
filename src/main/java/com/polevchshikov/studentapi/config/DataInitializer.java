package com.polevchshikov.studentapi.config;

import com.polevchshikov.studentapi.model.Student;
import com.polevchshikov.studentapi.repository.StudentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Inserts individual test data the first time the application starts with an empty database.
 */
@Component
public class DataInitializer implements CommandLineRunner {

	private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

	private final StudentRepository repository;

	public DataInitializer(StudentRepository repository) {
		this.repository = repository;
	}

	@Override
	public void run(String... args) {
		if (repository.count() > 0) {
			log.info("Database already contains {} students, skipping test data", repository.count());
			return;
		}
		repository.save(new Student("Zakariya", "Polevchshikov", "zakariya.polevchshikov@student.example", "IT2-2312"));
		repository.save(new Student("Demo", "Student-37052", "demo.37052@student.example", "IT2-2312"));
		log.info("Inserted test data for Polevchshikov Zakariya (IT2-2312)");
	}

}

package com.cs.assignment.eventhandler;

import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.cs.assignment.eventhandler.service.LogEventHandlerService;

import lombok.extern.apachecommons.CommonsLog;

@SpringBootApplication
@EnableJpaRepositories
@CommonsLog
public class SpringBootStarter implements CommandLineRunner {

	@Autowired
	LogEventHandlerService eventHandlerService;

	public static void main(String[] args) throws Exception {
		SpringApplication.run(SpringBootStarter.class, args);
		log.info("Starting main app..");
	}

	@Override
	public void run(String... args) throws Exception {
		log.info("Inside commandline runner..");
		URI uri = new ClassPathResource("logfile.txt").getURI();
		Path path = Paths.get(uri);
		eventHandlerService.logFileProcessor(path);
	}

}

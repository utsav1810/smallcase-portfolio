package com.smallcase.portfolio;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * @author utsav
 * Defining spring application
 * Entry point of code
 */
@SpringBootApplication
public class PortfolioApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(PortfolioApplication.class, args);
	}

}

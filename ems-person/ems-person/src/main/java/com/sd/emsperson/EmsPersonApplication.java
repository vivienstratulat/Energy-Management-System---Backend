package com.sd.emsperson;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
@ConfigurationPropertiesScan
public class EmsPersonApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmsPersonApplication.class, args);
	}

}

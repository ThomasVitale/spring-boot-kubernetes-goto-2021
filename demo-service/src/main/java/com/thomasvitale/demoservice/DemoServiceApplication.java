package com.thomasvitale.demoservice;

import reactor.core.publisher.Mono;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@ConfigurationPropertiesScan
public class DemoServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoServiceApplication.class, args);
	}

}

@RestController
class DemoController {

	private final DemoProperties demoProperties;

	DemoController(DemoProperties demoProperties) {
		this.demoProperties = demoProperties;
	}

	@GetMapping("/")
	public Mono<String> getMessage() {
		return Mono.just(demoProperties.getMessage());
	}

}

@ConfigurationProperties(prefix = "demo")
class DemoProperties {

	private String message;

	String getMessage() {
		return message;
	}

	void setMessage(String message) {
		this.message = message;
	}

}

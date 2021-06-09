package com.thomasvitale.demonative;

import java.net.URL;
import java.time.Duration;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication
@ConfigurationPropertiesScan
public class DemoNativeApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoNativeApplication.class, args);
	}

}

@RestController
class NativeController {

	private final WebClient webClient;

	NativeController(NativeProperties nativeProperties, WebClient.Builder webClientBuilder) {
		this.webClient = webClientBuilder
				.baseUrl(nativeProperties.getServiceUrl().toString())
				.build();
	}

	@GetMapping("/")
	public Flux<String> getMessages() {
		return webClient
				.get()
				.uri("/")
				.retrieve()
				.bodyToMono(String.class)
				.timeout(Duration.ofSeconds(1))
				.onErrorResume(exception -> Mono.empty())
				.retryWhen(Retry.backoff(3, Duration.ofMillis(500)))
				.concatWith(Mono.just(" And Spring Native rocks!"));
	}

}

@ConfigurationProperties(prefix = "native")
class NativeProperties {

	private URL serviceUrl;

	URL getServiceUrl() {
		return serviceUrl;
	}

	void setServiceUrl(URL serviceUrl) {
		this.serviceUrl = serviceUrl;
	}

}
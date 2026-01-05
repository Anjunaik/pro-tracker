package com.acoderpro.servicesImp;

import java.time.Duration;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.acoderpro.dto.OtpMailRequest;
import com.acoderpro.services.NotificationClient;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class NotificationClientImp implements NotificationClient {

	private final WebClient webClient;

	public NotificationClientImp(WebClient.Builder webClientBuilder) {
		// Build a WebClient with base URL
		this.webClient = webClientBuilder.baseUrl("http://localhost:9087").build();
	}


	@Override
	public void sendOtp(OtpMailRequest mailRequest) {

	    try {
	        webClient.post()
	                .uri("/api/notifications/send-email")
	                .contentType(MediaType.APPLICATION_JSON)   // ✅ REQUIRED
	                .accept(MediaType.APPLICATION_JSON)
	                .bodyValue(mailRequest)
	                .retrieve()
	                .onStatus(
	                        status -> status.is4xxClientError() || status.is5xxServerError(),
	                        response -> response.bodyToMono(String.class)
	                                .defaultIfEmpty("Notification service error")
	                                .flatMap(msg -> Mono.error(
	                                        new RuntimeException("Notification error: " + msg)
	                                ))
	                )
	                .bodyToMono(String.class)
	                .timeout(Duration.ofSeconds(10))
	                .block(); // 🔥 IMPORTANT

	    } catch (Exception ex) {
	        throw new RuntimeException("Notification service is DOWN", ex);
	    }
	}

}
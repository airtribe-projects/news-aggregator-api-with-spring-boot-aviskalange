package org.airtribe.newsApi.controller;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.airtribe.newsApi.JwtUtil.JwtUtil;
import org.airtribe.newsApi.entity.User;
import org.airtribe.newsApi.model.UserModel;
import org.airtribe.newsApi.service.UserService;
import org.airtribe.newsApi.util.Result;
import org.airtribe.newsApi.util.Topic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import reactor.core.publisher.Mono;

@RestController
public class RegistrationController {
	@Autowired
	private WebClient webClient;

	@Autowired
	private UserService userService;

	@Autowired
	private JwtUtil jwtUtil;

	@PostMapping("/register")
	public User registerUser(@Valid @RequestBody UserModel user, HttpServletRequest request) {
		User savedUser = userService.register(user);
		String token = UUID.randomUUID().toString();
		userService.createVerificationToken(savedUser, token);

		String applicationUrl = getApplicationUrl(request) + "/verification?token=" + token;
		System.out.println("Verification token created for user: " + savedUser.getEmail());
		System.out.println("Verification url for user: " + applicationUrl);
		return savedUser;
	}

	@PostMapping("/verification")
	public String userVerification(@RequestParam String token) {
		boolean isValid = userService.validateTokenAndEnableUser(token);
		if (!isValid) {
			return "Invalid token";
		}
		return "User enabled successfully";
	}

	@PostMapping("/signin")
	public String loginUser(@RequestParam String email, @RequestParam String password) {
		return userService.login(email, password);
	}

	private String getApplicationUrl(HttpServletRequest request) {
		return request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
	}

	@GetMapping("/hello")
	public String hello() {
		return "Welcome to News Aggregator application";
	}

	@GetMapping("/preferences")
	public List<Topic> getPreferences(@RequestHeader("Authorization") String token) {
		String email = jwtUtil.getEmailFromToken(token.replace("Bearer ", ""));
		User user = userService.getUsersByName(email).get(0);

		if (user == null || !user.isEnabled()) {
			return Collections.emptyList();
		}

		return user.getTopicPreference();
	}

	@PutMapping("/preferences")
	public void updatePreferences(@RequestHeader("Authorization") String token, @Valid @RequestBody List<Topic> preferences) {
		String email = jwtUtil.getEmailFromToken(token.replace("Bearer ", ""));
		User user = userService.getUsersByName(email).get(0);

		if (user == null || !user.isEnabled()) {
			return;
		}

		user.setTopicPreference(preferences);
		userService.updateUser(user);
	}

	@GetMapping("/news")
	public Mono<Result> fetchNews(@RequestHeader("Authorization") String token) {
		String email = jwtUtil.getEmailFromToken(token.replace("Bearer ", ""));
		User user = userService.getUsersByName(email).get(0);

		if (user == null || !user.isEnabled()) {
			return null;
		}

		List<Topic> preferences = user.getTopicPreference();
		String query = preferences.isEmpty() ? "keyword" : preferences.stream().map(Enum::name).collect(Collectors.joining(" OR "));
		Mono<Result> result = webClient.get()
				.uri("https://newsapi.org/v2/everything?apiKey=717ac5c1c69148e1bbbf7e16e2567d09&q=" + query)
				.retrieve().bodyToMono(Result.class)
				.doFinally(signal -> {
					System.out.println("Received response from News API");
				})
				.doFirst(() -> {
					System.out.println("Calling News API");
				});

		return result;
	}
}

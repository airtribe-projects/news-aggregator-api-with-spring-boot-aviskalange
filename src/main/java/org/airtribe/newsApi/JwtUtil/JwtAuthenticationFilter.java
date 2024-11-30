package org.airtribe.newsApi.JwtUtil;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	public static final String SECRET_KEY = "5b5df31f8ee5aafd5494c7bd0fec4b6c9359aab05bd59ec1b74ed8494c775c58";

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String authorizationHeader = request.getHeader("Authorization");

		if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
			String token = authorizationHeader.substring(7); // Extract the JWT token

			// Parse the token to extract claims
			Claims claims = Jwts.parserBuilder()
					.setSigningKey(Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8)))
					.build()
					.parseClaimsJws(token)
					.getBody();

			String username = claims.getSubject();

			if (username != null) {
				User authenticatedUser = new User(username, "", Collections.emptyList());
				UsernamePasswordAuthenticationToken authentication =
						new UsernamePasswordAuthenticationToken(authenticatedUser, null, Collections.emptyList());
				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

				SecurityContextHolder.getContext().setAuthentication(authentication);
			}
		}

		filterChain.doFilter(request, response);
	}
}
package com.leets.xcellentbe.global.config;

import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import static org.springframework.security.config.Customizer.withDefaults;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		return http
			.formLogin(AbstractHttpConfigurer::disable)
			.httpBasic(AbstractHttpConfigurer::disable)
			.cors(withDefaults())
			.csrf(AbstractHttpConfigurer::disable)
			.headers(
				headersConfigurer ->
					headersConfigurer
						.frameOptions(
							HeadersConfigurer.FrameOptionsConfig::sameOrigin
						)
			)

			// jwt 토큰을 사용하기 때문에 세션을 STATELESS로 설정
			.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

			.authorizeHttpRequests(
				authorize ->
					authorize
						.requestMatchers("/v3/api-docs", "/v3/api-docs/**", "/swagger-ui.html", "/swagger-ui/**",
							"/swagger/**", "/api/user/**").permitAll()
						.anyRequest().authenticated()
			)

			.build();
	}

	   @Bean
	   public CorsConfigurationSource corsConfigurationSource() {
	       CorsConfiguration configuration = new CorsConfiguration();

	       configuration.setAllowedOrigins(Arrays.asList());
	       configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PATCH", "DELETE"));
	       configuration.setAllowedHeaders(Arrays.asList("*"));
	       configuration.setExposedHeaders(Arrays.asList("Authorization, Authorization_refresh"));
	       configuration.setAllowCredentials(true);

	       UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
	       source.registerCorsConfiguration("/**", configuration);
	       return source;
	   }
}

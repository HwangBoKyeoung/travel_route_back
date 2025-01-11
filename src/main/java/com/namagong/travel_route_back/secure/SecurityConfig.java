package com.namagong.travel_route_back.secure;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.namagong.travel_route_back.secure.jwt.JWTFilter;
import com.namagong.travel_route_back.secure.jwt.JWTUtil;
import com.namagong.travel_route_back.secure.jwt.LoginFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity(debug=true)
@RequiredArgsConstructor
public class SecurityConfig {
	
	private final AuthenticationConfiguration authenticationConfiguration;
	@Autowired
	private final JWTUtil jwtUtil;
	
	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration conf = new CorsConfiguration();
		conf.addAllowedOrigin("http://localhost:3000");			// 허용할 origin 설정
		conf.addAllowedMethod("*");								// 모든 HTTP 메서드 허용
		conf.addAllowedHeader("*");								// 모든 Header 허용
		conf.setAllowCredentials(true);							// 쿠키허용
		
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", conf);			// 모든 경로에 대해 위 설정 적용
		
		return source;
	}
	
	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
			.httpBasic((auth) -> auth.disable())
			.csrf((auth) -> auth.disable())
			.formLogin((auth) -> auth.disable())
			.authorizeHttpRequests((auth) -> auth
					.requestMatchers(new AntPathRequestMatcher("/*")
									, new AntPathRequestMatcher("/login")
									, new AntPathRequestMatcher("/register")).permitAll()
					.requestMatchers(new AntPathRequestMatcher("/admin")).hasRole("admin")
					.anyRequest().authenticated());
		http
			.addFilterBefore(new JWTFilter(jwtUtil), LoginFilter.class)
			.addFilterAt(new LoginFilter(authenticationManager(authenticationConfiguration), jwtUtil), UsernamePasswordAuthenticationFilter.class);
		http
			.sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
		return http.build();
	}
	
	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) 
			throws Exception { 
		return configuration.getAuthenticationManager(); 
	}

}

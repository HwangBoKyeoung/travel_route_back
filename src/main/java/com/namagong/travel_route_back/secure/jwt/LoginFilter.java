package com.namagong.travel_route_back.secure.jwt;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.namagong.travel_route_back.user.domain.CustomUserDetails;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

/**
 * @since 2025. 1. 11.
 * @author 황보경
 * <PRE>
 * --------------------------
 * 개정이력
 * 2025. 1. 11. 황보경 : 최초작성
 * dispatcherservlet으로 가기 전 단계 filter
 */
@Log4j2
@RequiredArgsConstructor
public class LoginFilter extends UsernamePasswordAuthenticationFilter {
	
	private final AuthenticationManager authenticationManager;
	@Autowired
	private final JWTUtil jwtUtil;
	
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) 
			throws AuthenticationException {
		String username = "";
		String password = "";
		try {
			StringBuilder body = new StringBuilder();
			try(BufferedReader reader = request.getReader()) {
				String line;
				while((line = reader.readLine()) != null) {
					body.append(line);
				}
			} catch(IOException e) {
				throw new RuntimeException("Failed to read request body", e);
			}
			log.info("request body : " + body.toString());
			ObjectMapper obj = new ObjectMapper();
			JsonNode node = obj.readTree(body.toString());
			
			username = node.get("username").asText();
			password = node.get("password").asText();
		} catch(IOException e) {
			throw new RuntimeException("Failed to obtain username and password from request", e);
		} catch(Exception e) {
			throw new RuntimeException("Failed to obtain username and password from request", e);
		}
		
		log.info("username = " + username);
		log.info("password = " + password);
		// username, password를 token에 담기
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password, null);
		// token에 담은 검증을 위한 authentication manager로 전달
		return authenticationManager.authenticate(token);
	}
	
	// 로그인 성공하면 실행하는 코드 (여기서 JWT 발급)
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response
			, FilterChain chain, Authentication authentication) {
		CustomUserDetails details = (CustomUserDetails) authentication.getPrincipal();
		String username = details.getUsername();
		log.info("username = " + username);
		
		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
		Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
		
		GrantedAuthority auth = iterator.next();
		String role = auth.getAuthority();
		log.info("role = " + role);
		
		// username, role, 만료시간 설정
		String token = jwtUtil.createJwt(username, role, 60*60*10L);
		
		response.addHeader("Authorization", "Bearer " + token);
	}
	
	// 로그인 실패하면 실행하는 코드
	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response
			, AuthenticationException failed) {
		response.setStatus(401);
	}

}

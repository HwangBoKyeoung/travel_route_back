package com.namagong.travel_route_back.secure.jwt;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.namagong.travel_route_back.user.domain.CustomUserDetails;
import com.namagong.travel_route_back.user.domain.User;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;

/**
 * @since 2025. 1. 11.
 * @author 황보경
 * <PRE>
 * --------------------------
 * 개정이력
 * 2025. 1. 11. 황보경 : JWT filter 최초작성 (토큰 인증 등)
 */
@Log4j2
public class JWTFilter extends OncePerRequestFilter {
	
	@Autowired
	private final JWTUtil jwtUtil;
	
	public JWTFilter(JWTUtil jwtUtil) {
		this.jwtUtil = jwtUtil;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		// request에서 authorization 헤더 찾기
		String auth = request.getHeader("Authorization");
		// header 검증
		if(auth == null || !auth.startsWith("Bearer ")) {
			log.info("token null");
			filterChain.doFilter(request, response);
			return;
		}
		
		log.info("authorization start");
		// Bearer 부분 제거 후 순수 토큰 획득
		String token = auth.split(" ")[1];
		// 토큰 소멸시간 검증
		if(jwtUtil.isExpired(token)) {
			log.info("token expired!!");
			filterChain.doFilter(request, response);
			return;
		}
		
		// 토큰에서 username, role 등 획득
		String username = jwtUtil.getUsername(token);
		String role = jwtUtil.getRole(token);
		
		// 사용자 entity 생성
		User user = new User();
		user.setUsername(username);
		user.setPassword("temppassword");
		user.setRole(role);
		
		// 회원정보 객체 담기
		CustomUserDetails details = new CustomUserDetails(user);
		// spring security 인증 token 생성
		Authentication authToken = new UsernamePasswordAuthenticationToken(details, null, details.getAuthorities());
		// 세션에 사용자 등록
		SecurityContextHolder.getContext().setAuthentication(authToken);
		filterChain.doFilter(request, response);
	}

}

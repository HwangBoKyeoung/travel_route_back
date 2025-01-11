package com.namagong.travel_route_back.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.namagong.travel_route_back.user.domain.CustomUserDetails;
import com.namagong.travel_route_back.user.domain.User;
import com.namagong.travel_route_back.user.mapper.UserMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
	
	@Autowired
	private final UserMapper userMapper;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User userData = userMapper.selectUser(username);
		return userData==null ? null : new CustomUserDetails(userData);
	}

}

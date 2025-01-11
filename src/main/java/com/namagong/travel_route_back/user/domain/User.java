package com.namagong.travel_route_back.user.domain;

import lombok.Data;

@Data
public class User {
	private long id;
	private String username;
	private String email;
	private String password;
	private String role;
	
	public User() {
		this.role = "general";
		this.password = "1234";
	}
}

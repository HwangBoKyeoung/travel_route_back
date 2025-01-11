package com.namagong.travel_route_back.user.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.namagong.travel_route_back.user.domain.User;

@Mapper
public interface UserMapper {
	User selectUser(String username);
}

package com.namagong.travel_route_back.bulletin.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.namagong.travel_route_back.bulletin.domain.Bulletin;

@Mapper
public interface BulletinMapper {
	List<Bulletin> selectBulletinList();
}

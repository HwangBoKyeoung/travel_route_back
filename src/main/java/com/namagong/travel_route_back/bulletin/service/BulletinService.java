package com.namagong.travel_route_back.bulletin.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.namagong.travel_route_back.bulletin.domain.Bulletin;
import com.namagong.travel_route_back.bulletin.mapper.BulletinMapper;

@Service
public class BulletinService {
	@Autowired
	private BulletinMapper bulletinMapper;
	
	public List<Bulletin> selectBulletinList() {
		return bulletinMapper.selectBulletinList();
	}
}

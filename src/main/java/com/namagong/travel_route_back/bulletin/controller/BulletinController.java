package com.namagong.travel_route_back.bulletin.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.namagong.travel_route_back.bulletin.domain.Bulletin;
import com.namagong.travel_route_back.bulletin.service.BulletinService;

@RestController
public class BulletinController {
	@Autowired
	private BulletinService bulletinService;
	
	@GetMapping("/bulletinList")
	public ResponseEntity<List<Bulletin>> selectBulletinList() {
		List<Bulletin> list = bulletinService.selectBulletinList();
		return ResponseEntity.ok(list);
	}
}

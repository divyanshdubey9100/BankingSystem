package com.wgs.demo.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.wgs.demo.classes.AdminRegReq;
import com.wgs.demo.repo.AdminRegReqRepo;

@Component
public class AdminReqImpl {
	@Autowired
	AdminRegReqRepo adminRepo;
	@Autowired
	AdminRegReq regReq;

	public int getTokenId() {
		return (int) adminRepo.count();
	}

	public boolean isUserIdExists(String userId) {
		boolean id = adminRepo.existsByUserId(userId);
		if (id == true) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isMobileExists(String mobile) {
		boolean mob = adminRepo.existsByMobile(mobile);
		if (mob == true) {
			return true;
		} else {
			return false;
		}
	}
	
	public List<AdminRegReq> findAllReq(){
		return adminRepo.findAll();
	}
}
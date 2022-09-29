package com.wgs.demo.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.wgs.demo.classes.AdminRegReq;
import com.wgs.demo.classes.CustomerReqReq;
import com.wgs.demo.repo.CustRegReqRepo;
import com.wgs.demo.repo.CustRepo;

@Component
public class CustReqImpl {
	@Autowired
	CustRegReqRepo reqRepo;
	@Autowired
	CustRepo custRepo;

	public boolean isAccExists(int accno) {
		boolean acc = reqRepo.existsById(accno);
		if (acc == true) {
			return true;
		} else {
			return false;
		}
	}

	public int getTokenId() {
		return (int) custRepo.count();
	}

	public boolean isMobileExists(String mobile) {
		boolean mob = reqRepo.existsByMobile(mobile);
		if (mob == true) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isMailExists(String email) {
		boolean mail = reqRepo.existsByEmail(email);
		if (mail == true) {
			return true;
		} else {
			return false;
		}
	}
	
	public List<CustomerReqReq> findAllReq(){
		return reqRepo.findAll();
	}

}
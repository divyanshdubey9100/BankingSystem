package com.wgs.demo.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.wgs.demo.classes.AdminReg;
import com.wgs.demo.repo.AdminRegRepo;

@Component
public class AdminImpl {
	@Autowired
	AdminRegRepo adminRepo;

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
	
	public boolean isIdExists(int userId) {
		boolean id = adminRepo.existsById(userId);
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

	public List<AdminReg> findByuId(String userId) {
		List<AdminReg> list = adminRepo.findByUserId(userId);
		return list;
	}

	public boolean adminAuthintication(String uid, String pass) {
		List<AdminReg> list = adminRepo.findByUserIdAndPass(uid, pass);
		for (AdminReg reg : list) {
			if (reg.getUserId().equals(uid) && reg.getPass().equals(pass)) {
				return true;
			} else
				return false;
		}
		return false;
	}

	public List<AdminReg> findMobileAndName(String mobile, String name) {
		List<AdminReg> list = adminRepo.findByMobileAndName(mobile, name);
		return list;
	}

	public List<AdminReg> findUidAndMobile(String uid, String mobile) {
		List<AdminReg> list = adminRepo.findByUserIdAndMobile(uid, mobile);
		return list;
	}

}

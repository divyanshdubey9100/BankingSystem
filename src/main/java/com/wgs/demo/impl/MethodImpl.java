package com.wgs.demo.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.wgs.demo.repo.CustRepo;

@Component
public class MethodImpl {
	@Autowired
	CustRepo custRepo;

	public boolean isAccExists(int accno) {
		boolean acc = custRepo.existsById(accno);
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
		boolean mob = custRepo.existsByMobile(mobile);
		if (mob == true) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isPersonExists(String name) {
		boolean n = custRepo.existsByName(name);
		if (n == true) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isMailExists(String email) {
		boolean mail = custRepo.existsByEmail(email);
		if (mail == true) {
			return true;
		} else {
			return false;
		}
	}

	

<<<<<<< HEAD
}
=======
}
>>>>>>> 18ec1090d7c2d1c474902dffd040ad203a1c3000

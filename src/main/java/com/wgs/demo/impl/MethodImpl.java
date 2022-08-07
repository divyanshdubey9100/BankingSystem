package com.wgs.demo.impl;

import java.util.List;

import com.wgs.demo.cust.Customer;
import com.wgs.demo.repo.CustRepo;

public class MethodImpl {
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
		List<Customer> accList = custRepo.findAll();
		int token = accList.size();
		return token;
	}
}

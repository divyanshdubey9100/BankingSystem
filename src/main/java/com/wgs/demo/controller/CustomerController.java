package com.wgs.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wgs.demo.classes.Customer;
import com.wgs.demo.impl.MethodImpl;
import com.wgs.demo.repo.AdminRegRepo;
import com.wgs.demo.repo.CustRepo;
@Controller
public class CustomerController {
	@Autowired
	AdminRegRepo regRepo;
	@Autowired
	CustRepo custRepo;
	@Autowired
	MethodImpl impl;
	@RequestMapping("customerLogin")
	private String customerLogin() {
		return "views/customerLogin";
	}
	
	@RequestMapping("customer")
	private String adminUi() {
		return "views/customer";
	}

	@RequestMapping("editAccountDetails")
	private String editAccountDetails(Customer customer, Model model) {
		String mes = "Work in Progress";
		System.out.println(mes);
		model.addAttribute("cust", mes);
		return "views/customerDetails";
	}

	@RequestMapping("checkBalanceByCust")
	private String checkBalanceByCust(Customer customer, Model model) {
		String mes = "Work in Progress";
		System.out.println(mes);
		model.addAttribute("cust", mes);
		return "views/customerDetails";
	}

	@RequestMapping("checkAccountStatement")
	private String checkAccountStatement(Customer customer, Model model) {
		String mes = "Work in Progress";
		System.out.println(mes);
		model.addAttribute("cust", mes);
		return "views/customerDetails";
	}
}

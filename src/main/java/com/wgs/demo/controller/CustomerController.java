package com.wgs.demo.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.wgs.demo.classes.AdminReg;
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

	@RequestMapping("custAuth")
	private String CustomerAuth(@RequestParam String email, @RequestParam String pass, Model model, Customer customer,
			HttpSession session) {
		if (impl.customerAuthintication(email, pass) == true) {
			List<Customer> list = impl.findByEmail(email);
			for (Customer cust : list) {
				session.setAttribute("custName", cust.getName());
			}
			return "redirect:/customer";
		}
		return "redirect:/customerLogin";
	}

	@RequestMapping("customer")
	private String adminUi(HttpSession session,Model model) {
		Object userName = session.getAttribute("custName");
		if (session.getAttribute("custName") == null) {
			return "redirect:/customerLogin";
		}
		model.addAttribute("name", userName);
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
	private String checkBalanceByCust(Customer customer, Model model, HttpSession session) {
		if (session.getAttribute("custName") == null) {
			return "redirect:/customerLogin";
		}
		String mes = "Work in Progress";
		System.out.println(mes);
		model.addAttribute("cust", mes);
		return "views/customerDetails";
	}

	@RequestMapping("checkAccountStatement")
	private String checkAccountStatement(Customer customer, Model model, HttpSession session) {
		if (session.getAttribute("custName") == null) {
			return "redirect:/customerLogin";
		}
		String mes = "Work in Progress";
		System.out.println(mes);
		model.addAttribute("cust", mes);
		return "views/customerDetails";
	}

	@RequestMapping("customerHelp")
	public String custHelp() {
		return "views/custHelp";
	}

	@RequestMapping("/forgetCustPass&Mail")
	private String forgetCustUidAndPass() {
		return "views/forgetCustDetails";
	}

	@RequestMapping("resetCustPass")
	private String forgetPwd(@RequestParam String mobile, String email, Model model) {
		List<Customer> list = impl.findMailAndMobile(email, mobile);
		if (list.size() != 0) {
			model.addAttribute("cust", list);
			return "views/resetCustPass";
		} else {
			String msg = "Hi " + email + " Mobile_no " + mobile + " No Details Found !";
			model.addAttribute("cust", msg);
		}
		return "views/customerDetails";
	}

	@RequestMapping("resetCustMail")
	private String findCustMail(@RequestParam String name, String mobile, Model model) {
		List<Customer> list = impl.findUidAndMobile(name, mobile);
		if (list.size() != 0) {
			for (Customer cust : list) {
				model.addAttribute("cust", cust.getEmail());
			}
		} else {
			String msg = "Hi !" + name + " Mobile_no " + mobile + " No Details Found !";
			model.addAttribute("cust", msg);
		}
		return "views/customerDetails";
	}

	@RequestMapping("updateCustPass")
	private String updatePass(Model model, Customer customer) {
		custRepo.saveAndFlush(customer);
		model.addAttribute("cust", customer.getPass());
		return "views/customerDetails";
	}

	@RequestMapping("/custLogout")
	private String logout(HttpSession session) {
		session.removeAttribute("custName");
		return "redirect:/customerLogin";
	}
}

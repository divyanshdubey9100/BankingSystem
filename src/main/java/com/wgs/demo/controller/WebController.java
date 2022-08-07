package com.wgs.demo.controller;

import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import com.wgs.demo.cust.Customer;
import com.wgs.demo.impl.MethodImpl;
import com.wgs.demo.repo.CustRepo;

@Controller
public class WebController {
	@Autowired
	CustRepo custRepo;

	MethodImpl impl;

	@RequestMapping("admin")
	public String customerUi() {
		return "views/Admin";
	}

	@RequestMapping("openAccount")
	public String openAccount() {
		return "views/openAccount";
	}

	@RequestMapping("customerAccountDetails")
	public String customerAccountDetails(Customer customer, Model model) {
		int accno = 1000 + impl.getTokenId();
		System.out.println("Before increment accno " + accno);
		try {
			for (int i = 0; i <= impl.getTokenId(); i++) {
				accno++;
				System.out.println("After increment accno " + accno);
				if (customer.getBalance() >= 1000 && impl.isAccExists(accno) == false) {
					customer.setAccno(accno);
					custRepo.save(customer);
					System.out.println(customer);
					model.addAttribute("cust", "Account Created Successfully.." + customer);
					break;
				} else if (impl.isAccExists(accno) == true) {
					System.out.println("true");
					continue;
				}
			}
		} catch (Exception e) {
			System.out.println(e + " err hai err");
		}
		return "views/customerAccountDetails";
	}

	@RequestMapping("findCustomerDetails")
	public String findCustomerDetails() {
		System.out.println("Searching ....");
		return "views/findCustomerDetails";
	}

	@RequestMapping("findByAccno")
	public String detailsBasedOnAccNO(Model model, Customer customer) {
		List<Customer> custList = custRepo.findByAccno(customer.getAccno());
		System.out.println(custList);
		model.addAttribute("cust", custList);
		return "views/customerList";
	}

	@RequestMapping("showAllCustomers")
	public String showAllCustomers(Model model) {
		List<Customer> custList = custRepo.findAll();
		System.out.println(custList);
		model.addAttribute("cust", custList);
		return "views/customerList";
	}

	@RequestMapping("findByName")
	public String findByName(Model model, Customer customer) {
		List<Customer> custList = custRepo.findByName(customer.getName());
		System.out.println(custList);
		model.addAttribute("cust", custList);
		return "views/customerList";
	}

	@RequestMapping("findByMobile")
	public String findByMobile(Customer customer, Model model) {
		List<Customer> custList = custRepo.findByMobile(customer.getMobile());
		System.out.println(custList);
		model.addAttribute("cust", custList);
		return "views/customerList";
	}

	@RequestMapping("banking")
	public String banking() {
		return "views/banking";
	}

	@RequestMapping("deposit")
	public String deposit(Customer customer, Model model) {
		System.out.println("accno :" + customer.getAccno() + " Deposit Amount :" + customer.getBalance());
		List<Customer> custList = custRepo.findByAccno(customer.getAccno());
		for (Customer cust : custList) {
			int newAmount = cust.getBalance() + customer.getBalance();
			cust.setBalance(newAmount);
			String msg = "Hi " + cust.getName() + " " + customer.getBalance() + " is Successfully Deposited in A/c : "
					+ cust.getAccno() + " Updated Balance is " + cust.getBalance();
			System.out.println(msg);
			model.addAttribute("cust", msg);
			custRepo.flush();
		}
		return "views/customerAccountDetails";
	}

	@RequestMapping("withdraw")
	public String withdraw(Customer customer, Model model) {
		System.out.println("accno :" + customer.getAccno() + " Withdraw Amount :" + customer.getBalance());
		List<Customer> custList = custRepo.findByAccno(customer.getAccno());
		for (Customer cust : custList) {
			System.out.println(customer.getAccno() + " cusomer.getAccNO()");
			System.out.println(cust.getAccno());
			if (customer.getBalance() >= 1000 && customer.getBalance() < cust.getBalance()) {
				int newAmount = cust.getBalance() - customer.getBalance();
				cust.setBalance(newAmount);
				String msg = "Hi : " + cust.getName() + " : " + customer.getBalance()
						+ " is Successfully Withdrawn in a/c : " + cust.getAccno() + " Updated Balance is : "
						+ cust.getBalance();
				System.out.println(msg);
				model.addAttribute("cust", msg);
				custRepo.flush();

			} else {
				String msg = "Hi : " + cust.getName() + " your a/c : " + cust.getAccno()
						+ " has Low A/c Balance To Withraw";
				System.out.println(msg);
				model.addAttribute("cust", msg);
			}
		}
		return "views/customerAccountDetails";
	}

	@RequestMapping("checkBalance")
	public String checkBalance(Customer customer, Model model) {
		List<Customer> custList = custRepo.findByAccno(customer.getAccno());
		for (Customer cust : custList) {
			String bal = "Hello " + cust.getName() + " your a/c : " + cust.getAccno() + " balance : "
					+ cust.getBalance();
			System.out.println(bal);
			model.addAttribute("cust", bal);
		}

		return "views/customerAccountDetails";
	}

	@RequestMapping("deleteByAccno")
	public String deleteByAccno(Customer customer, Model model) {
		List<Customer> custList = custRepo.findByAccno(customer.getAccno());
		for (Customer cust : custList) {
			String mes = "Hello " + cust.getName() + " your a/c " + cust.getAccno() + " balance is:" + cust.getBalance()
					+ " is Deleted Successfully";
			System.out.println(mes);
			model.addAttribute("cust", mes);
		}
		custRepo.deleteById(customer.getAccno());
		return "views/customerAccountDetails";
	}

	@RequestMapping("customer")
	public String adminUi() {
		return "views/customer";
	}

	@RequestMapping("editAccountDetails")
	public String editAccountDetails(Customer customer, Model model) {
		String mes = "Work in Progress";
		System.out.println(mes);
		model.addAttribute("cust", mes);
		return "views/customerDetails";
	}

	@RequestMapping("checkBalanceByCust")
	public String checkBalanceByCust(Customer customer, Model model) {
		String mes = "Work in Progress";
		System.out.println(mes);
		model.addAttribute("cust", mes);
		return "views/customerDetails";
	}

	@RequestMapping("checkAccountStatement")
	public String checkAccountStatement(Customer customer, Model model) {
		String mes = "Work in Progress";
		System.out.println(mes);
		model.addAttribute("cust", mes);
		return "views/customerDetails";
	}
}

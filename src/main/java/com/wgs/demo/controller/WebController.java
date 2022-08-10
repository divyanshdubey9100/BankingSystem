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
	@Autowired
	MethodImpl impl;

	@RequestMapping("admin")
	private String customerUi() {
		return "views/Admin";
	}

	@RequestMapping("openAccount")
	private String openAccount() {
		return "views/openAccount";
	}

	@RequestMapping("customerAccountDetails")
	private String customerAccountDetails(Customer customer, Model model) {
		int accno = 1000 + impl.getTokenId();
		try {
			for (int i = 0; i <= impl.getTokenId(); i++) {
				accno++;
				if (customer.getBalance() >= 1000 && impl.isAccExists(accno) == false
						&& impl.isMobileExists(customer.getMobile()) == false) {
					customer.setAccno(accno);
					custRepo.save(customer);
					System.out.println(customer);
					model.addAttribute("cust", "Account Created Successfully.." + customer);
					break;
				} else if (impl.isAccExists(accno) == true) {
					String mes = accno + " already exists! plz Wait...";
					System.out.println(mes);
					model.addAttribute("cust", mes);
					continue;
				} else if (impl.isMobileExists(customer.getMobile()) == true) {
					String mes = "Try with new Mobile No.. " + customer.getMobile() + " already exists!";
					model.addAttribute("cust", mes);
					break;
				}
			}
		} catch (Exception e) {
			System.out.println(e + " err hai err");
		}
		return "views/customerAccountDetails";
	}

	@RequestMapping("findCustomerDetails")
	private String findCustomerDetails() {
		return "views/findCustomerDetails";
	}

	@RequestMapping("showAllCustomers")
	private String showAllCustomers(Model model) {
		if (impl.getTokenId() != 0) {
			List<Customer> custList = custRepo.findAll();
			model.addAttribute("cust", custList);
			return "views/customerList";
		} else {
			String msg = "Hii : Empty Response";
			model.addAttribute("cust", msg);
		}
		return "views/customerAccountDetails";
	}

	@RequestMapping("findByAccno")
	private String detailsBasedOnAccNO(Model model, Customer customer) {
		if (impl.isAccExists(customer.getAccno()) == true) {
			List<Customer> custList = custRepo.findByAccno(customer.getAccno());
			model.addAttribute("cust", custList);
			return "views/customerList";
		} else {
			String msg = "Hii : Invalid A/c no.";
			model.addAttribute("cust", msg);
		}
		return "views/customerAccountDetails";
	}

	@RequestMapping("findByName")
	private String findByName(Model model, Customer customer) {
		if (impl.isPersonExists(customer.getName()) == true) {
			List<Customer> custList = custRepo.findByName(customer.getName());
			model.addAttribute("cust", custList);
			return "views/customerList";
		} else {
			String msg = "Hii : No Person Exists!.";
			model.addAttribute("cust", msg);
		}
		return "views/customerAccountDetails";
	}

	@RequestMapping("findByMobile")
	private String findByMobile(Customer customer, Model model) {
		if (impl.isMobileExists(customer.getMobile()) == true) {
			List<Customer> custList = custRepo.findByMobile(customer.getMobile());
			model.addAttribute("cust", custList);
			return "views/customerList";
		} else {
			String msg = "Hii : Invalid Mobile No.";
			model.addAttribute("cust", msg);
		}
		return "views/customerAccountDetails";
	}

	@RequestMapping("banking")
	private String banking() {
		return "views/banking";
	}

	@RequestMapping("deposit")
	private String deposit(Customer customer, Model model) {
		if (impl.isAccExists(customer.getAccno()) == true) {
			List<Customer> custList = custRepo.findByAccno(customer.getAccno());
			for (Customer cust : custList) {
				if (impl.isAccExists(customer.getAccno()) == true) {
					int newAmount = cust.getBalance() + customer.getBalance();
					cust.setBalance(newAmount);
					String msg = "Hi " + cust.getName() + " " + customer.getBalance()
							+ " is Successfully Deposited in A/c : " + cust.getAccno() + " Updated Balance is "
							+ cust.getBalance();
					model.addAttribute("cust", msg);
					custRepo.flush();
				}
			}
		} else {
			String msg = "Hii :" + customer.getAccno() + " Invalid A/c no.";
			model.addAttribute("cust", msg);
		}
		return "views/customerAccountDetails";
	}

	@RequestMapping("withdraw")
	private String withdraw(Customer customer, Model model) {
		if (impl.isAccExists(customer.getAccno()) == true) {
			List<Customer> custList = custRepo.findByAccno(customer.getAccno());
			for (Customer cust : custList) {
				if ((cust.getBalance() - customer.getBalance()) > 1000 && cust.getBalance() > customer.getBalance()) {
					int newAmount = cust.getBalance() - customer.getBalance();
					cust.setBalance(newAmount);
					String msg = "Hi : " + cust.getName() + " : " + customer.getBalance()
							+ " is Successfully Withdrawn in a/c : " + cust.getAccno() + " Updated Balance is : "
							+ cust.getBalance();
					model.addAttribute("cust", msg);
					custRepo.flush();
				} else {
					String msg = "Hi : " + cust.getName() + " your a/c : " + cust.getAccno()
							+ " has Low A/c Balance To Withraw";
					model.addAttribute("cust", msg);
				}
			}
		} else {
			String msg = "Hii :" + customer.getAccno() + " Invalid A/c no.";
			model.addAttribute("cust", msg);
		}
		return "views/customerAccountDetails";
	}

	@RequestMapping("checkBalance")
	private String checkBalance(Customer customer, Model model) {
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
	private String deleteByAccno(Customer customer, Model model) {
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

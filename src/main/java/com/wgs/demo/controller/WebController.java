package com.wgs.demo.controller;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wgs.demo.cust.Customer;
import com.wgs.demo.repo.CustRepo;

@Controller
public class WebController {
//	Set<Customer> custList=new HashSet<Customer>();
	@Autowired
	CustRepo custRepo;
	private static int accno = 1000;
	@RequestMapping("customer")
	public String customerUi() {
		return "views/customer";
	}
	@RequestMapping("openAccount")
	public String openAccount() {
		return "views/openAccount";
	}

	@RequestMapping("customerAccountDetails")
	public String customerAccountDetails(Customer customer, Model model) {

		Optional<Customer> cust = custRepo.findById(accno);
		try {
			if (cust.isPresent()) {
				System.out.println(accno + " is present");
				System.out.println(cust.get());
				model.addAttribute("cust", cust.get());
			} else {
				if (customer.getBalance() >= 1000) {
					int accno=1000+getTokenId();
					customer.setAccno(++accno);
					custRepo.save(customer);
					System.out.println(customer);
					model.addAttribute("cust", customer);
				} else {
					String msg = "Minimum Balance To Open New A/c Is 1000";
					System.out.println(msg);
					model.addAttribute("cust", msg);
				}
			}
			

		} catch (NoSuchElementException e) {
			System.out.println(e + " err hai err");
		}
		return "views/customerAccountDetails";
	}
	public int getTokenId() {
		List<Customer> accList=custRepo.findAll();
		int token=accList.size();
		return token;
	}

	@RequestMapping("findCustomerDetails")
	public String findCustomerDetails() {
		System.out.println("Searching ....");
		return "views/findCustomerDetails";
	}

	@RequestMapping("findByAccno")
	public String detailsBasedOnAccNO(Model model, Customer customer) {
//		Customer customer1=custRepo.findById(accno).orElse(new Customer());
		System.out.println(custRepo.findById(customer.getAccno()));
		model.addAttribute("cust", custRepo.findById(customer.getAccno()));
		return "views/customerAccountDetails";
	}

	@RequestMapping("findByName")
	public String findByName(Model model, Customer customer) {
		System.out.println(custRepo.findByName(customer.getName()));
		model.addAttribute("cust", custRepo.findByName(customer.getName()));
		return "views/customerAccountDetails";
	}

	@RequestMapping("findByMobile")
	public String findByMobile(Customer customer, Model model) {
		System.out.println(custRepo.findByMobile(customer.getMobile()));
		model.addAttribute("cust", custRepo.findByMobile(customer.getMobile()));
		return "views/customerAccountDetails";
	}

	@RequestMapping("banking")
	public String banking() {
		return "views/banking";
	}

	@RequestMapping("deposit")
	public String deposit(Customer customer, Model model) {
		System.out.println("accno :" + customer.getAccno() + " Deposit Amount :" + customer.getBalance());
		Optional<Customer> cust = custRepo.findById(customer.getAccno());
		int newAmount = cust.get().getBalance() + customer.getBalance();
		cust.get().setBalance(newAmount);
		String msg = "Hi " + cust.get().getName() + " " + customer.getBalance() + " is Successfully Deposited in A/c : "
				+ cust.get().getAccno() + " Updated Balance is " + cust.get().getBalance();
		System.out.println(msg);
		model.addAttribute("cust", msg);
		custRepo.flush();
		return "views/customerAccountDetails";
	}

	@RequestMapping("withdraw")
	public String withdraw(Customer customer, Model model) {
		System.out.println("accno :" + customer.getAccno() + " Withdraw Amount :" + customer.getBalance());
		Optional<Customer> cust = custRepo.findById(customer.getAccno());
		if (customer.getBalance() >= 1000 && customer.getBalance() < cust.get().getBalance()) {
			int newAmount = cust.get().getBalance() - customer.getBalance();
			cust.get().setBalance(newAmount);
			String msg = "Hi : " + cust.get().getName() + " : " + customer.getBalance()
					+ " is Successfully Withdrawn in a/c : " + cust.get().getAccno() + " Updated Balance is : "
					+ cust.get().getBalance();
			System.out.println(msg);
			model.addAttribute("cust", msg);
			custRepo.flush();
		} else {
			String msg = "Hi : " + cust.get().getName() + " your a/c : " + cust.get().getAccno()
					+ " has Low A/c Balance To Withraw";
			System.out.println(msg);
			model.addAttribute("cust", msg);
		}

		return "views/customerAccountDetails";
	}

	@RequestMapping("checkBalance")
	public String checkBalance(Customer customer, Model model) {
		Optional<Customer> cust = custRepo.findById(customer.getAccno());
		String bal = "Hello " + cust.get().getName() + " your a/c : " + cust.get().getAccno() + " balance : "
				+ cust.get().getBalance();
		System.out.println(bal);
		model.addAttribute("cust", bal);
		return "views/customerAccountDetails";
	}

	@RequestMapping("showAllCustomers")
	public String showAllCustomers(Customer customer, Model model) {
		System.out.println(custRepo.findAll());
		model.addAttribute("cust", custRepo.findAll());
		return "views/customerAccountDetails";
	}

	@RequestMapping("deleteByAccno")
	public String deleteByAccno(Customer customer, Model model) {
		Optional<Customer> cust = custRepo.findById(customer.getAccno());
		String mes = "Hello " + cust.get().getName() + " your a/c " + cust.get().getAccno() + " balance is:"
				+ cust.get().getBalance() + " is Deleted Successfully";
		System.out.println(mes);
		model.addAttribute("cust", mes);
		custRepo.deleteById(customer.getAccno());
		return "views/customerAccountDetails";
	}
	@RequestMapping("admin")
	public String adminUi() {
		return "views/admin";
	}
}

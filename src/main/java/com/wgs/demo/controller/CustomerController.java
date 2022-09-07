package com.wgs.demo.controller;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.wgs.demo.classes.Customer;
import com.wgs.demo.classes.IndividualCustomer;
import com.wgs.demo.impl.MethodImpl;
import com.wgs.demo.repo.AdminRegRepo;
import com.wgs.demo.repo.CustRepo;
import com.wgs.demo.repo.IndividualTrxRepo;

@Controller
public class CustomerController {
	@Autowired
	AdminRegRepo regRepo;
	@Autowired
	CustRepo custRepo;
	@Autowired
	MethodImpl impl;
	@Autowired
	IndividualTrxRepo trxRepo;
	
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
				session.setAttribute("custAccno", cust.getAccno());
			}
			return "redirect:/customer";
		}
		return "redirect:/customerLogin";
	}

	@RequestMapping("customer")
	private String custUi(HttpSession session, Model model) {
		Object userName = session.getAttribute("custName");
		if (session.getAttribute("custName") == null) {
			return "redirect:/customerLogin";
		}
		model.addAttribute("name", userName);
		return "views/customer";
	}

	@RequestMapping("checkAccountStatement")
	private String checkAccountStatement(Customer customer, Model model, HttpSession session) {
		if (session.getAttribute("custName") == null && session.getAttribute("custAccno") == null) {
			return "redirect:/customerLogin";
		}
		int accNo=(int) session.getAttribute("custAccno");
		List<IndividualCustomer> list=trxRepo.findByAccNo(accNo);
		model.addAttribute("cust", list);
		return "views/custAccStmt";
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
		session.removeAttribute("custAccno");
		return "redirect:/customerLogin";
	}

	@RequestMapping("custBanking")
	private String banking(HttpSession session, Model model) {
		if (session.getAttribute("custName") == null && session.getAttribute("custAccno") == null) {
			return "redirect:/customerLogin";
		}
		model.addAttribute("accno", session.getAttribute("custAccno"));
		return "views/custBanking";
	}

	@RequestMapping("custWithdraw")
	private String withdraw(Customer customer, Model model, HttpSession session,IndividualCustomer indivCust) {
		if (session.getAttribute("custName") == null && session.getAttribute("custAccno") == null) {
			return "redirect:/customerLogin";
		}
		if (impl.isAccExists(customer.getAccno()) == true) {
			List<Customer> custList = custRepo.findByAccno(customer.getAccno());
			for (Customer cust : custList) {
				if ((cust.getBalance() - customer.getBalance()) > 1000 && cust.getBalance() > customer.getBalance()) {
					String timeStamp = new SimpleDateFormat("yyyy-MM-dd_hh:mm:ss").format(Calendar.getInstance().getTime());
					int trxId=1+impl.trxIdGen(customer.getAccno());
					indivCust.setTrxId(trxId);
					indivCust.setCustName(cust.getName());
					indivCust.setAccNo(cust.getAccno());
					indivCust.setAmtBefTrx(cust.getBalance());
					indivCust.setTrxAmt(customer.getBalance());
					int newAmount = cust.getBalance() - customer.getBalance();
					indivCust.setCurrentBalance(newAmount);
					indivCust.setTrxDate(timeStamp);
					indivCust.setTrxMode("Debit");
					cust.setBalance(newAmount);
					trxRepo.saveAndFlush(indivCust);
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
		return "views/customerDetails";
	}

	@RequestMapping("custCheckBalance")
	private String checkBalance(Customer customer, Model model, HttpSession session) {
		if (session.getAttribute("custName") == null && session.getAttribute("custAccno") == null) {
			return "redirect:/customerLogin";
		}
		List<Customer> custList = custRepo.findByAccno(customer.getAccno());
		for (Customer cust : custList) {
			String bal = "Hello " + cust.getName() + " your a/c : " + cust.getAccno() + " balance : "
					+ cust.getBalance();
			System.out.println(bal);
			model.addAttribute("cust", bal);
		}

		return "views/customerDetails";
	}

	@RequestMapping("custDeposit")
	private String deposit(Customer customer, Model model, HttpSession session,IndividualCustomer indivCust) {
		if (session.getAttribute("custName") == null && session.getAttribute("custAccno") == null) {
			return "redirect:/customerLogin";
		}
		if (impl.isAccExists(customer.getAccno()) == true) {
			List<Customer> custList = custRepo.findByAccno(customer.getAccno());
			for (Customer cust : custList) {
				if (impl.isAccExists(customer.getAccno()) == true) {
					String timeStamp = new SimpleDateFormat("yyyy-MM-dd_hh:mm:ss").format(Calendar.getInstance().getTime());
					int trxId=impl.trxIdGen(customer.getAccno());
					indivCust.setTrxId(trxId);
					indivCust.setCustName(cust.getName());
					indivCust.setAccNo(cust.getAccno());
					indivCust.setAmtBefTrx(cust.getBalance());
					indivCust.setTrxAmt(customer.getBalance());
					int newAmount = cust.getBalance() + customer.getBalance();
					indivCust.setCurrentBalance(newAmount);
					indivCust.setTrxDate(timeStamp);
					indivCust.setTrxMode("Credit");
					cust.setBalance(newAmount);
					trxRepo.saveAndFlush(indivCust);
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
		return "views/customerDetails";
	}
	
	@RequestMapping("custEdit")
	private String editCustInfo(Model model, HttpSession session) {
		if (session.getAttribute("custName") == null && session.getAttribute("custAccno") == null) {
			return "redirect:/customerLogin";
		}
		int accNo=(int) session.getAttribute("custAccno");
		List<Customer> acList = custRepo.findByAccno(accNo);
		model.addAttribute("cust", acList);
		return "views/editCustDetails";
	}

	@RequestMapping("editCustDetail")
	private String editCustDetail(Customer customer, Model model, HttpSession session) {
		if (session.getAttribute("custName") == null && session.getAttribute("custAccno") == null) {
			return "redirect:/customerLogin";
		}
		System.out.println(customer);
		if (impl.isMobileExists(customer.getMobile()) == false && impl.isAccExists(customer.getAccno()) == true) {
			custRepo.saveAndFlush(customer);
			model.addAttribute("cust", customer);
			custRepo.flush();
			return "views/custEditList";
		} else if (impl.isAccExists(customer.getAccno()) == false) {
			String mes = customer.getAccno() + " already exists! plz Wait...";
			System.out.println(mes);
			model.addAttribute("cust", mes);
		} else if (impl.isMobileExists(customer.getMobile()) == true) {
			String mes = "Try with new Mobile No.. " + customer.getMobile() + " already exists!";
			model.addAttribute("cust", mes);
		}
		return "views/customerDetails";
	}
	
}

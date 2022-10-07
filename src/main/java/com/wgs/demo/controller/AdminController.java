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

import com.wgs.demo.classes.AdminReg;
import com.wgs.demo.classes.AdminRegReq;
import com.wgs.demo.classes.Customer;
import com.wgs.demo.classes.IndividualCustomer;
import com.wgs.demo.impl.AdminImpl;
import com.wgs.demo.impl.CustReqImpl;
import com.wgs.demo.impl.MethodImpl;
import com.wgs.demo.repo.AdminRegRepo;
import com.wgs.demo.repo.AdminRegReqRepo;
import com.wgs.demo.repo.CustRegReqRepo;
import com.wgs.demo.repo.CustRepo;
import com.wgs.demo.repo.IndividualTrxRepo;

@Controller
public class AdminController {
	@Autowired
	AdminRegRepo adminRepo;
	@Autowired
	AdminRegReqRepo adminReqRepo;
	@Autowired
	CustRepo custRepo;
	@Autowired
	MethodImpl impl;
	@Autowired
	AdminImpl adminImpl;
	@Autowired
	IndividualTrxRepo trxRepo;
	@Autowired
	CustReqImpl custReqImpl;
	@Autowired
	CustRegReqRepo custRegReqRepo;
	@Autowired
	AdminRegReqRepo reqRepo;
//	@Autowired
//	AdminReqImpl reqImpl;

	@RequestMapping("adminLogin")
	private String adminLogin() {
		return "Admin/adminLogin";
	}

	@RequestMapping("/logout")
	private String logout(HttpSession session) {
		session.removeAttribute("name");
		return "redirect:/";
	}

	@RequestMapping("admin")
	private String adminUi(Model model, HttpSession session) {
		Object userName = session.getAttribute("name");
		if (userName == null) {
			return "redirect:/adminLogin";
		}
		model.addAttribute("name", userName);
		return "Admin/Admin";
	}

	@RequestMapping("adminAuth")
	private String adminAuth(@RequestParam String userId, @RequestParam String pass, Model model, AdminReg reg,
			HttpSession session) {
		if (adminImpl.adminAuthintication(userId, pass) == true) {
			List<AdminReg> list = adminImpl.findByuId(userId);
			for (AdminReg regList : list) {
				session.setAttribute("name", regList.getName());
			}
			return "redirect:/admin";
		} else {
			return "redirect:/adminLogin";
		}
	}

	@RequestMapping("registerAdmin")
	private String registerAdmin(HttpSession session) {
		Object userName = session.getAttribute("ownName");
		if (userName == null) {
			return "redirect:/ownLogin";
		}
		return "Owner/createAdminAcc";
	}

	@RequestMapping("createAdminAcc")
	private String createAdminAcc(AdminReg admin, Model model,HttpSession session) {
		Object userName = session.getAttribute("ownName");
		if (userName == null) {
			return "redirect:/ownLogin";
		}
		if (adminImpl.isUserIdExists(admin.getUserId()) == false
				&& adminImpl.isMobileExists(admin.getMobile()) == false) {
			AdminReg adList = adminRepo.save(admin);
			String mes = adList + " created Successfully!";
			model.addAttribute("cust", mes);
		} else if (adminImpl.isUserIdExists(admin.getUserId()) == true) {
			String mes = admin.getUserId() + " Already Exists";
			model.addAttribute("cust", mes);
		} else if (adminImpl.isMobileExists(admin.getMobile()) == true) {
			String mes = admin.getMobile() + " Already Exists";
			model.addAttribute("cust", mes);
		}
		return "Owner/ownerDetails";

	}

	@RequestMapping("findAdminDetail")
	private String findAdminDetail(HttpSession session) {
		if (session.getAttribute("name") == null) {
			return "redirect:/adminLogin";
		}
		return "Admin/findAdminDetail";
	}

	@RequestMapping("showAllAdmin")
	private String showAllAdmin(Model model, HttpSession session) {
		if (session.getAttribute("name") == null) {
			return "redirect:/adminLogin";
		}
		if (adminImpl.getTokenId() != 0) {
			List<AdminReg> adList = adminRepo.findAll();
			model.addAttribute("cust", adList);
			return "Admin/adminList";
		} else {
			String msg = "Hii : Empty Response";
			model.addAttribute("cust", msg);
		}
		return "Admin/customerAccountDetails";
	}

	@RequestMapping("deleteAdmin")
	private String deleteAdminInfo(@RequestParam int id, Model model, HttpSession session) {
		if (session.getAttribute("name") == null) {
			return "redirect:/adminLogin";
		}
		adminRepo.deleteById(id);
		adminRepo.flush();
		String mes = id + " is Deleted Successfully";
		System.out.println(mes);
		model.addAttribute("cust", mes);
		return "redirect:/showAllAdmin";
	}

	@RequestMapping("editAdmin")
	private String editAdminInfo(@RequestParam int id, Model model, HttpSession session) {
		if (session.getAttribute("name") == null) {
			return "redirect:/adminLogin";
		}
		List<AdminReg> acList = adminRepo.findById(id);
		model.addAttribute("cust", acList);
		return "Admin/editAdminDetails";
	}

	@RequestMapping("editAdminDetail")
	private String editAdminDetail(AdminReg admin, Model model, HttpSession session) {
		if (session.getAttribute("name") == null) {
			return "redirect:/adminLogin";
		}
		System.out.println(admin);
		if (impl.isMobileExists(admin.getMobile()) == false && adminImpl.isUserIdExists(admin.getUserId()) == true) {
			adminRepo.saveAndFlush(admin);
			model.addAttribute("cust", admin);
			custRepo.flush();
			return "Admin/adminList";
		} else if (adminImpl.isUserIdExists(admin.getUserId()) == false) {
			String mes = admin.getUserId() + " not avail! plz Wait...";
			System.out.println(mes);
			model.addAttribute("cust", mes);
		} else if (adminImpl.isMobileExists(admin.getMobile()) == true) {
			String mes = "Try with new Mobile No.. " + admin.getMobile() + " already exists!";
			model.addAttribute("cust", mes);
		}
		return "Admin/customerAccountDetails";
	}

	@RequestMapping("help")
	private String help() {
		return "Admin/help";
	}

	@RequestMapping("forgetUidAndPass")
	private String forgetUidAndPass() {
		return "Admin/forgetAdminDetails";
	}

	@RequestMapping("resetUid")
	private String forgetUserId(@RequestParam String mobile, String name, Model model) {
		List<AdminReg> list = adminImpl.findMobileAndName(mobile, name);
		if (list.size() != 0) {
			for (AdminReg reg : list) {
				model.addAttribute("cust", reg.getUserId());
			}
		} else {
			String msg = "Hi " + name + " Mobile_no " + mobile + " No Details Found !";
			model.addAttribute("cust", msg);
		}
		return "Admin/customerAccountDetails";
	}

	@RequestMapping("resetPass")
	private String forgetPwd(@RequestParam String mobile, String userId, Model model) {
		List<AdminReg> list = adminImpl.findUidAndMobile(userId, mobile);
		if (list.size() != 0) {
			model.addAttribute("cust", list);
			return "Admin/resetPass";
		} else {
			String msg = "Hi " + userId + " Mobile_no " + mobile + " No Details Found !";
			model.addAttribute("cust", msg);
		}
		return "Admin/customerAccountDetails";
	}

	@RequestMapping("updatePass")
	private String updatePass(Model model, AdminReg admin) {
		adminRepo.saveAndFlush(admin);
		model.addAttribute("cust", admin.getPass());
		return "Admin/customerAccountDetails";
	}

	@RequestMapping("openAccount")
	private String openAccount(HttpSession session) {
		if (session.getAttribute("name") == null) {
			return "redirect:/adminLogin";
		}
		return "Admin/openAccount";
	}

	@RequestMapping("customerAccountDetails")
	private String customerAccountDetails(Customer customer, Model model, HttpSession session) {
		if (session.getAttribute("name") == null) {
			return "redirect:/adminLogin";
		}
		int accno = 1000 + impl.getTokenId();
		try {
			for (int i = 0; i <= impl.getTokenId(); i++) {
				accno++;
				if (customer.getBalance() >= 1000 && impl.isAccExists(accno) == false
						&& impl.isMobileExists(customer.getMobile()) == false
						&& impl.isMailExists(customer.getEmail()) == false) {
					customer.setAccno(accno);
					custRepo.save(customer);
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
				} else if (impl.isMailExists(customer.getEmail()) == true) {
					String mes = "Try with new Mobile No.. " + customer.getEmail() + " already exists!";
					model.addAttribute("cust", mes);
					break;
				}
			}
		} catch (Exception e) {
			System.out.println(e + " err hai err");
		}
		return "Admin/customerAccountDetails";
	}

	@RequestMapping("findCustomerDetails")
	private String findCustomerDetails(HttpSession session) {
		if (session.getAttribute("name") == null) {
			return "redirect:/adminLogin";
		}
		return "Admin/findCustomerDetails";
	}

	@RequestMapping("showAllCustomers")
	private String showAllCustomers(Model model, HttpSession session) {
		if (session.getAttribute("name") == null) {
			return "redirect:/adminLogin";
		}
		if (impl.getTokenId() != 0) {
			List<Customer> custList = custRepo.findAll();
			model.addAttribute("cust", custList);
			return "Admin/customerList";
		} else {
			String msg = "Hii : Empty Response";
			model.addAttribute("cust", msg);
		}
		return "Admin/customerAccountDetails";
	}

	@RequestMapping("findByAccno")
	private String detailsBasedOnAccNO(Model model, Customer customer, HttpSession session) {
		if (impl.isAccExists(customer.getAccno()) == true) {
			if (session.getAttribute("name") == null) {
				return "redirect:/adminLogin";
			}
			List<Customer> custList = custRepo.findByAccno(customer.getAccno());
			model.addAttribute("cust", custList);
			return "Admin/customerEditList";
		} else {
			String msg = "Hii : Invalid A/c no.";
			model.addAttribute("cust", msg);
		}
		return "Admin/customerAccountDetails";
	}

	@RequestMapping("findByName")
	private String findByName(Model model, Customer customer, HttpSession session) {
		if (session.getAttribute("name") == null) {
			return "redirect:/adminLogin";
		}
		if (impl.isPersonExists(customer.getName()) == true) {
			List<Customer> custList = custRepo.findByName(customer.getName());
			model.addAttribute("cust", custList);
			return "Admin/customerEditList";
		} else {
			String msg = "Hii : No Person Exists!.";
			model.addAttribute("cust", msg);
		}
		return "Admin/customerAccountDetails";
	}

	@RequestMapping("findByMobile")
	private String findByMobile(Customer customer, Model model, HttpSession session) {
		if (session.getAttribute("name") == null) {
			return "redirect:/adminLogin";
		}
		if (impl.isMobileExists(customer.getMobile()) == true) {
			List<Customer> custList = custRepo.findByMobile(customer.getMobile());
			model.addAttribute("cust", custList);
			return "Admin/customerEditList";
		} else {
			String msg = "Hii : Invalid Mobile No.";
			model.addAttribute("cust", msg);
		}
		return "Admin/customerAccountDetails";
	}

	@RequestMapping("findByEmail")
	private String findByEmail(Customer customer, Model model, HttpSession session) {
		if (session.getAttribute("name") == null) {
			return "redirect:/adminLogin";
		}
		if (impl.isMailExists(customer.getEmail()) == true) {
			List<Customer> custList = custRepo.findByEmail(customer.getEmail());
			model.addAttribute("cust", custList);
			return "Admin/customerEditList";
		} else {
			String msg = "Hii : Invalid Mail_id.";
			model.addAttribute("cust", msg);
		}
		return "Admin/customerAccountDetails";
	}

	@RequestMapping("banking")
	private String banking(HttpSession session) {
		if (session.getAttribute("name") == null) {
			return "redirect:/adminLogin";
		}
		return "Admin/banking";
	}

	@RequestMapping("deposit")
	private String deposit(Customer customer, Model model, HttpSession session, IndividualCustomer indivCust) {
		if (session.getAttribute("name") == null) {
			return "redirect:/adminLogin";
		}
		if (impl.isAccExists(customer.getAccno()) == true) {
			List<Customer> custList = custRepo.findByAccno(customer.getAccno());
			for (Customer cust : custList) {
				if (impl.isAccExists(customer.getAccno()) == true) {
					String timeStamp = new SimpleDateFormat("yyyy-MM-dd_hh:mm:ss")
							.format(Calendar.getInstance().getTime());
					int trxId = 1 + impl.trxIdGen(customer.getAccno());
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
		return "Admin/customerAccountDetails";
	}

	@RequestMapping("withdraw")
	private String withdraw(Customer customer, Model model, HttpSession session, IndividualCustomer indivCust) {
		if (session.getAttribute("name") == null) {
			return "redirect:/adminLogin";
		}
		if (impl.isAccExists(customer.getAccno()) == true) {
			List<Customer> custList = custRepo.findByAccno(customer.getAccno());
			for (Customer cust : custList) {
				if ((cust.getBalance() - customer.getBalance()) > 1000 && cust.getBalance() > customer.getBalance()) {
					String timeStamp = new SimpleDateFormat("yyyy-MM-dd_hh:mm:ss")
							.format(Calendar.getInstance().getTime());
					int trxId = 1 + impl.trxIdGen(customer.getAccno());
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
		return "Admin/customerAccountDetails";
	}

	@RequestMapping("checkBalance")
	private String checkBalance(Customer customer, Model model, HttpSession session) {
		if (session.getAttribute("name") == null) {
			return "redirect:/adminLogin";
		}
		List<Customer> custList = custRepo.findByAccno(customer.getAccno());
		for (Customer cust : custList) {
			String bal = "Hello " + cust.getName() + " your a/c : " + cust.getAccno() + " balance : "
					+ cust.getBalance();
			System.out.println(bal);
			model.addAttribute("cust", bal);
		}

		return "Admin/customerAccountDetails";
	}

	@RequestMapping("deleteByAccno")
	private String deleteByAccno(Customer customer, Model model, HttpSession session) {
		if (session.getAttribute("name") == null) {
			return "redirect:/adminLogin";
		}
		if (impl.isAccExists(customer.getAccno()) == true) {
			List<Customer> custList = custRepo.findByAccno(customer.getAccno());
			for (Customer cust : custList) {
				String mes = "Hello " + cust.getName() + " your a/c " + cust.getAccno() + " balance is:"
						+ cust.getBalance() + " is Deleted Successfully";
				custRepo.deleteById(customer.getAccno());
				System.out.println(mes);
				model.addAttribute("cust", mes);
			}
			return "Admin/customerAccountDetails";
		} else {
			String msg = "Hii : Invalid A/c no.";
			model.addAttribute("cust", msg);
		}
		return "Admin/customerAccountDetails";
	}

	@RequestMapping("edit")
	private String editCustomerInfo(@RequestParam int accno, Model model, HttpSession session) {
		if (session.getAttribute("name") == null) {
			return "redirect:/adminLogin";
		}
		List<Customer> acList = custRepo.findByAccno(accno);
		model.addAttribute("cust", acList);
		return "Admin/editCustomerDetails";
	}

	@RequestMapping("editCustomerDetail")
	private String editCustomerDetail(Customer customer, Model model, HttpSession session) {
		if (session.getAttribute("name") == null) {
			return "redirect:/adminLogin";
		}
		System.out.println(customer);
		if (impl.isMobileExists(customer.getMobile()) == false && impl.isAccExists(customer.getAccno()) == true) {
			custRepo.saveAndFlush(customer);
			model.addAttribute("cust", customer);
			custRepo.flush();
			return "Admin/customerEditList";
		} else if (impl.isAccExists(customer.getAccno()) == false) {
			String mes = customer.getAccno() + " already exists! plz Wait...";
			System.out.println(mes);
			model.addAttribute("cust", mes);
		} else if (impl.isMobileExists(customer.getMobile()) == true) {
			String mes = "Try with new Mobile No.. " + customer.getMobile() + " already exists!";
			model.addAttribute("cust", mes);
		}
		return "Admin/customerAccountDetails";
	}

	@RequestMapping("delete")
	private String deleteCustomerInfo(@RequestParam int accno, Model model, HttpSession session) {
		if (session.getAttribute("name") == null) {
			return "redirect:/adminLogin";
		}
		String mes = accno + " is Deleted Successfully";
		custRepo.deleteById(accno);
		custRepo.flush();
		System.out.println(mes);
		model.addAttribute("cust", mes);
		return "redirect:/showAllCustomers";
	}

	@RequestMapping("viewPassbook")
	private String checkCustomerStatement(Customer customer, Model model, HttpSession session) {
		if (session.getAttribute("name") == null) {
			return "redirect:/adminLogin";
		}
		List<IndividualCustomer> list = trxRepo.findByAccNo(customer.getAccno());
		model.addAttribute("cust", list);
		return "Admin/customerPassbook";
	}
	@RequestMapping("reqAdminAcc")
	private String requestAdminAcc() {
		return "Admin/createAdminAcc";
	}
	@RequestMapping("createAdminAccReq")
	private String reqAdminAcc(AdminRegReq adminReq, Model model) {
		if (adminImpl.isUserIdExists(adminReq.getUserId()) == false
				&& adminImpl.isMobileExists(adminReq.getMobile()) == false) {
			AdminRegReq adReq = adminReqRepo.save(adminReq);
			String mes = adReq +"Request Submitted Successfully..";
			model.addAttribute("cust", mes);
		} else if (adminImpl.isUserIdExists(adminReq.getUserId()) == true) {
			String mes = adminReq.getUserId() + " Already Exists";
			model.addAttribute("cust", mes);
		} else if (adminImpl.isMobileExists(adminReq.getMobile()) == true) {
			String mes = adminReq.getMobile() + " Already Exists";
			model.addAttribute("cust", mes);
		}
		return "Admin/customerAccountDetails";
	}

	@RequestMapping("chkCustReqByAdmin")
	private String checkCustReqByAdmin(Model model, HttpSession session) {
		if (session.getAttribute("name") == null) {
			return "redirect:/adminLogin";
		}
		model.addAttribute("cust", custReqImpl.findAllReq());
		return "Admin/showCustReq";
	}

	@RequestMapping("acceptCustReqByAdmin")
	private String acceptCustomerRequestByAdmin(Customer customer, Model model, HttpSession session) {
		if (session.getAttribute("name") == null) {
			return "redirect:/adminLogin";
		}
		try {
			if (customer.getBalance() >= 1000 && impl.isAccExists(customer.getAccno()) == false
					&& impl.isMobileExists(customer.getMobile()) == false
					&& impl.isMailExists(customer.getEmail()) == false) {
				custRepo.save(customer);
				custRegReqRepo.deleteById(customer.getAccno());
				reqRepo.flush();
				model.addAttribute("cust", "Account Created Successfully.." + customer);
			} else if (impl.isAccExists(customer.getAccno()) == true) {
				String mes = customer.getAccno() + " already exists! plz Wait...";
				System.out.println(mes);
				model.addAttribute("cust", mes);
			} else if (impl.isMobileExists(customer.getMobile()) == true) {
				String mes = "Try with new Mobile No.. " + customer.getMobile() + " already exists!";
				model.addAttribute("cust", mes);
			} else if (impl.isMailExists(customer.getEmail()) == true) {
				String mes = "Try with new Mobile No.. " + customer.getEmail() + " already exists!";
				model.addAttribute("cust", mes);
			}
		} catch (Exception e) {
			System.out.println(e + " err hai err");
		}
		return "Admin/customerAccountDetails";
	}

	@RequestMapping("delCustReqByAdmin")
	private String deleteCustomerRequestByAdmin(HttpSession session,Customer customer, Model model) {
		if (session.getAttribute("name") == null) {
			return "redirect:/adminLogin";
		}
		custRegReqRepo.deleteById(customer.getAccno());
		reqRepo.flush();
		String msg = " Customer A/c Request Denied";
		model.addAttribute("cust", msg);
		return "redirect:/chkCustReq";
	}

}

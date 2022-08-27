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
import com.wgs.demo.impl.AdminImpl;
import com.wgs.demo.impl.MethodImpl;
import com.wgs.demo.repo.AdminRegRepo;
import com.wgs.demo.repo.CustRepo;

@Controller
public class AdminController {
	@Autowired
	AdminRegRepo adminRepo;
	@Autowired
	CustRepo custRepo;
	@Autowired
	MethodImpl impl;
	@Autowired
	AdminImpl adminImpl;

	@RequestMapping("admin")
	private String adminUi(Model model, HttpSession session) {
		Object userName = session.getAttribute("name");
		if (userName == null) {
			return "redirect:/adminLogin";
		}

		model.addAttribute("name", userName);
		return "views/Admin";
	}

	@RequestMapping("adminLogin")
	private String adminLogin() {
		return "views/adminLogin";
	}

	@RequestMapping("/logout")
	private String logout(HttpSession session) {
		session.invalidate();
		return "redirect:/adminLogin";
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
	private String registerAdmin() {
		return "views/createAdminAcc";
	}

	@RequestMapping("createAdminAcc")
	private String createAdminAcc(AdminReg admin, Model model) {
		if (adminImpl.isUserIdExists(admin.getUserId()) == false
				&& adminImpl.isMobileExists(admin.getMobile()) == false) {
			AdminReg adList = adminRepo.save(admin);
			String mes=adList+" created Successfully!";
			model.addAttribute("cust", mes);
		} else if (adminImpl.isUserIdExists(admin.getUserId()) == true) {
			String mes = admin.getUserId() + " Already Exists";
			model.addAttribute("cust", mes);
		} else if (adminImpl.isMobileExists(admin.getMobile()) == true) {
			String mes = admin.getMobile() + " Already Exists";
			model.addAttribute("cust", mes);
		}
		return "views/customerAccountDetails";

	}

	@RequestMapping("findAdminDetail")
	private String findAdminDetail(HttpSession session) {
		if (session.getAttribute("name") == null) {
			return "redirect:/adminLogin";
		}
		return "views/findAdminDetail";
	}

	@RequestMapping("showAllAdmin")
	private String showAllAdmin(Model model, HttpSession session) {
		if (session.getAttribute("name") == null) {
			return "redirect:/adminLogin";
		}
		if (adminImpl.getTokenId() != 0) {
			List<AdminReg> adList = adminRepo.findAll();
			model.addAttribute("cust", adList);
			return "views/adminList";
		} else {
			String msg = "Hii : Empty Response";
			model.addAttribute("cust", msg);
		}
		return "views/customerAccountDetails";
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
		return "views/editAdminDetails";
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
			return "views/adminList";
		} else if (adminImpl.isUserIdExists(admin.getUserId()) == false) {
			String mes = admin.getUserId() + " not avail! plz Wait...";
			System.out.println(mes);
			model.addAttribute("cust", mes);
		} else if (adminImpl.isMobileExists(admin.getMobile()) == true) {
			String mes = "Try with new Mobile No.. " + admin.getMobile() + " already exists!";
			model.addAttribute("cust", mes);
		}
		return "views/customerAccountDetails";
	}

	@RequestMapping("help")
	private String help() {
		return "views/help";
	}

	@RequestMapping("forgetUidAndPass")
	private String forgetUidAndPass() {
		return "views/forgetAdminDetails";
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
		return "views/customerAccountDetails";
	}

	@RequestMapping("resetPass")
	private String forgetPwd(@RequestParam String mobile, String userId, Model model, AdminReg admin) {
		List<AdminReg> list = adminImpl.findUidAndMobile(userId, mobile);
		if (list.size() != 0) {
			model.addAttribute("cust", list);
			return "views/resetPass";
		} else {
			String msg = "Hi " + userId + " Mobile_no " + mobile + " No Details Found !";
			model.addAttribute("cust", msg);
		}
		return "views/customerAccountDetails";
	}

	@RequestMapping("updatePass")
	private String updatePass(Model model, AdminReg admin) {
		adminRepo.saveAndFlush(admin);
		model.addAttribute("cust", admin.getPass());
		return "views/customerAccountDetails";
	}

	@RequestMapping("openAccount")
	private String openAccount(HttpSession session) {
		if (session.getAttribute("name") == null) {
			return "redirect:/adminLogin";
		}
		return "views/openAccount";
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
		return "views/customerAccountDetails";
	}

	@RequestMapping("findCustomerDetails")
	private String findCustomerDetails(HttpSession session) {
		if (session.getAttribute("name") == null) {
			return "redirect:/adminLogin";
		}
		return "views/findCustomerDetails";
	}

	@RequestMapping("showAllCustomers")
	private String showAllCustomers(Model model, HttpSession session) {
		if (session.getAttribute("name") == null) {
			return "redirect:/adminLogin";
		}
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
	private String detailsBasedOnAccNO(Model model, Customer customer, HttpSession session) {
		if (impl.isAccExists(customer.getAccno()) == true) {
			if (session.getAttribute("name") == null) {
				return "redirect:/adminLogin";
			}
			List<Customer> custList = custRepo.findByAccno(customer.getAccno());
			model.addAttribute("cust", custList);
			return "views/customerEditList";
		} else {
			String msg = "Hii : Invalid A/c no.";
			model.addAttribute("cust", msg);
		}
		return "views/customerAccountDetails";
	}

	@RequestMapping("findByName")
	private String findByName(Model model, Customer customer, HttpSession session) {
		if (session.getAttribute("name") == null) {
			return "redirect:/adminLogin";
		}
		if (impl.isPersonExists(customer.getName()) == true) {
			List<Customer> custList = custRepo.findByName(customer.getName());
			model.addAttribute("cust", custList);
			return "views/customerEditList";
		} else {
			String msg = "Hii : No Person Exists!.";
			model.addAttribute("cust", msg);
		}
		return "views/customerAccountDetails";
	}

	@RequestMapping("findByMobile")
	private String findByMobile(Customer customer, Model model, HttpSession session) {
		if (session.getAttribute("name") == null) {
			return "redirect:/adminLogin";
		}
		if (impl.isMobileExists(customer.getMobile()) == true) {
			List<Customer> custList = custRepo.findByMobile(customer.getMobile());
			model.addAttribute("cust", custList);
			return "views/customerEditList";
		} else {
			String msg = "Hii : Invalid Mobile No.";
			model.addAttribute("cust", msg);
		}
		return "views/customerAccountDetails";
	}

	@RequestMapping("findByEmail")
	private String findByEmail(Customer customer, Model model, HttpSession session) {
		if (session.getAttribute("name") == null) {
			return "redirect:/adminLogin";
		}
		if (impl.isMailExists(customer.getEmail()) == true) {
			List<Customer> custList = custRepo.findByEmail(customer.getEmail());
			model.addAttribute("cust", custList);
			return "views/customerEditList";
		} else {
			String msg = "Hii : Invalid Mail_id.";
			model.addAttribute("cust", msg);
		}
		return "views/customerAccountDetails";
	}

	@RequestMapping("banking")
	private String banking(HttpSession session) {
		if (session.getAttribute("name") == null) {
			return "redirect:/adminLogin";
		}
		return "views/banking";
	}

	@RequestMapping("deposit")
	private String deposit(Customer customer, Model model, HttpSession session) {
		if (session.getAttribute("name") == null) {
			return "redirect:/adminLogin";
		}
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
	private String withdraw(Customer customer, Model model, HttpSession session) {
		if (session.getAttribute("name") == null) {
			return "redirect:/adminLogin";
		}
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

		return "views/customerAccountDetails";
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
			return "views/customerAccountDetails";
		} else {
			String msg = "Hii : Invalid A/c no.";
			model.addAttribute("cust", msg);
		}
		return "views/customerAccountDetails";
	}

	@RequestMapping("edit")
	private String editCustomerInfo(@RequestParam int accno, Model model, HttpSession session) {
		if (session.getAttribute("name") == null) {
			return "redirect:/adminLogin";
		}
		List<Customer> acList = custRepo.findByAccno(accno);
		model.addAttribute("cust", acList);
		return "views/editCustomerDetails";
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
			return "views/customerEditList";
		} else if (impl.isAccExists(customer.getAccno()) == false) {
			String mes = customer.getAccno() + " already exists! plz Wait...";
			System.out.println(mes);
			model.addAttribute("cust", mes);
		} else if (impl.isMobileExists(customer.getMobile()) == true) {
			String mes = "Try with new Mobile No.. " + customer.getMobile() + " already exists!";
			model.addAttribute("cust", mes);
		}
		return "views/customerAccountDetails";
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
}

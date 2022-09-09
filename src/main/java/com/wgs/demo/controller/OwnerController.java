package com.wgs.demo.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.wgs.demo.classes.AdminReg;
import com.wgs.demo.classes.Owner;
import com.wgs.demo.impl.OwnerImpl;
import com.wgs.demo.repo.OwnerRepo;

@Controller
public class OwnerController {
	@Autowired
	OwnerRepo ownerRepo;
	@Autowired
	OwnerImpl ownerImpl;

	@RequestMapping("ownLogin")
	private String ownerLogin() {
		return "Owner/login";
	}

	@RequestMapping("regNewOwn")
	private String createOwnAcc() {
		return "Owner/openAccount";
	}

	@RequestMapping("regOwnAccount")
	private String registerOwner(Owner owner, Model model) {
		if (ownerImpl.isUserIdExists(owner.getUserId()) == false
				&& ownerImpl.isMobileExists(owner.getMobile()) == false) {
			Owner own = ownerRepo.save(owner);
			String mes = own + " created Successfully!";
			model.addAttribute("cust", mes);
		} else if (ownerImpl.isUserIdExists(owner.getUserId()) == true) {
			String mes = owner.getUserId() + " Already Exists";
			model.addAttribute("cust", mes);
		} else if (ownerImpl.isMobileExists(owner.getMobile()) == true) {
			String mes = owner.getMobile() + " Already Exists";
			model.addAttribute("cust", mes);
		}
		return "Owner/ownerDetails";
	}

	@RequestMapping("ownAuth")
	private String ownerAuthentication(@RequestParam String userId, @RequestParam String pass, Model model, Owner owner,
			HttpSession session) {
		if (ownerImpl.ownerAuthintication(userId, pass) == true) {
			List<Owner> list = ownerImpl.findByuId(userId);
			for (Owner own : list) {
				session.setAttribute("ownName", own.getName());
				session.setAttribute("ownId", own.getId());
			}
			return "redirect:/own";
		} else {
			return "redirect:/ownLogin";
		}
	}

	@RequestMapping("own")
	private String ownerUi(Model model, HttpSession session) {
		Object userName = session.getAttribute("ownName");
		if (userName == null) {
			return "redirect:/ownLogin";
		}
		model.addAttribute("name", userName);
		return "Owner/owner";
	}

	@RequestMapping("/ownLogout")
	private String ownerLogout(HttpSession session) {
		session.removeAttribute("ownName");
		return "redirect:/";
	}

	@RequestMapping("viewOwnProfile")
	private String viewOwnInfo(Model model, HttpSession session) {
		if (session.getAttribute("ownName") == null || session.getAttribute("ownId") == null) {
			return "redirect:/ownLogin";
		}
		int id = (int) session.getAttribute("ownId");
		model.addAttribute("cust", ownerImpl.getDetaislById(id));
		return "Owner/editProfileDetails";
	}

	@RequestMapping("editOwn")
	private String editOwner(@RequestParam int id,Model model,HttpSession session) {
		if (session.getAttribute("ownName") == null) {
			return "redirect:/ownLogin";
		}
		model.addAttribute("cust", ownerImpl.getDetaislById(id));
		return "Owner/editOwnDetails";
	}

	@RequestMapping("updateOwnProfile")
	private String editAdminDetail(Owner owner, Model model, HttpSession session) {
		if (session.getAttribute("ownName") == null) {
			return "redirect:/ownLogin";
		}
		if (ownerImpl.isMobileExists(owner.getMobile()) == false
				&& ownerImpl.isUserIdExists(owner.getUserId()) == false) {
			ownerRepo.saveAndFlush(owner);
			model.addAttribute("cust", owner);
			ownerRepo.flush();
			return "Owner/ownerDetails";
		} else if (ownerImpl.isUserIdExists(owner.getUserId()) == true) {
			String mes = owner.getUserId() + " not avail! plz Wait...";
			System.out.println(mes);
			model.addAttribute("cust", mes);
		} else if (ownerImpl.isMobileExists(owner.getMobile()) == true) {
			String mes = "Try with new Mobile No.. " + owner.getMobile() + " already exists!";
			model.addAttribute("cust", mes);
		}
		return "Owner/ownerDetails";
	}

}

package com.wgs.demo.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wgs.demo.classes.AdminReg;
public interface AdminRegRepo extends JpaRepository<AdminReg, Integer>{
	
	List<AdminReg> findByUserIdAndPass(String userId,String pass);
	List<AdminReg> findByMobileAndName(String mobile,String name);
	List<AdminReg> findByUserIdAndMobile(String UserId,String mobile);
	List<AdminReg> findByUserId(String userId);
	List<AdminReg> findById(int id);
	String findByPass(String pass);
	boolean existsByUserId(String userId);

	boolean existsByMobile(String mobile);
<<<<<<< HEAD
}
=======
}
>>>>>>> 18ec1090d7c2d1c474902dffd040ad203a1c3000

package com.wgs.demo.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wgs.demo.classes.AdminReg;
public interface AdminRegRepo extends JpaRepository<AdminReg, Integer>{
	
	List<AdminReg> findByUserIdAndPass(String userId,String pass);
	List<AdminReg> findByUserId(String userId);
	List<AdminReg> findById(int id);
	String findByPass(String pass);
	boolean existsByUserId(String userId);

	boolean existsByMobile(String mobile);
}

package com.wgs.demo.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wgs.demo.classes.AdminRegReq;
public interface AdminRegReqRepo extends JpaRepository<AdminRegReq, Integer>{
	
	List<AdminRegReq> findByUserIdAndPass(String userId,String pass);
	List<AdminRegReq> findByMobileAndName(String mobile,String name);
	List<AdminRegReq> findByUserIdAndMobile(String UserId,String mobile);
	List<AdminRegReq> findByUserId(String userId);
	List<AdminRegReq> findById(int id);
	String findByPass(String pass);
	boolean existsByUserId(String userId);

	boolean existsByMobile(String mobile);
	void deleteByMobile(String mobile);

}

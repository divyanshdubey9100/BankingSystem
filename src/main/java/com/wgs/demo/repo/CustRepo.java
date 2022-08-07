package com.wgs.demo.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wgs.demo.cust.Customer;

public interface CustRepo extends JpaRepository<Customer, Integer>{
	
	List<Customer> findByAccno(int accno);
	List<Customer> findByName(String name);
	List<Customer> findByMobile(String mobile);
}

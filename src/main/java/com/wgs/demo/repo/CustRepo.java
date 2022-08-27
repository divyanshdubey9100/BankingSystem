package com.wgs.demo.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wgs.demo.classes.Customer;

public interface CustRepo extends JpaRepository<Customer, Integer> {

	List<Customer> findByAccno(int accno);

	List<Customer> findByName(String name);

	List<Customer> findByMobile(String mobile);
	
	List<Customer> findByEmail(String email);

	boolean existsByMobile(String mobile);

	boolean existsByName(String name);
	
	boolean existsByEmail(String email);
<<<<<<< HEAD
}
=======
}
>>>>>>> 18ec1090d7c2d1c474902dffd040ad203a1c3000

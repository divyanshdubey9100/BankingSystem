package com.wgs.demo.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wgs.demo.classes.IndividualCustomer;

public interface IndividualTrxRepo extends JpaRepository<IndividualCustomer, Integer>{

	List<IndividualCustomer> findByAccNo(int accNo);

}

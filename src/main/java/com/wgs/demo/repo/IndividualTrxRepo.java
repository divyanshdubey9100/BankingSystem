package com.wgs.demo.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wgs.demo.classes.IndividualCustomer;

public interface IndividualTrxRepo extends JpaRepository<IndividualCustomer, Integer>{

}

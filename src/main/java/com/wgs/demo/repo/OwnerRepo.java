package com.wgs.demo.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wgs.demo.classes.Owner;

public interface OwnerRepo extends JpaRepository<Owner, String> {

	boolean existsByUserId(String userId);

	boolean existsByMobile(String mobile);

	List<Owner> findByUserId(String userId);

	List<Owner> findByUserIdAndPass(String uid, String pass);

}

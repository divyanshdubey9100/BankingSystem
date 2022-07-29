package com.wgs.demo.cust;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.springframework.stereotype.Component;

@Entity
@Table(
        name="Customer", 
        uniqueConstraints=
            @UniqueConstraint(columnNames={"accno","mobile"}))

@Component
public class Customer {
	@Id
	private int accno;
	private String name;
	private String mobile;
	private int balance;
	private String gender;
	private String address;
	public Customer() {
		super();
		// TODO Auto-generated constructor stub
	}

	
	
	public Customer(int accno, String name, String mobile,int balance, String gender, String address) {
		super();
		this.accno = accno;
		this.name = name;
		this.mobile = mobile;
		this.gender = gender;
		this.address = address;
		this.balance=balance;
	}



	public int getBalance() {
		return balance;
	}



	public void setBalance(int balance) {
		this.balance = balance;
	}



	public int getAccno() {
		return accno;
	}

	public void setAccno(int accno) {
		this.accno = accno;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}



	@Override
	public String toString() {
		return "Customer [accno=" + accno + ", name=" + name + ", mobile=" + mobile + ", balance=" + balance
				+ ", gender=" + gender + ", address=" + address + "]";
	}



	

	

	
	
	
}

package com.wgs.demo.classes;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.springframework.stereotype.Component;

@Table(name = "owner", uniqueConstraints = @UniqueConstraint(columnNames = { "userId", "mobile" }))
@Entity
@Component
public class Owner {
	@Id
	@GeneratedValue
	private int id;
	private String userId;
	private String name;
	private String mobile;
	private String gender;
	private String pass;
	public Owner() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Owner(int id, String userId, String name, String mobile, String gender, String pass) {
		super();
		this.id = id;
		this.userId = userId;
		this.name = name;
		this.mobile = mobile;
		this.gender = gender;
		this.pass = pass;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
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
	public String getPass() {
		return pass;
	}
	public void setPass(String pass) {
		this.pass = pass;
	}
	@Override
	public String toString() {
		return "Owner [id=" + id + ", userId=" + userId + ", name=" + name + ", mobile=" + mobile + ", gender=" + gender
				+ ", pass=" + pass + "]";
	}
	
	
}

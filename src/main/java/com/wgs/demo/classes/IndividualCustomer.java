package com.wgs.demo.classes;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Entity
@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
public class IndividualCustomer {
	@Id
	@GeneratedValue
	private int trxId;
	private int accNo;
	private String custName;
	private String trxMode;
	private String trxDate;
	private int amtBefTrx;
	private int trxAmt;
	private int currentBalance;
	
	
}

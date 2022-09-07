package com.wgs.demo.classes;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Table(name = "IndividualCustomer", uniqueConstraints = @UniqueConstraint(columnNames = { "trxId" }))
@Entity
@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
public class IndividualCustomer {
	@Id
	private int trxId;
	private int accNo;
	private String custName;
	private String trxMode;
	private String trxDate;
	private int amtBefTrx;
	private int trxAmt;
	private int currentBalance;
	
	
}

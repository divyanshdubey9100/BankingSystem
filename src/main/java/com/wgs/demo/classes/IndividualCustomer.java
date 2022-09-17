package com.wgs.demo.classes;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.springframework.stereotype.Component;
@Table(name = "IndividualCustomer", uniqueConstraints = @UniqueConstraint(columnNames = { "trxId" }))
@Entity
@Component
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
	public IndividualCustomer() {
		super();
		// TODO Auto-generated constructor stub
	}
	public IndividualCustomer(int trxId, int accNo, String custName, String trxMode, String trxDate, int amtBefTrx,
			int trxAmt, int currentBalance) {
		super();
		this.trxId = trxId;
		this.accNo = accNo;
		this.custName = custName;
		this.trxMode = trxMode;
		this.trxDate = trxDate;
		this.amtBefTrx = amtBefTrx;
		this.trxAmt = trxAmt;
		this.currentBalance = currentBalance;
	}
	public int getTrxId() {
		return trxId;
	}
	public void setTrxId(int trxId) {
		this.trxId = trxId;
	}
	public int getAccNo() {
		return accNo;
	}
	public void setAccNo(int accNo) {
		this.accNo = accNo;
	}
	public String getCustName() {
		return custName;
	}
	public void setCustName(String custName) {
		this.custName = custName;
	}
	public String getTrxMode() {
		return trxMode;
	}
	public void setTrxMode(String trxMode) {
		this.trxMode = trxMode;
	}
	public String getTrxDate() {
		return trxDate;
	}
	public void setTrxDate(String trxDate) {
		this.trxDate = trxDate;
	}
	public int getAmtBefTrx() {
		return amtBefTrx;
	}
	public void setAmtBefTrx(int amtBefTrx) {
		this.amtBefTrx = amtBefTrx;
	}
	public int getTrxAmt() {
		return trxAmt;
	}
	public void setTrxAmt(int trxAmt) {
		this.trxAmt = trxAmt;
	}
	public int getCurrentBalance() {
		return currentBalance;
	}
	public void setCurrentBalance(int currentBalance) {
		this.currentBalance = currentBalance;
	}
	@Override
	public String toString() {
		return "IndividualCustomer [trxId=" + trxId + ", accNo=" + accNo + ", custName=" + custName + ", trxMode="
				+ trxMode + ", trxDate=" + trxDate + ", amtBefTrx=" + amtBefTrx + ", trxAmt=" + trxAmt
				+ ", currentBalance=" + currentBalance + "]";
	}
	
	
	
	
}

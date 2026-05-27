package com.b1Banking.ZenBanking.Entity;


import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="AccountData")
public class BankingEntity {
	@Column
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
private long accountNo;
	@Column
private String accountHolder;
	@Column
private double balance;
	@Column
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
private Date age;
	@Column
	
	private String panNo;

	@Column
	
	private int pinNo;
	private String role;
	private String password;
	private String status;
	private String mail;
	private String mobile;

	private Integer cibilScore;

	@Column
	private String accountType;

public String getAccountType() {
		return accountType;
	}


	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}


public Integer getCibilScore() {
		return cibilScore;
	}


	public void setCibilScore(Integer cibilScore) {
		this.cibilScore = cibilScore;
	}




public BankingEntity(long accountNo, String accountHolder, double balance, Date age, String panNo, int pinNo,
			String role, String password, String status, String mail, String mobile, Integer cibilScore) {
		super();
		this.accountNo = accountNo;
		this.accountHolder = accountHolder;
		this.balance = balance;
		this.age = age;
		this.panNo = panNo;
		this.pinNo = pinNo;
		this.role = role;
		this.password = password;
		this.status = status;
		this.mail = mail;
		this.mobile = mobile;
		this.cibilScore = cibilScore;
	}


public String getMobile() {
	return mobile;
}





public void setMobile(String mobile) {
	this.mobile = mobile;
}


public String getPassword() {
	return password;
}


public void setPassword(String password) {
	this.password = password;
}


public String getStatus() {
	return status;
}


public void setStatus(String status) {
	this.status = status;
}


public BankingEntity() {
	super();
	// TODO Auto-generated constructor stub
}
	
public long getAccountNo() {
	return accountNo;
}
public void setAccountNo(long accountNo) {
	this.accountNo = accountNo;
}
public String getAccountHolder() {
	return accountHolder;
}
public void setAccountHolder(String accountHolder) {
	this.accountHolder = accountHolder;
}
public double getBalance() {
	return balance;
}
public void setBalance(double balance) {
	this.balance = balance;
}
public Date getAge() {
	return age;
}
public void setAge(Date age) {
	this.age = age;
}
public int getPinNo() {
	return pinNo;
}
public void setPinNo(int pinNo) {
	this.pinNo = pinNo;
}
public String getRole() {
	return role;
}
public void setRole(String role) {
	this.role = role;
}
public String getMail() {
	return mail;
}
public void setMail(String mail) {
	this.mail = mail;
}





public String getPanNo() {
	return panNo;
}





public void setPanNo(String panNo) {
	this.panNo = panNo;
}


@Override
public String toString() {
	return "BankingEntity [accountNo=" + accountNo + ", accountHolder=" + accountHolder + ", balance=" + balance
			+ ", age=" + age + ", panNo=" + panNo + ", pinNo=" + pinNo + ", role=" + role + ", password=" + password
			+ ", status=" + status + ", mail=" + mail + ", mobile=" + mobile + ", cibilScore=" + cibilScore + "]";
}
















}

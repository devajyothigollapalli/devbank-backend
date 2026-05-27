package com.b1Banking.ZenBanking.DTO;

public class TransferRequest {
	
	private int pinNo;
	
	public int getPinNo() {
		return pinNo;
	}
	public void setPinNo(int pinNo) {
		this.pinNo = pinNo;
	}
	private double amount;
//    private int otp;

    public double getAmount() { 
    	return amount;
    	}
    public void setAmount(double amount) {
    	this.amount = amount;
    	}
//
//    public int getOtp() {
//    	return otp; 
//    	}
//    public void setOtp(int otp) { 
//    	this.otp = otp;
//    	}
	}



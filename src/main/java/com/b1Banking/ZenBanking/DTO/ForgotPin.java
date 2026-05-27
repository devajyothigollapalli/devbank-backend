package com.b1Banking.ZenBanking.DTO;

public class ForgotPin {

    private long accountNo;
    private String mail;
    private int otp;
    private int newPin;
	public long getAccountNo() {
		return accountNo;
	}
	public void setAccountNo(long accountNo) {
		this.accountNo = accountNo;
	}
	public String getMail() {
		return mail;
	}
	public void setMail(String mail) {
		this.mail = mail;
	}
	public int getOtp() {
		return otp;
	}
	public void setOtp(int otp) {
		this.otp = otp;
	}
	public int getNewPin() {
		return newPin;
	}
	public void setNewPin(int newPin) {
		this.newPin = newPin;
	}

   
}


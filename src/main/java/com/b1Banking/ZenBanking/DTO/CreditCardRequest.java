package com.b1Banking.ZenBanking.DTO;

import java.util.Date;

public class CreditCardRequest {
	
	    private long accountNo;
	    private Date dob;
	    private String cardType;
		public long getAccountNo() {
			return accountNo;
		}
		public void setAccountNo(long accountNo) {
			this.accountNo = accountNo;
		}
		public Date getDob() {
			return dob;
		}
		public void setDob(Date dob) {
			this.dob = dob;
		}
		public String getCardType() {
			return cardType;
		}
		public void setCardType(String cardType) {
			this.cardType = cardType;
		}
	
}

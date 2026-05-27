package com.b1Banking.ZenBanking.Entity;




import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;



@Entity
public class PdfEntity {
	
	

	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long txnId;
        private String accountHolder;
	    public String getAccountHolder() {
			return accountHolder;
		}

		public void setAccountHolder(String accountHolder) {
			this.accountHolder = accountHolder;
		}

		private Long accountNo;
	    private String txnType; 
	    private double amount;
	    private double balanceAfterTxn;
	  private String txnpanNo;
	    private Date txnDate;
	    private String remarks;
	   private String txnMobile;
	   private String txnAccountHolder;
	    public String getTxnpanNo() {
		return txnpanNo;
	}

	   public void setTxnpanNo(String txnpanNo) {
		   this.txnpanNo = txnpanNo;
	   }

	   public String getTxnMobile() {
		   return txnMobile;
	   }

	   public void setTxnMobile(String txnMobile) {
		   this.txnMobile = txnMobile;
	   }

	   public String getTxnAccountHolder() {
		   return txnAccountHolder;
	   }

	   public void setTxnAccountHolder(String txnAccountHolder) {
		   this.txnAccountHolder = txnAccountHolder;
	   }

		@PrePersist
	    public void onCreate() {
	        this.txnDate = new Date();   // 💥 IMPORTANT
	    }
	    
		public Long getTxnId() {
			return txnId;
		}
		public void setTxnId(Long txnId) {
			this.txnId = txnId;
		}
		public Long getAccountNo() {
			return accountNo;
		}
		public void setAccountNo(Long accountNo) {
			this.accountNo = accountNo;
		}
		public String getTxnType() {
			return txnType;
		}
		public void setTxnType(String txnType) {
			this.txnType = txnType;
		}
		public double getAmount() {
			return amount;
		}
		public void setAmount(double amount) {
			this.amount = amount;
		}
		public double getBalanceAfterTxn() {
			return balanceAfterTxn;
		}
		public void setBalanceAfterTxn(double balanceAfterTxn) {
			this.balanceAfterTxn = balanceAfterTxn;
		}
		public Date getTxnDate() {
			return txnDate;
		}
		public void setTxnDate(Date txnDate) {
			this.txnDate = txnDate;
		}
		public String getRemarks() {
			return remarks;
		}
		public void setRemarks(String remarks) {
			this.remarks = remarks;
		}

		

	    
	

}

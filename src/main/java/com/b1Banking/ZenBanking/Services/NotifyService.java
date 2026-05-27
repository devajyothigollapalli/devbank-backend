package com.b1Banking.ZenBanking.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.b1Banking.ZenBanking.Entity.BankingEntity;

@Service
public class NotifyService {
	@Autowired
	MiniPdfService mini;
	@Autowired
	  private MailService emailService;
	public void sendMiniStatement(BankingEntity user, String msg) throws Exception {

	    byte[] pdf =mini.generateMiniStatement(user.getAccountNo());

	    emailService.sendMailWithAttachment(
	        user.getMail(),
	        "DevBank Mini Statement",
	        "Dear " + user.getAccountHolder()
	        + ",\n" + msg + "\n\n-DevBank",
	        pdf
	    );
	}

}

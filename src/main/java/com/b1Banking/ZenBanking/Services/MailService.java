package com.b1Banking.ZenBanking.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;


@Service
public class MailService {
	
	

	    @Autowired
	    private JavaMailSender emailService;

	    public void sendMail(String to, String subject, String body) {
	        SimpleMailMessage msg = new SimpleMailMessage();
	        msg.setTo(to);
	        msg.setSubject(subject);
	        msg.setText(body);
	        emailService.send(msg);
	    }
	    public void sendMailWithAttachment(
	            String to,
	            String subject,
	            String body,
	            byte[] pdf) throws Exception {

	        MimeMessage message = emailService.createMimeMessage();
	        MimeMessageHelper helper = new MimeMessageHelper(message, true);

	        helper.setTo(to);
	        helper.setSubject(subject);
	        helper.setText(body);
	        helper.addAttachment("MiniStatement.pdf",
	                new ByteArrayResource(pdf));

	        emailService.send(message);
	    }

	}



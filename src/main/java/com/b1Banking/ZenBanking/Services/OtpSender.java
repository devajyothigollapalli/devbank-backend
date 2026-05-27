package com.b1Banking.ZenBanking.Services;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public class OtpSender {
	
	    private Map<String, Integer> otpStore;

	    public OtpSender() {
	        otpStore=new HashMap<>();
	    }
        
	    public int generateOTP(String mail) {
	        int otp=(int)(Math.random()*900000)+100000;
	        otpStore.put(mail, otp);
	        System.out.println("Generated OTP for "+mail +": "+ otp);
	        return otp;
	    }

	    public boolean validateOTP(String mail,int otp){
	        return otpStore.containsKey(mail) && otpStore.get(mail)==otp;
	    }
	}


	



	



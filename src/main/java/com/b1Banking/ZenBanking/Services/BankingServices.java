package com.b1Banking.ZenBanking.Services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.b1Banking.ZenBanking.DTO.LoginRequest;
import com.b1Banking.ZenBanking.Entity.BankingEntity;
import com.b1Banking.ZenBanking.Repo.BankingReposit;

@Service
@Transactional
public class BankingServices implements BankingServiceInt {
@Autowired
private BankingReposit br;
@Autowired
private MailService emailService;
@Autowired
private PasswordEncoder encoder;

public BankingEntity login(LoginRequest req) {

    BankingEntity user = br.findByMail(req.getEmail())
            .orElseThrow(() -> new RuntimeException("User not found"));

    // 👇 IMPORTANT LINE
    if (!encoder.matches(req.getPassword(), user.getPassword())) {
        throw new RuntimeException("Invalid Credentials");
    }

    return user;
}

@Override
public BankingEntity openNewAccount(BankingEntity be) {
	return br.save(be);
}
@Override
public List<BankingEntity>getAllData(){
	return br.findAll();
}
@Override
public  Optional<BankingEntity> getById(long id) {
	
	return br.findById(id);
}
@Override
public BankingEntity depositedcash(BankingEntity ex_amount) {
	
	return br.save(ex_amount);
}
@Override
public void deleteAccount(long accountNo) {
	 br.deleteById(accountNo);
	
}
@Override
public Optional<BankingEntity> findByName(String name) {
	
	return br.findByAccountHolder(name);
}
@Override
public boolean isPinExists(int pin) {
    return br.existsByPinNo(pin);
}



   

    public void sendOtpToUser(BankingEntity user, int otp) {

        emailService.sendMail(
        		
            user.getMail(),                 
            "OTP Verification",
            "Your OTP is: " + otp
        );
    }
	@Override
	public Optional<BankingEntity> findByMail(String email) {
		return br.findByMail(email);
	}
	@Override
	public BankingEntity updateUser(BankingEntity user) {
		
		return br.save(user);
	}

	public BankingEntity save(BankingEntity user) {

		return br.save(user);
		
	}
	public boolean panExists(String pan) {
	    return br.existsByPanNo(pan);
	}

}




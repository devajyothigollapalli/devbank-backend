package com.b1Banking.ZenBanking.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.b1Banking.ZenBanking.Entity.CreditCardEntity;
import com.b1Banking.ZenBanking.Repo.CreditCardRepo;

@Service
public class CreditCardService {
	
	

	    @Autowired
	    private CreditCardRepo creditrepo;

	    public CreditCardEntity save(CreditCardEntity card) {
	        return creditrepo.save(card);
	    }
	    public boolean cardTypeExists(long accountNo, String cardType) {
	        return creditrepo.existsByAccountNoAndCardType(accountNo, cardType);
	    }
	    public long countCards(long accountNo) {
	        return creditrepo.countByAccountNo(accountNo);
	    }
	    
	}



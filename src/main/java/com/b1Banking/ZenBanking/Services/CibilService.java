package com.b1Banking.ZenBanking.Services;


import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.b1Banking.ZenBanking.Entity.BankingEntity;
import com.b1Banking.ZenBanking.Entity.CibilEntity;
import com.b1Banking.ZenBanking.Repo.CibilRepository;

@Service
public class CibilService {

    @Autowired
    private CreditCardService creditCardService;

    @Autowired
    private CibilRepository cibilRepository;

    @Autowired
    private BankingServices bs;

    public int calculateCibil(BankingEntity user) {

        int score = 300;
        double balance = user.getBalance();
        if (balance >= 100000) score += 150;
        else if (balance >= 50000) score += 100;
        else if (balance >= 10000) score += 50;
        else score += 20;
        long cards = creditCardService.countCards(user.getAccountNo());
        score += cards * 20;

        if (score > 900) score = 900;

        CibilEntity history = new CibilEntity();
        history.setAccountNo(user.getAccountNo());
        history.setScore(score);
        history.setCalculatedAt(new Date());

        cibilRepository.save(history);
        user.setCibilScore(score);
        bs.save(user);

        return score;
    }
}




package com.b1Banking.ZenBanking.Scheduler;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.b1Banking.ZenBanking.Entity.BankingEntity;
import com.b1Banking.ZenBanking.Repo.BankingReposit;
import com.b1Banking.ZenBanking.Repo.CreditCardRepo;
import com.b1Banking.ZenBanking.Services.MailService;

@Component
public class CreditCardScheduler {

    @Autowired
    private BankingReposit br;

    @Autowired
    private CreditCardRepo creditCardRepo;

    @Autowired
    private MailService mailService;

 
    @Scheduled(fixedRate = 2 * 24 * 60 * 60 * 1000)


    public void notifyHighBalanceUsers() {

        List<BankingEntity> users = br.findAll();

        for (BankingEntity user : users) {

            if (!"ACTIVE".equalsIgnoreCase(user.getStatus())) {
                continue;
            }

            if (user.getBalance() <= 300000) {
                continue;
            }

            long cardCount =
                    creditCardRepo.countByAccountNo(user.getAccountNo());

            int remainingCards = 3 - (int) cardCount;

            if (remainingCards <= 0) {
                continue;
            }

            mailService.sendMail(
                    user.getMail(),
                    "DevBank Credit Card Availability Update",
                    "Dear " + user.getAccountHolder() + ",\n\n"
                            + "You currently have " + cardCount + " credit card(s).\n"
                            + "You can apply for "
                            + remainingCards + " more card(s).\n\n"
                            + "- DevBank"
            );
        }
    }

    
}

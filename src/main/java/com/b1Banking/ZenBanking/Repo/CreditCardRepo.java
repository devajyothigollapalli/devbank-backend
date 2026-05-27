package com.b1Banking.ZenBanking.Repo;

import org.springframework.data.jpa.repository.JpaRepository;
import com.b1Banking.ZenBanking.Entity.CreditCardEntity;

public interface CreditCardRepo extends JpaRepository<CreditCardEntity, Long> {
	boolean existsByAccountNo(long accountNo);
	boolean existsByAccountNoAndCardType(long accountNo, String cardType);
	  long countByAccountNo(long accountNo); 
}

package com.b1Banking.ZenBanking.Repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.b1Banking.ZenBanking.Entity.BankingEntity;
@Repository
public interface BankingReposit extends JpaRepository<BankingEntity,Long> {


    Optional<BankingEntity> findByAccountHolder(String accountHolder);
    Optional<BankingEntity> findByMail(String mail);
        boolean existsByPinNo(int pinNo);
    
        boolean existsByPanNo(String panNo);

	

}

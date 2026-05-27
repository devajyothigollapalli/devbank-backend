package com.b1Banking.ZenBanking.Services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.b1Banking.ZenBanking.DTO.LoginRequest;
import com.b1Banking.ZenBanking.Entity.BankingEntity;

@Service
public interface BankingServiceInt {
	
public BankingEntity openNewAccount(BankingEntity be);
public List<BankingEntity>getAllData();
public Optional<BankingEntity> getById(long id);
public BankingEntity depositedcash(BankingEntity ex_be);
public void deleteAccount(long accountNo);
Optional<BankingEntity> findByName(String name);
boolean isPinExists(int pin);
public Optional<BankingEntity> findByMail(String email);
BankingEntity updateUser(BankingEntity user);
public BankingEntity login(LoginRequest req);
boolean panExists(String panNo);


}

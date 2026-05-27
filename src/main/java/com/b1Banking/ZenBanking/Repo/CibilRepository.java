package com.b1Banking.ZenBanking.Repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.b1Banking.ZenBanking.Entity.CibilEntity;

public interface CibilRepository extends JpaRepository<CibilEntity, Long> {
	Optional<CibilEntity> findTopByAccountNoOrderByCalculatedAtDesc(long accountNo);
	}



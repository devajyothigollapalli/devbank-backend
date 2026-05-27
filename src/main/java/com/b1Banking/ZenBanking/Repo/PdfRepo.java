package com.b1Banking.ZenBanking.Repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.b1Banking.ZenBanking.Entity.PdfEntity;
@Repository
public interface PdfRepo extends JpaRepository<PdfEntity, Long> {

	List<PdfEntity> findTop10ByAccountNoOrderByTxnDateDesc(Long accountNo);
	  List<PdfEntity> findByAccountNo(String accountNo);

}

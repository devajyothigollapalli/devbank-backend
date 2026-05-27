package com.b1Banking.ZenBanking.Services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.b1Banking.ZenBanking.Entity.BankingEntity;
import com.b1Banking.ZenBanking.Entity.PdfEntity;
import com.b1Banking.ZenBanking.Repo.PdfRepo;

@Service
public class TxnUtilService {

    @Autowired
    private PdfRepo pdfRepo;

    public void saveTxn(
            BankingEntity user,
            String type,
            double amount,
            String remarks
    ) {
        PdfEntity txn = new PdfEntity();
        txn.setAccountNo(user.getAccountNo());
        txn.setAccountHolder(user.getAccountHolder());
        txn.setTxnAccountHolder(user.getAccountHolder());
        txn.setTxnMobile(user.getMobile());
        txn.setTxnpanNo(user.getPanNo());

        txn.setTxnType(type);
        txn.setAmount(amount);
        txn.setBalanceAfterTxn(user.getBalance());
        txn.setRemarks(remarks);

        pdfRepo.save(txn);
    }
    public List<PdfEntity> getTxnHistory(String accountNo){
        return pdfRepo.findByAccountNo(accountNo);
    }
	public PdfEntity getTxnById(Long id) {
		return pdfRepo.findById(id)
			    .orElseThrow(() -> new RuntimeException("Transaction not found"));
	}
	
}

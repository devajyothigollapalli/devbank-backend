package com.b1Banking.ZenBanking.DTO;

public class CibilDTO {


    private long accountNo;
    private long cibilScore;

    public CibilDTO(long accountNo, long cibilScore) {
        this.accountNo = accountNo;
        this.cibilScore = cibilScore;
    }

    public long getAccountNo() {
        return accountNo;
    }

    public long getCibilScore() {
        return cibilScore;
    }
}


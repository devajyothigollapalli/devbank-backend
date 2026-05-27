package com.b1Banking.ZenBanking.DTO;

import com.b1Banking.ZenBanking.Entity.BankingEntity;

public class RegisterRequest {

    private BankingEntity user;
    private int otp;

    public BankingEntity getUser() {
        return user;
    }
    public void setUser(BankingEntity user) {
        this.user = user;
    }

    public int getOtp() {
        return otp;
    }
    public void setOtp(int otp) {
        this.otp = otp;
    }
}

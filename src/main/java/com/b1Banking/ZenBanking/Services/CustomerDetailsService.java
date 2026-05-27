package com.b1Banking.ZenBanking.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import com.b1Banking.ZenBanking.Entity.BankingEntity;

@Service
public class CustomerDetailsService implements UserDetailsService {

    @Autowired
    private BankingServiceInt bs;

    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {

        BankingEntity user = bs.findByMail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found"));

        if (!"ACTIVE".equalsIgnoreCase(user.getStatus())) {
            throw new UsernameNotFoundException("Account inactive");
        }

        return User.builder()
                .username(user.getMail())
                .password(user.getPassword())
                .roles("USER")
                .build();
    }
}

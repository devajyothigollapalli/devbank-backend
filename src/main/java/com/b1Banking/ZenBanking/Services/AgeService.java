package com.b1Banking.ZenBanking.Services;

	
	import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;

import org.springframework.stereotype.Service;

	@Service
	public class AgeService {

	    public int calculateAge(Date dob) {
	        LocalDate birthDate = dob.toInstant()
	                .atZone(ZoneId.systemDefault())
	                .toLocalDate();

	        return Period.between(birthDate, LocalDate.now()).getYears();
	    }
	}



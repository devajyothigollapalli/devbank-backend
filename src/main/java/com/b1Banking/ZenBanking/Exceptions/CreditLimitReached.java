package com.b1Banking.ZenBanking.Exceptions;

public class CreditLimitReached extends RuntimeException{
	
	private static final long serialVersionUID = 1L;

	public CreditLimitReached(String cl) {
		super(cl);
	}

}

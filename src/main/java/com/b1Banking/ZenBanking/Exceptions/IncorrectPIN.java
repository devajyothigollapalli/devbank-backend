package com.b1Banking.ZenBanking.Exceptions;

public class IncorrectPIN extends RuntimeException {

	
	private static final long serialVersionUID = 1L;
 public IncorrectPIN(String ip) {
	 super(ip);
 }
}

package com.b1Banking.ZenBanking.Exceptions;

public class InvalidCredentials extends RuntimeException{

	private static final long serialVersionUID = 1L;
 public InvalidCredentials(String ic) {
	 super(ic);
 }
}

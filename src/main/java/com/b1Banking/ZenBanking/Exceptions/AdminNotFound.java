package com.b1Banking.ZenBanking.Exceptions;

public class AdminNotFound extends RuntimeException {

	private static final long serialVersionUID = 1L;

public AdminNotFound(String af) {
	super(af);
}
}

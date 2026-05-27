package com.b1Banking.ZenBanking.Exceptions;

public class RoleMisMatch extends RuntimeException {

	private static final long serialVersionUID = 1L;

public RoleMisMatch(String rm) {
	super(rm);
}
}

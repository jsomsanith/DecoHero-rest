package com.jso.deco.api.exception;

public enum DHMessageCode {
	/**
	 * User creation : username already exists
	 */
	REGISTRATION_USERNAME_ALREADY_EXISTS, 
	/**
	 * User creation : email already exists
	 */
	REGISTRATION_EMAIL_ALREADY_EXISTS,
	/**
	 * Generic : missing parameter in request
	 */
	MISSING_FIELD
}
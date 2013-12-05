package com.jso.deco.session;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.NewCookie;

import org.apache.commons.codec.binary.Base64;

public class CookieEncoder {
	private static CookieEncoder INSTANCE = new CookieEncoder();
	private static final String HASH_SALT = "decoherodecohero";
	private final Base64 encoder = new Base64();
	private MessageDigest digestSHA1;

	private CookieEncoder() {
		try {
			digestSHA1 = MessageDigest.getInstance("SHA-1");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}

	public static CookieEncoder getInstance() {
		return INSTANCE;
	}

	public Session cookieToSession(String cookie_value) throws IOException {
		byte[] value = encoder.decode(cookie_value.getBytes());
		try {
			return new Session(new String(value));
		}
		catch(IllegalArgumentException e) {
			return null;
		}
	}

	public NewCookie sessionToCookie(Session session, ContainerRequestContext requestContext) {
		String token_value = new String(Base64.encodeBase64(session.toString().getBytes()));

		return new NewCookie(
				SessionManager.DECO_COOKIE, 
				token_value, 
				"/", 
				requestContext.getUriInfo().getBaseUri().getHost(), 
				1,
				null, 
				(int) (session.getExpirationTime() - System.currentTimeMillis()) / 1000,
				new Date(session.getExpirationTime()),
				false, //is secured 
				true);
	}

	public byte[] decodeBase64(String encoded) {
		return encoder.decode(encoded);
	}

	public byte[] encodeBase64(byte[] decoded) {
		return encoder.encode(decoded);
	}

	public byte[] hashFromSession(Session session) {
		String value = session.getExpirationTime() + ";" + session.getUserId() + ";" + HASH_SALT;
		return digestSHA1.digest(value.getBytes());
	}
}
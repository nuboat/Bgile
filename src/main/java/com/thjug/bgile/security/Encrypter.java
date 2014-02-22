/*
 * Attribution
 * CC BY
 * This license lets others distribute, remix, tweak,
 * and build upon your work, even commercially,
 * as long as they credit you for the original creation.
 * This is the most accommodating of licenses offered.
 * Recommended for maximum dissemination and use of licensed materials.
 *
 * http://creativecommons.org/licenses/by/3.0/
 * http://creativecommons.org/licenses/by/3.0/legalcode
 */
package com.thjug.bgile.security;

import org.apache.shiro.crypto.hash.Sha256Hash;

/**
 * 
 * @author @nuboat
 */
public final class Encrypter {

	public static String cipher(final String plain) {
		return new Sha256Hash(plain).toHex();
	}

	private Encrypter() {
	}
}

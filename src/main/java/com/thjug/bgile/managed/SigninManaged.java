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
package com.thjug.bgile.managed;

import com.google.inject.Inject;
import com.thjug.bgile.entity.Account;
import com.thjug.bgile.entity.AuthenSession;
import com.thjug.bgile.facade.AuthenSessionFacade;
import com.thjug.bgile.security.Encrypter;
import com.timgroup.jgravatar.Gravatar;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author @nuboat
 */
@ManagedBean
@ViewScoped
public class SigninManaged extends AccountAbstractManaged {

	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory.getLogger(SigninManaged.class);

	private String username;
	private String password;

	@Inject
	private transient AuthenSessionFacade facade;

	private final transient Gravatar gravatar = new Gravatar();

	/**
	 * gravatar.setRating(GravatarRating.GENERAL_AUDIENCES);
	 * gravatar.setDefaultImage(GravatarDefaultImage.IDENTICON);
	 */
	public String authen() {
		final String ciphertext = Encrypter.cipher(password);
		LOG.debug("Authen with Username: {} Password {}", username, ciphertext);

		final UsernamePasswordToken token = new UsernamePasswordToken(username, ciphertext, true);
		final Subject subject = SecurityUtils.getSubject();
		try {
			subject.login(token);

			final Account account = (Account) subject.getPrincipal();
			final AuthenSession authenSession = facade.saveSession(account, subject.isRemembered());
			putCookieValue("bgile_auth_token", authenSession.getId());

			final String gravatarUrl = (getPrincipal() != null) ? gravatar.getUrl(getPrincipal().getEmail()) : null;
			getSession().setAttribute("GRAVATARURL", gravatarUrl);
		} catch (final UnknownAccountException | IncorrectCredentialsException e) {
			addWarnMessage("Username Or Password not correct.", null);
			return null;
		} catch (final LockedAccountException e) {
			addWarnMessage("Username wasn't activated", null);
			return null;
		}
		return redirect("dashboard");
	}

	public String logout() {
		SecurityUtils.getSubject().logout();
		getSession().invalidate();
		return redirect("home");
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(final String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(final String password) {
		this.password = password;
	}

}

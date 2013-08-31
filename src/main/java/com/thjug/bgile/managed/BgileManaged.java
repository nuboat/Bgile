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
import com.thjug.bgile.entity.Board;
import com.thjug.bgile.facade.BoardFacade;
import java.util.List;

/**
 *
 * @author @nuboat
 */
public abstract class BgileManaged extends AbstractManaged {

	protected Board board;

	@Inject
	protected transient BoardFacade boardFacade;

	protected Board getBoard(final Integer boardid) {
		return boardFacade.findById(getAccountId(), boardid);
	}

	protected Integer getBoardIdfromAttribute() {
		final List<String> attributes = (List<String>) getAttribute("ATTRIBUTES");
		if (attributes != null && attributes.size() > 0) {
			return Integer.valueOf(attributes.get(0));
		} else {
			return (Integer) getSession().getAttribute("boardid");
		}
	}

}
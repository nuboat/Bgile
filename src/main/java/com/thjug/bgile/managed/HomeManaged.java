/*
 * <pre>
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
 * </pre>
 */
package com.thjug.bgile.managed;

import com.thjug.bgile.define.State;
import com.thjug.bgile.entity.Board;
import com.thjug.bgile.entity.Card;
import com.thjug.bgile.entity.Cardorder;
import com.thjug.bgile.facade.CardFacade;
import com.thjug.bgile.util.StringUtility;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.faces.application.Application;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.UnavailableSecurityManagerException;
import org.apache.shiro.subject.Subject;
import org.primefaces.component.dashboard.Dashboard;
import org.primefaces.component.panel.Panel;
import org.primefaces.model.DashboardColumn;
import org.primefaces.model.DashboardModel;
import org.primefaces.model.DefaultDashboardColumn;
import org.primefaces.model.DefaultDashboardModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author @nuboat
 */
@RequestScoped
@ManagedBean(name = "home")
public class HomeManaged extends BgileAbstractManaged {

	private static final long serialVersionUID = 2L;
	private static final Logger LOG = LoggerFactory.getLogger(HomeManaged.class);

	private static final String DASHBOARD = "org.primefaces.component.Dashboard";
	private static final String DASHBOARD_RENDERER = "org.primefaces.component.DashboardRenderer";
	private static final String PANEL = "org.primefaces.component.Panel";
	private static final String PANEL_RENDERER = "org.primefaces.component.PanelRenderer";

	private Board board;
	private Map<Integer, Card> cardMap;
	private List<Cardorder> cardorderList;
	private transient Dashboard dashboard;

	@Inject
	private transient CardFacade cardFacade;

	@PostConstruct
	public void initial() {
		final Integer boardid = 1;

		board = getBoard(boardid);
		if (board == null) {
			return;
		}

		cardMap = cardFacade.findAllByBoardId(boardid);
		cardorderList = cardFacade.findCardorder(board);
		renderDashboard();
	}

	public boolean isHasSession() {
		try {
			final Subject currentUser = SecurityUtils.getSubject();
			if (currentUser.isAuthenticated()) {
				return true;
			}
		} catch (final UnavailableSecurityManagerException e) {
			LOG.error(e.getMessage(), e);
		}
		return false;
	}

	private void renderDashboard() {
		final FacesContext fc = getFacesInstance();
		final Application application = fc.getApplication();

		dashboard = (Dashboard) application.createComponent(fc, DASHBOARD, DASHBOARD_RENDERER);
		dashboard.setId("dashboard");

		final DashboardModel model = new DefaultDashboardModel();
		for (int i = 0, n = State.values().length; i < n; i++) {
			final DashboardColumn column = new DefaultDashboardColumn();
			model.addColumn(column);
		}
		dashboard.setModel(model);

		final Set<Integer> addedList = addFromCardorder(model);

		addExceptCardorder(model, addedList);
	}

	private Set<Integer> addFromCardorder(final DashboardModel model) {
		final Set<Integer> addedList = new HashSet<>();

		for (final Cardorder order : cardorderList) {
			for (final String cardid : order.getOrderby().split(",")) {
				if (StringUtility.isEmpty(cardid.trim())) {
					continue;
				}

				final Integer id = Integer.valueOf(cardid.trim());
				final Card card = cardMap.get(id);
				if (card == null) { // || !order.getStateid().equals(card.getStateid())) {
					continue;
				}

				addedList.add(id);
				addToDashboard(model, card);
			}
		}

		return addedList;
	}

	private void addExceptCardorder(final DashboardModel model, final Set<Integer> addedList) {
		cardMap.keySet().stream()
				.filter((id) -> !(addedList.contains(id)))
				.map((id) -> cardMap.get(id))
				.forEach((card) -> addToDashboard(model, card));
	}

	private void addToDashboard(final DashboardModel model, final Card card) {
		final Panel panel = createPanel(card);
		dashboard.getChildren().add(panel);

		final DashboardColumn column = model.getColumn(card.getStateid());
		column.addWidget(panel.getId());
	}

	private Panel createPanel(final Card card) {
		final FacesContext fc = getFacesInstance();
		final Application application = fc.getApplication();
		final Panel panel = (Panel) application.createComponent(fc, PANEL, PANEL_RENDERER);
		panel.setClosable(false);
		panel.setToggleable(false);
		panel.setId("ID" + card.getId().toString());

		if (isViewonly()) {
			panel.setHeader(card.getStory());
		} else {
			panel.setHeader(
					String.format("<a href=\"%s/fcard/%s\"><i class=\"icon-edit\" style=\"padding-right: 4px;\"></i>%s</a>", getContextPath(), card.getId(), card.getStory()));
		}

		if (card.getOwner() != null) {
			panel.setFooter(card.getOwner().getFirstname() + " " + card.getOwner().getLastname());
		}

		final HtmlOutputText text = new HtmlOutputText();
		text.setEscape(false);
		text.setValue("<pre>" + card.getDescription() + "</pre>");
		panel.getChildren().add(text);

		return panel;
	}

	public Dashboard getDashboard() {
		return dashboard;
	}

	public boolean isViewonly() {
		return true;
	}

}

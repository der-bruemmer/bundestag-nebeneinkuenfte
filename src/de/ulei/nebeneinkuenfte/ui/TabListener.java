package de.ulei.nebeneinkuenfte.ui;

import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;
import com.vaadin.ui.TabSheet.SelectedTabChangeListener;

import de.ulei.nebeneinkuenfte.util.IConstants;

public class TabListener implements SelectedTabChangeListener {

	private static final long serialVersionUID = 4906571421432304590L;

	@Override
	public void selectedTabChange(SelectedTabChangeEvent event) {

		String tabCaption = event.getTabSheet().getSelectedTab().getCaption();

		if (tabCaption != null) {
			int tabIndex = Integer.valueOf(tabCaption);
			switch (tabIndex) {
			case IConstants.PERSON_BASIC_VIEW:
				NebeneinkuenfteApplication.getInstance().setURIFragment(IConstants.PERSON_BASIC_VIEW_FRAG);
				break;
			case IConstants.PERSON_PERSON_VIEW:
				NebeneinkuenfteApplication.getInstance().setURIFragment(
						NebeneinkuenfteApplication.getInstance().getMainController().getActualObjectURI());
				break;
			case IConstants.PERSON_FRACTION_VIEW:
				NebeneinkuenfteApplication.getInstance().setURIFragment(
						NebeneinkuenfteApplication.getInstance().getMainController().getActualObjectURI());
				break;
			case IConstants.PERSON_ORIGIN_VIEW:
				NebeneinkuenfteApplication.getInstance().setURIFragment(IConstants.PERSON_ORIGIN_VIEW_FRAG);
				break;
			case IConstants.IMPRESSUM_VIEW:
				NebeneinkuenfteApplication.getInstance().setURIFragment(IConstants.IMPRESSUM_VIEW_FRAG);
				break;
			case IConstants.ABOUT_PROJECT_VIEW:
				NebeneinkuenfteApplication.getInstance().setURIFragment(IConstants.ABOUT_PROJECT_VIEW_FRAG);
				break;

			default:
				break;
			}
		}

	}
}
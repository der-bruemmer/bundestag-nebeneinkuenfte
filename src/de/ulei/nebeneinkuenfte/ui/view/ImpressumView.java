package de.ulei.nebeneinkuenfte.ui.view;

import java.io.Serializable;

import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;

import de.ulei.nebeneinkuenfte.util.IConstants;

public class ImpressumView extends GridLayout implements Serializable {

	private static final long serialVersionUID = -3207647187091883100L;
	
	public ImpressumView() {
	
		setCaption(String.valueOf(IConstants.IMPRESSUM_VIEW));
		addComponent(new Label("In Arbeit"));
		
	}

}

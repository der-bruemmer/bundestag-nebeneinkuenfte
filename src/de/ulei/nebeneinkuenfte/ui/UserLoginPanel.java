package de.ulei.nebeneinkuenfte.ui;

import java.io.Serializable;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Panel;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class UserLoginPanel extends Panel implements Serializable {

	private static final long serialVersionUID = 8507024479507328843L;

	private TextField userName;
	private PasswordField password;
	private Button loginButton;

	private Button debugButton;

	public UserLoginPanel() {

		setCaption("Login");

		VerticalLayout vlayout = new VerticalLayout();
		vlayout.setMargin(true);
		vlayout.setSpacing(true);

		userName = new TextField("Benutzername");
		password = new PasswordField("Passwort");

		loginButton = new Button("Login");

		debugButton = new Button("DEBUG!");
		debugButton.addListener(new Button.ClickListener() {

			private static final long serialVersionUID = 711634698661727142L;

			@Override
			public void buttonClick(ClickEvent event) {

			}
		});
		
		

		vlayout.addComponent(userName);
		vlayout.addComponent(password);
		vlayout.addComponent(loginButton);

		addComponent(vlayout);

	}

}

package de.ulei.nebeneinkuenfte.ui.view;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;

import de.ulei.nebeneinkuenfte.ui.NebeneinkuenfteApplication;

public abstract class AbstractTextView extends GridLayout implements Serializable {

	private static final long serialVersionUID = -6261121273077228637L;
	private final int TEXT_WIDTH = 60;

	public AbstractTextView(int identifier) {
		setCaption(String.valueOf(identifier));
	}

	public void addHeadline(String headline, int type) {

		StringBuffer buffer = new StringBuffer();
		buffer.append("<h");
		buffer.append(String.valueOf(type));
		buffer.append(">");
		buffer.append(headline);
		buffer.append("</h");
		buffer.append(String.valueOf(type));
		buffer.append(">");

		Label label = new Label(buffer.toString(), Label.CONTENT_XHTML);
		label.setWidth(TEXT_WIDTH, UNITS_PERCENTAGE);
		addComponent(label);
		setComponentAlignment(label, Alignment.MIDDLE_CENTER);

	}

	public void addParagraph(String fileName, String folder) {

		StringBuffer buffer = new StringBuffer();
		String line = null;

		BufferedReader reader;
		try {

			reader = new BufferedReader(new FileReader(new File(NebeneinkuenfteApplication.getInstance().getContext()
					.getBaseDirectory()
					+ "/"+folder+"/" + fileName)));
			while ((line = reader.readLine()) != null) {

				buffer.append(line);
				buffer.append(System.getProperty("line.separator"));

			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		Label label = new Label("<p>" + buffer.toString() + "</p>", Label.CONTENT_XHTML);
		label.setWidth(TEXT_WIDTH, UNITS_PERCENTAGE);

		addComponent(label);
		setComponentAlignment(label, Alignment.MIDDLE_CENTER);

	}

	public void addTable(String fileName, String caption, boolean showHeader) {

		boolean headerMode = true;
		int index = 1;

		Table table = new Table(caption);
		table.setImmediate(true);
		table.setWidth(TEXT_WIDTH, UNITS_PERCENTAGE);

		if (!showHeader)
			table.setColumnHeaderMode(Table.COLUMN_HEADER_MODE_HIDDEN);

		String line = null;

		BufferedReader reader;
		try {

			reader = new BufferedReader(new FileReader(new File(NebeneinkuenfteApplication.getInstance().getContext()
					.getBaseDirectory()
					+ "/analysis/" + fileName)));
			while ((line = reader.readLine()) != null) {

				String[] items = line.split(";");
				if (headerMode) {

					for (String header : items)
						table.addContainerProperty(header, String.class, null);

					headerMode = false;

				} else {
					table.addItem(items, index++);
				}

			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		table.setPageLength(index - 1);

		addComponent(table);
		setComponentAlignment(table, Alignment.MIDDLE_CENTER);

	}

	public void addImage(String fileName, String imageCaption) {

		GridLayout gridLayout = new GridLayout(1, 1);
		gridLayout.setImmediate(true);
		gridLayout.setWidth(TEXT_WIDTH, UNITS_PERCENTAGE);
		gridLayout.setSpacing(true);
		gridLayout.setMargin(true);

		Embedded image = new Embedded("", new ThemeResource("images/".concat(fileName)));
		image.setCaption(imageCaption);
		gridLayout.addComponent(image);
		gridLayout.setComponentAlignment(image, Alignment.MIDDLE_CENTER);

		addComponent(gridLayout);
		setComponentAlignment(gridLayout, Alignment.MIDDLE_CENTER);

	}

	public void addImages(List<String> fileNames, List<String> imageCaptions) {

		GridLayout gridLayout = new GridLayout(fileNames.size(), 1);
		gridLayout.setImmediate(true);
		gridLayout.setWidth(TEXT_WIDTH, UNITS_PERCENTAGE);
		gridLayout.setSpacing(true);
		gridLayout.setMargin(true);

		Embedded image;

		for (int i = 0; i < fileNames.size(); i++) {

			image = new Embedded("", new ThemeResource("images/".concat(fileNames.get(i))));
			image.setCaption(imageCaptions.get(i));
			gridLayout.addComponent(image);
			gridLayout.setComponentAlignment(image, Alignment.MIDDLE_CENTER);

		}

		addComponent(gridLayout);
		setComponentAlignment(gridLayout, Alignment.MIDDLE_CENTER);

	}

}

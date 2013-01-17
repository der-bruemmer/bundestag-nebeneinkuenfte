package de.ulei.nebeneinkuenfte.ui.controller;

import java.awt.geom.Point2D.Double;
import java.util.Iterator;

import org.vaadin.hezamu.googlemapwidget.GoogleMap;
import org.vaadin.hezamu.googlemapwidget.overlay.BasicMarker;

import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

import de.ulei.nebeneinkuenfte.ui.NebeneinkuenfteApplication;
import de.ulei.nebeneinkuenfte.ui.model.Nebentaetigkeit;
import de.ulei.nebeneinkuenfte.ui.view.PersonView;
import de.ulei.nebeneinkuenfte.util.ActionEvent;
import de.ulei.nebeneinkuenfte.util.IActionListener;

public class PersonController extends AbstractSidelineJobController implements IActionListener {

	private static final long serialVersionUID = 7065013836922459523L;

	private PersonView personView;

	public PersonController(PersonView personView) {

		this.personView = personView;
		this.personView.addListener(this);

	}

	@Override
	public void handleAction(ActionEvent event) {

		switch (event.getActionType()) {
		case OPEN_PERSON_BASIC:
			fireEvent(event.getActionType());
			break;
		case GO_BACK:
			fireEvent(event.getActionType());
			break;
		case FILTER:
			// setTableFooter();
			break;
		case OPEN_PERSON_ORIGIN:
			setActualSidelineJob((Nebentaetigkeit) personView.getBasicTable().getValue());
			fireEvent(event.getActionType());
			break;
		case TABLE_SELECT:
			setActualSidelineJob((Nebentaetigkeit) personView.getBasicTable().getValue());
			break;
		case OPEN_MAP:
			openSidelineMap();
			break;
		default:
			break;
		}
	}

	private void openSidelineMap() {

		// Create a new map instance centered on the IT Mill offices
		GoogleMap googleMap = new GoogleMap(NebeneinkuenfteApplication.getInstance(), new Double(10.311699, 50.982425),
				6);
		googleMap.setWidth(1024, Sizeable.UNITS_PIXELS);
		googleMap.setHeight(768, Sizeable.UNITS_PIXELS);

		Nebentaetigkeit nt = null;
		BasicMarker marker = null;
		String title;
		long id = 0;

		@SuppressWarnings("unchecked")
		Iterator<Nebentaetigkeit> it = (Iterator<Nebentaetigkeit>) personView.getBasicTable().getItemIds().iterator();

		while (it.hasNext()) {
			nt = it.next();

			if (nt.getPlace() != null) {

				title = "";
				title = title.concat(nt.getPlace());
				title = title.concat(": ");
				title = title.concat(nt.getAuftraggeber() != null ? nt.getAuftraggeber() : "");
				title = title.concat(", ");
				title = title.concat(nt.getType() != null ? nt.getType() : "");
				title = title.concat(", ");
				title = title.concat(nt.getStufe() != null ? nt.getStufe() : "");

				marker = new BasicMarker(id++, new Double(nt.getLongitude(), nt.getLatitude()), title);
				marker.setDraggable(false);
				googleMap.addMarker(marker);

			}

		}

		VerticalLayout verticalLayout = new VerticalLayout();
		verticalLayout.setImmediate(true);
		verticalLayout.setSizeFull();
		verticalLayout.setSpacing(false);
		verticalLayout.setMargin(false);
		verticalLayout.addComponent(googleMap);
		verticalLayout.setComponentAlignment(googleMap, Alignment.MIDDLE_CENTER);

		Window w = new Window("Nebeneinkünfte");
		w.setModal(true);
		w.setResizable(false);
		w.setWidth(1024, Sizeable.UNITS_PIXELS);
		googleMap.setHeight(768, Sizeable.UNITS_PIXELS);
		w.addComponent(verticalLayout);

		NebeneinkuenfteApplication.getInstance().getMainWindow().addWindow(w);

	}

	@Override
	public void setActualSidelineJob(Nebentaetigkeit actualSidelineJob) {

		personView.enableOpenOriginButton(actualSidelineJob != null);
		super.setActualSidelineJob(actualSidelineJob);

	}
}

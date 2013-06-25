/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2013 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.plugin.geocoder.client;

import org.geomajas.puregwt.client.map.MapPresenter;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Map gadget that contains a {@link GeocoderTextBox} with a clickable magnifying glass.
 * <p>
 * Alternatives for ambiguous searches are shown in a drop-down panel below the text box.
 * </p>
 * <p>
 * <b>Tip</b>: To position next to the {@link org.geomajas.puregwt.client.map.gadget.ZoomToRectangleGadget}, set top to
 * 5 and left to 90 with <code>new GeocoderGadget(5, 90)</code>
 * </p>
 * 
 * @author Emiel Ackermann
 */
public class GeocoderWidget extends FlowPanel {

	private static final String GM_GEOCODER_GADGET_GLASS = "gm-GeocoderGadget-glass";

	private static final String GM_GEOCODER_GADGET = "gm-GeocoderGadget";

	static final String GM_GEOCODER_GADGET_TEXT_BOX_WITH_ALTS = "gm-GeocoderGadget-textBox-withAlts";

	/** Extra CSS to style the tip. */
	static final String GM_GEOCODER_GADGET_ALT_PANEL = "gm-GeocoderGadget-altPanel";

	static final String GM_GEOCODER_GADGET_ALT_LABEL = "gm-GeocoderGadget-altLabel";

	private final MapPresenter mapPresenter;

	private GeocoderTextBox box;

	public GeocoderWidget(MapPresenter mapPresenter, int top, int left) {
		this.mapPresenter = mapPresenter;
		asWidget().getElement().getStyle().setTop(top, Unit.PX);
		asWidget().getElement().getStyle().setLeft(left, Unit.PX);

		buildGui();
	}

	private void buildGui() {
		setStyleName(GM_GEOCODER_GADGET);
		box = new GeocoderTextBox(mapPresenter);
		add(box);
		add(createMagnifyingGlass());
		StopPropagationHandler stopHandler = new StopPropagationHandler();
		addDomHandler(stopHandler, MouseDownEvent.getType());
		addDomHandler(stopHandler, MouseUpEvent.getType());
		addDomHandler(stopHandler, ClickEvent.getType());
		addDomHandler(stopHandler, DoubleClickEvent.getType());

		// Create drop-down panel below text box with alternatives
		new AlternativesPanel(box);
	}

	private Widget createMagnifyingGlass() {
		Button glass = new Button();
		glass.setStyleName(GM_GEOCODER_GADGET_GLASS);
		glass.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				box.goToLocation();
				event.stopPropagation();
			}
		});
		return glass;
	}

	/**
	 * Combination of different handlers with a single goal: stop all the events from propagating to the map. This is
	 * meant to be used for clickable widgets.
	 * 
	 * @author Pieter De Graef
	 */
	public class StopPropagationHandler implements MouseDownHandler, MouseUpHandler, ClickHandler, 
			DoubleClickHandler {

		public void onDoubleClick(DoubleClickEvent event) {
			event.stopPropagation();
		}

		public void onClick(ClickEvent event) {
			event.stopPropagation();
		}

		public void onMouseDown(MouseDownEvent event) {
			event.stopPropagation();
		}

		public void onMouseUp(MouseUpEvent event) {
			event.stopPropagation();
		}
	}
}
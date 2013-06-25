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

package org.geomajas.puregwt.client.widget;

import org.geomajas.annotation.Api;
import org.geomajas.puregwt.client.map.MapPresenter;
import org.geomajas.puregwt.client.map.ViewPort;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Map widget that displays zoom in, zoom out and zoom to maximum extent buttons. This widget is meant to be added to
 * the map's widget pane (see {@link MapPresenter#getWidgetPane()}).
 * 
 * @author Pieter De Graef
 * @since 1.0.0
 */
@Api(allMethods = true)
public class SimpleZoomWidget extends AbstractMapWidget {

	/**
	 * UI binder definition for the {@link SimpleZoomWidget} widget.
	 * 
	 * @author Pieter De Graef
	 */
	interface SimpleZoomGadgetUiBinder extends UiBinder<Widget, SimpleZoomWidget> {
	}

	private static final SimpleZoomGadgetUiBinder UI_BINDER = GWT.create(SimpleZoomGadgetUiBinder.class);

	@UiField
	protected SimplePanel zoomInElement;

	@UiField
	protected SimplePanel zoomOutElement;

	@UiField
	protected SimplePanel extentElement;

	// ------------------------------------------------------------------------
	// Constructors:
	// ------------------------------------------------------------------------

	/**
	 * Create a widget with zoom in, zoom out and zoom to maximum extent button.
	 * 
	 * @param mapPresenter
	 *            The map to show this widget on.
	 * @param top
	 * @param left
	 */
	public SimpleZoomWidget(MapPresenter mapPresenter, int top, int left) {
		super(mapPresenter);
		buildGui();
		getElement().getStyle().setTop(top, Unit.PX);
		getElement().getStyle().setLeft(left, Unit.PX);
		getElement().getStyle().setPosition(Position.ABSOLUTE);
	}

	// ------------------------------------------------------------------------
	// Private methods:
	// ------------------------------------------------------------------------

	private void buildGui() {
		initWidget(UI_BINDER.createAndBindUi(this));
		StopPropagationHandler preventWeirdBehaviourHandler = new StopPropagationHandler();
		addDomHandler(preventWeirdBehaviourHandler, MouseDownEvent.getType());
		addDomHandler(preventWeirdBehaviourHandler, MouseUpEvent.getType());
		addDomHandler(preventWeirdBehaviourHandler, ClickEvent.getType());
		addDomHandler(preventWeirdBehaviourHandler, DoubleClickEvent.getType());

		final ViewPort viewPort = mapPresenter.getViewPort();

		// Zoom in button:
		zoomInElement.addDomHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				int index = viewPort.getZoomStrategy().getZoomStepIndex(viewPort.getScale());
				try {
					viewPort.applyScale(viewPort.getZoomStrategy().getZoomStepScale(index - 1));
				} catch (IllegalArgumentException e) {
				}
				event.stopPropagation();
			}
		}, ClickEvent.getType());

		// Zoom out button:
		zoomOutElement.addDomHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				int index = viewPort.getZoomStrategy().getZoomStepIndex(viewPort.getScale());
				try {
					viewPort.applyScale(viewPort.getZoomStrategy().getZoomStepScale(index + 1));
				} catch (IllegalArgumentException e) {
				}
				event.stopPropagation();
			}
		}, ClickEvent.getType());

		// Zoom to maximum extent button:
		extentElement.addDomHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				viewPort.applyBounds(viewPort.getMaximumBounds());
				event.stopPropagation();
			}
		}, ClickEvent.getType());
	}
}
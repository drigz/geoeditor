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

package org.geomajas.puregwt.client.event;

import org.geomajas.annotation.Api;
import org.geomajas.puregwt.client.map.ViewPort;

import com.google.web.bindery.event.shared.Event;

/**
 * Event that is fired when a translation on the {@link ViewPort} has occurred, while the scale has remained the same.
 * 
 * @author Pieter De Graef
 * @since 1.0.0
 */
@Api(allMethods = true)
public class ViewPortTranslatedEvent extends Event<ViewPortChangedHandler> {

	private final ViewPort viewPort;

	// -------------------------------------------------------------------------
	// Constructor:
	// -------------------------------------------------------------------------

	/**
	 * Create an event for the specified view port.
	 * 
	 * @param viewPort the view port
	 */
	public ViewPortTranslatedEvent(ViewPort viewPort) {
		this.viewPort = viewPort;
	}

	// -------------------------------------------------------------------------
	// Event implementation:
	// -------------------------------------------------------------------------

	@Override
	public Type<ViewPortChangedHandler> getAssociatedType() {
		return ViewPortChangedHandler.TYPE;
	}

	/**
	 * Get the view port that has translated.
	 * 
	 * @return the view port
	 */
	public ViewPort getViewPort() {
		return viewPort;
	}	

	// ------------------------------------------------------------------------
	// Protected methods:
	// ------------------------------------------------------------------------

	/** @todo javadoc unknown. */
	protected void dispatch(ViewPortChangedHandler handler) {
		handler.onViewPortTranslated(this);
	}
}
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
import org.geomajas.annotation.UserImplemented;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent.Type;

/**
 * Handler for events that indicate a certain scale level has been rendered. For a tile based system, this could mean
 * that all tiles are rendered.
 * 
 * @author Pieter De Graef
 * @since 1.0.0
 */
@Api(allMethods = true)
@UserImplemented
public interface ScaleLevelRenderedHandler extends EventHandler {

	/**
	 * The type of the handler.
	 */
	Type<ScaleLevelRenderedHandler> TYPE = new Type<ScaleLevelRenderedHandler>();

	/**
	 * Called when a scale level has been fully rendered.
	 * 
	 * @param event
	 *            The scale level rendered event.
	 */
	void onScaleLevelRendered(ScaleLevelRenderedEvent event);
}
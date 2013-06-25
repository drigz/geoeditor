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

package org.geomajas.plugin.editing.client.merge.event;

import org.geomajas.annotation.Api;
import org.geomajas.annotation.UserImplemented;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

/**
 * Handler for catching events for geometries being added to the merging list.
 * 
 * @author Pieter De Graef
 * @since 1.0.0
 */
@Api(allMethods = true)
@UserImplemented
public interface GeometryMergeAddedHandler extends EventHandler {

	/** Type of this handler. */
	GwtEvent.Type<GeometryMergeAddedHandler> TYPE = new GwtEvent.Type<GeometryMergeAddedHandler>();

	/**
	 * Executed when a geometry has been added to the list for merging.
	 * 
	 * @param event
	 *            The geometry merging add event.
	 */
	void onGeometryMergingAdded(GeometryMergeAddedEvent event);
}
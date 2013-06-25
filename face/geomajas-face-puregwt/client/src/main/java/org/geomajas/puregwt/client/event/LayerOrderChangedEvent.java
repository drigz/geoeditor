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

import com.google.web.bindery.event.shared.Event;

/**
 * Event that is fired when the order of a layer is changed within the
 * {@link org.geomajas.puregwt.client.map.layer.LayersModel}. This event contains indices pointing to the original index
 * and the target index for the layer. Of course, changing the index of a single layer, also changes the indices of
 * other layers.
 * 
 * @author Pieter De Graef
 * @author Jan De Moerloose
 * @since 1.0.0
 */
@Api(allMethods = true)
public class LayerOrderChangedEvent extends Event<LayerOrderChangedHandler> {

	private final int fromIndex;

	private final int toIndex;

	/**
	 * Create an event for the specified layer indices.
	 * 
	 * @param fromIndex
	 *            the index before the move
	 * @param toIndex
	 *            the index after the move
	 */
	public LayerOrderChangedEvent(int fromIndex, int toIndex) {
		this.fromIndex = fromIndex;
		this.toIndex = toIndex;
	}

	@Override
	public Type<LayerOrderChangedHandler> getAssociatedType() {
		return LayerOrderChangedHandler.TYPE;
	}

	@Override
	protected void dispatch(LayerOrderChangedHandler layerOrderChangedHandler) {
		layerOrderChangedHandler.onLayerOrderChanged(this);
	}

	/**
	 * Get the original index at which a layer has been moved.
	 * 
	 * @return The original index at which a layer has been moved.
	 */
	public int getFromIndex() {
		return fromIndex;
	}

	/**
	 * Get the new index at which the layer has been placed.
	 * 
	 * @return The new index at which the layer has been placed.
	 */
	public int getToIndex() {
		return toIndex;
	}

	/**
	 * Return the minimum of the 2 indices (fromIndex, toIndex).
	 * 
	 * @return The minimum of the 2 indices.
	 */
	public int getMinIndex() {
		return (toIndex < fromIndex) ? toIndex : fromIndex;
	}

	/**
	 * Return the maximum of the 2 indices (fromIndex, toIndex).
	 * 
	 * @return The maximum of the 2 indices.
	 */
	public int getMaxIndex() {
		return (toIndex < fromIndex) ? fromIndex : toIndex;
	}
}
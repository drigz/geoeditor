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

package org.geomajas.puregwt.client.map.layer;

import java.util.ArrayList;
import java.util.List;

import org.geomajas.configuration.client.ClientLayerInfo;
import org.geomajas.configuration.client.ClientMapInfo;
import org.geomajas.configuration.client.ClientRasterLayerInfo;
import org.geomajas.configuration.client.ClientVectorLayerInfo;
import org.geomajas.puregwt.client.event.LayerAddedEvent;
import org.geomajas.puregwt.client.event.LayerDeselectedEvent;
import org.geomajas.puregwt.client.event.LayerOrderChangedEvent;
import org.geomajas.puregwt.client.event.LayerRemovedEvent;
import org.geomajas.puregwt.client.event.LayerSelectedEvent;
import org.geomajas.puregwt.client.event.LayerSelectionHandler;
import org.geomajas.puregwt.client.map.MapEventBus;
import org.geomajas.puregwt.client.map.ViewPort;

import com.google.inject.Inject;

/**
 * Default implementation of the {@link LayersModel} interface.
 * 
 * @author Pieter De Graef
 */
public final class LayersModelImpl implements LayersModel {

	private ClientMapInfo mapInfo;

	private ViewPort viewPort;

	private MapEventBus eventBus;

	/**
	 * An ordered list of layers. The drawing order on the map is as follows: the first layer will be placed at the
	 * bottom, the last layer on top.
	 */
	private List<Layer> layers = new ArrayList<Layer>();

	@Inject
	private LayerFactory layerFactory;

	// ------------------------------------------------------------------------
	// Constructors:
	// ------------------------------------------------------------------------

	@Inject
	private LayersModelImpl() {
	}

	// ------------------------------------------------------------------------
	// MapModel implementation:
	// ------------------------------------------------------------------------

	/**
	 * Initialization method for the layers model.
	 * 
	 * @param mapInfo
	 *            The configuration object from which this model should build itself.
	 * @param viewPort
	 *            The view port that is associated with the same map this layer model belongs to.
	 * @param eventBus
	 *            Event bus that governs all event related to this layers model.
	 */
	public void initialize(ClientMapInfo mapInfo, ViewPort viewPort, MapEventBus eventBus) {
		this.mapInfo = mapInfo;
		this.viewPort = viewPort;
		this.eventBus = eventBus;

		// Add a layer selection handler that allows only one selected layer at a time:
		eventBus.addLayerSelectionHandler(new LayerSelectionHandler() {

			public void onSelectLayer(LayerSelectedEvent event) {
				for (Layer layer : layers) {
					if (layer.isSelected() && !layer.equals(event.getLayer())) {
						layer.setSelected(false);
					}
				}
			}

			public void onDeselectLayer(LayerDeselectedEvent event) {
			}
		});

		// Create all the layers:
		layers = new ArrayList<Layer>();
		for (ClientLayerInfo layerInfo : mapInfo.getLayers()) {
			Layer layer = createLayer(layerInfo);
			addLayer(layer);
		}
	}

	@Override
	public boolean addLayer(Layer layer) {
		if (layer == null) {
			throw new IllegalArgumentException("Layer is null.");
		}
		if (getLayer(layer.getId()) == null) {
			layers.add(layer);
			if (layer instanceof AbstractLayer) {
				AbstractLayer aLayer = (AbstractLayer) layer;
				aLayer.setViewPort(viewPort);
				aLayer.setEventBus(eventBus);
			}
			eventBus.fireEvent(new LayerAddedEvent(layer));
			return true;
		}
		return false;
	}

	private Layer createLayer(ClientLayerInfo layerInfo) {
		ServerLayer<?> layer = null;
		switch (layerInfo.getLayerType()) {
			case RASTER:
				layer = layerFactory.createRasterLayer((ClientRasterLayerInfo) layerInfo, viewPort, eventBus);
				break;
			default:
				layer = layerFactory.createVectorLayer((ClientVectorLayerInfo) layerInfo, viewPort, eventBus);
				break;
		}
		if (!mapInfo.getLayers().contains(layer.getLayerInfo())) {
			mapInfo.getLayers().add(layer.getLayerInfo());
		}
		return layer;
	}

	public boolean removeLayer(String id) {
		Layer layer = getLayer(id);
		if (layer != null) {
			int index = getLayerPosition(layer);
			layers.remove(layer);
			if (layer instanceof ServerLayer) {
				ServerLayer<?> serverLayer = (ServerLayer<?>) layer;
				mapInfo.getLayers().remove(serverLayer.getLayerInfo());
			}
			eventBus.fireEvent(new LayerRemovedEvent(layer, index));
			return true;
		}
		return false;
	}

	/**
	 * Get a single layer by its identifier.
	 * 
	 * @param id
	 *            The layers unique identifier within this map.
	 * @return Returns the layer, or null if it could not be found.
	 */
	public Layer getLayer(String id) {
		if (id == null) {
			throw new IllegalArgumentException("Null ID passed to the getLayer method.");
		}
		for (Layer layer : layers) {
			if (id.equals(layer.getId())) {
				return layer;
			}
		}
		return null;
	}

	/**
	 * Return the total number of layers within this map.
	 * 
	 * @return The layer count.
	 */
	public int getLayerCount() {
		return layers.size();
	}

	public boolean moveLayer(Layer layer, int index) {
		int currentIndex = getLayerPosition(layer);
		// Check the new index:
		if (index < 0) {
			index = 0;
		} else if (index > layers.size() - 1) {
			index = layers.size() - 1;
		}
		if (currentIndex < 0 || currentIndex == index) {
			return false;
		}

		
		ClientLayerInfo layerInfo = null;
		int newIndexMapInfo = -1;
		// Check if both the layer with whom the specified layer will swap (concerning the ordering) and 
		// the specified layer are server layers. If so their position in the mapInfo.getLayers() must also be swapped 
		if (layer instanceof ServerLayer && layers.get(index) instanceof ServerLayer) {
			ServerLayer<?> serverLayer = (ServerLayer<?>) layer;
			layerInfo = serverLayer.getLayerInfo();
			
			int idx = 0;
			for (ClientLayerInfo layerInMapInfo : mapInfo.getLayers()) {
				if (layerInMapInfo.getId().equals(layerInfo.getId())) {
					
					if (index > currentIndex) {
						newIndexMapInfo = idx + 1; 
					} else {
						newIndexMapInfo = idx - 1;
					}
					break; // Stop when found
				}
				idx++;
			}
		}

		
		// Index might have been altered; check again if it is really a change:
		if (currentIndex == index) {
			return false;
		}

		// First remove the layer from the list:
		layers.remove(layer);
		// Change the order:
		layers.add(index, layer);

		if (layerInfo != null && newIndexMapInfo >= 0) {
			mapInfo.getLayers().remove(layerInfo); // remove from mapInfo.layers
			mapInfo.getLayers().add(newIndexMapInfo, layerInfo); // put back (changed order)
		}

		// Send out the correct event:
		eventBus.fireEvent(new LayerOrderChangedEvent(currentIndex, index));
		return true;
	}

	public boolean moveLayerUp(Layer layer) {
		return moveLayer(layer, getLayerPosition(layer) + 1);
	}

	public boolean moveLayerDown(Layer layer) {
		return moveLayer(layer, getLayerPosition(layer) - 1);
	}

	/**
	 * Get the position of a certain layer in this map model.
	 * 
	 * @param layer
	 *            The layer to return the position for.
	 * @return Returns the position of the layer in the map. This position determines layer order. If the layer was not
	 *         found, than -1 is returned.
	 */
	public int getLayerPosition(Layer layer) {
		if (layer == null) {
			throw new IllegalArgumentException("Null value passed to the getLayerPosition method.");
		}
		for (int i = 0; i < layers.size(); i++) {
			if (layer.getId().equals(layers.get(i).getId())) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * Return the layer at a certain index. If the index can't be found, null is returned.
	 * 
	 * @param index
	 *            The specified index.
	 * @return Returns the layer, or null if the index can't be found.
	 */
	public Layer getLayer(int index) {
		return layers.get(index);
	}

	/**
	 * Return the currently selected layer within this map model.
	 * 
	 * @return Returns the selected layer, or null if no layer is selected.
	 */
	public Layer getSelectedLayer() {
		if (layers != null) {
			for (Layer layer : layers) {
				if (layer.isSelected()) {
					return layer;
				}
			}
		}
		return null;
	}
}
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
package org.geomajas.plugin.rasterizing.layer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.geomajas.configuration.client.ClientLayerInfo;
import org.geomajas.configuration.client.ClientRasterLayerInfo;
import org.geomajas.global.GeomajasException;
import org.geomajas.layer.RasterLayer;
import org.geomajas.layer.RasterLayerService;
import org.geomajas.layer.tile.RasterTile;
import org.geomajas.plugin.rasterizing.api.LayerFactory;
import org.geomajas.plugin.rasterizing.command.dto.RasterLayerRasterizingInfo;
import org.geomajas.service.ConfigurationService;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.Layer;
import org.geotools.map.MapContext;
import org.geotools.map.MapViewport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * This factory creates a GeoTools layer that is capable of rendering raster layers.
 * 
 * @author Jan De Moerloose
 */
@Component
public class RasterLayerFactory implements LayerFactory {

	@Autowired
	private RasterLayerService rasterLayerService;

	@Autowired
	private ConfigurationService configurationService;

	public boolean canCreateLayer(MapContext mapContext, ClientLayerInfo clientLayerInfo) {
		return clientLayerInfo instanceof ClientRasterLayerInfo;
	}

	public Layer createLayer(MapContext mapContext, ClientLayerInfo clientLayerInfo) throws GeomajasException {
		if (!(clientLayerInfo instanceof ClientRasterLayerInfo)) {
			throw new IllegalArgumentException(
					"RasterLayerFactory.createLayer() should only be called using ClientRasterLayerInfo");
		}
		ClientRasterLayerInfo rasterInfo = (ClientRasterLayerInfo) clientLayerInfo;
		RasterLayerRasterizingInfo extraInfo = (RasterLayerRasterizingInfo) rasterInfo
				.getWidgetInfo(RasterLayerRasterizingInfo.WIDGET_KEY);
		ReferencedEnvelope areaOfInterest = mapContext.getAreaOfInterest();
		RasterLayer layer = configurationService.getRasterLayer(clientLayerInfo.getServerLayerId());
		MapViewport port = mapContext.getViewport();
		double rasterScale = port.getScreenArea().getWidth() / port.getBounds().getWidth();
		List<RasterTile> tiles = rasterLayerService.getTiles(clientLayerInfo.getServerLayerId(),
				areaOfInterest.getCoordinateReferenceSystem(), areaOfInterest, rasterScale);
		RasterDirectLayer rasterLayer = new RasterDirectLayer(tiles, layer.getLayerInfo().getTileWidth(), layer
				.getLayerInfo().getTileHeight(), extraInfo.getCssStyle());
		rasterLayer.setTitle(clientLayerInfo.getLabel());
		rasterLayer.getUserData().put(USERDATA_KEY_LAYER_ID, layer.getId());
		rasterLayer.getUserData().put(USERDATA_KEY_SHOWING, extraInfo.isShowing());
		return rasterLayer;
	}

	public Map<String, Object> getLayerUserData(MapContext mapContext, ClientLayerInfo clientLayerInfo) {
		Map<String, Object> userData = new HashMap<String, Object>();
		RasterLayerRasterizingInfo extraInfo = (RasterLayerRasterizingInfo) clientLayerInfo
				.getWidgetInfo(RasterLayerRasterizingInfo.WIDGET_KEY);
		userData.put(USERDATA_KEY_SHOWING, extraInfo.isShowing());
		return userData;
	}

}

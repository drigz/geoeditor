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

import org.geomajas.configuration.client.ClientVectorLayerInfo;

/**
 * Default layer for {@link ClientVectorLayerInfo}.
 * 
 * @author Jan De Moerloose
 * 
 */
public interface VectorServerLayer extends ServerLayer<ClientVectorLayerInfo>, LabelsSupported,
		FeaturesSupported, StyleSupported {
}
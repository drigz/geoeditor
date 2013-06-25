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

package org.geomajas.layer.common.proxy;

import java.io.IOException;
import java.io.InputStream;

/**
 * Service which handles the (secured) HTTP communication for TMS requests.
 *
 * @author Joachim Van der Auwera
 * @since 1.0.0
 */
public interface LayerHttpService {

	/**
	 * Add credentials in the TMS URL in case it needs to be provided there.
	 *
	 * @param url URL without credentials
	 * @param authentication authentication object for the request
	 * @return URL with credentials
	 */
	String addCredentialsToUrl(String url, LayerAuthentication authentication);

	/**
	 * Get the contents from the request URL.
	 *
	 * @param url URL to get the response from
	 * @param authentication authentication object for the request
	 * @param layerId used to find interceptors
	 * @return {@link InputStream} with the content
	 * @throws IOException cannot get content
	 */
	InputStream getStream(String url, LayerAuthentication authentication, String layerId) throws IOException;

}

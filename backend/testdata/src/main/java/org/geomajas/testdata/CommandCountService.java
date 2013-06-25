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

package org.geomajas.testdata;

/**
 * Service which allows testing the number of commands sent to the server.
 *
 * @author Joachim Van der Auwera
 */
public interface CommandCountService {

	/**
	 * Get the current value of the command count.
	 *
	 * @return current command count value
	 */
	long getCount();

}

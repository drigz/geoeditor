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

package com.my.program.command.dto;

import org.geomajas.annotation.Api;
import org.geomajas.command.CommandRequest;

/**
 * Sample command request class.
 *
 * @author Joachim Van der Auwera
 * @since 1.0.0
 */
@Api(allMethods = true)
public class MySuperDoItRequest implements CommandRequest {

	public static final String COMMAND = "command.MySuperDoIt";

}

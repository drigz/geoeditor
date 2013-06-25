/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2013 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the Apache
 * License, Version 2.0. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.sld;

import java.io.Serializable;
import org.geomajas.annotation.Api;

/**
 * Schema fragment(s) for this class:...
 * 
 * <pre>
 * &lt;xs:element
 * xmlns:ns="http://www.opengis.net/sld"
 * xmlns:xs="http://www.w3.org/2001/XMLSchema" type="xs:string" name="WellKnownName"/>
 * </pre>
 * 
 * @author Jan De Moerloose
 * @since 1.0.0
 */

@Api(allMethods = true)
public class WellKnownNameInfo implements Serializable {

	private static final long serialVersionUID = 100;

	private String wellKnownName;

	/**
	 * Get the 'WellKnownName' element value.
	 * 
	 * @return value
	 */
	public String getWellKnownName() {
		return wellKnownName;
	}

	/**
	 * Set the 'WellKnownName' element value.
	 * 
	 * @param wellKnownName
	 */
	public void setWellKnownName(String wellKnownName) {
		this.wellKnownName = wellKnownName;
	}

	@Override
	@java.lang.SuppressWarnings("all")
	public java.lang.String toString() {
		return "WellKnownNameInfo(wellKnownName=" + this.getWellKnownName() + ")";
	}

	@Override
	@java.lang.SuppressWarnings("all")
	public boolean equals(final java.lang.Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof WellKnownNameInfo)) {
			return false;
		}
		final WellKnownNameInfo other = (WellKnownNameInfo) o;
		if (!other.canEqual((java.lang.Object) this)) {
			return false;
		}
		if (this.getWellKnownName() == null ? other.getWellKnownName() != null : !this.getWellKnownName().equals(
				(java.lang.Object) other.getWellKnownName())) {
			return false;
		}
		return true;
	}

	/**
	 * Is there a chance that the object are equal? Verifies that the other object has a comparable type.
	 *
	 * @param other other object
	 * @return true when other is an instance of this type
	 */
	public boolean canEqual(final java.lang.Object other) {
		return other instanceof WellKnownNameInfo;
	}

	@Override
	@java.lang.SuppressWarnings("all")
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = result * prime + (this.getWellKnownName() == null ? 0 : this.getWellKnownName().hashCode());
		return result;
	}
}
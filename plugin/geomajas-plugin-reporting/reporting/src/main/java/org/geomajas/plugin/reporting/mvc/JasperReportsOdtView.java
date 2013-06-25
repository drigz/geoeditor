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
package org.geomajas.plugin.reporting.mvc;

import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.export.oasis.JROdtExporter;

import org.geomajas.annotation.Api;
import org.springframework.web.servlet.view.jasperreports.AbstractJasperReportsSingleFormatView;

/**
 * Implementation of {@link AbstractJasperReportsSingleFormatView} that renders report results in ODT format
 * (OpenOffice Writer).
 * 
 * @author Jan De Moerloose
 * @since 1.0.0
 */
@Api // @todo allow template itself to be determined at runtime?
public class JasperReportsOdtView extends AbstractJasperReportsSingleFormatView {

	public JasperReportsOdtView() {
		setContentType("application/odt");
	}

	@Override
	protected JRExporter createExporter() {
		return new JROdtExporter();
	}

	@Override
	protected boolean useWriter() {
		return false;
	}

}

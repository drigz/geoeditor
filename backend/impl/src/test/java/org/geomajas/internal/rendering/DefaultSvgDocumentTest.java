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

package org.geomajas.internal.rendering;

import com.vividsolutions.jts.geom.Coordinate;
import org.geomajas.rendering.GraphicsDocument;
import org.junit.Assert;
import org.junit.Test;

import java.io.StringWriter;
import java.text.DecimalFormat;

/**
 * Test for {@link DefaultSvgDocument}.
 *
 * @author Joachim Van der Auwera
 */
public class DefaultSvgDocumentTest {

	@Test
	public void testSimple() throws Exception {
		StringWriter writer = new StringWriter();
		GraphicsDocument document = new DefaultSvgDocument(writer, false);
		document.writeElement("g", true);
		document.writeId("featureId");
		document.writeElement("g", true);
		document.writeAttribute("style", "my-super-style:true;");
		document.writeId("styleId");
		document.closeElement();
		document.closeElement();
		Assert.assertEquals("<g id=\"featureId\"><g style=\"my-super-style:true;\" id=\"featureId.styleId\"/></g>", writer.getBuffer().toString());
	}

	@Test
	public void testSimpleSvg() throws Exception {
		StringWriter writer = new StringWriter();
		GraphicsDocument document = new DefaultSvgDocument(writer);
		document.writeElement("g", true);
		document.writeId("featureId");
		document.writeElement("g", true);
		document.writeAttribute("style", "my-super-style:true;");
		document.writeId("styleId");
		document.closeElement();
		document.closeElement();
		Assert.assertEquals("<svg xmlns=\"http://www.w3.org/2000/svg\" xmlns:hlink=\"http://www.w3.org/1999/xlink\">" +
				"<g id=\"featureId\"><g style=\"my-super-style:true;\" id=\"featureId.styleId\"/></g>", writer.getBuffer().toString());
	}

	@Test
	public void testSimpleFlush() throws Exception {
		StringWriter writer = new StringWriter();
		GraphicsDocument document = new DefaultSvgDocument(writer, false);
		document.writeElement("g", true);
		document.writeId("featureId");
		document.writeElement("g", true);
		document.writeAttribute("style", "my-super-style:true;");
		document.writeId("styleId");
		Assert.assertEquals("featureId.styleId", document.getCurrentId());
		document.flush();
		Assert.assertEquals("<g id=\"featureId\"><g style=\"my-super-style:true;\" id=\"featureId.styleId\"/></g>", writer.getBuffer().toString());
	}

	@Test
	public void testIds() throws Exception {
		StringWriter writer = new StringWriter();
		GraphicsDocument document = new DefaultSvgDocument(writer, false);
		document.setRootId("bla");
		document.writeElement("g", true);
		document.writeId("featureId");
		Assert.assertEquals("bla.featureId", document.getCurrentId());
		document.writeElement("g", true);
		document.writeAttribute("style", "my-super-style:true;");
		document.writeId("styleId");
		Assert.assertEquals("bla.featureId.styleId", document.getCurrentId());
		document.closeElement();
		document.closeElement();
		Assert.assertEquals("<g id=\"bla.featureId\"><g style=\"my-super-style:true;\" id=\"bla.featureId.styleId\"/></g>", writer.getBuffer().toString());
	}

	@Test
	public void testFormatter() throws Exception {
		StringWriter writer = new StringWriter();
		GraphicsDocument document = new DefaultSvgDocument(writer, false);
		DecimalFormat formatter = document.getFormatter();
		Assert.assertEquals("1.234", formatter.format(1.234));
		Assert.assertEquals("1.23457", formatter.format(1.23456789));
		Assert.assertEquals("9.876", formatter.format(9.876));
		Assert.assertEquals("9.87654", formatter.format(9.87654321));
		document.setMinimumFractionDigits(4);
		document.setMaximumFractionDigits(6);
		Assert.assertEquals("1.2340", formatter.format(1.234));
		Assert.assertEquals("1.234568", formatter.format(1.23456789));
		Assert.assertEquals("9.8760", formatter.format(9.876));
		Assert.assertEquals("9.876543", formatter.format(9.87654321));
	}

	@Test
	public void writePathContent() throws Exception {
		StringWriter writer = new StringWriter();
		GraphicsDocument document = new DefaultSvgDocument(writer, false);
		document.writeElement("path", false);
		document.writeAttributeStart("points");
		Coordinate[] coordinates = new Coordinate[2];
		coordinates[0] = new Coordinate(1.23456789, 9.87654321);
		coordinates[1] = new Coordinate(9.876, 1.234);
		document.writePathContent(coordinates);
		document.writeAttributeEnd();
		document.closeElement();
		Assert.assertEquals("<path points=\"M1.23457 9.87654l8.64143 -8.64254 \"/>", writer.getBuffer().toString());
	}

	@Test
	public void escapeAttribute() throws Exception {
		StringWriter writer = new StringWriter();
		GraphicsDocument document = new DefaultSvgDocument(writer, false);
		document.writeElement("x", true);
		document.writeAttribute("bla", "<stop&go>");
		document.closeElement();
		Assert.assertEquals("<x bla=\"&lt;stop&amp;go&gt;\"/>", writer.getBuffer().toString());
	}
}

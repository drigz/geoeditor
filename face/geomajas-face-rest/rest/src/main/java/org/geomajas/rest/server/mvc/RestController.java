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
package org.geomajas.rest.server.mvc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.geomajas.configuration.AttributeInfo;
import org.geomajas.configuration.VectorLayerInfo;
import org.geomajas.global.GeomajasException;
import org.geomajas.layer.LayerException;
import org.geomajas.layer.VectorLayer;
import org.geomajas.layer.VectorLayerService;
import org.geomajas.layer.feature.InternalFeature;
import org.geomajas.rest.server.RestException;
import org.geomajas.service.ConfigurationService;
import org.geomajas.service.FilterService;
import org.geomajas.service.GeoService;
import org.opengis.filter.Filter;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import com.vividsolutions.jts.geom.Envelope;

/**
 * Spring MVC controller that maps a REST request to vectorlayers.
 * 
 * @author Oliver May
 * @author Jan De Moerlose
 */
@Controller("/rest/**")
public class RestController {

	@Autowired
	private VectorLayerService vectorLayerService;

	@Autowired
	private FilterService filterService;

	@Autowired
	private ConfigurationService configurationService;

	@Autowired
	private GeoService geoService;

	static final String VIEW = "rest.server.mvc.GeoJsonView";

	static final String FEATURE_COLLECTION = "FeatureCollection";

	static final String VECTOR_LAYER_INFO = "VectorLayerInfo";

	static final String ATTRIBUTES = "Attrs";

	@RequestMapping(value = "/rest/{layerId}/{featureId}.json", method = RequestMethod.GET)
	public String readOneFeature(@PathVariable String layerId, @PathVariable String featureId,
			@RequestParam(value = "no_geom", required = false) boolean noGeom,
			@RequestParam(value = "attrs", required = false) List<String> attrs, Model model) throws RestException {
		List<InternalFeature> features;
		try {
			features = vectorLayerService.getFeatures(layerId, null, filterService
					.createFidFilter(new String[] { featureId }), null, getIncludes(noGeom));
		} catch (GeomajasException e) {
			throw new RestException(e, RestException.PROBLEM_READING_LAYERSERVICE, layerId);
		}
		if (features.size() != 1) {
			throw new RestException(RestException.FEATURE_NOT_FOUND, featureId, layerId);
		}
		model.addAttribute(FEATURE_COLLECTION, features.get(0));
		model.addAttribute(VECTOR_LAYER_INFO, features.get(0).getLayer().getLayerInfo());
		model.addAttribute(ATTRIBUTES, attrs);
		return VIEW;
	}

	@RequestMapping(value = "/rest/{layerId}", method = RequestMethod.GET)
	public String readFeatures(@PathVariable String layerId,
			@RequestParam(value = "no_geom", required = false) boolean noGeom,
			@RequestParam(value = "attrs", required = false) List<String> attrs,
			@RequestParam(value = "box", required = false) Envelope box,
			@RequestParam(value = "bbox", required = false) Envelope bbox,
			@RequestParam(value = "maxFeatures", required = false) Integer maxFeatures,
			@RequestParam(value = "limit", required = false) Integer limit,
			@RequestParam(value = "offset", required = false) Integer offset,
			@RequestParam(value = "order_by", required = false) String orderBy,
			@RequestParam(value = "dir", required = false) FeatureOrder dir,
			@RequestParam(value = "queryable", required = false) List<String> queryable,
			@RequestParam(value = "epsg", required = false) String epsg, WebRequest request, Model model)
			throws RestException {

		List<Filter> filters = new ArrayList<Filter>();
		filters.add(createBBoxFilter(layerId, box, bbox));
		if (queryable != null) {
			for (String attributeName : queryable) {
				String prefix = attributeName + "_";
				for (Map.Entry<String, String[]> entry : request.getParameterMap().entrySet()) {
					if (entry.getKey().startsWith(prefix)) {
						filters.add(createAttributeFilter(attributeName, entry.getKey().substring(prefix.length()),
								entry.getValue()[0]));
					}
				}
			}
		}
		List<InternalFeature> features;
		try {
			CoordinateReferenceSystem crs = null;
			if (epsg != null) {
				crs = geoService.getCrs2("EPSG:" + epsg);
			}
			features = vectorLayerService.getFeatures(layerId, crs, and(filters), null, getIncludes(noGeom),
					getOffset(offset), getLimit(maxFeatures, limit));
		} catch (Exception e) {
			throw new RestException(e, RestException.PROBLEM_READING_LAYERSERVICE, layerId);
		}
		if (features.size() > 0) {
			VectorLayerInfo info = features.get(0).getLayer().getLayerInfo();
			model.addAttribute(VECTOR_LAYER_INFO, info);
			VectorLayer layer = configurationService.getVectorLayer(layerId);
			if (orderBy != null) {
				Collections.sort(features, createComparator(layer, orderBy, dir));
			}
			model.addAttribute(FEATURE_COLLECTION, features);
			model.addAttribute(ATTRIBUTES, attrs);
		}
		return VIEW;
	}

	public ModelAndView createUpdateFeatures() {
		return new ModelAndView();
	}

	public ModelAndView updateFeature() {
		return new ModelAndView();
	}

	public ModelAndView deleteFeature() {
		return new ModelAndView();
	}

	public ModelAndView countFeatures() {
		return new ModelAndView();
	}

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.setConversionService(new RestParameterConversionService());
	}

	private int getIncludes(Boolean noGeom) {
		int featureIncludes = VectorLayerService.FEATURE_INCLUDE_ALL;
		if (noGeom) {
			featureIncludes = VectorLayerService.FEATURE_INCLUDE_ATTRIBUTES;
		}
		return featureIncludes;
	}

	private int getOffset(Integer offset) {
		if (offset != null && offset > 0) {
			return offset;
		} else {
			return 0;
		}
	}

	private int getLimit(Integer... limit) {
		int result = Integer.MAX_VALUE;
		for (Integer l : limit) {
			if (l != null && l >= 0) {
				result = Math.min(result, l);
			}
		}
		return result;
	}

	private Filter and(List<Filter> filters) {
		if (filters.size() == 0) {
			return null;
		} else {
			Filter result = filters.get(0);
			for (int i = 1; i < filters.size(); i++) {
				result = filterService.createAndFilter(result, filters.get(i));
			}
			return result;
		}
	}

	private Filter createBBoxFilter(String layerId, Envelope... bbox) throws RestException {
		VectorLayer layer = configurationService.getVectorLayer(layerId);
		for (Envelope envelope : bbox) {
			if (envelope != null) {
				try {
					return filterService.createBboxFilter(layer.getLayerInfo().getCrs(), envelope, layer
							.getFeatureModel().getGeometryAttributeName());
				} catch (LayerException e) {
					throw new RestException(e, RestException.PROBLEM_READING_LAYERSERVICE, layerId);
				}
			}
		}
		return filterService.createTrueFilter();
	}

	private Filter createAttributeFilter(String attributeName, String operation, String value) throws RestException {
		if ("eq".equalsIgnoreCase(operation)) {
			return filterService.createCompareFilter(attributeName, "==", value);
		} else if ("ne".equalsIgnoreCase(operation)) {
			return filterService.createCompareFilter(attributeName, "<>", value);
		} else if ("lt".equalsIgnoreCase(operation)) {
			return filterService.createCompareFilter(attributeName, "<", value);
		} else if ("lte".equalsIgnoreCase(operation)) {
			return filterService.createCompareFilter(attributeName, "<=", value);
		} else if ("gt".equalsIgnoreCase(operation)) {
			return filterService.createCompareFilter(attributeName, ">", value);
		} else if ("gte".equalsIgnoreCase(operation)) {
			return filterService.createCompareFilter(attributeName, ">=", value);
		} else if ("like".equalsIgnoreCase(operation)) {
			return filterService.createLikeFilter(attributeName, value);
		} else {
			throw new RestException(RestException.UNSUPPORTED_QUERY_OPERATION, operation);
		}
	}

	private Comparator<? super InternalFeature> createComparator(VectorLayer layer, final String attributeName,
			final FeatureOrder order) throws RestException {
		List<AttributeInfo> attributes = layer.getLayerInfo().getFeatureInfo().getAttributes();
		AttributeInfo info = null;
		for (AttributeInfo attributeInfo : attributes) {
			// be tolerant on casing
			if (attributeInfo.getName().equalsIgnoreCase(attributeName)) {
				info = attributeInfo;
			}
		}
		if (info == null) {
			throw new RestException(RestException.NO_SUCH_ATTRIBUTE, attributeName, layer.getId());
		}
		return new InternalFeatureComparator(order, attributeName);
	}

	/**
	 * Compares features based on a single attribute.
	 * 
	 * @author Jan De Moerloose
	 */
	static class InternalFeatureComparator implements Comparator<InternalFeature> {

		private FeatureOrder order;

		private String attributeName;

		public InternalFeatureComparator(FeatureOrder order, String attributeName) {
			this.order = (order == null ? FeatureOrder.ASC : FeatureOrder.DESC);
			this.attributeName = attributeName;
		}

		public int compare(InternalFeature f1, InternalFeature f2) {
			Object value1 = f1.getAttributes().get(attributeName).getValue();
			Object value2 = f2.getAttributes().get(attributeName).getValue();
			if (value1 == null && value2 == null) {
				return 0;
			}
			if (value1 == null) {
				return 1;
			} else if (value2 == null) {
				return -1;
			} else {
				if (value1 instanceof Comparable && order == FeatureOrder.ASC) {
					return ((Comparable) value1).compareTo(value2);
				} else if (value2 instanceof Comparable && order == FeatureOrder.DESC) {
					return ((Comparable) value2).compareTo(value1);
				} else {
					// can this happen ?
					return 0;
				}
			}
		}
	}

}

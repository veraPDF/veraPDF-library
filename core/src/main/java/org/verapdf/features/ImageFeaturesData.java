package org.verapdf.features;

import java.io.InputStream;
import java.util.*;

/**
 * Features data of an image for feature extractor
 *
 * @author Maksim Bezrukov
 */
public final class ImageFeaturesData extends FeaturesData {

	private final InputStream metadata;
	private final Integer width;
	private final Integer height;
	private final List<Filter> filters;

	@SuppressWarnings("synthetic-access")
	private ImageFeaturesData(InputStream metadata, InputStream stream, Integer width, Integer height, List<Filter> filters) {
		super(stream);
		this.metadata = metadata;
		this.width = width;
		this.height = height;
		if (filters == null) {
			this.filters = null;
		} else {
			this.filters = new ArrayList<>();
			for (Filter fs : filters) {
				this.filters.add(new Filter(fs.name, fs.properties, fs.stream));
			}
		}
	}

	/**
	 * Creates ICCProfileFeaturesData
	 *
	 * @param metadata metadata stream
	 * @param stream   object stream
	 * @param width    parameter Width from the iccprofile dictionary
	 * @param height   parameter Height from the iccprofile dictionary
	 * @param filters  list of FilterStructures elements. The order of them is the same as in pdf file
	 */
	public static ImageFeaturesData newInstance(InputStream metadata, InputStream stream, Integer width, Integer height, List<Filter> filters) {
		if (stream == null) {
			throw new IllegalArgumentException("Image stream can not be null");
		}
		return new ImageFeaturesData(metadata, stream, width, height, filters);
	}

	/**
	 * @return metadata stream
	 */
	public InputStream getMetadata() {
		return this.metadata;
	}

	/**
	 * @return parameter Width from the iccprofile dictionary
	 */
	public Integer getWidth() {
		return this.width;
	}

	/**
	 * @return parameter Height from the iccprofile dictionary
	 */
	public Integer getHeight() {
		return this.height;
	}

	/**
	 * @return list of FilterStructures elements. The order of them is the same as in pdf files
	 */
	public List<Filter> getFilters() {
		return this.filters == null ? null : Collections.unmodifiableList(this.filters);
	}


	/**
	 * Class which represents a filter and it's parameters. For Any filter which has params dictionary,
	 * that dictionary will be present as a map, except of JBIG2Decode. As JBIG2Decode filter has only one entry in it's
	 * params dictionary and this entry's value is a stream, then, if this entry is present, we will have an empty properties
	 * and not null stream.
	 */
	public static class Filter {
		private final String name;
		private final Map<String, String> properties;
		private final InputStream stream;

		Filter(String name, Map<String, String> properties, InputStream stream) {
			this.name = name;
			this.properties = properties == null ? new HashMap<String, String>() : new HashMap<>(properties);
			this.stream = stream;
		}

		/**
		 * Constructs new Filter
		 *
		 * @param name       name of a filter
		 * @param properties map of properties of a filter
		 * @param stream     stream which used in filter as its parameter for JBIG2Decode filter
		 */
		public static Filter newInstance(String name, Map<String, String> properties, InputStream stream) {
			if (name == null) {
				throw new IllegalArgumentException("Name of a filter can not be null");
			}
			if (properties == null) {
				throw new IllegalArgumentException("Properties can not be null");
			}
			return new Filter(name, properties, stream);
		}

		/**
		 * @return name of a filter
		 */
		public String getName() {
			return this.name;
		}

		/**
		 * @return map of properties of a filter
		 */
		public Map<String, String> getProperties() {
			return Collections.unmodifiableMap(this.properties);
		}

		/**
		 * @return stream which used in filter as its parameter for JBIG2Decode filter
		 */
		public InputStream getStream() {
			return this.stream;
		}

	}
}

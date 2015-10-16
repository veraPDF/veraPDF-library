package org.verapdf.features;

import java.util.*;

/**
 * Features data of an image for feature extractor
 *
 * @author Maksim Bezrukov
 */
public final class ImageFeaturesData extends FeaturesData {

	private Integer width;
	private Integer height;
	private List<FilterStructure> filters;


	/**
	 * Creates ICCProfileFeaturesData
	 *
	 * @param metadata byte array represents metadata stream
	 * @param stream   byte array represents object stream
	 * @param width    parameter Width from the iccprofile dictionary
	 * @param height   parameter Height from the iccprofile dictionary
	 * @param filters  list of FilterStructures elements. The order of them is the same as in pdf file
	 */
	public ImageFeaturesData(byte[] metadata, byte[] stream, Integer width, Integer height, List<FilterStructure> filters) {
		super(metadata, stream);
		this.width = width;
		this.height = height;
		if (filters == null) {
			this.filters = null;
		} else {
			this.filters = new ArrayList<>();
			for (FilterStructure fs : filters) {
				this.filters.add(new FilterStructure(fs.name, fs.properties, fs.stream));
			}
		}
	}

	/**
	 * @return parameter Width from the iccprofile dictionary
	 */
	public Integer getWidth() {
		return width;
	}

	/**
	 * @return parameter Height from the iccprofile dictionary
	 */
	public Integer getHeight() {
		return height;
	}

	/**
	 * @return list of FilterStructures elements. The order of them is the same as in pdf files
	 */
	public List<FilterStructure> getFilters() {
		return filters == null ? null : Collections.unmodifiableList(filters);
	}


	/**
	 * Class which represents a filter and it's parameters. For Any filter which has params dictionary,
	 * that dictionary will be present as a map, except of JBIG2Decode. As JBIG2Decode filter has only one entry in it's
	 * params dictionary and this entry's value is a stream, then, if this entry is present, we will have an empty properties
	 * and not null stream.
	 */
	public static class FilterStructure {
		private String name;
		private Map<String, String> properties;
		private byte[] stream;

		/**
		 * Constructs new FilterStructure
		 *
		 * @param name       name of a filter
		 * @param properties map of properties of a filter
		 * @param stream     stream which used in filter as its parameter for JBIG2Decode filter
		 */
		public FilterStructure(String name, Map<String, String> properties, byte[] stream) {
			if (name == null) {
				throw new IllegalArgumentException("Name of a filter can not be null");
			}
			if (properties == null) {
				throw new IllegalArgumentException("Properties can not be null");
			}
			this.name = name;
			this.properties = new HashMap<>(properties);
			this.stream = Arrays.copyOf(stream, stream.length);
		}

		/**
		 * @return name of a filter
		 */
		public String getName() {
			return name;
		}

		/**
		 * @return map of properties of a filter
		 */
		public Map<String, String> getProperties() {
			return properties == null ? null : Collections.unmodifiableMap(properties);
		}

		/**
		 * @return stream which used in filter as its parameter for JBIG2Decode filter
		 */
		public byte[] getStream() {
			return Arrays.copyOf(stream, stream.length);
		}

	}
}

package org.verapdf.features;

import java.util.*;

/**
 * Features data of an image for feature extractor
 *
 * @author Maksim Bezrukov
 */
public final class ImageFeaturesData extends FeaturesData {

	private final byte[] metadata;
	private final Integer width;
	private final Integer height;
	private final List<FilterStructure> filters;

	private ImageFeaturesData(byte[] metadata, byte[] stream, Integer width, Integer height, List<FilterStructure> filters) {
		super(stream);
		this.metadata = metadata == null ? null : Arrays.copyOf(metadata, metadata.length);
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
	 * Creates ICCProfileFeaturesData
	 *
	 * @param metadata byte array represents metadata stream
	 * @param stream   byte array represents object stream
	 * @param width    parameter Width from the iccprofile dictionary
	 * @param height   parameter Height from the iccprofile dictionary
	 * @param filters  list of FilterStructures elements. The order of them is the same as in pdf file
	 */
	public static ImageFeaturesData newInstance(byte[] metadata, byte[] stream, Integer width, Integer height, List<FilterStructure> filters) {
		if (stream == null) {
			throw new IllegalArgumentException("Image stream can not be null");
		}
		return new ImageFeaturesData(metadata, stream, width, height, filters);
	}

	/**
	 * @return byte array represent metadata stream
	 */
	public byte[] getMetadata() {
		return metadata == null ? null : Arrays.copyOf(metadata, metadata.length);
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
		private final String name;
		private final Map<String, String> properties;
		private final byte[] stream;

		private FilterStructure(String name, Map<String, String> properties, byte[] stream) {
			this.name = name;
			this.properties = properties == null ? new HashMap<String, String>() : new HashMap<>(properties);
			this.stream = Arrays.copyOf(stream, stream.length);
		}

		/**
		 * Constructs new FilterStructure
		 *
		 * @param name       name of a filter
		 * @param properties map of properties of a filter
		 * @param stream     stream which used in filter as its parameter for JBIG2Decode filter
		 */
		public static FilterStructure newInstance(String name, Map<String, String> properties, byte[] stream) {
			if (name == null) {
				throw new IllegalArgumentException("Name of a filter can not be null");
			}
			if (properties == null) {
				throw new IllegalArgumentException("Properties can not be null");
			}
			return new FilterStructure(name, properties, stream);
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
			return Collections.unmodifiableMap(properties);
		}

		/**
		 * @return stream which used in filter as its parameter for JBIG2Decode filter
		 */
		public byte[] getStream() {
			return Arrays.copyOf(stream, stream.length);
		}

	}
}

package org.verapdf.features;

import java.util.Calendar;

/**
 * Features data of an embedded file for feature extractor
 *
 * @author Maksim Bezrukov
 */
public class EmbeddedFileFeaturesData extends FeaturesData {
	private final String name;
	private final String description;
	private final String subtype;
	private final Calendar creationDate;
	private final Calendar modDate;
	private final String checkSum;
	private final Integer size;

	private EmbeddedFileFeaturesData(byte[] stream, Integer size, String checkSum, Calendar modDate, Calendar creationDate, String subtype, String description, String name) {
		super(stream);
		this.size = size;
		this.checkSum = checkSum;
		this.modDate = modDate == null ? null : (Calendar) modDate.clone();
		this.creationDate = creationDate == null ? null : (Calendar) creationDate.clone();
		this.subtype = subtype;
		this.description = description;
		this.name = name;
	}

	/**
	 * @return name of a file
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return description of a file
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @return subtype of a file
	 */
	public String getSubtype() {
		return subtype;
	}

	/**
	 * @return creation date of a file
	 */
	public Calendar getCreationDate() {
		return (Calendar) creationDate.clone();
	}

	/**
	 * @return modification date of a file
	 */
	public Calendar getModDate() {
		return (Calendar) modDate.clone();
	}

	/**
	 * @return checksum of a file
	 */
	public String getCheckSum() {
		return checkSum;
	}

	/**
	 * @return size of a file
	 */
	public Integer getSize() {
		return size;
	}

	public static final class Builder {

		private byte[] stream = null;
		private String name = null;
		private String description = null;
		private String subtype = null;
		private Calendar creationDate = null;
		private Calendar modDate = null;
		private String checkSum = null;
		private Integer size = null;

		public Builder(byte[] stream) {
			this.stream = stream;
		}

		public EmbeddedFileFeaturesData build() {
			if (this.stream == null) {
				throw new IllegalArgumentException("Embedded file stream can not be null");
			}
			return new EmbeddedFileFeaturesData(stream, size, checkSum, modDate, creationDate, subtype, description, name);
		}

		public Builder stream(byte[] stream) {
			this.stream = stream;
			return this;
		}

		public Builder name(String name) {
			this.name = name;
			return this;
		}

		public Builder description(String description) {
			this.description = description;
			return this;
		}

		public Builder subtype(String subtype) {
			this.subtype = subtype;
			return this;
		}

		public Builder creationDate(Calendar creationDate) {
			this.creationDate = creationDate;
			return this;
		}

		public Builder modDate(Calendar modDate) {
			this.modDate = modDate;
			return this;
		}

		public Builder checkSum(String checkSum) {
			this.checkSum = checkSum;
			return this;
		}

		public Builder size(Integer size) {
			this.size = size;
			return this;
		}
	}
}

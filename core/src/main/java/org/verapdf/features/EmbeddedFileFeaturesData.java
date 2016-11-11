package org.verapdf.features;

import java.io.InputStream;
import java.util.Calendar;

/**
 * Features data of an embedded file for feature extractor
 *
 * @author Maksim Bezrukov
 */
public class EmbeddedFileFeaturesData extends FeaturesData {
	private final String name;
	private final String description;
	private final String afRelationship;
	private final String subtype;
	private final Calendar creationDate;
	private final Calendar modDate;
	private final String checkSum;
	private final Integer size;

	EmbeddedFileFeaturesData(InputStream stream, Integer size, String checkSum, Calendar modDate, Calendar creationDate, String subtype, String description, String name, String afRelationship) {
		super(stream);
		this.size = size;
		this.checkSum = checkSum;
		this.modDate = modDate == null ? null : (Calendar) modDate.clone();
		this.creationDate = creationDate == null ? null : (Calendar) creationDate.clone();
		this.subtype = subtype;
		this.description = description;
		this.name = name;
		this.afRelationship = afRelationship;
	}

	/**
	 * @return name of a file
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * @return description of a file
	 */
	public String getDescription() {
		return this.description;
	}

	/**
	 * @return subtype of a file
	 */
	public String getSubtype() {
		return this.subtype;
	}

	/**
	 * @return creation date of a file
	 */
	public Calendar getCreationDate() {
		return (Calendar) this.creationDate.clone();
	}

	/**
	 * @return modification date of a file
	 */
	public Calendar getModDate() {
		return (Calendar) this.modDate.clone();
	}

	/**
	 * @return checksum of a file
	 */
	public String getCheckSum() {
		return this.checkSum;
	}

	/**
	 * @return size of a file
	 */
	public Integer getSize() {
		return this.size;
	}

	/**
	 * @return AFRelationship value
	 */
	public String getAFRelationship() {
		return this.afRelationship;
	}

	public static final class Builder {

		private InputStream stream = null;
		private String name = null;
		private String description = null;
		private String afRelationship;
		private String subtype = null;
		private Calendar creationDate = null;
		private Calendar modDate = null;
		private String checkSum = null;
		private Integer size = null;

		public Builder(InputStream stream) {
			this.stream = stream;
		}

		public EmbeddedFileFeaturesData build() {
			if (this.stream == null) {
				throw new IllegalArgumentException("Embedded file stream can not be null");
			}
			return new EmbeddedFileFeaturesData(this.stream, this.size, this.checkSum, this.modDate, this.creationDate, this.subtype, this.description, this.name, this.afRelationship);
		}

		public Builder stream(InputStream streamBuild) {
			this.stream = streamBuild;
			return this;
		}

		public Builder name(String nameBuild) {
			this.name = nameBuild;
			return this;
		}

		public Builder description(String descriptionBuild) {
			this.description = descriptionBuild;
			return this;
		}

		public Builder afRelationship(String afRelationshipBuild) {
			this.afRelationship = afRelationshipBuild;
			return this;
		}

		public Builder subtype(String subtypeBuild) {
			this.subtype = subtypeBuild;
			return this;
		}

		public Builder creationDate(Calendar creationDateBuild) {
			this.creationDate = creationDateBuild;
			return this;
		}

		public Builder modDate(Calendar modDateBuild) {
			this.modDate = modDateBuild;
			return this;
		}

		public Builder checkSum(String checkSumBuild) {
			this.checkSum = checkSumBuild;
			return this;
		}

		public Builder size(Integer sizeBuild) {
			this.size = sizeBuild;
			return this;
		}
	}
}

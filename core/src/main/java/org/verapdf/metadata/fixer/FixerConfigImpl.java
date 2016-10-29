package org.verapdf.metadata.fixer;

import java.nio.file.FileSystems;
import java.nio.file.Path;

import javax.xml.bind.annotation.XmlAttribute;

/**
 * @author Evgeniy Muravitskiy
 */
class FixerConfigImpl implements MetadataFixerConfig {
	private final static MetadataFixerConfig defaultInstance = new FixerConfigImpl();
	@XmlAttribute
	private final boolean fixId;
	@XmlAttribute
	private final String fixesPrefix;
	@XmlAttribute
	private final Path fixesFolder;
	
	private FixerConfigImpl() {
		this("veraFixMd_", FileSystems.getDefault().getPath(""), true);
	}

	private FixerConfigImpl(final String fixesPrefix, final Path fixesFolder, boolean fixId) {
		super();
		this.fixId = fixId;
		this.fixesPrefix = fixesPrefix;
		this.fixesFolder = fixesFolder;
	}

	/**
	 * @return the fixId
	 */
	@Override
	public boolean isFixId() {
		return this.fixId;
	}

	/**
	 * @return the fixesPrefix
	 */
	@Override
	public String getFixesPrefix() {
		return this.fixesPrefix;
	}

	/**
	 * @return the fixesFolder
	 */
	@Override
	public Path getFixesFolder() {
		return this.fixesFolder;
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (this.fixId ? 1231 : 1237);
		result = prime * result + ((this.fixesFolder == null) ? 0 : this.fixesFolder.hashCode());
		result = prime * result + ((this.fixesPrefix == null) ? 0 : this.fixesPrefix.hashCode());
		return result;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof FixerConfigImpl)) {
			return false;
		}
		FixerConfigImpl other = (FixerConfigImpl) obj;
		if (this.fixId != other.fixId) {
			return false;
		}
		if (this.fixesFolder == null) {
			if (other.fixesFolder != null) {
				return false;
			}
		} else if (!this.fixesFolder.equals(other.fixesFolder)) {
			return false;
		}
		if (this.fixesPrefix == null) {
			if (other.fixesPrefix != null) {
				return false;
			}
		} else if (!this.fixesPrefix.equals(other.fixesPrefix)) {
			return false;
		}
		return true;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "FixerConfigImpl [fixId=" + this.fixId + ", fixesPrefix=" + this.fixesPrefix + ", fixesFolder="
				+ this.fixesFolder + "]";
	}

	static MetadataFixerConfig defaultInstance() {
		return defaultInstance;
	}
	
	static MetadataFixerConfig fromValues(final String fixesPrefix, final Path fixesFolder, boolean fixId) {
		return new FixerConfigImpl(fixesPrefix, fixesFolder, fixId);
	}
}

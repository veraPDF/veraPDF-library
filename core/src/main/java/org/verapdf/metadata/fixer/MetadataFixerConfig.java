package org.verapdf.metadata.fixer;

import java.nio.file.Path;

/**
 * @author Evgeniy Muravitskiy
 */
public interface MetadataFixerConfig {
	public boolean isFixId();
	public String getFixesPrefix();
	public Path getFixesFolder();
}

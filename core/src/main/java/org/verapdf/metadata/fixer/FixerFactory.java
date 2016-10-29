/**
 * 
 */
package org.verapdf.metadata.fixer;

import java.nio.file.Path;

/**
 * @author  <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *          <a href="https://github.com/carlwilson">carlwilson AT github</a>
 *
 * @version 0.1
 * 
 * Created 30 Oct 2016:01:30:05
 */

public final class FixerFactory {
	private FixerFactory() {
		
	}
	
	public static MetadataFixerConfig defaultConfig() {
		return FixerConfigImpl.defaultInstance();
	}
	
	public static MetadataFixerConfig fromValues(final String fixesPrefix, final Path fixesFolder, boolean fixId) {
		return FixerConfigImpl.fromValues(fixesPrefix, fixesFolder, fixId);
	}
}

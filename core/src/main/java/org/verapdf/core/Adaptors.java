/**
 * 
 */
package org.verapdf.core;

import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * @author  <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *          <a href="https://github.com/carlwilson">carlwilson AT github</a>
 *
 * @version 0.1
 * 
 * Created 31 Oct 2016:17:12:58
 */

public final class Adaptors {
	private Adaptors() {
		
	}

	public static class PathAdapter extends XmlAdapter<String, Path> {

		@Override
		public Path unmarshal(String v) throws Exception {
			Path path = Paths.get(new URI(v));
			return path.toAbsolutePath();
		}

		@Override
		public String marshal(Path v) {
			return v.toAbsolutePath().toUri().toString();
		}
	}
}

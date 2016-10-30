package org.verapdf.metadata.fixer;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * @author Evgeniy Muravitskiy
 */
@XmlJavaTypeAdapter(FixerConfigImpl.Adapter.class)
public interface MetadataFixerConfig {
	public boolean isFixId();
	public String getFixesPrefix();
}

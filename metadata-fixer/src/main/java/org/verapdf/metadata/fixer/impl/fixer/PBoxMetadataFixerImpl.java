package org.verapdf.metadata.fixer.impl.fixer;

import org.verapdf.metadata.fixer.impl.MetadataFixerImpl;
import org.verapdf.metadata.fixer.impl.pb.FixerConfigImpl;
import org.verapdf.metadata.fixer.utils.FixerConfig;
import org.verapdf.pdfa.results.MetadataFixerResult;
import org.verapdf.pdfa.results.ValidationResult;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBoxMetadataFixerImpl extends MetadataFixerImpl {

	PBoxMetadataFixerImpl() {

	}

	@Override
	public MetadataFixerResult fixMetadata(InputStream toFix,
										   OutputStream outputRepaired,
										   ValidationResult result) {
		FixerConfig config = FixerConfigImpl.getFixerConfig(toFix, result);
		return super.fixMetadata(outputRepaired, config);
	}

}

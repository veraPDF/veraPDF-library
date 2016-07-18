/**
 *
 */
package org.verapdf.pdfa;

import org.verapdf.core.ModelParsingException;
import org.verapdf.features.FeaturesExtractor;
import org.verapdf.features.config.FeaturesConfig;
import org.verapdf.features.tools.FeaturesCollection;
import org.verapdf.model.coslayer.CosDocument;
import org.verapdf.pdfa.flavours.PDFAFlavour;

import java.util.List;

/**
 * Simple interface that needs a little more work. This abstracts the parsing of
 * the veraPDF ValidationModel allowing the implementation and run-time
 * selection of different model parsers possible.
 *
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 */
public interface PDFParser {
	/**
	 * @return the {@link CosDocument} element that is the root object of the
	 * validation model instance.
	 * @throws ModelParsingException when there's a problem establishing the model
	 *                     root.
	 */
	public org.verapdf.model.baselayer.Object getRoot() throws ModelParsingException;

	/**
	 * @return flavour for which model has been parsed
	 */
	public PDFAFlavour getFlavour();

	/**
	 * @return features collection of the document
	 */
	public FeaturesCollection getFeatures(FeaturesConfig config);

	/**
	 * @param extractors extractors for features reporting
	 * @return features collection of the document
	 */
	public FeaturesCollection getFeatures(FeaturesConfig config, List<FeaturesExtractor> extractors);
}

/**
 *
 */
package org.verapdf.pdfa;

import java.io.Closeable;
import java.util.List;

import org.verapdf.core.ModelParsingException;
import org.verapdf.features.FeaturesExtractor;
import org.verapdf.features.config.FeaturesConfig;
import org.verapdf.features.tools.FeaturesCollection;
import org.verapdf.metadata.fixer.entity.PDFDocument;
import org.verapdf.model.baselayer.Object;
import org.verapdf.pdfa.flavours.PDFAFlavour;

/**
 * Simple interface that needs a little more work. This abstracts the parsing of
 * the veraPDF ValidationModel allowing the implementation and run-time
 * selection of different model parsers possible.
 *
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 */
public interface PDFParser extends Closeable {
	/**
	 * @return the {@link CosDocument} element that is the root object of the
	 *         validation model instance.
	 * @throws ModelParsingException
	 *             when there's a problem establishing the model root.
	 */
	public Object getRoot() throws ModelParsingException;

	/**
	 * @return flavour for which model has been parsed
	 */
	public PDFAFlavour getFlavour();

	/**
	 * @return the {@link org.verapdf.metadata.fixer.entity.PDFDocument} parsed.
	 */
	public PDFDocument getPDFDocument();

	/**
	 * @return features collection of the document
	 */
	public FeaturesCollection getFeatures(FeaturesConfig config);

	/**
	 * @param extractors
	 *            extractors for features reporting
	 * @return features collection of the document
	 */
	public FeaturesCollection getFeatures(FeaturesConfig config, List<FeaturesExtractor> extractors);
}

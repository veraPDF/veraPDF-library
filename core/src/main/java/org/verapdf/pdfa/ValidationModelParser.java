/**
 *
 */
package org.verapdf.pdfa;

import org.verapdf.core.ModelParsingException;
import org.verapdf.model.coslayer.CosDocument;
import org.verapdf.pdfa.flavours.PDFAFlavour;

/**
 * Simple interface that needs a little more work. This abstracts the parsing of
 * the veraPDF ValidationModel allowing the implementation and run-time
 * selection of different model parsers possible.
 *
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 */
public interface ValidationModelParser {
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
}

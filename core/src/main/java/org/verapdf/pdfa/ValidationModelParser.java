/**
 *
 */
package org.verapdf.pdfa;

import org.verapdf.model.coslayer.CosDocument;

import java.io.IOException;

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
	 * @throws IOException when there's a problem parsing or establishing the model
	 *                     root.
	 */
	public org.verapdf.model.baselayer.Object getRoot() throws IOException;
}

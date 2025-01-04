/**
 * This file is part of veraPDF Library core, a module of the veraPDF project.
 * Copyright (c) 2015-2025, veraPDF Consortium <info@verapdf.org>
 * All rights reserved.
 *
 * veraPDF Library core is free software: you can redistribute it and/or modify
 * it under the terms of either:
 *
 * The GNU General public license GPLv3+.
 * You should have received a copy of the GNU General Public License
 * along with veraPDF Library core as the LICENSE.GPL file in the root of the source
 * tree.  If not, see http://www.gnu.org/licenses/ or
 * https://www.gnu.org/licenses/gpl-3.0.en.html.
 *
 * The Mozilla Public License MPLv2+.
 * You should have received a copy of the Mozilla Public License along with
 * veraPDF Library core as the LICENSE.MPL file in the root of the source tree.
 * If a copy of the MPL was not distributed with this file, you can obtain one at
 * http://mozilla.org/MPL/2.0/.
 */
package org.verapdf.pdfa;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.verapdf.component.Component;
import org.verapdf.pdfa.results.MetadataFixerResult;
import org.verapdf.pdfa.results.ValidationResult;

/**
 * Simple interface for PDF/A metadata repair.
 * 
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 */
public interface MetadataFixer extends Component {

	/**
	 * @param toFix
	 *            an {@link InputStream} from which the PDF/A data to repair can
	 *            be read.
	 * @param outputRepaired
	 *            an {@link OutputStream} to which the Fixer instance should
	 *            write the repaired PDF/A data.
	 * @param result
	 *            a {@link ValidationResult} instance for the PDF/A to be
	 *            repaired, the toFix InputStream.
	 * @return a {@link MetadataFixerResult} that holds the repair status and
	 *         records any fixes applied.
	 */
	public MetadataFixerResult fixMetadata(InputStream toFix, OutputStream outputRepaired, ValidationResult result)
			throws IOException;

	/**
	 * @param parser
	 *            a veraPDF {@link PDFAParser} instance that has parsed the
	 *            PDF/A to repair.
	 * @param outputRepaired
	 *            an {@link OutputStream} to which the Fixer instance should
	 *            write the repaired PDF/A data.
	 * @param result
	 *            a {@link ValidationResult} instance for the PDF/A to be
	 *            repaired, the toFix InputStream.
	 * @return a {@link MetadataFixerResult} that holds the repair status and
	 *         records any fixes applied.
	 */
	public MetadataFixerResult fixMetadata(PDFAParser parser, OutputStream outputRepaired, ValidationResult result);
}

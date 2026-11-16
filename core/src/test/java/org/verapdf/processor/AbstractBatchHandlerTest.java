/*
 * This file is part of veraPDF Library core, a module of the veraPDF project.
 * Copyright (c) 2015-2026, veraPDF Consortium <info@verapdf.org>
 * All rights reserved.
 *
 * veraPDF Library core is free software: you can redistribute it and/or modify
 * it under the terms of either:
 *
 * The GNU General public license GPLv3+.
 * You should have received a copy of the GNU General public license
 * along with veraPDF Library core as the LICENSE.GPL file in the source
 * tree. If not, see https://www.gnu.org/licenses/gpl-3.0.en.html.
 *
 * The Mozilla Public License MPLv2+.
 * You should have received a copy of the Mozilla Public License along with
 * veraPDF Library core as the LICENSE.MPL file in the source tree. If not,
 * see http://mozilla.org/MPL/2.0/.
 */
package org.verapdf.processor;

import org.junit.Test;
import org.verapdf.component.Components;
import org.verapdf.core.VeraPDFException;
import org.verapdf.processor.reports.ItemDetails;

import java.io.PrintWriter;
import java.io.StringWriter;

import static org.junit.Assert.assertEquals;

public class AbstractBatchHandlerTest {

	@Test
	public void reportsParserExceptionInsteadOfEncryption() throws VeraPDFException {
		StringWriter output = new StringWriter();
		BatchProcessingHandler handler = SingleLineResultHandler.newInstance(new PrintWriter(output));
		ProcessorResult result = ProcessorResultImpl.veraExceptionResult(ItemDetails.fromValues("repro-prev-cycle.pdf"),
				TaskResultImpl.fromValues(TaskType.PARSE, Components.defaultDuration(), new VeraPDFException("XRef loop")));

		handler.handleResult(result, false);

		assertEquals("repro-prev-cycle.pdf does not appear to be a valid PDF file and could not be parsed.\n",
				output.toString());
	}

	@Test
	public void reportsEncryptedPdfAsEncrypted() throws VeraPDFException {
		StringWriter output = new StringWriter();
		BatchProcessingHandler handler = SingleLineResultHandler.newInstance(new PrintWriter(output));
		ProcessorResult result = ProcessorResultImpl.encryptedResult(ItemDetails.fromValues("encrypted.pdf"),
				TaskResultImpl.fromValues(TaskType.PARSE, Components.defaultDuration(), new VeraPDFException("Encrypted")));

		handler.handleResult(result, false);

		assertEquals("encrypted.pdf appears to be an encrypted PDF file and could not be processed.\n", output.toString());
	}
}

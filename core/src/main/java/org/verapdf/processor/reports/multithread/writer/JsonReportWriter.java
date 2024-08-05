/**
 * This file is part of veraPDF Library core, a module of the veraPDF project.
 * Copyright (c) 2015-2024, veraPDF Consortium <info@verapdf.org>
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
package org.verapdf.processor.reports.multithread.writer;

import org.verapdf.processor.reports.ResultStructure;

import java.io.PrintWriter;
import java.util.logging.Logger;

public class JsonReportWriter extends ReportWriter {

	private static final Logger LOGGER = Logger.getLogger(JsonReportWriter.class.getCanonicalName());

	protected boolean isFirstReport;

	protected JsonReportWriter(PrintWriter outputStreamWriter, PrintWriter errorStreamWriter) {
		super(outputStreamWriter, errorStreamWriter);
	}

	@Override
	public void write(ResultStructure result) {
		if (isFirstReport) {
			isFirstReport = false;
		} else {
			outputStreamWriter.write(",");
		}
		merge(result.getReportFile(), outputStreamWriter);
		deleteTemp(result);
	}

	@Override
	public void startDocument() {
		outputStreamWriter.write("{\"reports\":[");
		isFirstReport = true;
	}

	@Override
	public void endDocument() {
		outputStreamWriter.write("]}");
	}

}

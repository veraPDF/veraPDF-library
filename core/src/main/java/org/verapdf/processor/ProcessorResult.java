/**
 * This file is part of veraPDF Library core, a module of the veraPDF project.
 * Copyright (c) 2015, veraPDF Consortium <info@verapdf.org>
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
package org.verapdf.processor;

import java.util.*;

import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.verapdf.pdfa.results.MetadataFixerResult;
import org.verapdf.pdfa.results.ValidationResult;
import org.verapdf.processor.reports.ItemDetails;
import org.verapdf.report.FeaturesReport;

@XmlJavaTypeAdapter(ProcessorResultImpl.Adapter.class)
public interface ProcessorResult {
	/**
	 * @return the {@link ItemDetails} for the item processed
	 */
	public ItemDetails getProcessedItem();

	/**
	 * @return an {@link EnumMap} of {@link TaskType}s and {@link TaskResult}s
	 *         for the {@link TaskResult}s held in this {@link ProcessorResult}
	 */
	public EnumMap<TaskType, TaskResult> getResults();

	/**
	 * @return an {@link EnumSet} of the {@link TaskType}s carried in this
	 *         result.
	 */
	public EnumSet<TaskType> getTaskTypes();

	/**
	 * @return the {@link Collection} of {@link TaskResult}s for this
	 *         {@link ProcessorResult}
	 */
	public Collection<TaskResult> getResultSet();

	/**
	 * @param taskType
	 *            the {@link TaskType} to retrieve the {@link TaskResult} for
	 * @return the {@link TaskResult} result for taskType
	 */
	public TaskResult getResultForTask(TaskType taskType);

	/**
	 * @return the {@link ValidationResult} or
	 *         {@link org.verapdf.pdfa.results.ValidationResults#defaultResult()}
	 *         if validation not performed.
	 */
	public List<ValidationResult> getValidationResults();

	/**
	 * @return the {@link FeaturesReport}.
	 */
	public FeaturesReport getFeaturesReport();

	/**
	 * @return the {@link MetadataFixerResult}
	 */
	public MetadataFixerResult getFixerResult();

	/**
	 * @return true if the parsed file was a valid PDF, false if the PDF parser
	 *         failed to parse the document and couldn't continue to process it.
	 */
	public boolean isPdf();

	/**
	 * @return true if the parser detected that the PDF was encrypted and could
	 *         not continue to process it.
	 */
	public boolean isEncryptedPdf();

	/**
	 * @return true if the parser detected OutOfMemoryException and could
	 *         not continue to process PDF.
	 */
	public boolean isOutOfMemory();

	/**
	 * @return true if processed PDF has VeraPDFException.
	 */
	public boolean hasException();
}
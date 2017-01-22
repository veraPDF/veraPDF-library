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
/**
 *
 */
package org.verapdf.pdfa;

import java.util.List;

import org.verapdf.component.Component;
import org.verapdf.core.ModelParsingException;
import org.verapdf.features.AbstractFeaturesExtractor;
import org.verapdf.features.FeatureExtractionResult;
import org.verapdf.features.FeatureExtractorConfig;
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
public interface PDFAParser extends Component {
	/**
	 * @return the {@link CosDocument} element that is the root object of the
	 *         validation model instance.
	 * @throws ModelParsingException
	 *             when there's a problem establishing the model root.
	 */
	public Object getRoot() throws ModelParsingException;

	/**
	 * @return {@link PDFAFlavour} for the model that has been parsed
	 */
	public PDFAFlavour getFlavour();

	/**
	 * @return the {@link org.verapdf.metadata.fixer.entity.PDFDocument} parsed.
	 */
	public PDFDocument getPDFDocument();

	/**
	 * @param config
	 *            a {@link FeatureExtractorConfig} that denotes the features to
	 *            be extracted.
	 * @return features collection of the document as a
	 *         {@link FeatureExtractionResult}
	 */
	public FeatureExtractionResult getFeatures(FeatureExtractorConfig config);

	/**
	 * @param config
	 *            a {@link FeatureExtractorConfig} that denotes the features to
	 *            be extracted.
	 * @param extractors
	 *            extractors for features reporting
	 * @return features collection of the document as a
	 *         {@link FeatureExtractionResult}
	 */
	public FeatureExtractionResult getFeatures(FeatureExtractorConfig config,
			List<AbstractFeaturesExtractor> extractors);
}

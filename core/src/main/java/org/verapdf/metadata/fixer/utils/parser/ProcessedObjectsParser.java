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
package org.verapdf.metadata.fixer.utils.parser;

import org.verapdf.metadata.fixer.utils.model.ProcessedObjects;
import org.verapdf.pdfa.flavours.PDFAFlavour;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;

/**
 * @author Evgeniy Muravitskiy
 */
public interface ProcessedObjectsParser {

	ProcessedObjects getProcessedObjects(PDFAFlavour flavour) throws IOException, URISyntaxException, ParserConfigurationException, SAXException;

	ProcessedObjects getProcessedObjects(String path) throws IOException, SAXException, ParserConfigurationException;

	ProcessedObjects getProcessedObjects(InputStream file) throws ParserConfigurationException, IOException, SAXException;

	String getProcessedObjectsPathProperty(PDFAFlavour flavour);

}

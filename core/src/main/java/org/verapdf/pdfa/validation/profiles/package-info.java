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
 * Interfaces and reference implementations for PDF/A validation and validation
 * profiles.
 * <p>
 * These types are intended for use by developers wishing to implement custom
 * validators and validation rules.
 * </p>
 *
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 * @version 0.7
 * @since 0.7
 */
@javax.xml.bind.annotation.XmlSchema(namespace = "http://www.verapdf.org/ValidationProfile", xmlns = {@XmlNs(prefix = "", namespaceURI = "http://www.verapdf.org/ValidationProfile")}, elementFormDefault = XmlNsForm.QUALIFIED)
package org.verapdf.pdfa.validation.profiles;

import javax.xml.bind.annotation.XmlNs;
import javax.xml.bind.annotation.XmlNsForm;

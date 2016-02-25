/**
 * Interfaces and reference implementations encapsulating the results of PDF/A
 * validation.
 * <p>
 * See the static {@link org.verapdf.pdfa.results.ValidationResults} utility
 * class for helper methods that create the different Result types. All of the
 * reference implementations for the types are JAXB annotated. There are helper
 * methods for serialisation and de-serialisation to and from XML.
 * </p>
 *
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 * @version 0.7
 * @since 0.7
 */
@javax.xml.bind.annotation.XmlSchema(namespace = "http://www.verapdf.org/ValidationProfile", xmlns = {@XmlNs(prefix = "", namespaceURI = "http://www.verapdf.org/ValidationProfile")}, elementFormDefault = XmlNsForm.QUALIFIED)
package org.verapdf.report;

import javax.xml.bind.annotation.XmlNs;
import javax.xml.bind.annotation.XmlNsForm;


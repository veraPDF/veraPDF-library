/**
* Interfaces for PDF/A validation and Metadata repair.
* <p>
* Developers wishing to integrate veraPDF PDF/A validation into
* their own Java applications should only need to use:
* <ul>
* <li>{@link org.verapdf.pdfa.ValidatorFactory} to obtain a {@link org.verapdf.pdfa.Validator} instance.</li>
* <li>{@link org.verapdf.pdfa.PDFAValidator} to validate a {@link java.io.InputStream} believed to be a PDF/A document.</li>
* <li>{@link org.verapdf.pdfa.flavours.PDFAFlavour} to determine the flavour of PDF/A enforced by a particular validator instance.</li>
* </ul>
* </p>
*
* @since 0.7
* @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
* @version 0.7
*/
package org.verapdf.pdfa;

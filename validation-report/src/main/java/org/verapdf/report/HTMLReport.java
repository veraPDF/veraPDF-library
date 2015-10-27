package org.verapdf.report;

import javax.xml.bind.JAXBException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.verapdf.pdfa.results.ValidationResult;
import org.verapdf.pdfa.results.ValidationResults;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Generating HTML validation report
 *
 * @author Maksim Bezrukov
 */
public final class HTMLReport {

	private static final String LOGO_NAME = "veraPDF-logo-200.png";

	private HTMLReport() {
	}

	/**
	 * Creates html validation report
	 *
	 * @param htmlReportPath    path with name of the resulting html report
	 * @param xmlReport         xml validation report file
	 * @param validationProfile validation profile file
	 * @throws TransformerException if an unrecoverable error occurs during the course of the transformation or
	 * @throws IOException          file system exceptions
	 * @throws JAXBException 
	 */
	public static void writeHTMLReport(ValidationResult result, OutputStream destination) throws TransformerException, IOException, JAXBException {

		TransformerFactory factory = TransformerFactory.newInstance();

		Transformer transformer = factory.newTransformer(new StreamSource(HTMLReport.class.getClassLoader().getResourceAsStream("HTMLReportStylesheet.xsl")));

		transformer.transform(new StreamSource(ValidationResults.resultToXml(result, Boolean.FALSE)), new StreamResult(destination));
	}

	/**
	 * @return name of the generated image
	 */
	public static String getLogoImageName() {
		return LOGO_NAME;
	}

}

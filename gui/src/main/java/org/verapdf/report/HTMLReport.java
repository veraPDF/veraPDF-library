package org.verapdf.report;

import javax.xml.bind.JAXBException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Generating HTML validation report
 *
 * @author Maksim Bezrukov
 */
public final class HTMLReport {

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
	public static void writeHTMLReport(File report, OutputStream destination) throws TransformerException, IOException, JAXBException {

		TransformerFactory factory = TransformerFactory.newInstance();

		Transformer transformer = factory.newTransformer(new StreamSource(HTMLReport.class.getClassLoader().getResourceAsStream("HTMLReportStylesheet.xsl")));

		transformer.transform(new StreamSource(report), new StreamResult(destination));
	}

}

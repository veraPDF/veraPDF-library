package org.verapdf.report;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.*;

/**
 * @author Maksim Bezrukov
 */
public final class XsltTransformer {

	private XsltTransformer() {
	}

	/**
	 * Creates html validation report
	 *
	 * @param source
	 *            an {@link InputStream} instance that is the source Machine
	 *            Readable Report.
	 * @param destination
	 *            an {@link OutputStream} to write the HTML report to.
	 *
	 * @throws TransformerException
	 *             if an unrecoverable error occurs during the course of the
	 *             transformation
	 * @throws IOException
	 *             file system exceptions
	 */
	public static void transform(InputStream source,
								 InputStream xslt, OutputStream destination) throws TransformerException, IOException{

		TransformerFactory factory = TransformerFactory.newInstance();
		Transformer transformer = factory.newTransformer(new StreamSource(xslt));
		transformer.transform(new StreamSource(source), new StreamResult(
				destination));
	}

	public static void main(String[] args) {
		InputStream xslt = XsltTransformer.class.getClassLoader().getResourceAsStream("policy-example.xsl");
		try {
			InputStream source = new FileInputStream(new File("/home/bezrukov/Downloads/xmlReport.xml"));
			transform(source, xslt, System.out);
		} catch (IOException | TransformerException e) {
			e.printStackTrace();
		}
	}
}

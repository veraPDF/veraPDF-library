package org.verapdf.report;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.util.Map;

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
								 InputStream xslt, OutputStream destination, Map<String, String> arguments) throws TransformerException, IOException{

		TransformerFactory factory = TransformerFactory.newInstance();
		Transformer transformer = factory.newTransformer(new StreamSource(xslt));

		if (arguments != null) {
			for (Map.Entry<String, String> entry : arguments.entrySet()) {
				if (entry.getKey() != null && entry.getValue() != null) {
					transformer.setParameter(entry.getKey(), entry.getValue());
				}
			}
		}

		transformer.transform(new StreamSource(source), new StreamResult(
				destination));
	}
}

package org.verapdf.report;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

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
								 InputStream xslt, OutputStream destination, String argument) throws TransformerException, IOException{

		TransformerFactory factory = TransformerFactory.newInstance();
		Transformer transformer = factory.newTransformer(new StreamSource(xslt));

		if (argument != null) {
			transformer.setParameter("argument", argument);
		}

		transformer.transform(new StreamSource(source), new StreamResult(
				destination));
	}
}

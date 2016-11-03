/**
 * 
 */
package org.verapdf.processor;

import java.io.File;
import java.io.OutputStream;
import java.io.Writer;
import java.util.List;

import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;

/**
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *         <a href="https://github.com/carlwilson">carlwilson AT github</a>
 * @version 0.1 Created 2 Nov 2016:11:10:17
 */

public interface StreamingProcessor extends Processor {
	public BatchSummary process(List<? extends File> toProcess, OutputStream dest) throws XMLStreamException, JAXBException;

	public BatchSummary processDirectory(File toProcess, OutputStream dest, boolean recurse)
			throws XMLStreamException, JAXBException;

	public BatchSummary process(List<? extends File> toProcess, Writer dest) throws XMLStreamException, JAXBException;

	public BatchSummary processDirectory(File toProcess, Writer dest, boolean recurse)
			throws XMLStreamException, JAXBException;
}

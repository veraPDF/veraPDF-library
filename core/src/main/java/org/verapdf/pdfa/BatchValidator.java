/**
 * 
 */
package org.verapdf.pdfa;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipException;

import org.verapdf.report.ValidationBatchReport;

/**
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *         <a href="https://github.com/carlwilson">carlwilson AT github</a>
 *
 * @version 0.1 Created 22 Sep 2016:07:15:16
 */
public interface BatchValidator {
	public ValidationBatchReport process(File toProcess);

	public ValidationBatchReport processDirectory(File toProcess);

	public ValidationBatchReport processArchive(File toProcess) throws ZipException, IOException;
}

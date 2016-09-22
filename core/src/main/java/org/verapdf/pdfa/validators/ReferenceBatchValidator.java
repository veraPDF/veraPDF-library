/**
 * 
 */
package org.verapdf.pdfa.validators;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import org.verapdf.pdfa.BatchValidator;
import org.verapdf.pdfa.PDFAValidator;
import org.verapdf.pdfa.PDFParser;
import org.verapdf.pdfa.VeraPDFFoundry;
import org.verapdf.pdfa.flavours.PDFAFlavour;
import org.verapdf.pdfa.results.ValidationResult;
import org.verapdf.pdfa.results.ValidationResults;
import org.verapdf.report.ItemDetails;
import org.verapdf.report.TaskDetails;
import org.verapdf.report.ValidationBatchReport;
import org.verapdf.report.ValidationSummary;

/**
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *         <a href="https://github.com/carlwilson">carlwilson AT github</a>
 * @version 0.1 Created 22 Sep 2016:07:24:50
 */

public class ReferenceBatchValidator implements BatchValidator {
	private static VeraPDFFoundry FOUNDRY = null;
	private final PDFAFlavour flavour;
	private final boolean recurse;
	private ValidationBatchReport.Builder batchBuilder;
	private final PDFAValidator validator;
	private final String name;

	public ReferenceBatchValidator(PDFAFlavour flavour, boolean recurse) {
		this("Batch Validation", flavour, recurse);
	}

	public ReferenceBatchValidator(final String name, PDFAFlavour flavour, boolean recurse) {
		this.name = name;
		this.flavour = flavour;
		this.recurse = recurse;
		this.validator = Validators.createValidator(flavour, false, 0);
	}

	@Override
	public ValidationBatchReport process(File toProcess) {
		// TODO Auto-generated method stub
		return null;
	}

	public static void initialise(VeraPDFFoundry foundry) {
		if (FOUNDRY == null) FOUNDRY = foundry;
	}
	@Override
	public ValidationBatchReport processDirectory(File toProcess) {
		if (!toProcess.isDirectory()) {
			throw new IllegalArgumentException("File: " + toProcess + " must be an existing directory.");
		}
		this.batchBuilder = new ValidationBatchReport.Builder(this.name);
		this.processDir(toProcess);
		return this.batchBuilder.build();
	}

	@Override
	public ValidationBatchReport processArchive(File toProcess) throws ZipException, IOException {
		this.batchBuilder = new ValidationBatchReport.Builder("Batch Validation");
        try (ZipFile zipSource = new ZipFile( toProcess)) {
            Enumeration<? extends ZipEntry> entries = zipSource.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                if (entry.isDirectory()
                        || !entry.getName().endsWith("pdf"))
                    continue;
                processStream(ItemDetails.fromValues(entry.getName(), entry.getSize()), zipSource.getInputStream(entry));
            }
        }
		return this.batchBuilder.build();
	}

	private void processDir(final File dir) {
		for (File file : dir.listFiles()) {
			if (file.isFile()) {
				int extIndex = file.getName().lastIndexOf(".");
				String ext = file.getName().substring(extIndex + 1);
				if ("pdf".equalsIgnoreCase(ext)) {
					processFile(file);
				}
			} else if (file.isDirectory()) {
				if (this.recurse) {
					processDir(file);
				}
			}
		}
	}

	private void processFile(final File pdfFile) {
		if (checkFileCanBeProcessed(pdfFile)) {
			try (InputStream toProcess = new FileInputStream(pdfFile)) {
				processStream(ItemDetails.fromFile(pdfFile), toProcess);
			} catch (IOException e) {
				System.err.println("Exception raised while processing " + pdfFile.getAbsolutePath());
				e.printStackTrace();
			}
		}
	}

	private static boolean checkFileCanBeProcessed(final File file) {
		if (!file.isFile()) {
			System.err.println("Path " + file.getAbsolutePath() + " is not an existing file.");
			return false;
		} else if (!file.canRead()) {
			System.err.println("Path " + file.getAbsolutePath() + " is not a readable file.");
			return false;
		}
		return true;
	}

	private void processStream(final ItemDetails item, final InputStream toProcess) {
		try (PDFParser parser = FOUNDRY.newPdfParser(toProcess, this.flavour)) {
			TaskDetails.TimedFactory timer = new TaskDetails.TimedFactory("PDF/A Validation");
			ValidationResult validationResult = this.validator.validate(parser);
			ValidationSummary summary = new ValidationSummary(item, validationResult, timer.stop());
			this.batchBuilder.addValidationSummary(summary);
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
}

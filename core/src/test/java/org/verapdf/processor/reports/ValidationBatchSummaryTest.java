/**
 * 
 */
package org.verapdf.processor.reports;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import javax.xml.bind.JAXBException;

import org.junit.Test;
import org.verapdf.core.XmlSerialiser;

import nl.jqno.equalsverifier.EqualsVerifier;

/**
 * @author  <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *          <a href="https://github.com/carlwilson">carlwilson AT github</a>
 *
 * @version 0.1
 * 
 * Created 18 Apr 2017:17:14:00
 */
@SuppressWarnings("static-method")
public class ValidationBatchSummaryTest {

    @Test
    public final void testHashCodeAndEquals() {
        EqualsVerifier.forClass(ValidationBatchSummaryImpl.class).verify();
        EqualsVerifier.forClass(FeaturesBatchSummary.class).verify();
        EqualsVerifier.forClass(MetadataRepairBatchSummary.class).verify();
    }

    /**
	 * Test method for {@link org.verapdf.processor.reports.ValidationBatchSummaryImpl#getCompliantPdfaCount()}.
	 */
	@Test
	public void testGetCompliantPdfaTota1() {
		ValidationBatchSummary summary = ValidationBatchSummaryImpl.fromValues(12, 0, 0);
		assertTrue(summary.getCompliantPdfaCount() == 12);
		summary = ValidationBatchSummaryImpl.fromValues(10, 12, 0);
		assertTrue(summary.getCompliantPdfaCount() == 10);
		summary = ValidationBatchSummaryImpl.fromValues(15, 0, 20);
		assertTrue(summary.getCompliantPdfaCount() == 15);
		summary = ValidationBatchSummaryImpl.fromValues(25, 13, 20);
		assertTrue(summary.getCompliantPdfaCount() == 25);
		summary = ValidationBatchSummaryImpl.fromValues(0, 13, 20);
		assertTrue(summary.getCompliantPdfaCount() == 0);
	}

	/**
	 * Test method for {@link org.verapdf.processor.reports.ValidationBatchSummaryImpl#getNonCompliantPdfaCount()}.
	 */
	@Test
	public void testGetNonCompliantPdfaTota1() {
		ValidationBatchSummary summary = ValidationBatchSummaryImpl.fromValues(0, 12, 0);
		assertTrue(summary.getNonCompliantPdfaCount() == 12);
		summary = ValidationBatchSummaryImpl.fromValues(10, 15, 0);
		assertTrue(summary.getNonCompliantPdfaCount() == 15);
		summary = ValidationBatchSummaryImpl.fromValues(0, 18, 20);
		assertTrue(summary.getNonCompliantPdfaCount() == 18);
		summary = ValidationBatchSummaryImpl.fromValues(25, 13, 20);
		assertTrue(summary.getNonCompliantPdfaCount() == 13);
		summary = ValidationBatchSummaryImpl.fromValues(13, 0, 20);
		assertTrue(summary.getNonCompliantPdfaCount() == 0);
	}

	/**
	 * Test method for {@link org.verapdf.processor.reports.ValidationBatchSummaryImpl#getFailedJobCount()}.
	 */
	@Test
	public void testGetFailedJobCount() {
		ValidationBatchSummary summary = ValidationBatchSummaryImpl.fromValues(0, 0, 12);
		assertTrue(summary.getFailedJobCount() == 12);
		summary = ValidationBatchSummaryImpl.fromValues(10, 0, 15);
		assertTrue(summary.getFailedJobCount() == 15);
		summary = ValidationBatchSummaryImpl.fromValues(0, 20, 18);
		assertTrue(summary.getFailedJobCount() == 18);
		summary = ValidationBatchSummaryImpl.fromValues(25, 13, 20);
		assertTrue(summary.getFailedJobCount() == 20);
		summary = ValidationBatchSummaryImpl.fromValues(13, 0, 4);
		assertTrue(summary.getFailedJobCount() == 4);
	}

	/**
	 * Test method for {@link org.verapdf.processor.reports.ValidationBatchSummaryImpl#getTotalJobCount()}.
	 */
	@Test
	public void testGetTotalJobCount() throws JAXBException {
		ValidationBatchSummary summary = ValidationBatchSummaryImpl.fromValues(0, 0, 12);
		assertTrue(summary.getTotalJobCount() == 12);
		summary = ValidationBatchSummaryImpl.fromValues(10, 0, 15);
		assertTrue(summary.getTotalJobCount() == 25);
		summary = ValidationBatchSummaryImpl.fromValues(0, 20, 18);
		assertTrue(summary.getTotalJobCount() == 38);
		summary = ValidationBatchSummaryImpl.fromValues(25, 13, 20);
		assertTrue(summary.getTotalJobCount() == 58);
		summary = ValidationBatchSummaryImpl.fromValues(13, 0, 4);
		assertTrue(summary.getTotalJobCount() == 17);
		System.out.println(XmlSerialiser.toXml(summary, true, true));
	}

	/**
	 * Test method for {@link org.verapdf.processor.reports.ValidationBatchSummaryImpl#defaultInstance()}.
	 */
	@Test
	public void testDefaultInstance() {
		ValidationBatchSummary defaultInstance = ValidationBatchSummaryImpl.defaultInstance();
		assertTrue(defaultInstance == ValidationBatchSummaryImpl.defaultInstance());
	}

	/**
	 * Test method for {@link org.verapdf.processor.reports.ValidationBatchSummaryImpl#fromValues(int, int, int)}.
	 */
	@Test
	public void testFromValues() {
		ValidationBatchSummary instance = ValidationBatchSummaryImpl.fromValues(0, 0, 0);
		assertFalse(instance == ValidationBatchSummaryImpl.defaultInstance());
		assertEquals(ValidationBatchSummaryImpl.defaultInstance(), instance);
	}

}

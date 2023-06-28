/**
 * 
 */
package org.verapdf.processor.reports;

import javax.xml.bind.JAXBException;

import org.junit.Test;
import org.verapdf.core.XmlSerialiser;

import nl.jqno.equalsverifier.EqualsVerifier;

import static org.junit.Assert.*;

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
		assertEquals(12, summary.getCompliantPdfaCount());
		summary = ValidationBatchSummaryImpl.fromValues(10, 12, 0);
		assertEquals(10, summary.getCompliantPdfaCount());
		summary = ValidationBatchSummaryImpl.fromValues(15, 0, 20);
		assertEquals(15, summary.getCompliantPdfaCount());
		summary = ValidationBatchSummaryImpl.fromValues(25, 13, 20);
		assertEquals(25, summary.getCompliantPdfaCount());
		summary = ValidationBatchSummaryImpl.fromValues(0, 13, 20);
		assertEquals(0, summary.getCompliantPdfaCount());
	}

	/**
	 * Test method for {@link org.verapdf.processor.reports.ValidationBatchSummaryImpl#getNonCompliantPdfaCount()}.
	 */
	@Test
	public void testGetNonCompliantPdfaTota1() {
		ValidationBatchSummary summary = ValidationBatchSummaryImpl.fromValues(0, 12, 0);
		assertEquals(12, summary.getNonCompliantPdfaCount());
		summary = ValidationBatchSummaryImpl.fromValues(10, 15, 0);
		assertEquals(15, summary.getNonCompliantPdfaCount());
		summary = ValidationBatchSummaryImpl.fromValues(0, 18, 20);
		assertEquals(18, summary.getNonCompliantPdfaCount());
		summary = ValidationBatchSummaryImpl.fromValues(25, 13, 20);
		assertEquals(13, summary.getNonCompliantPdfaCount());
		summary = ValidationBatchSummaryImpl.fromValues(13, 0, 20);
		assertEquals(0, summary.getNonCompliantPdfaCount());
	}

	/**
	 * Test method for {@link org.verapdf.processor.reports.ValidationBatchSummaryImpl#getFailedJobCount()}.
	 */
	@Test
	public void testGetFailedJobCount() {
		ValidationBatchSummary summary = ValidationBatchSummaryImpl.fromValues(0, 0, 12);
		assertEquals(12, summary.getFailedJobCount());
		summary = ValidationBatchSummaryImpl.fromValues(10, 0, 15);
		assertEquals(15, summary.getFailedJobCount());
		summary = ValidationBatchSummaryImpl.fromValues(0, 20, 18);
		assertEquals(18, summary.getFailedJobCount());
		summary = ValidationBatchSummaryImpl.fromValues(25, 13, 20);
		assertEquals(20, summary.getFailedJobCount());
		summary = ValidationBatchSummaryImpl.fromValues(13, 0, 4);
		assertEquals(4, summary.getFailedJobCount());
	}

	/**
	 * Test method for {@link org.verapdf.processor.reports.ValidationBatchSummaryImpl#getTotalJobCount()}.
	 */
	@Test
	public void testGetTotalJobCount() throws JAXBException {
		ValidationBatchSummary summary = ValidationBatchSummaryImpl.fromValues(0, 0, 12);
		assertEquals(12, summary.getTotalJobCount());
		summary = ValidationBatchSummaryImpl.fromValues(10, 0, 15);
		assertEquals(25, summary.getTotalJobCount());
		summary = ValidationBatchSummaryImpl.fromValues(0, 20, 18);
		assertEquals(38, summary.getTotalJobCount());
		summary = ValidationBatchSummaryImpl.fromValues(25, 13, 20);
		assertEquals(58, summary.getTotalJobCount());
		summary = ValidationBatchSummaryImpl.fromValues(13, 0, 4);
		assertEquals(17, summary.getTotalJobCount());
		System.out.println(XmlSerialiser.toXml(summary, true, true));
	}

	/**
	 * Test method for {@link org.verapdf.processor.reports.ValidationBatchSummaryImpl#defaultInstance()}.
	 */
	@Test
	public void testDefaultInstance() {
		ValidationBatchSummary defaultInstance = ValidationBatchSummaryImpl.defaultInstance();
		assertSame(defaultInstance, ValidationBatchSummaryImpl.defaultInstance());
	}

	/**
	 * Test method for {@link org.verapdf.processor.reports.ValidationBatchSummaryImpl#fromValues(int, int, int)}.
	 */
	@Test
	public void testFromValues() {
		ValidationBatchSummary instance = ValidationBatchSummaryImpl.fromValues(0, 0, 0);
		assertNotSame(instance, ValidationBatchSummaryImpl.defaultInstance());
		assertEquals(ValidationBatchSummaryImpl.defaultInstance(), instance);
	}

}

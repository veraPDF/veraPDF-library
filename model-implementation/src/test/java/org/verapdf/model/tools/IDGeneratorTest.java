package org.verapdf.model.tools;

import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.cos.COSObject;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.junit.Assert;
import org.junit.Test;
import org.verapdf.model.impl.pb.pd.font.PBoxPDType1Font;

import java.io.IOException;

/**
 * @author Evgeniy Muravitskiy
 */
public class IDGeneratorTest {

	@Test
	public void testCOSObjectIDGenerator() throws IOException {
		COSObject object = new COSObject(null);
		object.setObjectNumber(10);
		object.setGenerationNumber(0);
		String expectedID = "10 0";
		String actualID = IDGenerator.generateID(object);
		Assert.assertEquals(expectedID, actualID);
	}

	@Test
	public void testCOSDictionaryIDGenerator() {
		COSDictionary dictionary = new COSDictionary();
		String actual = IDGenerator.generateID(dictionary);
		Assert.assertNull(actual);
	}

	@Test
	public void testGlyphIDGenerator() {
		String actualID = IDGenerator.generateID(1298374, "name", 10);
		String expectedID = "1298374 name 10";
		Assert.assertEquals(expectedID, actualID);
	}

	@Test
	public void testFontIDGenerator() throws IOException {
		COSDictionary dictionary = new COSDictionary();
		dictionary.setItem(COSName.BASE_FONT, COSName.getPDFName("Arial+MTT"));
		PDType1Font font = new PDType1Font(dictionary);
		PBoxPDType1Font actualFont = new PBoxPDType1Font(font);

		String actualID = actualFont.getID();
		String expectedID = dictionary.hashCode() + " Arial+MTT";
		Assert.assertEquals(expectedID, actualID);
	}
}

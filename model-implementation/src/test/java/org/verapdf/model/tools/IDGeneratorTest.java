package org.verapdf.model.tools;

import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSObject;
import org.junit.Assert;
import org.junit.Test;

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
		String actualID = IDGenerator.generateID("name", 10);
		String expectedID = "name 10";
		Assert.assertEquals(expectedID, actualID);
	}
}

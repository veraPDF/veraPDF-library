package org.verapdf.model.impl;

import org.junit.Assert;
import org.junit.Test;
import org.verapdf.model.ModelHelper;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * @author Evgeniy Muravitskiy
 */

public abstract class BaseTest {

    protected static org.verapdf.model.baselayer.Object actual;

    protected static String expectedType;
    protected static String expectedID;

    @Test
    public void testTypeAndID() {
        Assert.assertEquals(expectedType, actual.getType());
        Assert.assertEquals(expectedID, actual.getID());
    }

	@Test
	public void testLinksMethod() {
		List<String> expectedLinks = ModelHelper.getListOfLinks(actual.getType());
		for (String link : expectedLinks) {
			Assert.assertNotNull(actual.getLinkedObjects(link));
		}
		expectedLinks.clear();
	}

	@Test(expected = IllegalAccessError.class)
	public void testNonexistentParentLink() {
		actual.getLinkedObjects("Wrong link.");
	}

	protected static String getSystemIndependentPath(String path) throws URISyntaxException {
		URL resourceUrl = ClassLoader.class.getResource(path);
		Path resourcePath = Paths.get(resourceUrl.toURI());
		return resourcePath.toString();
	}
}

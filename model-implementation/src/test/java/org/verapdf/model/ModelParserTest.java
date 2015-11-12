package org.verapdf.model;

import org.junit.Assert;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author Evgeniy Muravitskiy
 */
@SuppressWarnings({"static-method", "javadoc"})
public class ModelParserTest {

	@Test
	public void testExistingFile() throws URISyntaxException, IOException {
		String path = getSystemIndependentPath("/model/impl/pb/pd/Fonts.pdf");
		try (ModelParser loader = new ModelParser(new FileInputStream(path))) {
			Assert.assertNotNull(loader.getPDDocument());
			Assert.assertNotNull(loader.getRoot());
		}
	}

	private static String getSystemIndependentPath(String path) throws URISyntaxException {
		URL resourceUrl = ClassLoader.class.getResource(path);
		Path resourcePath = Paths.get(resourceUrl.toURI());
		return resourcePath.toString();
	}
}

package org.verapdf.core.utils;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;

public class FileUtilsTest {
	static final String name = "name"; //$NON-NLS-1$
	static final String ext = ".ext"; //$NON-NLS-1$
	static final String shortExt = ".x"; //$NON-NLS-1$
	static final String shortSchemaExt = ".sc"; //$NON-NLS-1$
	static final String schemaExtAlphas = "sch"; //$NON-NLS-1$
	static final String schemaExt = ".sch"; //$NON-NLS-1$
	static final String longSchemaExt = ".schema"; //$NON-NLS-1$
	static final String thisDir = '.' + File.separator;
	static final String parentDir = '.' + thisDir;
	static final String rootDir = File.separator;
	static final String aDir = File.separator + name + File.separator + name + '.' + name + File.separator;

	@Test
	public void testHasExt() {
		assertTrue(FileUtils.hasExt(name + ext, "ext")); //$NON-NLS-1$
		assertFalse(FileUtils.hasExt(name + ext, "EXT")); //$NON-NLS-1$
		assertTrue(FileUtils.hasExtNoCase(name + ext, "EXT")); //$NON-NLS-1$
		assertTrue(FileUtils.hasExt(name + ext, ext));
		assertFalse(FileUtils.hasExt(name + ext, ext.toUpperCase()));
		assertTrue(FileUtils.hasExtNoCase(name + ext, ext.toUpperCase())); //$NON-NLS-1$
		assertFalse(FileUtils.hasExt(name + ext, schemaExt));
		assertFalse(FileUtils.hasExt(name + ext, '.' + schemaExt));
	}
	@Test
	public void testExtensionless() {
		final String testName = name;
		final String expectedName = name + schemaExt;
		testName(testName, expectedName);
	}

	@Test
	public void testExtension() {
		final String testName = name + ext;
		String expectedName = name + schemaExt;
		doubleTestName(testName, expectedName);
	}

	@Test
	public void testShortExt() {
		final String testName = name + shortExt;
		String expectedName = name + schemaExt;
		doubleTestName(testName, expectedName);
	}

	@Test
	public void testShortSchemaExt() {
		final String testName = name + shortSchemaExt;
		String expectedName = name + schemaExt;
		doubleTestName(testName, expectedName);
	}

	@Test
	public void testSchemaExt() {
		final String testName = name + schemaExt;
		String expectedName = name + schemaExt;
		doubleTestName(testName, expectedName);
	}

	@Test
	public void testLongSchemaExt() {
		final String testName = name + longSchemaExt;
		String expectedName = name + schemaExt;
		doubleTestName(testName, expectedName);
	}

	private static void doubleTestName(final String nameToTest, final String expectedName) {
		testName(nameToTest, expectedName);
		testName(nameToTest + '.' + nameToTest, nameToTest + '.' + expectedName);
	}

	private static void testName(final String nameToTest, final String expectedName) {
		assertEquals(expectedName, FileUtils.addExt(nameToTest, schemaExtAlphas));
		assertEquals(FileUtils.addExt(thisDir + nameToTest, schemaExtAlphas), thisDir + expectedName);
		assertEquals(FileUtils.addExt(parentDir + nameToTest, schemaExtAlphas), parentDir + expectedName);
		assertEquals(FileUtils.addExt(rootDir + nameToTest, schemaExtAlphas), rootDir + expectedName);
		assertEquals(FileUtils.addExt(aDir + nameToTest, schemaExtAlphas), aDir + expectedName);
	}
}

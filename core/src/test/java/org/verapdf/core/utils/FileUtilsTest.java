/**
 * This file is part of veraPDF Library core, a module of the veraPDF project.
 * Copyright (c) 2015-2025, veraPDF Consortium <info@verapdf.org>
 * All rights reserved.
 *
 * veraPDF Library core is free software: you can redistribute it and/or modify
 * it under the terms of either:
 *
 * The GNU General public license GPLv3+.
 * You should have received a copy of the GNU General Public License
 * along with veraPDF Library core as the LICENSE.GPL file in the root of the source
 * tree.  If not, see http://www.gnu.org/licenses/ or
 * https://www.gnu.org/licenses/gpl-3.0.en.html.
 *
 * The Mozilla Public License MPLv2+.
 * You should have received a copy of the Mozilla Public License along with
 * veraPDF Library core as the LICENSE.MPL file in the root of the source tree.
 * If a copy of the MPL was not distributed with this file, you can obtain one at
 * http://mozilla.org/MPL/2.0/.
 */
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

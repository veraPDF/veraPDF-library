/**
 * This file is part of veraPDF Library core, a module of the veraPDF project.
 * Copyright (c) 2015, veraPDF Consortium <info@verapdf.org>
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
/**
 * 
 */
package org.verapdf.core.utils;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.verapdf.core.VeraPDFException;

/**
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *         <a href="https://github.com/carlwilson">carlwilson AT github</a>
 * @version 0.1 Created 14 Nov 2016:00:53:15
 */
@SuppressWarnings("static-method")
public class VersioningMapperTest {
	private static Path tmpRoot;
	private static final String custPrfx = "custPrfx_"; //$NON-NLS-1$
	private static final String custSfx = "_custSfx"; //$NON-NLS-1$
	private static final String custSubFld = "custSubFld"; //$NON-NLS-1$
	private static final String origExt = ".pdf"; //$NON-NLS-1$
	private static final String origNm = "toMap" + origExt; //$NON-NLS-1$
	private static final String custExt = ".ext"; //$NON-NLS-1$

	@BeforeClass
	public static final void before() throws IOException {
		tmpRoot = Files.createTempDirectory("veraTest"); //$NON-NLS-1$
	}

	@Before
	public final void beforeTest() {
		emptyDir(tmpRoot.toFile());
	}

	/**
	 * Test method for
	 * {@link org.verapdf.core.utils.VersioningMapper#getVersion(java.io.File)}.
	 */
	@Test
	public void testGetVersionSib() throws IOException, VeraPDFException {
		FileOutputMapper toCompare = FileOutputMappers.sibFiles("", ""); //$NON-NLS-1$ //$NON-NLS-2$
		FileOutputMapper toTest = FileOutputMappers.verSibFiles("", ""); //$NON-NLS-1$ //$NON-NLS-2$
		File toVersion = new File(tmpRoot.toFile(), origNm);
		assertTrue(toVersion.createNewFile());
		for (int iLoop = 0; iLoop < 20; iLoop++) {
			File mapped = toTest.mapFile(toVersion);
			assertTrue(toCompare.mapFile(toVersion).exists());
			assertFalse(mapped + " exists.", mapped.exists()); //$NON-NLS-1$
			assertTrue(mapped.createNewFile());
		}
	}

	/**
	 * Test method for
	 * {@link org.verapdf.core.utils.VersioningMapper#getVersion(java.io.File)}.
	 */
	@Test
	public void testGetVersionSibPrf() throws IOException, VeraPDFException {
		FileOutputMapper toCompare = FileOutputMappers.sibFiles(custPrfx, ""); //$NON-NLS-1$
		FileOutputMapper toTest = FileOutputMappers.verSibFiles(custPrfx, ""); //$NON-NLS-1$
		File toVersion = new File(tmpRoot.toFile(), origNm);
		assertTrue(toVersion.createNewFile());
		for (int iLoop = 0; iLoop < 20; iLoop++) {
			File mapped = toTest.mapFile(toVersion);
			if (iLoop > 0)
				assertTrue(toCompare.mapFile(toVersion).exists());
			assertFalse(mapped + " exists.", mapped.exists()); //$NON-NLS-1$
			assertTrue(mapped.createNewFile());
		}
	}

	/**
	 * Test method for
	 * {@link org.verapdf.core.utils.VersioningMapper#getVersion(java.io.File)}.
	 */
	@Test
	public void testGetVersionSibSfx() throws IOException, VeraPDFException {
		FileOutputMapper toCompare = FileOutputMappers.sibFiles("", custSfx); //$NON-NLS-1$
		FileOutputMapper toTest = FileOutputMappers.verSibFiles("", custSfx); //$NON-NLS-1$
		File toVersion = new File(tmpRoot.toFile(), origNm);
		assertTrue(toVersion.createNewFile());
		for (int iLoop = 0; iLoop < 20; iLoop++) {
			File mapped = toTest.mapFile(toVersion);
			if (iLoop > 0)
				assertTrue(toCompare.mapFile(toVersion).exists());
			assertFalse(mapped + " exists.", mapped.exists()); //$NON-NLS-1$
			assertTrue(mapped.createNewFile());
		}
	}

	/**
	 * Test method for
	 * {@link org.verapdf.core.utils.VersioningMapper#getVersion(java.io.File)}.
	 */
	@Test
	public void testGetVersionSibExt() throws IOException, VeraPDFException {
		FileOutputMapper toCompare = FileOutputMappers.sibFiles("", custExt); //$NON-NLS-1$
		FileOutputMapper toTest = FileOutputMappers.verSibFiles("", custExt); //$NON-NLS-1$
		File toVersion = new File(tmpRoot.toFile(), origNm);
		assertTrue(toVersion.createNewFile());
		for (int iLoop = 0; iLoop < 20; iLoop++) {
			File mapped = toTest.mapFile(toVersion);
			if (iLoop > 0)
				assertTrue(toCompare.mapFile(toVersion).exists());
			assertFalse(mapped + " exists.", mapped.exists()); //$NON-NLS-1$
			assertTrue(mapped.createNewFile());
		}
	}

	/**
	 * Test method for
	 * {@link org.verapdf.core.utils.VersioningMapper#getVersion(java.io.File)}.
	 */
	@Test
	public void testGetVersionSibPrfxExt() throws IOException, VeraPDFException {
		FileOutputMapper toCompare = FileOutputMappers.sibFiles(custPrfx, custExt);
		FileOutputMapper toTest = FileOutputMappers.verSibFiles(custPrfx, custExt);
		File toVersion = new File(tmpRoot.toFile(), origNm);
		assertTrue(toVersion.createNewFile());
		for (int iLoop = 0; iLoop < 20; iLoop++) {
			File mapped = toTest.mapFile(toVersion);
			if (iLoop > 0)
				assertTrue(toCompare.mapFile(toVersion).exists());
			assertFalse(mapped + " exists.", mapped.exists()); //$NON-NLS-1$
			assertTrue(mapped.createNewFile());
		}
	}

	/**
	 * Test method for
	 * {@link org.verapdf.core.utils.VersioningMapper#getVersion(java.io.File)}.
	 */
	@Test
	public void testGetVersionSub() throws IOException, VeraPDFException {
		FileOutputMapper toCompare = FileOutputMappers.subFold(custSubFld, "", ""); //$NON-NLS-1$ //$NON-NLS-2$
		FileOutputMapper toTest = FileOutputMappers.verSubFold(custSubFld, "", ""); //$NON-NLS-1$ //$NON-NLS-2$
		File toVersion = new File(tmpRoot.toFile(), origNm);
		assertTrue(toVersion.createNewFile());
		for (int iLoop = 0; iLoop < 20; iLoop++) {
			File mapped = toTest.mapFile(toVersion);
			if (iLoop > 0)
				assertTrue(toCompare.mapFile(toVersion).exists());
			assertFalse(mapped + " exists.", mapped.exists()); //$NON-NLS-1$
			assertTrue(mapped.createNewFile());
		}
	}

	/**
	 * Test method for
	 * {@link org.verapdf.core.utils.VersioningMapper#getVersion(java.io.File)}.
	 */
	@Test
	public void testGetVersionSubPrfx() throws IOException, VeraPDFException {
		FileOutputMapper toCompare = FileOutputMappers.subFold(custSubFld, custPrfx, ""); //$NON-NLS-1$
		FileOutputMapper toTest = FileOutputMappers.verSubFold(custSubFld, custPrfx, ""); //$NON-NLS-1$
		File toVersion = new File(tmpRoot.toFile(), origNm);
		assertTrue(toVersion.createNewFile());
		for (int iLoop = 0; iLoop < 20; iLoop++) {
			File mapped = toTest.mapFile(toVersion);
			if (iLoop > 0)
				assertTrue(toCompare.mapFile(toVersion).exists());
			assertFalse(mapped + " exists.", mapped.exists()); //$NON-NLS-1$
			assertTrue(mapped.createNewFile());
		}
	}

	/**
	 * Test method for
	 * {@link org.verapdf.core.utils.VersioningMapper#getVersion(java.io.File)}.
	 */
	@Test
	public void testGetVersionSubSfx() throws IOException, VeraPDFException {
		FileOutputMapper toCompare = FileOutputMappers.subFold(custSubFld, "", custSfx); //$NON-NLS-1$
		FileOutputMapper toTest = FileOutputMappers.verSubFold(custSubFld, "", custSfx); //$NON-NLS-1$
		File toVersion = new File(tmpRoot.toFile(), origNm);
		assertTrue(toVersion.createNewFile());
		for (int iLoop = 0; iLoop < 20; iLoop++) {
			File mapped = toTest.mapFile(toVersion);
			if (iLoop > 0)
				assertTrue(toCompare.mapFile(toVersion).exists());
			assertFalse(mapped + " exists.", mapped.exists()); //$NON-NLS-1$
			assertTrue(mapped.createNewFile());
		}
	}

	/**
	 * Test method for
	 * {@link org.verapdf.core.utils.VersioningMapper#getVersion(java.io.File)}.
	 */
	@Test
	public void testGetVersionExt() throws IOException, VeraPDFException {
		FileOutputMapper toCompare = FileOutputMappers.subFold(custSubFld, "", custExt); //$NON-NLS-1$
		FileOutputMapper toTest = FileOutputMappers.verSubFold(custSubFld, "", custExt); //$NON-NLS-1$
		File toVersion = new File(tmpRoot.toFile(), origNm);
		assertTrue(toVersion.createNewFile());
		for (int iLoop = 0; iLoop < 20; iLoop++) {
			File mapped = toTest.mapFile(toVersion);
			if (iLoop > 0)
				assertTrue(toCompare.mapFile(toVersion).exists());
			assertFalse(mapped + " exists.", mapped.exists()); //$NON-NLS-1$
			assertTrue(mapped.createNewFile());
		}
	}

	/**
	 * Test method for
	 * {@link org.verapdf.core.utils.VersioningMapper#getVersion(java.io.File)}.
	 */
	@Test
	public void testGetVersionPfxExt() throws IOException, VeraPDFException {
		FileOutputMapper toCompare = FileOutputMappers.subFold(custSubFld, custPrfx, custExt);
		FileOutputMapper toTest = FileOutputMappers.verSubFold(custSubFld, custPrfx, custExt);
		File toVersion = new File(tmpRoot.toFile(), origNm);
		assertTrue(toVersion.createNewFile());
		for (int iLoop = 0; iLoop < 20; iLoop++) {
			File mapped = toTest.mapFile(toVersion);
			if (iLoop > 0)
				assertTrue(toCompare.mapFile(toVersion).exists());
			assertFalse(mapped + " exists.", mapped.exists()); //$NON-NLS-1$
			assertTrue(mapped.createNewFile());
		}
	}

	private static void emptyDir(final File dir) {
		for (File file : dir.listFiles()) {
			if (file.isDirectory())
				emptyDir(file);
			file.delete();
		}
	}
}

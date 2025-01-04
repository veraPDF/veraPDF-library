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

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.BeforeClass;
import org.junit.Test;
import org.verapdf.core.VeraPDFException;

import static org.junit.Assert.*;

@SuppressWarnings("static-method")
public class RelativeDirectoryMapperTest {
	private static Path tmpRoot;
	private static final String custPrfx = "custPrfx_"; //$NON-NLS-1$
	private static final String custSfx = "_custSfx"; //$NON-NLS-1$
	private static final String custSubFld = "custSubFld"; //$NON-NLS-1$
	private static final String origNm = "toMap"; //$NON-NLS-1$
	private static final String origExt = ".pdf"; //$NON-NLS-1$
	private static final String custExt = ".ext"; //$NON-NLS-1$

	@BeforeClass
	public static final void before() throws IOException {
		tmpRoot = Files.createTempDirectory("veraTest"); //$NON-NLS-1$
	}

	@Test
	public void testGetdefaultInstance() {
		FileOutputMapper defaultInstance = RelativeDirectoryMapper.defaultInstance();
		assertSame(defaultInstance, RelativeDirectoryMapper.defaultInstance());
	}

	@Test(expected=NullPointerException.class)
	public void testToSibFileNullPrfx() {
		FileOutputMappers.sibFiles(null);
	}

	@Test(expected=NullPointerException.class)
	public void testToSibFileNullPrfxSfx() {
		FileOutputMappers.sibFiles(null, custSfx);
	}

	@Test(expected=NullPointerException.class)
	public void testToSibFilePrfxNullSfx() {
		FileOutputMappers.sibFiles(custPrfx, null);
	}

	@Test
	public void testToSibFile() throws VeraPDFException {
		File toMap = new File(tmpRoot.toFile(), origNm);
		FileOutputMapper toTest = FileOutputMappers.sibFiles();
		testSibFileMapper(toMap, toTest);
	}

	@Test
	public void testToSibFileWithExt() throws VeraPDFException {
		File toMap = new File(tmpRoot.toFile(), origNm + origExt);
		FileOutputMapper toTest = FileOutputMappers.sibFiles();
		testSibFileMapper(toMap, toTest);
	}

	@Test
	public void testToSibFilePrfx() throws VeraPDFException {
		File toMap = new File(tmpRoot.toFile(), origNm);
		FileOutputMapper toTest = FileOutputMappers.sibFiles(custPrfx);
		testSibFileMapper(toMap, toTest);
	}

	@Test
	public void testToSibFilePrfxWithExt() throws VeraPDFException {
		File toMap = new File(tmpRoot.toFile(), origNm + origExt);
		FileOutputMapper toTest = FileOutputMappers.sibFiles(custPrfx);
		testSibFileMapper(toMap, toTest);
	}

	@Test
	public void testToSibFileSfx() throws VeraPDFException {
		File toMap = new File(tmpRoot.toFile(), origNm);
		FileOutputMapper toTest = FileOutputMappers.sibFiles(custPrfx, custSfx);
		testSibFileMapper(toMap, toTest);
	}

	@Test
	public void testToSibFileSfxWithExt() throws VeraPDFException {
		File toMap = new File(tmpRoot.toFile(), origNm + origExt);
		FileOutputMapper toTest = FileOutputMappers.sibFiles(custPrfx, custSfx);
		testSibFileMapper(toMap, toTest);
	}

	@Test
	public void testToSibFileCustExt() throws VeraPDFException {
		File toMap = new File(tmpRoot.toFile(), origNm);
		FileOutputMapper toTest = FileOutputMappers.sibFiles(custPrfx, custExt);
		testSibFileMapper(toMap, toTest);
	}

	@Test
	public void testToSibFileCustExtWithExt() throws VeraPDFException {
		File toMap = new File(tmpRoot.toFile(), origNm + origExt);
		FileOutputMapper toTest = FileOutputMappers.sibFiles(custPrfx, custExt);
		testSibFileMapper(toMap, toTest);
	}

	@Test(expected=NullPointerException.class)
	public void testToSubFldNullRelPath() {
		FileOutputMappers.subFold(null);
	}

	@Test(expected=NullPointerException.class)
	public void testToSubFldNullRelPathPrfx() {
		FileOutputMappers.subFold(null, custPrfx);
	}

	@Test(expected=NullPointerException.class)
	public void testToSubFldNullRelPathPrfxSfx() {
		FileOutputMappers.subFold(null, custPrfx, custSfx);
	}

	@Test(expected=NullPointerException.class)
	public void testToSubFldNullPrfx() {
		FileOutputMappers.subFold(custSubFld, null);
	}

	@Test(expected=NullPointerException.class)
	public void testToSubFldNullPrfxSfx() {
		FileOutputMappers.subFold(custSubFld, null, custSfx);
	}

	@Test(expected=NullPointerException.class)
	public void testToSubFldPrfxNullSfx() {
		FileOutputMappers.subFold(custSubFld, custPrfx, null);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testToSubFldEmptySubFld() {
		FileOutputMappers.subFold("");
	}

	@Test(expected=IllegalArgumentException.class)
	public void testToSubFldEmptySubFldPrfx() {
		FileOutputMappers.subFold("", custPrfx);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testToSubFldEmptySubFldPrfxSfx() {
		FileOutputMappers.subFold("", custPrfx, custSfx);
	}

	@Test
	public void testToSubFld() throws VeraPDFException {
		File toMap = new File(tmpRoot.toFile(), origNm);
		FileOutputMapper toTest = FileOutputMappers.subFold(custSubFld);
		testSubFoldMapper(toMap, toTest);
	}

	@Test
	public void testToSubFldWithExt() throws VeraPDFException {
		File toMap = new File(tmpRoot.toFile(), origNm + origExt);
		FileOutputMapper toTest = FileOutputMappers.subFold(custSubFld);
		testSubFoldMapper(toMap, toTest);
	}

	@Test
	public void testToSubFldPrfx() throws VeraPDFException {
		File toMap = new File(tmpRoot.toFile(), origNm);
		FileOutputMapper toTest = FileOutputMappers.subFold(custSubFld, custPrfx);
		testSubFoldMapper(toMap, toTest);
	}

	@Test
	public void testToSubFldPrfxWithExt() throws VeraPDFException {
		File toMap = new File(tmpRoot.toFile(), origNm + origExt);
		FileOutputMapper toTest = FileOutputMappers.subFold(custSubFld, custPrfx);
		testSubFoldMapper(toMap, toTest);
	}

	@Test
	public void testToSubFldSfx() throws VeraPDFException {
		File toMap = new File(tmpRoot.toFile(), origNm);
		FileOutputMapper toTest = FileOutputMappers.subFold(custSubFld, custPrfx, custSfx);
		testSubFoldMapper(toMap, toTest);
	}

	@Test
	public void testToSubFldSfxWithExt() throws VeraPDFException {
		File toMap = new File(tmpRoot.toFile(), origNm + origExt);
		FileOutputMapper toTest = FileOutputMappers.subFold(custSubFld, custPrfx, custSfx);
		testSubFoldMapper(toMap, toTest);
	}

	@Test
	public void testToSubFldCustExt() throws VeraPDFException {
		File toMap = new File(tmpRoot.toFile(), origNm);
		FileOutputMapper toTest = FileOutputMappers.subFold(custSubFld, custPrfx, custExt);
		testSubFoldMapper(toMap, toTest);
	}

	@Test
	public void testToSubFldCustExtWithExt() throws VeraPDFException {
		File toMap = new File(tmpRoot.toFile(), origNm + origExt);
		FileOutputMapper toTest = FileOutputMappers.subFold(custSubFld, custPrfx, custExt);
		testSubFoldMapper(toMap, toTest);
	}

	static void testSibFileMapper(File orig, FileOutputMapper toTest) throws VeraPDFException {
		String origParent = orig.toPath().getParent().normalize().toString();
		File mapped = toTest.mapFile(orig);
		String mappedParent = mapped.toPath().getParent().normalize().toString();
		assertEquals(origParent + " != " + mappedParent, origParent, mappedParent); //$NON-NLS-1$
		assertTrue(mapped.getName() + " does not start with " + toTest.getPrefix(), //$NON-NLS-1$
				mapped.getName().startsWith(toTest.getPrefix()));
		assertEquals(mapped.getName() + " != " + toTest.getPrefix() + sfxName(orig, toTest.getSuffix()), mapped.getName(), toTest.getPrefix() + sfxName(orig, toTest.getSuffix()));
	}

	static void testSubFoldMapper(File orig, FileOutputMapper toTest) throws VeraPDFException {
		String mapTrgt = new File(orig.getParentFile(), custSubFld).toPath().normalize().toString();
		File mapped = toTest.mapFile(orig);
		String mappedParent = mapped.toPath().getParent().normalize().toString();
		assertEquals(mapTrgt + " != " + mappedParent, mapTrgt, mappedParent); //$NON-NLS-1$
		assertTrue(mapped.getName() + " does not start with " + toTest.getPrefix(), //$NON-NLS-1$
				mapped.getName().startsWith(toTest.getPrefix()));
		assertEquals(mapped.getName() + " != " + toTest.getPrefix() + sfxName(orig, toTest.getSuffix()), mapped.getName(), toTest.getPrefix() + sfxName(orig, toTest.getSuffix()));
	}

	static String sfxName(File orig, final String sfx) {
		if (orig.getName().lastIndexOf(".") > 0 && !sfx.startsWith(".")) {
			return orig.getName().substring(0, orig.getName().lastIndexOf(".")) + sfx + orig.getName().substring(orig.getName().lastIndexOf("."));
		}
		return orig.getName() + sfx;
	}
}

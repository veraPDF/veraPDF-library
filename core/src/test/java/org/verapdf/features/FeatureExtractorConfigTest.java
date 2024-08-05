/**
 * This file is part of veraPDF Library core, a module of the veraPDF project.
 * Copyright (c) 2015-2024, veraPDF Consortium <info@verapdf.org>
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
package org.verapdf.features;

import java.util.EnumSet;

import jakarta.xml.bind.JAXBException;

import org.junit.Test;
import org.verapdf.core.XmlSerialiser;

import nl.jqno.equalsverifier.EqualsVerifier;

import static org.junit.Assert.*;

@SuppressWarnings("static-method")
public class FeatureExtractorConfigTest {

	/**
	 * Test method for {@link org.verapdf.ReleaseDetails#hashCode()}.
	 */
	@Test
	public final void testHashCodeAndEquals() {
		EqualsVerifier.forClass(FeatureExtractorConfigImpl.class).verify();
	}

	@Test
	public void testIsFeatureEnabled() {
		for (FeatureObjectType disabledType : FeatureObjectType.values()) {
			EnumSet<FeatureObjectType> allBarOneSet = EnumSet.allOf(FeatureObjectType.class);
			allBarOneSet.remove(disabledType);
			FeatureExtractorConfig allBarOneConfig = FeatureExtractorConfigImpl.fromFeatureSet(allBarOneSet);
			assertFalse(allBarOneConfig.isFeatureEnabled(disabledType));
			for (FeatureObjectType enabledType : allBarOneSet) {
				assertTrue(allBarOneConfig.isFeatureEnabled(enabledType));
			}
		}
	}

	@Test
	public void testIsAnyFeatureEnabled() {
		EnumSet<FeatureObjectType> enabledFeaturesSet = EnumSet.noneOf(FeatureObjectType.class);
		EnumSet<FeatureObjectType> disabledFeaturesSet = EnumSet.allOf(FeatureObjectType.class);
		FeatureExtractorConfig testConfig = FeatureExtractorConfigImpl.fromFeatureSet(enabledFeaturesSet);
		assertEquals(0, testConfig.getEnabledFeatures().size());
		assertFalse(testConfig.isAnyFeatureEnabled(enabledFeaturesSet));
		assertFalse(testConfig.isAnyFeatureEnabled(disabledFeaturesSet));
		for (FeatureObjectType type : FeatureObjectType.values()) {
			enabledFeaturesSet.add(type);
			disabledFeaturesSet.remove(type);
			testConfig = FeatureExtractorConfigImpl.fromFeatureSet(enabledFeaturesSet);
			assertTrue(testConfig.isAnyFeatureEnabled(enabledFeaturesSet));
			for (FeatureObjectType enabledType : enabledFeaturesSet) {
				EnumSet<FeatureObjectType> testSet = EnumSet.copyOf(disabledFeaturesSet);
				assertFalse(testConfig.isAnyFeatureEnabled(testSet));
				testSet.add(enabledType);
				assertTrue(testConfig.isAnyFeatureEnabled(testSet));
			}
		}
		assertEquals(enabledFeaturesSet.size(), FeatureObjectType.values().length);
		assertTrue(disabledFeaturesSet.isEmpty());
	}

	@Test
	public void testDefaultInstance() {
		assertSame(FeatureExtractorConfigImpl.defaultInstance(), FeatureExtractorConfigImpl.defaultInstance());
		assertEquals(FeatureExtractorConfigImpl.defaultInstance(), FeatureExtractorConfigImpl.defaultInstance());
		for (FeatureObjectType type : FeatureObjectType.values()) {
			if (type != FeatureObjectType.INFORMATION_DICTIONARY)
				assertFalse(FeatureExtractorConfigImpl.defaultInstance().isFeatureEnabled(type));
		}
		assertTrue(FeatureExtractorConfigImpl.defaultInstance()
				.isFeatureEnabled(FeatureObjectType.INFORMATION_DICTIONARY));
	}

	@Test
	public void testXmlSerialisation() throws JAXBException {
		EnumSet<FeatureObjectType> enabledFeaturesSet = EnumSet.noneOf(FeatureObjectType.class);
		EnumSet<FeatureObjectType> disabledFeaturesSet = EnumSet.allOf(FeatureObjectType.class);
		FeatureExtractorConfig testConfig = FeatureExtractorConfigImpl.fromFeatureSet(enabledFeaturesSet);
		assertEquals(testConfig, XmlSerialiser.typeFromXml(FeatureExtractorConfigImpl.class,
				XmlSerialiser.toXml(testConfig, true, true)));
		for (FeatureObjectType type : FeatureObjectType.values()) {
			enabledFeaturesSet.add(type);
			disabledFeaturesSet.remove(type);
			testConfig = FeatureExtractorConfigImpl.fromFeatureSet(enabledFeaturesSet);
			assertEquals(testConfig, XmlSerialiser.typeFromXml(FeatureExtractorConfigImpl.class,
					XmlSerialiser.toXml(testConfig, true, true)));
		}
	}
}

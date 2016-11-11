package org.verapdf.features;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.EnumSet;

import javax.xml.bind.JAXBException;

import org.junit.Test;
import org.verapdf.core.XmlSerialiser;

import nl.jqno.equalsverifier.EqualsVerifier;

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
		assertTrue(testConfig.getEnabledFeatures().size() == 0);
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
		assertTrue(enabledFeaturesSet.size() == FeatureObjectType.values().length);
		assertTrue(disabledFeaturesSet.isEmpty());
	}

	@Test
	public void testDefaultInstance() {
		assertTrue(FeatureExtractorConfigImpl.defaultInstance() == FeatureExtractorConfigImpl.defaultInstance());
		assertTrue(FeatureExtractorConfigImpl.defaultInstance().equals(FeatureExtractorConfigImpl.defaultInstance()));
		for (FeatureObjectType type : FeatureObjectType.values()) {
			if (type != FeatureObjectType.INFORMATION_DICTIONARY)
				assertFalse(FeatureExtractorConfigImpl.defaultInstance().isFeatureEnabled(type));
		}
		assertTrue(FeatureExtractorConfigImpl.defaultInstance().isFeatureEnabled(FeatureObjectType.INFORMATION_DICTIONARY));
	}

	@Test
	public void testXmlSerialisation() throws JAXBException {
		EnumSet<FeatureObjectType> enabledFeaturesSet = EnumSet.noneOf(FeatureObjectType.class);
		EnumSet<FeatureObjectType> disabledFeaturesSet = EnumSet.allOf(FeatureObjectType.class);
		FeatureExtractorConfig testConfig = FeatureExtractorConfigImpl.fromFeatureSet(enabledFeaturesSet);
		assertTrue(testConfig.equals(XmlSerialiser.typeFromXml(FeatureExtractorConfigImpl.class, XmlSerialiser.toXml(testConfig, true, true))));
		for (FeatureObjectType type : FeatureObjectType.values()) {
			enabledFeaturesSet.add(type);
			disabledFeaturesSet.remove(type);
			testConfig = FeatureExtractorConfigImpl.fromFeatureSet(enabledFeaturesSet);
			assertTrue(testConfig.equals(XmlSerialiser.typeFromXml(FeatureExtractorConfigImpl.class, XmlSerialiser.toXml(testConfig, true, true))));
		}
	}
}

package org.verapdf.features.config;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.EnumSet;

import javax.xml.bind.JAXBException;

import org.junit.Test;
import org.verapdf.features.FeatureObjectType;

import nl.jqno.equalsverifier.EqualsVerifier;

public class FeaturesConfigTest {

    /**
     * Test method for {@link org.verapdf.ReleaseDetails#hashCode()}.
     */
    @Test
    public final void testHashCodeAndEquals() {
        EqualsVerifier.forClass(FeaturesConfig.class).verify();
    }


	@Test
	public void testIsFeatureEnabled() {
		for (FeatureObjectType disabledType : FeatureObjectType.values()) {
			EnumSet<FeatureObjectType> allBarOneSet = EnumSet.allOf(FeatureObjectType.class);
			allBarOneSet.remove(disabledType);
			FeaturesConfig allBarOneConfig = FeaturesConfig.fromFeatureSet(allBarOneSet);
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
		FeaturesConfig testConfig = FeaturesConfig.fromFeatureSet(enabledFeaturesSet);
		assertTrue(testConfig.getEnabledFeatures().size() == 0);
		assertFalse(testConfig.isAnyFeatureEnabled(enabledFeaturesSet));
		assertFalse(testConfig.isAnyFeatureEnabled(disabledFeaturesSet));
		for (FeatureObjectType type : FeatureObjectType.values()) {
			enabledFeaturesSet.add(type);
			disabledFeaturesSet.remove(type);
			testConfig = FeaturesConfig.fromFeatureSet(enabledFeaturesSet);
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
		assertTrue(FeaturesConfig.defaultInstance() == FeaturesConfig.defaultInstance());
		assertTrue(FeaturesConfig.defaultInstance().equals(FeaturesConfig.defaultInstance()));
		for (FeatureObjectType type : FeatureObjectType.values()) {
			if (type != FeatureObjectType.INFORMATION_DICTIONARY)
				assertFalse(FeaturesConfig.defaultInstance().isFeatureEnabled(type));
		}
		assertTrue(FeaturesConfig.defaultInstance().isFeatureEnabled(FeatureObjectType.INFORMATION_DICTIONARY));
	}

	@Test
	public void testXmlSerialisation() throws JAXBException, IOException {
		EnumSet<FeatureObjectType> enabledFeaturesSet = EnumSet.noneOf(FeatureObjectType.class);
		EnumSet<FeatureObjectType> disabledFeaturesSet = EnumSet.allOf(FeatureObjectType.class);
		FeaturesConfig testConfig = FeaturesConfig.fromFeatureSet(enabledFeaturesSet);
		assertTrue(testConfig.equals(FeaturesConfig.fromXml(FeaturesConfig.toXml(testConfig, Boolean.FALSE))));
		for (FeatureObjectType type : FeatureObjectType.values()) {
			enabledFeaturesSet.add(type);
			disabledFeaturesSet.remove(type);
			testConfig = FeaturesConfig.fromFeatureSet(enabledFeaturesSet);
			assertTrue(testConfig.equals(FeaturesConfig.fromXml(FeaturesConfig.toXml(testConfig, Boolean.FALSE))));
		}
	}
}

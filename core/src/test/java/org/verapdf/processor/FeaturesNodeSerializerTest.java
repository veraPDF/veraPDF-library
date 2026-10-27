/*
 * This file is part of veraPDF Library core, a module of the veraPDF project.
 * Copyright (c) 2015-2026, veraPDF Consortium <info@verapdf.org>
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
package org.verapdf.processor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.junit.Test;
import org.verapdf.report.FeaturesNode;

import jakarta.xml.bind.JAXBElement;
import javax.xml.namespace.QName;
import java.io.StringWriter;
import java.lang.reflect.Constructor;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class FeaturesNodeSerializerTest {

	@Test
	public void serializesEmptyXmpNodeAsEmptyObject() throws Exception {
		Constructor<FeaturesNode> constructor = FeaturesNode.class.getDeclaredConstructor(Map.class, List.class);
		constructor.setAccessible(true);
		FeaturesNode emptyXmpNode = constructor.newInstance(Collections.emptyMap(), Collections.singletonList(null));
		JAXBElement<FeaturesNode> xmpPackage = new JAXBElement<>(new QName("xmpPackage"), FeaturesNode.class,
				emptyXmpNode);
		FeaturesNode metadata = constructor.newInstance(Collections.emptyMap(), Collections.singletonList(xmpPackage));

		SimpleModule module = new SimpleModule();
		module.addSerializer(FeaturesNode.class, new FeaturesNodeSerializer(FeaturesNode.class));
		ObjectMapper objectMapper = new ObjectMapper().registerModule(module);
		StringWriter output = new StringWriter();

		try (com.fasterxml.jackson.core.JsonGenerator generator = objectMapper.getFactory().createGenerator(output)) {
			generator.writeStartObject();
			generator.writeFieldName("metadata");
			generator.writeObject(metadata);
			generator.writeEndObject();
		}

		assertEquals("{\"metadata\":{}}", output.toString());
	}
}

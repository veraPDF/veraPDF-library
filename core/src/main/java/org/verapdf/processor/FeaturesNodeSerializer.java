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
package org.verapdf.processor;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.verapdf.report.FeaturesNode;

import jakarta.xml.bind.JAXBElement;
import javax.xml.namespace.QName;
import java.io.IOException;
import java.util.Map;

public class FeaturesNodeSerializer extends StdSerializer<FeaturesNode> {

	public FeaturesNodeSerializer(Class<FeaturesNode> t) {
		super(t);
	}

	@Override
	public void serialize(FeaturesNode featuresNode, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
			throws IOException {

		if ((featuresNode.getAttributes() == null || featuresNode.getAttributes().isEmpty())
				&& (featuresNode.getChildren() != null && featuresNode.getChildren().size() == 1)) {
			Object child = featuresNode.getChildren().get(0);
			if (child instanceof JAXBElement) {
				Object value = (((JAXBElement<?>) child).getValue());
				if (value instanceof FeaturesNode) {
					jsonGenerator.writeObject(value);
				}
			} else if (child instanceof String) {
				jsonGenerator.writeObject(child);
			}
		} else if ((featuresNode.getAttributes() != null && featuresNode.getAttributes().size() == 1)
				&& (featuresNode.getChildren() == null || featuresNode.getChildren().isEmpty())) {
			for (Map.Entry<QName, Object> entry : featuresNode.getAttributes().entrySet()) {
				jsonGenerator.writeObject(entry.getValue());
			}
		} else {
			jsonGenerator.writeStartObject();
			if (featuresNode.getAttributes() != null && !featuresNode.getAttributes().isEmpty()) {
				serializeAttributes(featuresNode, jsonGenerator);
			}
			if (featuresNode.getChildren() != null && !featuresNode.getChildren().isEmpty()) {
				serializeChildren(featuresNode, jsonGenerator);
			}
			jsonGenerator.writeEndObject();
		}
	}

	private void serializeAttributes(FeaturesNode featuresNode, JsonGenerator jsonGenerator) throws IOException {
		for (Map.Entry<QName, Object> entry : featuresNode.getAttributes().entrySet()) {
			jsonGenerator.writeFieldName(entry.getKey().toString());
			jsonGenerator.writeObject(entry.getValue());
		}
	}

	private void serializeChildren(FeaturesNode featuresNode, JsonGenerator jsonGenerator) throws IOException {
		for (Object child : featuresNode.getChildren()) {
			if (child instanceof JAXBElement) {
				Object value = (((JAXBElement<?>) child).getValue());
				if (value instanceof FeaturesNode) {
					jsonGenerator.writeFieldName((((JAXBElement<?>) child).getName().toString()));
					jsonGenerator.writeObject(value);
				}
			} else if (child instanceof String) {
				jsonGenerator.writeFieldName("value");
				jsonGenerator.writeObject(child);
			}
		}
	}
}

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

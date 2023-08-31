package org.verapdf.processor;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.verapdf.core.VeraPDFException;

import java.io.IOException;

public class VeraPDFExceptionSerializer extends StdSerializer<VeraPDFException> {

	public VeraPDFExceptionSerializer(Class<VeraPDFException> t) {
		super(t);
	}

	public void serialize(VeraPDFException exception, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
			throws IOException {
		jsonGenerator.writeString(exception.getMessage());
	}
}

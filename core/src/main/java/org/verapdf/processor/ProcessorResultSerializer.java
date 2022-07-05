package org.verapdf.processor;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.verapdf.processor.reports.Reports;

import java.io.IOException;

public class ProcessorResultSerializer extends StdSerializer<ProcessorResult> {

	private final boolean logPassed;

	public ProcessorResultSerializer(Class<ProcessorResult> t, boolean logPassed) {
		super(t);
		this.logPassed = logPassed;
	}

	public void serialize(ProcessorResult result, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
			throws IOException {

		jsonGenerator.writeStartObject();

		if (result.getResults().containsKey(TaskType.VALIDATE)) {
			jsonGenerator.writeFieldName(RawResultHandler.VALIDATION_RESULT);
			jsonGenerator.writeObject(Reports.createValidationReport(result.getValidationResult(), this.logPassed));
		}

		if (result.getResults().containsKey(TaskType.FIX_METADATA)) {
			jsonGenerator.writeFieldName(RawResultHandler.FIXER_RESULT);
			jsonGenerator.writeObject(result.getFixerResult());
		}

		if (result.getResults().containsKey(TaskType.EXTRACT_FEATURES)) {
			jsonGenerator.writeFieldName(RawResultHandler.FEATURES_REPORT);
			jsonGenerator.writeObject(result.getFeaturesReport());
		}

		jsonGenerator.writeEndObject();
	}

}

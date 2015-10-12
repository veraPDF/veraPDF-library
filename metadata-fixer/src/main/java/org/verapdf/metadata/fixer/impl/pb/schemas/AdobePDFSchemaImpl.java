package org.verapdf.metadata.fixer.impl.pb.schemas;

import org.apache.xmpbox.schema.AdobePDFSchema;
import org.verapdf.metadata.fixer.entity.Metadata;
import org.verapdf.metadata.fixer.schemas.AdobePDF;

/**
 * @author Evgeniy Muravitskiy
 */
public class AdobePDFSchemaImpl implements AdobePDF {

	private final AdobePDFSchema schema;
	private final Metadata metadata;

	public AdobePDFSchemaImpl(AdobePDFSchema schema, Metadata metadata) {
		this.schema = schema;
		this.metadata = metadata;
	}

	@Override
	public String getProducer() {
		return this.schema.getProducer();
	}

	@Override
	public void setProducer(String producer) {
		this.schema.setProducer(producer);
	}

	@Override
	public String getKeywords() {
		return this.schema.getKeywords();
	}

	@Override
	public void setKeywords(String keywords) {
		this.schema.setKeywords(keywords);
	}

	@Override
	public void setNeedToBeUpdated(boolean needToBeUpdated) {
		this.metadata.setNeedToBeUpdated(needToBeUpdated);
	}
}

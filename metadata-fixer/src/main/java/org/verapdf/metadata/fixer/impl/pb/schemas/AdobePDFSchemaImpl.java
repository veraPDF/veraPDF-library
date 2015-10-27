package org.verapdf.metadata.fixer.impl.pb.schemas;

import org.apache.xmpbox.schema.AdobePDFSchema;
import org.verapdf.metadata.fixer.entity.Metadata;
import org.verapdf.metadata.fixer.schemas.AdobePDF;

import static org.verapdf.metadata.fixer.utils.MetadataFixerConstants.KEYWORDS;
import static org.verapdf.metadata.fixer.utils.MetadataFixerConstants.PRODUCER;

/**
 * @author Evgeniy Muravitskiy
 */
public class AdobePDFSchemaImpl extends BasicSchemaImpl implements AdobePDF {

	public AdobePDFSchemaImpl(AdobePDFSchema schema, Metadata metadata) {
		super(schema, metadata);
	}

	@Override
	public String getProducer() {
		return ((AdobePDFSchema) this.schema).getProducer();
	}

	@Override
	public void setProducer(String producer) {
		this.removeProperty(PRODUCER);
		((AdobePDFSchema) this.schema).setProducer(producer);
	}

	@Override
	public String getKeywords() {
		return ((AdobePDFSchema) this.schema).getKeywords();
	}

	@Override
	public void setKeywords(String keywords) {
		this.removeProperty(KEYWORDS);
		((AdobePDFSchema) this.schema).setKeywords(keywords);
	}

}

package org.verapdf.metadata.fixer.impl.pb.schemas;

import org.apache.xmpbox.schema.XMPSchema;
import org.apache.xmpbox.type.AbstractField;
import org.verapdf.metadata.fixer.entity.Metadata;
import org.verapdf.metadata.fixer.schemas.BasicSchema;

/**
 * @author Evgeniy Muravitskiy
 */
public abstract class BasicSchemaImpl implements BasicSchema {

	protected final XMPSchema schema;
	protected final Metadata metadata;

	protected BasicSchemaImpl(XMPSchema schema, Metadata metadata) {
		if (schema == null) {
			throw new IllegalArgumentException("Schema representation can not be null");
		}
		if (metadata == null) {
			throw new IllegalArgumentException("Metadata representation can not be null");
		}
		this.schema = schema;
		this.metadata = metadata;
	}

	@Override
	public void setNeedToBeUpdated(boolean needToBeUpdated) {
		this.metadata.setNeedToBeUpdated(needToBeUpdated);
	}

	protected void removeProperty(String propertyName) {
		AbstractField property = this.schema.getProperty(propertyName);
		if (property != null) {
			this.schema.removeProperty(property);
		}
	}

}

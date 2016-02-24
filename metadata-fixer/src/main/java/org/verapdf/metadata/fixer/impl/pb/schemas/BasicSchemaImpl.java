package org.verapdf.metadata.fixer.impl.pb.schemas;

import com.adobe.xmp.impl.VeraPDFMeta;
import org.verapdf.metadata.fixer.entity.Metadata;
import org.verapdf.metadata.fixer.schemas.BasicSchema;

/**
 * @author Evgeniy Muravitskiy
 */
public abstract class BasicSchemaImpl implements BasicSchema {

	protected final VeraPDFMeta meta;
	protected final Metadata metadata;

	protected BasicSchemaImpl(VeraPDFMeta meta, Metadata metadata) {
		if (meta == null) {
			throw new IllegalArgumentException("Schema representation can not be null");
		}
		if (metadata == null) {
			throw new IllegalArgumentException("Metadata representation can not be null");
		}
		this.meta = meta;
		this.metadata = metadata;
	}

	@Override
	public void setNeedToBeUpdated(boolean needToBeUpdated) {
		this.metadata.setNeedToBeUpdated(needToBeUpdated);
	}

}

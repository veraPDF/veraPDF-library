package org.verapdf.metadata.fixer.impl.pb.schemas;

import org.apache.xmpbox.schema.XMPBasicSchema;
import org.verapdf.metadata.fixer.entity.Metadata;
import org.verapdf.metadata.fixer.schemas.XMPBasic;
import org.verapdf.metadata.fixer.utils.DateConverter;

/**
 * @author Evgeniy Muravitskiy
 */
public class XMPBasicSchemaImpl implements XMPBasic {

	private final XMPBasicSchema schema;
	private final Metadata metadata;

	public XMPBasicSchemaImpl(XMPBasicSchema schema, Metadata metadata) {
		if (schema == null) {
			throw new IllegalArgumentException("XMPBasic schema representation can not be null");
		}
		if (metadata == null) {
			throw new IllegalArgumentException("Metadata representation can not be null");
		}
		this.schema = schema;
		this.metadata = metadata;
	}

	@Override
	public String getCreator() {
		return this.schema.getCreatorTool();
	}

	@Override
	public void setCreator(String creatorTool) {
		this.schema.setCreatorTool(creatorTool);
	}

	@Override
	public String getCreationDate() {
		return DateConverter.toUTCString(this.schema.getCreateDate());
	}

	@Override
	public void setCreationDate(String creationDate) {
		this.schema.setCreateDate(DateConverter.toCalendar(creationDate));
	}

	@Override
	public String getModificationDate() {
		return DateConverter.toUTCString(this.schema.getModifyDate());
	}

	@Override
	public void setModificationDate(String modificationDate) {
		this.schema.setModifyDate(DateConverter.toCalendar(modificationDate));
	}

	@Override
	public void setNeedToBeUpdated(boolean needToBeUpdated) {
		this.metadata.setNeedToBeUpdated(needToBeUpdated);
	}
}

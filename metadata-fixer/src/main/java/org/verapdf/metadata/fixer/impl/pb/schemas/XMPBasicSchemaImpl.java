package org.verapdf.metadata.fixer.impl.pb.schemas;

import org.apache.xmpbox.schema.XMPBasicSchema;
import org.verapdf.metadata.fixer.entity.Metadata;
import org.verapdf.metadata.fixer.schemas.XMPBasic;
import org.verapdf.metadata.fixer.utils.DateConverter;

import static org.verapdf.metadata.fixer.utils.MetadataFixerConstants.METADATA_CREATOR;
import static org.verapdf.metadata.fixer.utils.MetadataFixerConstants.METADATA_CREATION_DATE;
import static org.verapdf.metadata.fixer.utils.MetadataFixerConstants.METADATA_MODIFICATION_DATE;

/**
 * @author Evgeniy Muravitskiy
 */
public class XMPBasicSchemaImpl extends BasicSchemaImpl implements XMPBasic {

	public XMPBasicSchemaImpl(XMPBasicSchema schema, Metadata metadata) {
		super(schema, metadata);
	}

	@Override
	public String getCreator() {
		return ((XMPBasicSchema) this.schema).getCreatorTool();
	}

	@Override
	public void setCreator(String creatorTool) {
		this.removeProperty(METADATA_CREATOR);
		((XMPBasicSchema) this.schema).setCreatorTool(creatorTool);
	}

	@Override
	public String getCreationDate() {
		return DateConverter.toUTCString(((XMPBasicSchema) this.schema).getCreateDate());
	}

	@Override
	public void setCreationDate(String creationDate) {
		this.removeProperty(METADATA_CREATION_DATE);
		((XMPBasicSchema) this.schema).setCreateDate(DateConverter.toCalendar(creationDate));
	}

	@Override
	public String getModificationDate() {
		return DateConverter.toUTCString(((XMPBasicSchema) this.schema).getModifyDate());
	}

	@Override
	public void setModificationDate(String modificationDate) {
		this.removeProperty(METADATA_MODIFICATION_DATE);
		((XMPBasicSchema) this.schema).setModifyDate(DateConverter.toCalendar(modificationDate));
	}

}

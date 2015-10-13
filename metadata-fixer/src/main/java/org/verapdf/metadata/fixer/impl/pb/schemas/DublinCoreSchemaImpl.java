package org.verapdf.metadata.fixer.impl.pb.schemas;

import org.apache.xmpbox.XmpConstants;
import org.apache.xmpbox.schema.DublinCoreSchema;
import org.apache.xmpbox.type.AbstractField;
import org.apache.xmpbox.type.ArrayProperty;
import org.apache.xmpbox.type.Cardinality;
import org.apache.xmpbox.type.TextType;
import org.verapdf.metadata.fixer.entity.Metadata;
import org.verapdf.metadata.fixer.schemas.DublinCore;

import java.util.List;

/**
 * @author Evgeniy Muravitskiy
 */
public class DublinCoreSchemaImpl implements DublinCore {

	private static final String AUTHOR = "creator";

	private final DublinCoreSchema schema;
	private final Metadata metadata;

	public DublinCoreSchemaImpl(DublinCoreSchema schema, Metadata metadata) {
		if (schema == null) {
			throw new IllegalArgumentException("DublinCore schema representation can not be null");
		}
		if (metadata == null) {
			throw new IllegalArgumentException("Metadata representation can not be null");
		}
		this.schema = schema;
		this.metadata = metadata;
	}

	@Override
	public String getTitle() {
		return this.schema.getTitle();
	}

	@Override
	public void setTitle(String title) {
		this.schema.setTitle(title);
	}

	@Override
	public String getSubject() {
		return this.schema.getDescription();
	}

	@Override
	public void setSubject(String description) {
		this.schema.setDescription(description);
	}

	@Override
	public String getAuthor() {
		List<String> creators = this.schema.getCreators();
		return creators != null && creators.size() > 0 ? creators.get(0) : null;
	}

	@Override
	public void setAuthor(String creator) {
		ArrayProperty seq = (ArrayProperty) schema.getAbstractProperty(AUTHOR);
		ArrayProperty newSeq = schema.createArrayProperty(AUTHOR, Cardinality.Seq);
		TextType li = schema.createTextType(XmpConstants.LIST_NAME, creator);
		newSeq.getContainer().addProperty(li);
		if (seq != null) {
			List<AbstractField> properties = seq.getContainer().getAllProperties();
			for (int i = 1; i < properties.size(); i++) {
				newSeq.getContainer().addProperty(properties.get(i));
			}
			this.schema.removeProperty(seq);
		}
		this.schema.addProperty(newSeq);
	}

	@Override
	public void setNeedToBeUpdated(boolean needToBeUpdated) {
		this.metadata.setNeedToBeUpdated(needToBeUpdated);
	}
}

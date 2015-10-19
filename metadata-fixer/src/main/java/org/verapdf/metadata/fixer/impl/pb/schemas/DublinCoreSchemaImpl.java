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
public class DublinCoreSchemaImpl extends BasicSchemaImpl implements DublinCore {

	private static final String AUTHOR = "creator";

	public DublinCoreSchemaImpl(DublinCoreSchema schema, Metadata metadata) {
		super(schema, metadata);
	}

	@Override
	public String getTitle() {
		return ((DublinCoreSchema) this.schema).getTitle();
	}

	@Override
	public void setTitle(String title) {
		this.removeProperty("title");
		((DublinCoreSchema) this.schema).setTitle(title);
	}

	@Override
	public String getSubject() {
		return ((DublinCoreSchema) this.schema).getDescription();
	}

	@Override
	public void setSubject(String description) {
		this.removeProperty("description");
		((DublinCoreSchema) this.schema).setDescription(description);
	}

	@Override
	public String getAuthor() {
		List<String> creators = ((DublinCoreSchema) this.schema).getCreators();
		return creators != null && creators.size() > 0 ? creators.get(0) : null;
	}

	@Override
	public void setAuthor(String creator) {
		ArrayProperty seq = (ArrayProperty) this.schema.getAbstractProperty(AUTHOR);
		ArrayProperty newSeq = this.schema.createArrayProperty(AUTHOR, Cardinality.Seq);
		TextType li = this.schema.createTextType(XmpConstants.LIST_NAME, creator);
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

}

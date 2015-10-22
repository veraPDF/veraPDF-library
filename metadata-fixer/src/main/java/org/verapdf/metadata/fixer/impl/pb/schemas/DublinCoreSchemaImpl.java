package org.verapdf.metadata.fixer.impl.pb.schemas;

import org.apache.xmpbox.XmpConstants;
import org.apache.xmpbox.schema.DublinCoreSchema;
import org.apache.xmpbox.type.*;
import org.verapdf.metadata.fixer.entity.Metadata;
import org.verapdf.metadata.fixer.schemas.DublinCore;

import static org.verapdf.metadata.fixer.utils.MetadataFixerConstants.METADATA_TITLE;
import static org.verapdf.metadata.fixer.utils.MetadataFixerConstants.METADATA_AUTHOR;
import static org.verapdf.metadata.fixer.utils.MetadataFixerConstants.METADATA_SUBJECT;

import javax.xml.XMLConstants;
import java.util.List;

/**
 * @author Evgeniy Muravitskiy
 */
public class DublinCoreSchemaImpl extends BasicSchemaImpl implements DublinCore {

	public DublinCoreSchemaImpl(DublinCoreSchema schema, Metadata metadata) {
		super(schema, metadata);
	}

	@Override
	public String getTitle() {
		return ((DublinCoreSchema) this.schema).getTitle();
	}

	@Override
	public void setTitle(String title) {
		this.removeProperty(METADATA_TITLE);
		((DublinCoreSchema) this.schema).setTitle(title);
	}

	@Override
	public String getSubject() {
		return ((DublinCoreSchema) this.schema).getDescription();
	}

	@Override
	public void setSubject(String description) {
		ArrayProperty seq = (ArrayProperty) this.schema.getAbstractProperty(METADATA_SUBJECT);
		ArrayProperty newSeq = this.schema.createArrayProperty(METADATA_SUBJECT, Cardinality.Alt);

		TextType li = this.schema.createTextType(XmpConstants.LIST_NAME, description);
		li.setAttribute(new Attribute(XMLConstants.XML_NS_URI, XmpConstants.LANG_NAME, XmpConstants.X_DEFAULT));
		newSeq.addProperty(li);

		int position = this.getPosition(seq);
		this.copySubArray(seq, newSeq, position);
		this.schema.addProperty(li);
	}

	@Override
	public String getAuthor() {
		List<String> creators = ((DublinCoreSchema) this.schema).getCreators();
		return creators != null && creators.size() > 0 ? creators.get(0) : null;
	}

	@Override
	public void setAuthor(String creator) {
		ArrayProperty seq = (ArrayProperty) this.schema.getAbstractProperty(METADATA_AUTHOR);
		ArrayProperty newSeq = this.schema.createArrayProperty(METADATA_AUTHOR, Cardinality.Seq);
		TextType li = this.schema.createTextType(XmpConstants.LIST_NAME, creator);
		newSeq.addProperty(li);
		this.copySubArray(seq, newSeq, 0);
		this.schema.addProperty(newSeq);
	}

	private int getPosition(ArrayProperty seq) {
		if (seq != null) {
			List<AbstractField> properties = seq.getContainer().getAllProperties();
			for (int index = 0; index < properties.size(); index++) {
				String attributeValue = properties.get(index)
						.getAttribute(XmpConstants.LANG_NAME).getValue();
				if (XmpConstants.X_DEFAULT.equals(attributeValue)) {
					return index;
				}
			}
		}

		return 0;
	}

	private void copySubArray(ArrayProperty seq, ArrayProperty newSeq, int exceptIndex) {
		if (seq != null) {
			List<AbstractField> properties = seq.getContainer().getAllProperties();
			ComplexPropertyContainer container = newSeq.getContainer();
			for (int i = 1; i < properties.size(); i++) {
				if (i != exceptIndex) {
					container.addProperty(properties.get(i));
				}
			}
			this.schema.removeProperty(seq);
		}
	}

}

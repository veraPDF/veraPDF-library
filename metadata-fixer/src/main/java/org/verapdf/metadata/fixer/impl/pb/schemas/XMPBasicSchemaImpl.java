package org.verapdf.metadata.fixer.impl.pb.schemas;

import com.adobe.xmp.XMPException;
import com.adobe.xmp.impl.VeraPDFMeta;
import org.apache.log4j.Logger;
import org.verapdf.metadata.fixer.entity.Metadata;
import org.verapdf.metadata.fixer.schemas.XMPBasic;
import org.verapdf.metadata.fixer.utils.DateConverter;

/**
 * @author Evgeniy Muravitskiy
 */
public class XMPBasicSchemaImpl extends BasicSchemaImpl implements XMPBasic {

	private static final Logger LOGGER = Logger.getLogger(XMPBasicSchemaImpl.class);

	public XMPBasicSchemaImpl(VeraPDFMeta meta, Metadata metadata) {
		super(meta, metadata);
	}

	@Override
	public String getCreator() {
		try {
			return this.meta.getCreatorTool();
		} catch (XMPException e) {
			LOGGER.error("Can not get creator tool.", e);
			throw new IllegalStateException(e);
		}
	}

	@Override
	public void setCreator(String creatorTool) {
		try {
			this.meta.setCreatorTool(creatorTool);
		} catch (XMPException e) {
			LOGGER.error("Can not set creator tool.", e);
			throw new IllegalStateException(e);
		}
	}

	@Override
	public String getCreationDate() {
		try {
			return DateConverter.toUTCString(this.meta.getCreateDate());
		} catch (XMPException e) {
			LOGGER.error("Can not get creation date.", e);
			throw new IllegalStateException(e);
		}
	}

	@Override
	public void setCreationDate(String creationDate) {
		try {
			this.meta.setCreateDate(DateConverter.toCalendar(creationDate));
		} catch (XMPException e) {
			LOGGER.error("Can not set creation date.", e);
			throw new IllegalStateException(e);
		}
	}

	@Override
	public String getModificationDate() {
		try {
			return DateConverter.toUTCString(this.meta.getModifyDate());
		} catch (XMPException e) {
			LOGGER.error("Can not get modification date.", e);
			throw new IllegalStateException(e);
		}
	}

	@Override
	public void setModificationDate(String modificationDate) {
		try {
			this.meta.setModifyDate(DateConverter.toCalendar(modificationDate));
		} catch (XMPException e) {
			LOGGER.error("Can not set modification date.", e);
			throw new IllegalStateException(e);
		}
	}

}

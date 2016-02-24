package org.verapdf.metadata.fixer.impl.pb.schemas;

import com.adobe.xmp.XMPException;
import com.adobe.xmp.impl.VeraPDFMeta;
import org.apache.log4j.Logger;
import org.verapdf.metadata.fixer.entity.Metadata;
import org.verapdf.metadata.fixer.schemas.DublinCore;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Evgeniy Muravitskiy
 */
public class DublinCoreSchemaImpl extends BasicSchemaImpl implements DublinCore {

	private static final Logger LOGGER = Logger.getLogger(DublinCoreSchemaImpl.class);

	public DublinCoreSchemaImpl(VeraPDFMeta meta, Metadata metadata) {
		super(meta, metadata);
	}

	@Override
	public String getTitle() {
		try {
			return this.meta.getTitle();
		} catch (XMPException e) {
			LOGGER.error("Can not get title.", e);
			throw new IllegalStateException(e);
		}
	}

	@Override
	public void setTitle(String title) {
		try {
			this.meta.setTitle(title);
		} catch (XMPException e) {
			LOGGER.error("Can not set title.", e);
			throw new IllegalStateException(e);
		}
	}

	@Override
	public String getSubject() {
		try {
			return this.meta.getDescription();
		} catch (XMPException e) {
			LOGGER.error("Can not get subject.", e);
			throw new IllegalStateException(e);
		}
	}

	@Override
	public void setSubject(String description) {
		try {
			this.meta.setDescription(description);
		} catch (XMPException e) {
			LOGGER.error("Can not set description.", e);
			throw new IllegalStateException(e);
		}
	}

	@Override
	public String getAuthor() {
		try {
			List<String> creators = this.meta.getCreator();
			if (creators == null) {
				return null;
			}
			if (creators.size() > 1) {
				StringBuilder builder = new StringBuilder();
				for (String str : creators) {
					builder.append(str).append(", ");
				}
				List<String> res = new ArrayList<>(1);
				String s = builder.toString();
				res.add(s.substring(0, s.length() - 2));
				this.meta.setCreator(res);
				return res.get(0);
			} else {
				return creators.size() == 0 ? null : creators.get(0);
			}
		} catch (XMPException e) {
			LOGGER.error("Can not get creator.", e);
			throw new IllegalStateException(e);
		}
	}

	@Override
	public void setAuthor(String creator) {
		try {
			List<String> res = new ArrayList<>(1);
			res.add(creator);
			this.meta.setCreator(res);
		} catch (XMPException e) {
			LOGGER.error("Can not set creator.", e);
			throw new IllegalStateException(e);
		}
	}

}

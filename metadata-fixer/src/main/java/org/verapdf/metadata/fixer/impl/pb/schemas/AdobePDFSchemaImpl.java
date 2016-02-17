package org.verapdf.metadata.fixer.impl.pb.schemas;

import com.adobe.xmp.XMPException;
import com.adobe.xmp.impl.VeraPDFMeta;
import org.apache.log4j.Logger;
import org.verapdf.metadata.fixer.entity.Metadata;
import org.verapdf.metadata.fixer.schemas.AdobePDF;

/**
 * @author Evgeniy Muravitskiy
 */
public class AdobePDFSchemaImpl extends BasicSchemaImpl implements AdobePDF {
	private static final Logger LOGGER = Logger.getLogger(AdobePDFSchemaImpl.class);

	public AdobePDFSchemaImpl(VeraPDFMeta meta, Metadata metadata) {
		super(meta, metadata);
	}

	@Override
	public String getProducer() {
		try {
			return this.meta.getProducer();
		} catch (XMPException e) {
			LOGGER.error("Can not get producer.", e);
			throw new IllegalStateException(e);
		}
	}

	@Override
	public void setProducer(String producer) {
		try {
			this.meta.setProducer(producer);
		} catch (XMPException e) {
			LOGGER.error("Can not set producer.", e);
			throw new IllegalStateException(e);
		}
	}

	@Override
	public String getKeywords() {
		try {
			return this.meta.getKeywords();
		} catch (XMPException e) {
			LOGGER.error("Can not get keywords.", e);
			throw new IllegalStateException(e);
		}
	}

	@Override
	public void setKeywords(String keywords) {
		try {
			this.meta.setKeywords(keywords);
		} catch (XMPException e) {
			LOGGER.error("Can not set keywords.", e);
			throw new IllegalStateException(e);
		}
	}

}

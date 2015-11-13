package org.verapdf.model.impl.pb.cos;

import org.apache.log4j.Logger;
import org.apache.pdfbox.cos.COSName;
import org.verapdf.model.coslayer.CosUnicodeName;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

/**
 * Implementation of Unicode name
 *
 * @author Evgeniy Muravitskiy
 */
public class PBCosUnicodeName extends PBCosName implements CosUnicodeName {

	public static final Logger LOGGER = Logger.getLogger(PBCosUnicodeName.class);

	public static final String COS_UNICODE_NAME_TYPE = "CosUnicodeName";

	/**
	 * Default constructor.
	 *
	 * @param cosName Apache pdfbox
	 */
	public PBCosUnicodeName(COSName cosName) {
		super(cosName, COS_UNICODE_NAME_TYPE);
	}

	/**
	 * @return true if name is valid UTF-8 string
	 */
	// TODO : check implementation correctness
	@Override
	public Boolean getisValidUtf8() {
		String name = ((COSName) this.baseObject).getName();
		CharsetDecoder charsetDecoder = Charset.forName("UTF-8").newDecoder();
		ByteBuffer wrap = ByteBuffer.wrap(name.getBytes());
		try {
			charsetDecoder.decode(wrap);
			return Boolean.TRUE;
		} catch (CharacterCodingException e) {
			LOGGER.info("Name is not valid utf-8 string", e);
			return Boolean.FALSE;
		}
	}

	/**
	 * @return converted to UTF-8 name
	 */
	// TODO : check implementation correctness
	@Override
	public String getunicodeValue() {
		String name = ((COSName) this.baseObject).getName();
		byte[] bytes = name.getBytes();
		try {
			return new String(bytes, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			LOGGER.warn("Can not transform " + name + " to unicode string.", e);
			return null;
		}
	}

}

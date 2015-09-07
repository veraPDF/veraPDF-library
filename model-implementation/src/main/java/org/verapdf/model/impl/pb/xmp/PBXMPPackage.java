package org.verapdf.model.impl.pb.xmp;

import org.apache.xmpbox.XMPMetadata;
import org.verapdf.model.factory.xmp.PBSchemaFactory;
import org.verapdf.model.xmplayer.XMPPackage;
import org.verapdf.model.xmplayer.XMPSchema;

import java.util.ArrayList;
import java.util.List;

/**
 * Current class is representation of XMPPackage interface from abstract model
 * based on xmpbox from pdfbox.
 *
 * @author Maksim Bezrukov
 */
public class PBXMPPackage extends PBXMPObject implements XMPPackage {

	public static final String XMP_PACKAGE_TYPE = "XMPPackage";

	public static final String SCHEMAS = "Schemas";

	private XMPMetadata xmpMetadata;
	private boolean isMetadataValid;

	/**
	 * Constructs new object
	 *
	 * @param xmpMetadata     - object from xmpbox represented this package
	 * @param isMetadataValid - true if metadata is valid
	 */
	public PBXMPPackage(XMPMetadata xmpMetadata, boolean isMetadataValid) {
		super(XMP_PACKAGE_TYPE);
		this.xmpMetadata = xmpMetadata;
		this.isMetadataValid = isMetadataValid;
	}

	/**
	 * Constructs new object
	 *
	 * @param xmpMetadata     object from xmpbox represented this package
	 * @param isMetadataValid true if metadata is valid
	 * @param type            type of current object
	 */
	public PBXMPPackage(XMPMetadata xmpMetadata, boolean isMetadataValid,
						final String type) {
		super(type);
		this.xmpMetadata = xmpMetadata;
		this.isMetadataValid = isMetadataValid;
	}

	/**
	 * @return true if metadata is valid
	 */
	@Override
	public Boolean getisMetadataValid() {
		return Boolean.valueOf(isMetadataValid);
	}

	/**
	 * @return bytes attribute of the xmp packet
	 */
	@Override
	public String getbytes() {
		return xmpMetadata == null ? null : xmpMetadata.getXpacketBytes();
	}

	/**
	 * @return encoding attribute of the xmp packet
	 */
	@Override
	public String getencoding() {
		return xmpMetadata == null ? null : xmpMetadata.getXpacketEncoding();
	}

	/**
	 * @param link name of the link
	 * @return List of all objects with link name
	 */
	@Override
	public List<? extends org.verapdf.model.baselayer.Object> getLinkedObjects(
			String link) {
		if (SCHEMAS.equals(link)) {
			return this.getSchemas();
		}
		return super.getLinkedObjects(link);
	}

	protected XMPMetadata getXmpMetadata() {
		return xmpMetadata;
	}

	private List<XMPSchema> getSchemas() {
		List<XMPSchema> resultSchemas = new ArrayList<>();

		if (xmpMetadata != null && xmpMetadata.getAllSchemas() != null) {
			for (org.apache.xmpbox.schema.XMPSchema pbschema : xmpMetadata
					.getAllSchemas()) {
				if (pbschema != null) {
					XMPSchema schema = PBSchemaFactory.createSchema(pbschema);
					resultSchemas.add(schema);
				}
			}
		}

		return resultSchemas;
	}
}

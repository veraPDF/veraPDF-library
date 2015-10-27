package org.verapdf.model.impl.pb.xmp;

import org.apache.xmpbox.XMPMetadata;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.factory.xmp.PBSchemaFactory;
import org.verapdf.model.xmplayer.XMPPackage;
import org.verapdf.model.xmplayer.XMPSchema;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Current class is representation of XMPPackage interface from
 * abstract model based on xmpbox from pdfbox.
 *
 * @author Maksim Bezrukov
 */
public class PBXMPPackage extends PBXMPObject implements XMPPackage {

	public static final String XMP_PACKAGE_TYPE = "XMPPackage";

	public static final String SCHEMAS = "Schemas";

	private XMPMetadata xmpMetadata;
	private boolean isMetadataValid;
	private String errorMessage;

	/**
	 * Constructs new object
	 *
	 * @param xmpMetadata     object from xmpbox represented this package
	 * @param isMetadataValid true if metadata is valid
	 * @param errorMessage    message from the error if metadata is not valid
	 */
	public PBXMPPackage(XMPMetadata xmpMetadata, boolean isMetadataValid, String errorMessage) {
		this(xmpMetadata, isMetadataValid, XMP_PACKAGE_TYPE, errorMessage);
	}

	protected PBXMPPackage(XMPMetadata xmpMetadata, boolean isMetadataValid,
						   final String type, String errorMessage) {
		super(type);
		this.xmpMetadata = xmpMetadata;
		this.isMetadataValid = isMetadataValid;
		this.errorMessage = errorMessage;
	}

	/**
	 * @return true if metadata is valid
	 */
	@Override
	public Boolean getisMetadataValid() {
		return Boolean.valueOf(isMetadataValid);
	}

	@Override
	public String geterrorMessage() {
		return errorMessage;
	}

	/**
	 * @return bytes attribute of the xmp packet
	 */
	@Override
	public String getbytes() {
		return this.xmpMetadata == null ? null : this.xmpMetadata.getXpacketBytes();
	}

	/**
	 * @return encoding attribute of the xmp packet
	 */
	@Override
	public String getencoding() {
		return this.xmpMetadata == null ? null : this.xmpMetadata.getXpacketEncoding();
	}

	/**
	 * @param link name of the link
	 * @return List of all objects with link name
	 */
	@Override
	public List<? extends Object> getLinkedObjects(String link) {
		if (SCHEMAS.equals(link)) {
			return this.getSchemas();
		}
		return super.getLinkedObjects(link);
	}

	protected XMPMetadata getXmpMetadata() {
		return this.xmpMetadata;
	}

	private List<XMPSchema> getSchemas() {
		if (this.xmpMetadata != null && this.xmpMetadata.getAllSchemas() != null) {
			List<XMPSchema> resultSchemas = new ArrayList<>();
			for (org.apache.xmpbox.schema.XMPSchema pbschema : this.xmpMetadata
					.getAllSchemas()) {
				XMPSchema schema = PBSchemaFactory.createSchema(pbschema);
				if (schema != null) {
					resultSchemas.add(schema);
				}
			}
			return Collections.unmodifiableList(resultSchemas);
		}

		return Collections.emptyList();
	}

}

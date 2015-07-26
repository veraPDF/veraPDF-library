package org.verapdf.model.impl.pb.pd;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.cos.COSStream;
import org.apache.xmpbox.XMPMetadata;
import org.apache.xmpbox.xml.DomXmpParser;
import org.apache.xmpbox.xml.XmpParsingException;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.coslayer.CosStream;
import org.verapdf.model.impl.pb.cos.PBCosStream;
import org.verapdf.model.impl.pb.xmp.PBXMPMainPackage;
import org.verapdf.model.impl.pb.xmp.PBXMPPackage;
import org.verapdf.model.pdlayer.PDMetadata;
import org.verapdf.model.xmplayer.XMPPackage;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBoxPDMetadata extends PBoxPDObject implements PDMetadata {

	private static final Logger LOGGER = Logger.getLogger(PBoxPDMetadata.class);

    /**
     * String name for XMP Package
     */
	public static final String XMP_PACKAGE = "XMPPackage";
    /**
     * String name for stream
     */
	public static final String STREAM = "stream";

	private Boolean isMainMetadata;

    /**
     * @param simplePDObject
     *            a {@link org.apache.pdfbox.pdmodel.common.PDMetadata} used to
     *            populate the instance
     * @param isMainMetadata
     *            a {@link Boolean} that should be true if this is the main
     *            metadata block
     */
    public PBoxPDMetadata(org.apache.pdfbox.pdmodel.common.PDMetadata simplePDObject, Boolean isMainMetadata) {
        super(simplePDObject);
		this.isMainMetadata = isMainMetadata;
        setType("PDMetadata");
    }

    @Override
    public String getFilter() {
		List<COSName> filters = ((org.apache.pdfbox.pdmodel.common.PDMetadata) simplePDObject).getFilters();
		if (filters != null && filters.size() > 0) {
			StringBuilder result = new StringBuilder();
			for (COSName filter : filters) {
				result.append(filter.getName()).append(' ');
			}
			return result.substring(0, result.length() - 1);
		}
        return null;
    }

	@Override
	public List<? extends Object> getLinkedObjects(String link) {
		List<? extends Object> list;

		switch (link) {
			case XMP_PACKAGE:
				list = getXMPPackage();
				break;
			case STREAM:
				list = getStream();
				break;
			default:
				list = super.getLinkedObjects(link);
				break;
		}

		return list;
	}

	private List<XMPPackage> getXMPPackage() {
		List<XMPPackage> xmp = new ArrayList<>(1);
		try {
		        COSStream stream = ((org.apache.pdfbox.pdmodel.common.PDMetadata) simplePDObject).getStream();
		        if (stream != null) {
				DomXmpParser xmpParser = new DomXmpParser();
				XMPMetadata metadata = xmpParser.parse(stream.getUnfilteredStream());
				xmp.add(isMainMetadata.booleanValue() ? new PBXMPMainPackage(metadata, true) : new PBXMPPackage(metadata, true));
			}
		} catch (XmpParsingException e) {
			LOGGER.error("Problems with parsing metadata. " + e.getMessage(), e);
			xmp.add(new PBXMPPackage(null, false));
		} catch (IOException e) {
			LOGGER.error("Metadata stream is closed. " + e.getMessage(), e);
			xmp.add(new PBXMPPackage(null, false));
		}
		return xmp;
	}

	private List<CosStream> getStream() {
		List<CosStream> streams = new ArrayList<>(1);
		COSStream stream = ((org.apache.pdfbox.pdmodel.common.PDMetadata) simplePDObject).getStream();
		streams.add(new PBCosStream(stream));
		return streams;
	}
}
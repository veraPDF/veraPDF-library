package org.verapdf.model.impl.pb.pd;

import com.adobe.xmp.XMPException;
import com.adobe.xmp.impl.VeraPDFMeta;
import org.apache.log4j.Logger;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.cos.COSStream;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.coslayer.CosStream;
import org.verapdf.model.impl.axl.AXLMainXMPPackage;
import org.verapdf.model.impl.axl.AXLXMPPackage;
import org.verapdf.model.impl.pb.cos.PBCosStream;
import org.verapdf.model.pdlayer.PDMetadata;
import org.verapdf.model.xmplayer.XMPPackage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBoxPDMetadata extends PBoxPDObject implements PDMetadata {

    private static final Logger LOGGER = Logger.getLogger(PBoxPDMetadata.class);

    public static final String METADATA_TYPE = "PDMetadata";

    public static final String XMP_PACKAGE = "XMPPackage";
    public static final String STREAM = "stream";

    private boolean isMainMetadata;

    public PBoxPDMetadata(
            org.apache.pdfbox.pdmodel.common.PDMetadata simplePDObject,
            Boolean isMainMetadata) {
        super(simplePDObject, METADATA_TYPE);
        this.isMainMetadata = isMainMetadata.booleanValue();
    }

    @Override
    public String getFilter() {
        List<COSName> filters = ((org.apache.pdfbox.pdmodel.common.PDMetadata) this.simplePDObject)
                .getFilters();
        if (filters != null && !filters.isEmpty()) {
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
		switch (link) {
			case XMP_PACKAGE:
				return this.getXMPPackage();
			case STREAM:
				return this.getStream();
			default:
				return super.getLinkedObjects(link);
		}
	}

    private List<XMPPackage> getXMPPackage() {
        List<XMPPackage> xmp = new ArrayList<>(MAX_NUMBER_OF_ELEMENTS);
        try {
            COSStream stream = ((org.apache.pdfbox.pdmodel.common.PDMetadata) this.simplePDObject)
                    .getStream();
            if (stream != null) {
                VeraPDFMeta metadata = VeraPDFMeta.parse(stream.getUnfilteredStream());
                //TODO: change null schema definition in not main package to extension schema container from main package
                xmp.add(this.isMainMetadata ? new AXLMainXMPPackage(metadata, true)
                        : new AXLXMPPackage(metadata, true, null));
            }
        } catch (XMPException | IOException e) {
            LOGGER.debug("Problems with parsing metadata. " + e.getMessage(), e);
            xmp.add(new AXLXMPPackage(null, false, null));
        }
        return xmp;
    }

    private List<CosStream> getStream() {
        COSStream stream = ((org.apache.pdfbox.pdmodel.common.PDMetadata) this.simplePDObject)
                .getStream();
		if (stream != null) {
			List<CosStream> streams = new ArrayList<>(MAX_NUMBER_OF_ELEMENTS);
			streams.add(new PBCosStream(stream));
			return Collections.unmodifiableList(streams);
		}
        return Collections.emptyList();
    }
}
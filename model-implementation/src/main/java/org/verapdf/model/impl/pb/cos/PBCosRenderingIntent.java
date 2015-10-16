package org.verapdf.model.impl.pb.cos;

import org.apache.pdfbox.cos.COSName;
import org.verapdf.model.coslayer.CosRenderingIntent;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBCosRenderingIntent extends PBCosName implements
        CosRenderingIntent {

    /** Type name for PBCosRenderingIntent */
    public static final String COS_RENDERING_INTENT_TYPE = "CosRenderingIntent";

    /**
     * Default constructor
     * @param value pdfbox COSName
     */
    public PBCosRenderingIntent(COSName value) {
        super(value, COS_RENDERING_INTENT_TYPE);
    }
}

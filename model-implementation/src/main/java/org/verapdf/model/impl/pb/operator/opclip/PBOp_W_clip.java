package org.verapdf.model.impl.pb.operator.opclip;

import org.apache.pdfbox.cos.COSBase;
import org.verapdf.model.operator.Op_W_clip;

import java.util.List;

/**
 * Operator which modifies the current clipping path by intersecting
 * it with the current path, using the nonzero winding number rule
 * to determine which regions lie inside the clipping path
 *
 * @author Timur Kamalov
 */
public class PBOp_W_clip extends PBOpClip implements Op_W_clip {

	/** Type name for {@code PBOp_W_clip} */
    public static final String OP_W_CLIP_TYPE = "Op_W_clip";

    public PBOp_W_clip(List<COSBase> arguments) {
        super(arguments, OP_W_CLIP_TYPE);
    }

}

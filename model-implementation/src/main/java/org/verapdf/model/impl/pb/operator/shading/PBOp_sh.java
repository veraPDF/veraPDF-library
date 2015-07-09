package org.verapdf.model.impl.pb.operator.shading;

import org.apache.log4j.Logger;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.pdmodel.graphics.pattern.PDShadingPattern;
import org.verapdf.model.impl.pb.pd.pattern.PBoxPDShading;
import org.verapdf.model.operator.Op_sh;
import org.verapdf.model.pdlayer.PDShading;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Timur Kamalov
 */
public class PBOp_sh extends PBOpShading implements Op_sh {

	public static final Logger logger = Logger.getLogger(PBOp_sh.class);

    public static final String OP_SH_TYPE = "Op_sh";

    public static final String SHADING = "shading";

	private PDShadingPattern shadingPattern;

    public PBOp_sh(List<COSBase> arguments, PDShadingPattern shadingPattern) {
        super(arguments);
		this.shadingPattern = shadingPattern;
        setType(OP_SH_TYPE);
    }

    @Override
    public List<? extends org.verapdf.model.baselayer.Object> getLinkedObjects(String link) {
        List<? extends org.verapdf.model.baselayer.Object> list;

        switch (link) {
            case SHADING:
                list = this.getShading();
                break;
            default: list = super.getLinkedObjects(link);
        }

        return list;
    }

    private List<PDShading> getShading() {
        List<PDShading> list = new ArrayList<>();
        if (this.shadingPattern != null) {
			try {
				org.apache.pdfbox.pdmodel.graphics.shading.PDShading shading = this.shadingPattern.getShading();
				if (shading != null) {
					list.add(new PBoxPDShading(shading));
				}
			} catch (IOException e) {
				logger.error("Problems with shading obtaining. " + e.getMessage());
			}
		}
        return list;
    }

}

package org.verapdf.model.impl.pb.cos;

import org.verapdf.model.coslayer.CosXRef;

/**
 * Class describe special properties of cross reference table of current
 * document
 *
 * @author Evgeniy Muravitskiy
 */
public class PBCosXRef extends PBCosObject implements CosXRef {

    /** Type name for PBCosXRef */
    public static final String COS_XREF_TYPE = "CosXRef";

    private Boolean xrefHeaderSpacingsComplyPDFA;
    private Boolean xrefEOLSpacingsComplyPDFA;

    /**
     * Default constructor
     * @param xrefHeaderSpacingsComplyPDFA is xref header spacings comply pdfa specification
     * @param xrefEOLSpacingsComplyPDFA is xref eol spacings comply pdfa
     */
    public PBCosXRef(Boolean xrefHeaderSpacingsComplyPDFA,
            Boolean xrefEOLSpacingsComplyPDFA) {
        super(null, COS_XREF_TYPE);
        this.xrefHeaderSpacingsComplyPDFA = xrefHeaderSpacingsComplyPDFA;
        this.xrefEOLSpacingsComplyPDFA = xrefEOLSpacingsComplyPDFA;
    }

    /**
     * true if header of cross reference table complies PDF/A standard
     */
    @Override
    public Boolean getxrefHeaderSpacingsComplyPDFA() {
        return this.xrefHeaderSpacingsComplyPDFA;
    }

    /**
     * true if EOL
     */
    @Override
    public Boolean getxrefEOLMarkersComplyPDFA() {
        return xrefEOLSpacingsComplyPDFA;
    }
}

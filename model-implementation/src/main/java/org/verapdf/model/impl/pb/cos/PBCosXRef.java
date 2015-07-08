package org.verapdf.model.impl.pb.cos;

import org.verapdf.model.coslayer.CosXRef;

/**
 * Class describe special properties of cross reference table of current document
 *
 * @author Evgeniy Muravitskiy
 */
public class PBCosXRef extends PBCosObject implements CosXRef {

	private Boolean xrefHeaderSpacingsComplyPDFA;
	private Boolean xrefEOLSpacingsComplyPDFA;

	public PBCosXRef(Boolean xrefHeaderSpacingsComplyPDFA, Boolean xrefEOLSpacingsComplyPDFA) {
		super(null);
		setType("CosXRef");
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

package org.verapdf.model.factory.operator;

import org.apache.log4j.Logger;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.graphics.PDXObject;
import org.apache.pdfbox.pdmodel.graphics.color.PDColorSpace;
import org.apache.pdfbox.pdmodel.graphics.color.PDDeviceCMYK;
import org.apache.pdfbox.pdmodel.graphics.color.PDDeviceGray;
import org.apache.pdfbox.pdmodel.graphics.color.PDDeviceRGB;
import org.apache.pdfbox.pdmodel.graphics.pattern.PDAbstractPattern;
import org.apache.pdfbox.pdmodel.graphics.shading.PDShading;
import org.apache.pdfbox.pdmodel.graphics.state.PDExtendedGraphicsState;
import org.apache.pdfbox.pdmodel.graphics.state.RenderingIntent;
import org.verapdf.model.impl.pb.operator.color.PBOpColor;
import org.verapdf.model.impl.pb.operator.generalgs.*;
import org.verapdf.model.impl.pb.operator.inlineimage.PBOpInlineImage;
import org.verapdf.model.impl.pb.operator.markedcontent.*;
import org.verapdf.model.impl.pb.operator.opclip.PBOp_WStar;
import org.verapdf.model.impl.pb.operator.opclip.PBOp_W_clip;
import org.verapdf.model.impl.pb.operator.opcompability.PBOp_BX;
import org.verapdf.model.impl.pb.operator.opcompability.PBOp_EX;
import org.verapdf.model.impl.pb.operator.opcompability.PBOp_Undefined;
import org.verapdf.model.impl.pb.operator.pathconstruction.*;
import org.verapdf.model.impl.pb.operator.pathpaint.*;
import org.verapdf.model.impl.pb.operator.shading.PBOp_sh;
import org.verapdf.model.impl.pb.operator.specialgs.PBOp_Q_grestore;
import org.verapdf.model.impl.pb.operator.specialgs.PBOp_cm;
import org.verapdf.model.impl.pb.operator.specialgs.PBOp_q_gsave;
import org.verapdf.model.impl.pb.operator.textobject.PBOpTextObject;
import org.verapdf.model.impl.pb.operator.textposition.PBOpTextPosition;
import org.verapdf.model.impl.pb.operator.textshow.PBOpTextShow;
import org.verapdf.model.impl.pb.operator.textstate.PBOpTextState;
import org.verapdf.model.impl.pb.operator.type3font.PBOpType3Font;
import org.verapdf.model.impl.pb.operator.xobject.PBOp_Do;
import org.verapdf.model.operator.Operator;
import org.verapdf.model.tools.constants.Operators;

import java.io.IOException;
import java.util.*;

/**
 * @author Timur Kamalov
 */
public final class OperatorFactory {

	public static final Logger logger = Logger.getLogger(OperatorFactory.class);

	public static final Integer REQIURED_RESOURCES_COUNT = Integer.valueOf(6);
	public static final Integer STROKE_COLOR_SPACE = Integer.valueOf(0);
	public static final Integer FILL_COLOR_SPACE = Integer.valueOf(1);
	public static final Integer EXT_G_STATE = Integer.valueOf(2);
	public static final Integer PATTERN = Integer.valueOf(3);
	public static final Integer XOBJECT = Integer.valueOf(4);
	public static final Integer SHADING = Integer.valueOf(5);
	public static final Integer NESTING_LEVEL = Integer.valueOf(6);

	public static Operator parseOperator(org.apache.pdfbox.contentstream.operator.Operator pdfBoxOperator,
										 List<COSBase> arguments, Map<Integer, Object> resources) {
        String operatorName = pdfBoxOperator.getName();
        switch (operatorName) {
            // GENERAL GS
            case Operators.D_SET_DASH : return new PBOp_d(arguments);
            case Operators.GS : return new PBOp_gs(arguments, (PDExtendedGraphicsState) resources.get(EXT_G_STATE));
            case Operators.I_SETFLAT : return new PBOp_i(arguments);
            case Operators.J_LINE_CAP : return new PBOp_J_line_cap(arguments);
            case Operators.J_LINE_JOIN : return new PBOp_j_line_join(arguments);
            case Operators.M_MITER_LIMIT : return new PBOp_M_miter_limit(arguments);
            case Operators.RI : return new PBOp_ri(arguments);
            case Operators.W_LINE_WIDTH : return new PBOp_w_line_width(arguments);

            // MARKED CONTENT
            case Operators.BMC : return new PBOp_BMC(arguments);
            case Operators.BDC : return new PBOp_BDC(arguments);
            case Operators.EMC : return new PBOp_EMC(arguments);
            case Operators.MP : return new PBOp_MP(arguments);
            case Operators.DP : return new PBOp_DP(arguments);

            // CLIP
            case Operators.W_CLIP : return new PBOp_W_clip(arguments);
            case Operators.W_STAR_EOCLIP : return new PBOp_WStar(arguments);

			// COLOR
			case Operators.CS_STROKE:
			case Operators.CS_FILL:
			case Operators.SCN_STROKE:
			case Operators.SCN_FILL:
			case Operators.SC_STROKE:
			case Operators.SC_FILL:
			case Operators.G_STROKE:
			case Operators.G_FILL:
			case Operators.RG_STROKE:
			case Operators.RG_FILL:
			case Operators.K_STROKE:
			case Operators.K_FILL: return new PBOpColor(arguments);

			// TEXT OBJECT
			case Operators.ET:
			case Operators.BT: return new PBOpTextObject(arguments);

			// TEXT POSITION
			case Operators.TD_MOVE:
			case Operators.TD_MOVE_SET_LEADING:
			case Operators.TM:
			case Operators.T_STAR: return new PBOpTextPosition(arguments);

			// TEXT SHOW
			case Operators.TJ_SHOW: return new PBOp_Tj(arguments);
			case Operators.TJ_SHOW_POS: return new PBOp_TJ_Big(arguments);
			case Operators.QUOTE: return new PBOp_Quote(arguments);
			case Operators.DOUBLE_QUOTE: return new PBOp_DoubleQuote(arguments);

			// TEXT STATE
			case Operators.TC:
			case Operators.TW:
			case Operators.TZ:
			case Operators.TL:
			case Operators.TF:
			case Operators.TR:
			case Operators.TS: return new PBOpTextState(arguments);

			// TYPE 3 FONT
			case Operators.D0:
			case Operators.D1: return new PBOpType3Font(arguments);

			// INLINE IMAGE
			case Operators.BI:
			case Operators.ID:
			case Operators.EI: {
				arguments.add(pdfBoxOperator.getImageParameters());
				return new PBOpInlineImage(arguments);
			}

            // COMPABILITY
            case Operators.BX : return new PBOp_BX(arguments);
            case Operators.EX : return new PBOp_EX(arguments);

            // PATH CONSTRUCTION
            case Operators.C_CURVE_TO : return new PBOp_c(arguments);
            case Operators.H_CLOSEPATH : return new PBOp_h(arguments);
            case Operators.L_LINE_TO : return new PBOp_l(arguments);
            case Operators.M_MOVE_TO : return new PBOp_m_moveto(arguments);
            case Operators.RE : return new PBOp_re(arguments);
            case Operators.V : return new PBOp_v(arguments);
            case Operators.Y : return new PBOp_y(arguments);

            // PATH PAINT
            case Operators.B_CLOSEPATH_FILL_STROKE : return new PBOp_b_closepath_fill_stroke(arguments,
					(PDColorSpace) resources.get(STROKE_COLOR_SPACE),
					(PDColorSpace) resources.get(FILL_COLOR_SPACE));
            case Operators.B_FILL_STROKE : return new PBOp_B_fill_stroke(arguments,
					(PDColorSpace) resources.get(STROKE_COLOR_SPACE),
					(PDColorSpace) resources.get(FILL_COLOR_SPACE));
            case Operators.B_STAR_CLOSEPATH_EOFILL_STROKE : return new PBOp_bstar_closepath_eofill_stroke(arguments,
					(PDColorSpace) resources.get(STROKE_COLOR_SPACE),
					(PDColorSpace) resources.get(FILL_COLOR_SPACE));
            case Operators.B_STAR_EOFILL_STROKE : return new PBOp_BStar_eofill_stroke(arguments,
					(PDColorSpace) resources.get(STROKE_COLOR_SPACE),
					(PDColorSpace) resources.get(FILL_COLOR_SPACE));
            case Operators.F_FILL : return new PBOp_f_fill(arguments,
					(PDColorSpace) resources.get(FILL_COLOR_SPACE));
            case Operators.F_FILL_OBSOLETE : return new PBOp_F_fill_obsolete(arguments,
					(PDColorSpace) resources.get(FILL_COLOR_SPACE));
            case Operators.F_STAR_FILL : return new PBOp_FStar(arguments,
					(PDColorSpace) resources.get(FILL_COLOR_SPACE));
            case Operators.N : return new PBOp_n(arguments);
            case Operators.S_CLOSE_STROKE : return new PBOp_s_close_stroke(arguments,
					(PDColorSpace) resources.get(STROKE_COLOR_SPACE));
            case Operators.S_STROKE : return new PBOp_S_stroke(arguments,
					(PDColorSpace) resources.get(STROKE_COLOR_SPACE));

            // SHADING
            case Operators.SH : return new PBOp_sh(arguments, (PDShading) resources.get(SHADING));

            // SPECIAL GS
            case Operators.CM_CONCAT : return new PBOp_cm(arguments);
            case Operators.Q_GRESTORE : return new PBOp_Q_grestore(arguments);
            case Operators.Q_GSAVE : return new PBOp_q_gsave(arguments, (Integer) resources.get(NESTING_LEVEL));

            // XOBJECT
            case Operators.DO : return new PBOp_Do(arguments, (PDXObject) resources.get(XOBJECT));

            default: return new PBOp_Undefined(arguments);
        }
    }

    public static List<Operator> parseOperators(List<Object> pdfBoxTokens, PDResources resources) {
		Map<Integer, Object> currentResources = new HashMap<>(REQIURED_RESOURCES_COUNT);
		currentResources.put(NESTING_LEVEL, Integer.valueOf(0));
		return getOperators(pdfBoxTokens.iterator(), resources, currentResources);
    }


	private static List<Operator> getOperators(Iterator<Object> pdfBoxTokens, PDResources resources,
											   Map<Integer, Object> defaultResources) {
		List<Operator> result = new ArrayList<>();
		Map<Integer, Object> currentResources = new HashMap<>(defaultResources.size());
		List<COSBase> arguments = new ArrayList<>();
		currentResources.putAll(defaultResources);

		while (pdfBoxTokens.hasNext()){
			Object pdfBoxToken = pdfBoxTokens.next();
			if (pdfBoxToken instanceof COSBase) {
				arguments.add((COSBase) pdfBoxToken);
			} else if (pdfBoxToken instanceof org.apache.pdfbox.contentstream.operator.Operator) {
				checkResourcesRelevance((org.apache.pdfbox.contentstream.operator.Operator) pdfBoxToken,
						arguments, resources, currentResources);
				result.add(parseOperator((org.apache.pdfbox.contentstream.operator.Operator) pdfBoxToken,
						arguments, currentResources));
				arguments.clear();
				String operatorName = ((org.apache.pdfbox.contentstream.operator.Operator) pdfBoxToken).getName();
				if (Operators.Q_GSAVE.equals(operatorName)) {
					changeNestingLevel(defaultResources, Boolean.TRUE);
					result.addAll(getOperators(pdfBoxTokens, resources, currentResources));
				} else if (Operators.Q_GRESTORE.equals(operatorName)) {
					changeNestingLevel(defaultResources, Boolean.FALSE);
					return result;
				}
			} else if (pdfBoxToken instanceof RenderingIntent){
				String value = ((RenderingIntent) pdfBoxToken).stringValue();
				arguments.add(COSName.getPDFName(value));
			} else {
				logger.error("Unexpected type of object in tokens: " + pdfBoxToken.getClass().getName());
			}
		}
		return result;
	}

	private static void checkResourcesRelevance(org.apache.pdfbox.contentstream.operator.Operator pdfBoxToken,
												List<COSBase> arguments,
												PDResources resources,
												Map<Integer, Object> currentResources) {
		COSBase lastElement = null;
		switch (pdfBoxToken.getName()) {
			case Operators.G_STROKE:
				currentResources.put(STROKE_COLOR_SPACE, PDDeviceGray.INSTANCE);
				break;
			case Operators.G_FILL:
				currentResources.put(FILL_COLOR_SPACE, PDDeviceGray.INSTANCE);
				break;
			case Operators.RG_STROKE:
				currentResources.put(STROKE_COLOR_SPACE, PDDeviceRGB.INSTANCE);
				break;
			case Operators.RG_FILL:
				currentResources.put(FILL_COLOR_SPACE, PDDeviceRGB.INSTANCE);
				break;
			case Operators.K_STROKE:
				currentResources.put(STROKE_COLOR_SPACE, PDDeviceCMYK.INSTANCE);
				break;
			case Operators.K_FILL:
				currentResources.put(FILL_COLOR_SPACE, PDDeviceCMYK.INSTANCE);
				break;
			default: lastElement = arguments.size() > 0 ? arguments.get(arguments.size() - 1) : null;
		}
		if (lastElement instanceof COSName) {
			try {
				switch (pdfBoxToken.getName()) {
					case Operators.CS_STROKE:
						currentResources.put(STROKE_COLOR_SPACE, resources.getColorSpace((COSName) lastElement));
						break;
					case Operators.CS_FILL:
						currentResources.put(FILL_COLOR_SPACE, resources.getColorSpace((COSName) lastElement));
						break;
					// TODO : do we need use color spaces for sc operators?
					case Operators.SCN_STROKE:
						setResource((COSName) lastElement, resources, currentResources, STROKE_COLOR_SPACE);
						break;
					case Operators.SCN_FILL:
						setResource((COSName) lastElement, resources, currentResources, FILL_COLOR_SPACE);
						break;
					case Operators.GS:
						currentResources.put(EXT_G_STATE, resources.getExtGState((COSName) lastElement));
						break;
					case Operators.SH:
						PDShading shading = resources.getShading((COSName) lastElement);
						currentResources.put(SHADING, shading);
						break;
					case Operators.DO:
						currentResources.put(XOBJECT, resources.getXObject((COSName) lastElement));
						break;
				}
			} catch (IOException e) {
				logger.error("Problems with resources obtaining for " + pdfBoxToken.getName() + ". " + e.getMessage());
			}
		}
	}

	private static void setResource(COSName resourceName, PDResources resources,
									Map<Integer, Object> currentResources,
									Integer mode) throws IOException {
		PDColorSpace colorSpace = resources.getColorSpace(resourceName);
		PDAbstractPattern pattern = resources.getPattern(resourceName);
		Boolean conflict = Boolean.valueOf(colorSpace != null && pattern != null);
		if (!conflict) {
			if (colorSpace != null) {
				currentResources.put(mode, colorSpace);
			} else {
				// TODO : is this correct for PDF/A validation?
				// examples of patterns use says that for using pattern in scn(SCN) before must be
				// operator cs(CS) with /Pattern operand
				currentResources.put(PATTERN, pattern);
			}
		} else {
			// TODO : implement this. Now it`s not correct work. Need to find /Pattern cs(CS)
			currentResources.put(mode, colorSpace);
		}
	}

	public static void changeNestingLevel(Map<Integer, Object> currentResources, Boolean increment) {
		Integer nestingLevel = (Integer) currentResources.get(NESTING_LEVEL);
		currentResources.put(NESTING_LEVEL, increment ? ++nestingLevel : --nestingLevel);
	}

}

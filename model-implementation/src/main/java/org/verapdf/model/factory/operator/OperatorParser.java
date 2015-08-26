/**
 * 
 */
package org.verapdf.model.factory.operator;

import org.apache.log4j.Logger;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSInteger;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.graphics.PDXObject;
import org.apache.pdfbox.pdmodel.graphics.color.PDColorSpace;
import org.apache.pdfbox.pdmodel.graphics.color.PDDeviceCMYK;
import org.apache.pdfbox.pdmodel.graphics.color.PDDeviceGray;
import org.apache.pdfbox.pdmodel.graphics.color.PDDeviceRGB;
import org.apache.pdfbox.pdmodel.graphics.pattern.PDAbstractPattern;
import org.apache.pdfbox.pdmodel.graphics.shading.PDShading;
import org.apache.pdfbox.pdmodel.graphics.state.PDExtendedGraphicsState;
import org.apache.pdfbox.pdmodel.graphics.state.RenderingMode;
import org.verapdf.model.factory.colors.ColorSpaceFactory;
import org.verapdf.model.impl.pb.operator.color.PBOpColor;
import org.verapdf.model.impl.pb.operator.generalgs.*;
import org.verapdf.model.impl.pb.operator.inlineimage.PBOp_BI;
import org.verapdf.model.impl.pb.operator.inlineimage.PBOp_EI;
import org.verapdf.model.impl.pb.operator.inlineimage.PBOp_ID;
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
import org.verapdf.model.impl.pb.operator.textposition.PBOp_TD_Big;
import org.verapdf.model.impl.pb.operator.textposition.PBOp_Td;
import org.verapdf.model.impl.pb.operator.textshow.PBOp_DoubleQuote;
import org.verapdf.model.impl.pb.operator.textshow.PBOp_Quote;
import org.verapdf.model.impl.pb.operator.textshow.PBOp_TJ_Big;
import org.verapdf.model.impl.pb.operator.textshow.PBOp_Tj;
import org.verapdf.model.impl.pb.operator.textstate.PBOpTextState;
import org.verapdf.model.impl.pb.operator.textstate.PBOp_Tr;
import org.verapdf.model.impl.pb.operator.textstate.PBOp_Tz;
import org.verapdf.model.impl.pb.operator.type3font.PBOpType3Font;
import org.verapdf.model.impl.pb.operator.xobject.PBOp_Do;
import org.verapdf.model.operator.Operator;
import org.verapdf.model.tools.constants.Operators;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Stateful parser that create veraPDF Model operator instances from individual
 * PDF Tokens. The parsing process holds some state (previously in the factory).
 * Separated this into it's own class as the parsing is pretty dense.
 * 
 * @author carlwilson
 *
 */
class OperatorParser {

    private static final Logger LOGGER = Logger.getLogger(OperatorParser.class);
    private static final String MSG_PROBEM_OBTAINING_RESOURCE = "Problem encountered while obtaining resources for ";

    private final Stack<GraphicState> graphicStateStack = new Stack<>();
    private final GraphicState graphicState = new GraphicState();

	OperatorParser() {
		// limit the scope
	}

	void parseOperator(List<Operator> operators,
					   org.apache.pdfbox.contentstream.operator.Operator pdfBoxOperator,
					   PDResources resources, List<COSBase> arguments) throws CloneNotSupportedException {
		String operatorName = pdfBoxOperator.getName();
		switch (operatorName) {
			// GENERAL GS
			case Operators.D_SET_DASH:
				operators.add(new PBOp_d(arguments));
				break;
			case Operators.GS:
				PDExtendedGraphicsState extGState = getExtGStateFromResources(resources,
						getLastCOSName(arguments));
				graphicState.copyPropertiesFromExtGState(extGState);
				operators.add(new PBOp_gs(arguments, extGState));
				break;
			case Operators.I_SETFLAT:
				operators.add(new PBOp_i(arguments));
				break;
			case Operators.J_LINE_CAP:
				operators.add(new PBOp_J_line_cap(arguments));
				break;
			case Operators.J_LINE_JOIN:
				operators.add(new PBOp_j_line_join(arguments));
				break;
			case Operators.M_MITER_LIMIT:
				operators.add(new PBOp_M_miter_limit(arguments));
				break;
			case Operators.RI:
				operators.add(new PBOp_ri(arguments));
				break;
			case Operators.W_LINE_WIDTH:
				operators.add(new PBOp_w_line_width(arguments));
				break;

				// MARKED CONTENT
			case Operators.BMC:
				operators.add(new PBOp_BMC(arguments));
				break;
			case Operators.BDC:
				operators.add(new PBOp_BDC(arguments));
				break;
			case Operators.EMC:
				operators.add(new PBOp_EMC(arguments));
				break;
			case Operators.MP:
				operators.add(new PBOp_MP(arguments));
				break;
			case Operators.DP:
				operators.add(new PBOp_DP(arguments));
				break;

				// CLIP
			case Operators.W_CLIP:
				operators.add(new PBOp_W_clip(arguments));
				break;
			case Operators.W_STAR_EOCLIP:
				operators.add(new PBOp_WStar(arguments));
				break;

				// COLOR
			case Operators.G_STROKE:
				graphicState.setStrokeColorSpace(PDDeviceGray.INSTANCE);
				operators.add(new PBOpColor(arguments));
				break;
			case Operators.G_FILL:
				graphicState.setFillColorSpace(PDDeviceGray.INSTANCE);
				operators.add(new PBOpColor(arguments));
				break;
			case Operators.RG_STROKE:
				graphicState.setStrokeColorSpace(PDDeviceRGB.INSTANCE);
				operators.add(new PBOpColor(arguments));
				break;
			case Operators.RG_FILL:
				graphicState.setFillColorSpace(PDDeviceRGB.INSTANCE);
				operators.add(new PBOpColor(arguments));
				break;
			case Operators.K_STROKE:
				operators.add(new PBOpColor(arguments));
				break;
			case Operators.K_FILL:
				graphicState.setFillColorSpace(PDDeviceCMYK.INSTANCE);
				operators.add(new PBOpColor(arguments));
				break;
			case Operators.CS_STROKE:
				graphicState.setStrokeColorSpace(getColorSpaceFromResources(
						resources, getLastCOSName(arguments)));
				operators.add(new PBOpColor(arguments));
				break;
			case Operators.CS_FILL:
				graphicState.setFillColorSpace(getColorSpaceFromResources(
						resources, getLastCOSName(arguments)));
				operators.add(new PBOpColor(arguments));
				break;
			case Operators.SCN_STROKE:
				PDColorSpace strokeColorSpace = graphicState.getStrokeColorSpace();
				if (strokeColorSpace != null &&
						ColorSpaceFactory.PATTERN.equals(strokeColorSpace.getName())) {
					graphicState.setPattern(getPatternFromResources(resources,
							getLastCOSName(arguments)));
				}
				operators.add(new PBOpColor(arguments));
				break;
			case Operators.SCN_FILL:
				PDColorSpace fillColorSpace = graphicState.getFillColorSpace();
				if (fillColorSpace != null &&
						ColorSpaceFactory.PATTERN.equals(fillColorSpace.getName())) {
					graphicState.setPattern(getPatternFromResources(resources,
							getLastCOSName(arguments)));
				}
				operators.add(new PBOpColor(arguments));
				break;
			case Operators.SC_STROKE:
				operators.add(new PBOpColor(arguments));
				break;
			case Operators.SC_FILL:
				operators.add(new PBOpColor(arguments));
				break;

				// TEXT OBJECT
			case Operators.ET:
			case Operators.BT:
				operators.add(new PBOpTextObject(arguments));
				break;

				// TEXT POSITION
			case Operators.TD_MOVE:
				operators.add(new PBOp_Td(arguments));
				break;
			case Operators.TD_MOVE_SET_LEADING:
				operators.add(new PBOp_TD_Big(arguments));
				break;
			case Operators.TM:
			case Operators.T_STAR:
				operators.add(new PBOpTextPosition(arguments));
				break;

				// TEXT SHOW
			case Operators.TJ_SHOW:
				operators.add(new PBOp_Tj(arguments, graphicState.getFont()));
				break;
			case Operators.TJ_SHOW_POS:
				operators.add(new PBOp_TJ_Big(arguments, graphicState.getFont()));
				break;
			case Operators.QUOTE:
				operators.add(new PBOp_Quote(arguments, graphicState.getFont()));
				break;
			case Operators.DOUBLE_QUOTE:
				operators.add(new PBOp_DoubleQuote(arguments, graphicState.getFont()));
				break;

				// TEXT STATE
			case Operators.TZ:
				operators.add(new PBOp_Tz(arguments));
				break;
			case Operators.TR:
				graphicState.setRenderingMode(getRenderingMode(arguments));
				operators.add(new PBOp_Tr(arguments));
				break;
			case Operators.TF:
				graphicState.setFont(getFontFromResources(resources,
						getFirstCOSName(arguments)));
				operators.add(new PBOpTextState(arguments));
				break;
			case Operators.TC:
			case Operators.TW:
			case Operators.TL:
			case Operators.TS:
				operators.add(new PBOpTextState(arguments));
				break;

				// TYPE 3 FONT
			case Operators.D0:
			case Operators.D1:
				operators.add(new PBOpType3Font(arguments));
				break;

				// INLINE IMAGE
			case Operators.BI: {
				addInlineImage(operators, pdfBoxOperator, resources, arguments);
			}
				break;

				// COMPABILITY
			case Operators.BX:
				operators.add(new PBOp_BX(arguments));
				break;
			case Operators.EX:
				operators.add(new PBOp_EX(arguments));
				break;

				// PATH CONSTRUCTION
			case Operators.C_CURVE_TO:
				operators.add(new PBOp_c(arguments));
				break;
			case Operators.H_CLOSEPATH:
				operators.add(new PBOp_h(arguments));
				break;
			case Operators.L_LINE_TO:
				operators.add(new PBOp_l(arguments));
				break;
			case Operators.M_MOVE_TO:
				operators.add(new PBOp_m_moveto(arguments));
				break;
			case Operators.RE:
				operators.add(new PBOp_re(arguments));
				break;
			case Operators.V:
				operators.add(new PBOp_v(arguments));
				break;
			case Operators.Y:
				operators.add(new PBOp_y(arguments));
				break;

				// PATH PAINT
			case Operators.B_CLOSEPATH_FILL_STROKE:
				operators.add(new PBOp_b_closepath_fill_stroke(arguments,
						graphicState.getStrokeColorSpace(),
						graphicState.getFillColorSpace(), graphicState.getPattern()));
				break;
			case Operators.B_FILL_STROKE:
				operators.add(new PBOp_B_fill_stroke(arguments,
						graphicState.getStrokeColorSpace(),
						graphicState.getFillColorSpace(), graphicState.getPattern()));
				break;
			case Operators.B_STAR_CLOSEPATH_EOFILL_STROKE:
				operators.add(new PBOp_bstar_closepath_eofill_stroke(arguments,
						graphicState.getStrokeColorSpace(),
						graphicState.getFillColorSpace(), graphicState.getPattern()));
				break;
			case Operators.B_STAR_EOFILL_STROKE:
				operators.add(new PBOp_BStar_eofill_stroke(arguments,
						graphicState.getStrokeColorSpace(),
						graphicState.getFillColorSpace(), graphicState.getPattern()));
				break;
			case Operators.F_FILL:
				operators.add(new PBOp_f_fill(arguments, graphicState.getFillColorSpace(),
						graphicState.getPattern()));
				break;
			case Operators.F_FILL_OBSOLETE:
				operators.add(new PBOp_F_fill_obsolete(arguments,
						graphicState.getFillColorSpace(), graphicState.getPattern()));
				break;
			case Operators.F_STAR_FILL:
				operators.add(new PBOp_FStar(arguments, graphicState.getFillColorSpace(),
						graphicState.getPattern()));
				break;
			case Operators.N:
				operators.add(new PBOp_n(arguments));
				break;
			case Operators.S_CLOSE_STROKE:
				operators.add(new PBOp_s_close_stroke(arguments,
						graphicState.getStrokeColorSpace(),
						graphicState.getPattern()));
				break;
			case Operators.S_STROKE:
				operators.add(new PBOp_S_stroke(arguments,
						graphicState.getStrokeColorSpace(),
						graphicState.getPattern()));
				break;

				// SHADING
			case Operators.SH:
				operators.add(new PBOp_sh(arguments, getShadingFromResources(resources,
						getLastCOSName(arguments))));
				break;

				// SPECIAL GS
			case Operators.CM_CONCAT:
				operators.add(new PBOp_cm(arguments));
				break;
			case Operators.Q_GRESTORE:
				if (graphicStateStack.size() > 0) {
					graphicState.copyProperties(graphicStateStack.pop());
				}
				operators.add(new PBOp_Q_grestore(arguments));
				break;
			case Operators.Q_GSAVE:
				graphicStateStack.push(graphicState.clone());
				operators.add(new PBOp_q_gsave(arguments, graphicStateStack.size()));
				break;

				// XOBJECT
			case Operators.DO:
				operators.add(new PBOp_Do(arguments, getXObjectFromResources(resources,
						getLastCOSName(arguments))));
				break;
			default:
				operators.add(new PBOp_Undefined(arguments));
				break;
		}
	}

	private static void addInlineImage(List<Operator> operators,
									   org.apache.pdfbox.contentstream.operator.Operator pdfBoxOperator,
									   PDResources resources,
									   List<COSBase> arguments) {
		if (pdfBoxOperator.getImageParameters() != null &&
				pdfBoxOperator.getImageData() != null) {
			arguments.add(pdfBoxOperator.getImageParameters());
			operators.add(new PBOp_BI(new ArrayList<COSBase>()));
			operators.add(new PBOp_ID(arguments));
			operators.add(new PBOp_EI(arguments,
					pdfBoxOperator.getImageData(), resources));
		}
	}

	private static COSName getFirstCOSName(List<COSBase> arguments) {
        COSBase lastElement = arguments.isEmpty() ? null : arguments.get(0);
        if (lastElement instanceof COSName) {
            return (COSName) lastElement;
        }
        return null;
    }

    private static COSName getLastCOSName(List<COSBase> arguments) {
        COSBase lastElement = arguments.isEmpty() ? null : arguments.get(arguments
                .size() - 1);
        if (lastElement instanceof COSName) {
            return (COSName) lastElement;
        }
        return null;
    }

    private static PDXObject getXObjectFromResources(PDResources resources,
            COSName xobject) {
        try {
            return resources.getXObject(xobject);
        } catch (IOException e) {
            LOGGER.error(
                    MSG_PROBEM_OBTAINING_RESOURCE + xobject + ". "
                            + e.getMessage(), e);
            return null;
        }
    }

    private static PDColorSpace getColorSpaceFromResources(
            PDResources resources, COSName colorSpace) {
        try {
            return resources.getColorSpace(colorSpace);
        } catch (IOException e) {
            LOGGER.error(
                    MSG_PROBEM_OBTAINING_RESOURCE + colorSpace + ". "
                            + e.getMessage(), e);
            return null;
        }
    }

    private static PDShading getShadingFromResources(PDResources resources,
            COSName shading) {
        try {
            return resources.getShading(shading);
        } catch (IOException e) {
            LOGGER.error(
                    MSG_PROBEM_OBTAINING_RESOURCE + shading + ". "
                            + e.getMessage(), e);
            return null;
        }
    }

    private static PDExtendedGraphicsState getExtGStateFromResources(
            PDResources resources, COSName extGState) {
        return resources.getExtGState(extGState);
    }

    private static PDFont getFontFromResources(PDResources resources,
            COSName font) {
        try {
            return resources.getFont(font);
        } catch (IOException e) {
            LOGGER.error(
                    MSG_PROBEM_OBTAINING_RESOURCE + font + ". "
                            + e.getMessage(), e);
            return null;
        }
    }

    private static RenderingMode getRenderingMode(List<COSBase> arguments) {
        if (!arguments.isEmpty()) {
            COSBase renderingMode = arguments.get(0);
            if (renderingMode instanceof COSInteger) {
                RenderingMode.fromInt(((COSInteger) renderingMode).intValue());
            }
        }
        return null;
    }

    private static PDAbstractPattern getPatternFromResources(
            PDResources resources, COSName pattern) {
        try {
            return resources.getPattern(pattern);
        } catch (IOException e) {
            LOGGER.error(
                    MSG_PROBEM_OBTAINING_RESOURCE + pattern + ". "
                            + e.getMessage(), e);
            return null;
        }
    }

}

/**
 * 
 */
package org.verapdf.model.factory.operator;

import org.apache.log4j.Logger;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSInteger;
import org.apache.pdfbox.cos.COSName;
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
import org.verapdf.model.impl.pb.operator.textposition.PBOp_Tm;
import org.verapdf.model.impl.pb.operator.textshow.PBOp_DoubleQuote;
import org.verapdf.model.impl.pb.operator.textshow.PBOp_Quote;
import org.verapdf.model.impl.pb.operator.textshow.PBOp_TJ_Big;
import org.verapdf.model.impl.pb.operator.textshow.PBOp_Tj;
import org.verapdf.model.impl.pb.operator.textstate.*;
import org.verapdf.model.impl.pb.operator.type3font.PBOp_d0;
import org.verapdf.model.impl.pb.operator.type3font.PBOp_d1;
import org.verapdf.model.impl.pb.operator.xobject.PBOp_Do;
import org.verapdf.model.operator.Operator;
import org.verapdf.model.tools.constants.Operators;
import org.verapdf.model.tools.resources.PDInheritableResources;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

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
    private static final String MSG_PROBLEM_OBTAINING_RESOURCE = "Problem encountered while obtaining resources for ";

    private final Deque<GraphicState> graphicStateStack = new ArrayDeque<>();
    private final GraphicState graphicState = new GraphicState();

	OperatorParser() {
		// limit the scope
	}

	void parseOperator(List<Operator> operators,
					   org.apache.pdfbox.contentstream.operator.Operator pdfBoxOperator,
					   PDInheritableResources resources, List<COSBase> arguments)
			throws CloneNotSupportedException, IOException {
		String operatorName = pdfBoxOperator.getName();
		PDColorSpace cs;
		switch (operatorName) {
			// GENERAL GS
			case Operators.D_SET_DASH:
				operators.add(new PBOp_d(arguments));
				break;
			case Operators.GS:
				this.addExtGState(operators, resources, arguments);
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
			case Operators.G_STROKE: {
				cs = resources == null ? PDDeviceGray.INSTANCE :
						resources.getColorSpace(COSName.DEVICEGRAY);
				this.graphicState.setStrokeColorSpace(cs);
				operators.add(new PBOpColor(arguments));
				break;
			}
			case Operators.G_FILL: {
				cs = resources == null ? PDDeviceGray.INSTANCE :
						resources.getColorSpace(COSName.DEVICEGRAY);
				this.graphicState.setFillColorSpace(cs);
				operators.add(new PBOpColor(arguments));
				break;
			}
			case Operators.RG_STROKE: {
				cs = resources == null ? PDDeviceRGB.INSTANCE :
						resources.getColorSpace(COSName.DEVICERGB);
				this.graphicState.setStrokeColorSpace(cs);
				operators.add(new PBOpColor(arguments));
				break;
			}
			case Operators.RG_FILL: {
				cs = resources == null ? PDDeviceRGB.INSTANCE :
						resources.getColorSpace(COSName.DEVICERGB);
				this.graphicState.setFillColorSpace(cs);
				operators.add(new PBOpColor(arguments));
				break;
			}
			case Operators.K_STROKE: {
				cs = resources == null ? PDDeviceCMYK.INSTANCE :
						resources.getColorSpace(COSName.DEVICECMYK);
				this.graphicState.setStrokeColorSpace(cs);
				operators.add(new PBOpColor(arguments));
				break;
			}
			case Operators.K_FILL: {
				cs = resources == null ? PDDeviceCMYK.INSTANCE :
						resources.getColorSpace(COSName.DEVICECMYK);
				this.graphicState.setFillColorSpace(cs);
				operators.add(new PBOpColor(arguments));
				break;
			}
			case Operators.CS_STROKE:
				this.graphicState.setStrokeColorSpace(getColorSpaceFromResources(
						resources, getLastCOSName(arguments)));
				operators.add(new PBOpColor(arguments));
				break;
			case Operators.CS_FILL:
				this.graphicState.setFillColorSpace(getColorSpaceFromResources(
						resources, getLastCOSName(arguments)));
				operators.add(new PBOpColor(arguments));
				break;
			case Operators.SCN_STROKE:
				this.setPatternColorSpace(operators, graphicState.getStrokeColorSpace(),
						resources, arguments);
				break;
			case Operators.SCN_FILL:
				this.setPatternColorSpace(operators, graphicState.getFillColorSpace(),
						resources, arguments);
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
				operators.add(new PBOp_Tm(arguments));
				break;
			case Operators.T_STAR:
				operators.add(new PBOpTextPosition(arguments));
				break;

				// TEXT SHOW
			case Operators.TJ_SHOW:
				operators.add(new PBOp_Tj(arguments, this.graphicState.clone(), resources));
				break;
			case Operators.TJ_SHOW_POS:
				operators.add(new PBOp_TJ_Big(arguments, this.graphicState.clone(), resources));
				break;
			case Operators.QUOTE:
				operators.add(new PBOp_Quote(arguments, this.graphicState.clone(), resources));
				break;
			case Operators.DOUBLE_QUOTE:
				operators.add(new PBOp_DoubleQuote(arguments, this.graphicState.clone(), resources));
				break;

				// TEXT STATE
			case Operators.TZ:
				operators.add(new PBOp_Tz(arguments));
				break;
			case Operators.TR:
				this.graphicState.setRenderingMode(getRenderingMode(arguments));
				operators.add(new PBOp_Tr(arguments));
				break;
			case Operators.TF:
				this.graphicState.setFont(getFontFromResources(resources,
						getFirstCOSName(arguments)));
				operators.add(new PBOp_Tf(arguments));
				break;
			case Operators.TC:
				operators.add(new PBOp_Tc(arguments));
				break;
			case Operators.TW:
				operators.add(new PBOp_Tw(arguments));
				break;
			case Operators.TL:
				operators.add(new PBOp_Tl(arguments));
				break;
			case Operators.TS:
				operators.add(new PBOp_Ts(arguments));
				break;

				// TYPE 3 FONT
			case Operators.D0:
				operators.add(new PBOp_d0(arguments));
				break;
			case Operators.D1:
				operators.add(new PBOp_d1(arguments));
				break;

				// INLINE IMAGE
			case Operators.BI:
				addInlineImage(operators, pdfBoxOperator, resources, arguments);
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
						this.graphicState, resources));
				break;
			case Operators.B_FILL_STROKE:
				operators.add(new PBOp_B_fill_stroke(arguments,
						this.graphicState, resources));
				break;
			case Operators.B_STAR_CLOSEPATH_EOFILL_STROKE:
				operators.add(new PBOp_bstar_closepath_eofill_stroke(arguments,
						this.graphicState, resources));
				break;
			case Operators.B_STAR_EOFILL_STROKE:
				operators.add(new PBOp_BStar_eofill_stroke(arguments,
						this.graphicState, resources));
				break;
			case Operators.F_FILL:
				operators.add(new PBOp_f_fill(arguments,
						this.graphicState, resources));
				break;
			case Operators.F_FILL_OBSOLETE:
				operators.add(new PBOp_F_fill_obsolete(arguments,
						this.graphicState, resources));
				break;
			case Operators.F_STAR_FILL:
				operators.add(new PBOp_FStar(arguments,
						this.graphicState, resources));
				break;
			case Operators.N:
				operators.add(new PBOp_n(arguments));
				break;
			case Operators.S_CLOSE_STROKE:
				operators.add(new PBOp_s_close_stroke(arguments,
						this.graphicState, resources));
				break;
			case Operators.S_STROKE:
				operators.add(new PBOp_S_stroke(arguments,
						this.graphicState, resources));
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
				if (!graphicStateStack.isEmpty()) {
					this.graphicState.copyProperties(this.graphicStateStack.pop());
				}
				operators.add(new PBOp_Q_grestore(arguments));
				break;
			case Operators.Q_GSAVE:
				this.graphicStateStack.push(this.graphicState.clone());
				operators.add(new PBOp_q_gsave(arguments, this.graphicStateStack.size()));
				break;

				// XOBJECT
			case Operators.DO:
				operators.add(new PBOp_Do(arguments, getXObjectFromResources(resources,
						getLastCOSName(arguments)), resources));
				break;
			default:
				operators.add(new PBOp_Undefined(arguments));
				break;
		}
	}

	private void setPatternColorSpace(List<Operator> operators, PDColorSpace colorSpace,
									  PDInheritableResources resources, List<COSBase> arguments) {
		if (colorSpace != null &&
				ColorSpaceFactory.PATTERN.equals(colorSpace.getName())) {
			graphicState.setPattern(getPatternFromResources(resources,
					getLastCOSName(arguments)));
		}
		operators.add(new PBOpColor(arguments));
	}

	private void addExtGState(List<Operator> operators,
							  PDInheritableResources resources, List<COSBase> arguments) {
		PDExtendedGraphicsState extGState = getExtGStateFromResources(resources,
				getLastCOSName(arguments));
		graphicState.copyPropertiesFromExtGState(extGState);
		operators.add(new PBOp_gs(arguments, extGState));
	}

	private static void addInlineImage(List<Operator> operators,
									   org.apache.pdfbox.contentstream.operator.Operator pdfBoxOperator,
									   PDInheritableResources resources,
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

    private static PDXObject getXObjectFromResources(PDInheritableResources resources,
            COSName xobject) {
		if (resources == null) {
			return null;
		}
        try {
            return resources.getXObject(xobject);
        } catch (IOException e) {
            LOGGER.debug(
                    MSG_PROBLEM_OBTAINING_RESOURCE + xobject + ". "
                            + e.getMessage(), e);
            return null;
        }
    }

    private static PDColorSpace getColorSpaceFromResources(
			PDInheritableResources resources, COSName colorSpace) {
		if (resources == null) {
			return null;
		}
        try {
            return resources.getColorSpace(colorSpace);
        } catch (IOException e) {
            LOGGER.debug(
                    MSG_PROBLEM_OBTAINING_RESOURCE + colorSpace + ". "
                            + e.getMessage(), e);
            return null;
        }
    }

    private static PDShading getShadingFromResources(
			PDInheritableResources resources, COSName shading) {
		if (resources == null) {
			return null;
		}
        try {
            return resources.getShading(shading);
        } catch (IOException e) {
            LOGGER.debug(
                    MSG_PROBLEM_OBTAINING_RESOURCE + shading + ". "
                            + e.getMessage(), e);
            return null;
        }
    }

    private static PDExtendedGraphicsState getExtGStateFromResources(
			PDInheritableResources resources, COSName extGState) {
		return resources == null ? null : resources.getExtGState(extGState);
	}

    private static PDFont getFontFromResources(PDInheritableResources resources,
            COSName font) {
		if (resources == null) {
			return null;
		}
        try {
            return resources.getFont(font);
        } catch (IOException e) {
            LOGGER.debug(
                    MSG_PROBLEM_OBTAINING_RESOURCE + font + ". "
                            + e.getMessage(), e);
            return null;
        }
    }

    private static RenderingMode getRenderingMode(List<COSBase> arguments) {
        if (!arguments.isEmpty()) {
            COSBase renderingMode = arguments.get(0);
            if (renderingMode instanceof COSInteger) {
				try {
					return RenderingMode.fromInt(((COSInteger) renderingMode).intValue());
				} catch (ArrayIndexOutOfBoundsException e) {
					LOGGER.debug("Rendering mode value is incorrect : " + renderingMode, e);
				}
			}
        }
        return RenderingMode.FILL;
    }

    private static PDAbstractPattern getPatternFromResources(
			PDInheritableResources resources, COSName pattern) {
		if (resources == null) {
			return null;
		}
        try {
            return resources.getPattern(pattern);
        } catch (IOException e) {
            LOGGER.debug(
                    MSG_PROBLEM_OBTAINING_RESOURCE + pattern + ". "
                            + e.getMessage(), e);
            return null;
        }
    }

}

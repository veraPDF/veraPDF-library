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
import org.verapdf.model.impl.pb.operator.textposition.PBOp_TD_Big;
import org.verapdf.model.impl.pb.operator.textposition.PBOp_Td;
import org.verapdf.model.impl.pb.operator.textshow.PBOp_DoubleQuote;
import org.verapdf.model.impl.pb.operator.textshow.PBOp_Quote;
import org.verapdf.model.impl.pb.operator.textshow.PBOp_TJ_Big;
import org.verapdf.model.impl.pb.operator.textshow.PBOp_Tj;
import org.verapdf.model.impl.pb.operator.textstate.PBOpTextState;
import org.verapdf.model.impl.pb.operator.textstate.PBOp_Tz;
import org.verapdf.model.impl.pb.operator.type3font.PBOpType3Font;
import org.verapdf.model.impl.pb.operator.xobject.PBOp_Do;
import org.verapdf.model.operator.Operator;
import org.verapdf.model.tools.constants.Operators;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

/**
 * @author Timur Kamalov
 */
public final class OperatorFactory {

	private static final Logger LOGGER = Logger.getLogger(OperatorFactory.class);
	private static final String RESOURCE_MESSAGE = "Problems with resources obtaining for ";

	private OperatorFactory() {
	    // Disable default public constructor
	}

    /**
     * @param pdfBoxOperator
     *            {@link org.apache.pdfbox.contentstream.operator.Operator} to
     *            be parsed
     * @param resources
     *            the {@link PDResources} object used to get state for some
     *            operators
     * @param arguments
     *            a {@link List} of {@link COSBase} objects used as arguments
     *            for operator instantiation
     * @param graphicStateStack
     *            a {@link Stack} of {@link GraphicState} objects used to
     *            instantiate some operators
     * @param graphicState
     *            a single {@link GraphicState} object used in operator
     *            instantiation
     * @return a new {@link Operator} object instantiated from the arguments
     */
	public static Operator parseOperator(org.apache.pdfbox.contentstream.operator.Operator pdfBoxOperator,
										 PDResources resources, List<COSBase> arguments,
										 Stack<GraphicState> graphicStateStack, GraphicState graphicState) {
		String operatorName = pdfBoxOperator.getName();
		switch (operatorName) {
			// GENERAL GS
			case Operators.D_SET_DASH:
				return new PBOp_d(arguments);
			case Operators.GS:
				return new PBOp_gs(arguments, getExtGStateFromResources(resources, getLastElement(arguments)));
			case Operators.I_SETFLAT:
				return new PBOp_i(arguments);
			case Operators.J_LINE_CAP:
				return new PBOp_J_line_cap(arguments);
			case Operators.J_LINE_JOIN:
				return new PBOp_j_line_join(arguments);
			case Operators.M_MITER_LIMIT:
				return new PBOp_M_miter_limit(arguments);
			case Operators.RI:
				return new PBOp_ri(arguments);
			case Operators.W_LINE_WIDTH:
				return new PBOp_w_line_width(arguments);

			// MARKED CONTENT
			case Operators.BMC:
				return new PBOp_BMC(arguments);
			case Operators.BDC:
				return new PBOp_BDC(arguments);
			case Operators.EMC:
				return new PBOp_EMC(arguments);
			case Operators.MP:
				return new PBOp_MP(arguments);
			case Operators.DP:
				return new PBOp_DP(arguments);

			// CLIP
			case Operators.W_CLIP:
				return new PBOp_W_clip(arguments);
			case Operators.W_STAR_EOCLIP:
				return new PBOp_WStar(arguments);

			// COLOR
			case Operators.G_STROKE:
				graphicState.setStrokeColorSpace(PDDeviceGray.INSTANCE);
				return new PBOpColor(arguments);
			case Operators.G_FILL:
				graphicState.setFillColorSpace(PDDeviceGray.INSTANCE);
				return new PBOpColor(arguments);
			case Operators.RG_STROKE:
				graphicState.setStrokeColorSpace(PDDeviceRGB.INSTANCE);
				return new PBOpColor(arguments);
			case Operators.RG_FILL:
				graphicState.setFillColorSpace(PDDeviceRGB.INSTANCE);
				return new PBOpColor(arguments);
			case Operators.K_STROKE:
				return new PBOpColor(arguments);
			case Operators.K_FILL:
				graphicState.setFillColorSpace(PDDeviceCMYK.INSTANCE);
				return new PBOpColor(arguments);
			case Operators.CS_STROKE:
				graphicState.setStrokeColorSpace(getColorSpaceFromResources(resources, getLastElement(arguments)));
				return new PBOpColor(arguments);
			case Operators.CS_FILL:
				graphicState.setFillColorSpace(getColorSpaceFromResources(resources, getLastElement(arguments)));
				return new PBOpColor(arguments);
			case Operators.SCN_STROKE:
				return new PBOpColor(arguments);
			case Operators.SCN_FILL:
				return new PBOpColor(arguments);
			case Operators.SC_STROKE:
				return new PBOpColor(arguments);
			case Operators.SC_FILL:
				return new PBOpColor(arguments);

			// TEXT OBJECT
			case Operators.ET:
			case Operators.BT:
				return new PBOpTextObject(arguments);

			// TEXT POSITION
			case Operators.TD_MOVE:
				return new PBOp_Td(arguments);
			case Operators.TD_MOVE_SET_LEADING:
				return new PBOp_TD_Big(arguments);
			case Operators.TM:
			case Operators.T_STAR:
				return new PBOpTextPosition(arguments);

			// TEXT SHOW
			case Operators.TJ_SHOW:
				return new PBOp_Tj(arguments);
			case Operators.TJ_SHOW_POS:
				return new PBOp_TJ_Big(arguments);
			case Operators.QUOTE:
				return new PBOp_Quote(arguments);
			case Operators.DOUBLE_QUOTE:
				return new PBOp_DoubleQuote(arguments);

			// TEXT STATE
			case Operators.TZ:
				return new PBOp_Tz(arguments);
			case Operators.TC:
			case Operators.TW:
			case Operators.TL:
			case Operators.TF:
			case Operators.TR:
			case Operators.TS:
				return new PBOpTextState(arguments);

			// TYPE 3 FONT
			case Operators.D0:
			case Operators.D1:
				return new PBOpType3Font(arguments);

			// INLINE IMAGE
			case Operators.BI:
			case Operators.ID:
			case Operators.EI: {
				arguments.add(pdfBoxOperator.getImageParameters());
				return new PBOpInlineImage(arguments);
			}

			// COMPABILITY
			case Operators.BX:
				return new PBOp_BX(arguments);
			case Operators.EX:
				return new PBOp_EX(arguments);

			// PATH CONSTRUCTION
			case Operators.C_CURVE_TO:
				return new PBOp_c(arguments);
			case Operators.H_CLOSEPATH:
				return new PBOp_h(arguments);
			case Operators.L_LINE_TO:
				return new PBOp_l(arguments);
			case Operators.M_MOVE_TO:
				return new PBOp_m_moveto(arguments);
			case Operators.RE:
				return new PBOp_re(arguments);
			case Operators.V:
				return new PBOp_v(arguments);
			case Operators.Y:
				return new PBOp_y(arguments);

			// PATH PAINT
			case Operators.B_CLOSEPATH_FILL_STROKE:
				return new PBOp_b_closepath_fill_stroke(arguments,
						graphicState.getStrokeColorSpace(),
						graphicState.getFillColorSpace());
			case Operators.B_FILL_STROKE:
				return new PBOp_B_fill_stroke(arguments,
						graphicState.getStrokeColorSpace(),
						graphicState.getFillColorSpace());
			case Operators.B_STAR_CLOSEPATH_EOFILL_STROKE:
				return new PBOp_bstar_closepath_eofill_stroke(arguments,
						graphicState.getStrokeColorSpace(),
						graphicState.getFillColorSpace());
			case Operators.B_STAR_EOFILL_STROKE:
				return new PBOp_BStar_eofill_stroke(arguments,
						graphicState.getStrokeColorSpace(),
						graphicState.getFillColorSpace());
			case Operators.F_FILL:
				return new PBOp_f_fill(arguments,
						graphicState.getFillColorSpace());
			case Operators.F_FILL_OBSOLETE:
				return new PBOp_F_fill_obsolete(arguments,
						graphicState.getFillColorSpace());
			case Operators.F_STAR_FILL:
				return new PBOp_FStar(arguments,
						graphicState.getFillColorSpace());
			case Operators.N:
				return new PBOp_n(arguments);
			case Operators.S_CLOSE_STROKE:
				return new PBOp_s_close_stroke(arguments,
						graphicState.getStrokeColorSpace());
			case Operators.S_STROKE:
				return new PBOp_S_stroke(arguments,
						graphicState.getStrokeColorSpace());

			// SHADING
			case Operators.SH:
				return new PBOp_sh(arguments, getShadingFromResources(resources, getLastElement(arguments)));

			// SPECIAL GS
			case Operators.CM_CONCAT:
				return new PBOp_cm(arguments);
			case Operators.Q_GRESTORE:
				graphicState.copyProperties(graphicStateStack.pop());
				return new PBOp_Q_grestore(arguments);
			case Operators.Q_GSAVE:
				graphicStateStack.push(graphicState.clone());
				return new PBOp_q_gsave(arguments, Integer.valueOf(graphicStateStack.size()));

			// XOBJECT
			case Operators.DO:
				return new PBOp_Do(arguments, getXObjectFromResources(resources, getLastElement(arguments)));

			default:
				return new PBOp_Undefined(arguments);
		}
	}

    /**
     * @param pdfBoxTokens
     *            a {@link List} of {@link Object} instances that's iterated to
     *            create the list of operators
     * @param resources
     *            the {@link PDResources} used to instantiate the operators
     * @return a {@link List} of {@link Operator} instances created from parsing
     *         the passed arguments
     */
	public static List<Operator> parseOperators(List<Object> pdfBoxTokens, PDResources resources) {
		Stack<GraphicState> graphicStateStack = new Stack<>();
		return parseOperators(pdfBoxTokens.iterator(), resources, graphicStateStack);
	}


	private static List<Operator> parseOperators(Iterator<Object> pdfBoxTokens, PDResources resources,
												 Stack<GraphicState> graphicStateStack) {
		List<Operator> result = new ArrayList<>();
		List<COSBase> arguments = new ArrayList<>();
		GraphicState graphicState = new GraphicState();

		while (pdfBoxTokens.hasNext()) {
			Object pdfBoxToken = pdfBoxTokens.next();
			if (pdfBoxToken instanceof COSBase) {
				arguments.add((COSBase) pdfBoxToken);
			} else if (pdfBoxToken instanceof org.apache.pdfbox.contentstream.operator.Operator) {
				result.add(parseOperator((org.apache.pdfbox.contentstream.operator.Operator) pdfBoxToken,
						resources, arguments,
						graphicStateStack, graphicState));
				arguments.clear();
			} else if (pdfBoxToken instanceof RenderingIntent) {
				String value = ((RenderingIntent) pdfBoxToken).stringValue();
				arguments.add(COSName.getPDFName(value));
			} else {
				LOGGER.error("Unexpected type of object in tokens: " + pdfBoxToken.getClass().getName());
			}
		}
		return result;
	}

	private static COSName getLastElement(List<COSBase> arguments) {
		COSBase lastElement = arguments.isEmpty() ? null : arguments.get(arguments.size() - 1);
		if (lastElement instanceof COSName) {
			return (COSName) lastElement;
		}
        return null;
	}

	private static PDColorSpace getColorSpaceFromResources(PDResources resources, COSName colorSpace) {
		try {
			return resources.getColorSpace(colorSpace);
		} catch (IOException e) {
		    LOGGER.error(RESOURCE_MESSAGE + colorSpace + ". " + e.getMessage(), e);
			return null;
		}
	}

	private static PDShading getShadingFromResources(PDResources resources, COSName shading) {
		try {
			return resources.getShading(shading);
		} catch (IOException e) {
		    LOGGER.error(RESOURCE_MESSAGE + shading + ". " + e.getMessage(), e);
			return null;
		}
	}

	private static PDXObject getXObjectFromResources(PDResources resources, COSName xobject) {
		try {
			return resources.getXObject(xobject);
		} catch (IOException e) {
		    LOGGER.error(RESOURCE_MESSAGE + xobject + ". " + e.getMessage(), e);
			return null;
		}
	}

	private static PDExtendedGraphicsState getExtGStateFromResources(PDResources resources, COSName extGState) {
		return resources.getExtGState(extGState);
	}

}

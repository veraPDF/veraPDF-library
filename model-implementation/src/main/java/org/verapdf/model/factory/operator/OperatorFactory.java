package org.verapdf.model.factory.operator;

import org.apache.pdfbox.cos.COSBase;
import org.verapdf.model.impl.pb.operator.generalgs.*;
import org.verapdf.model.impl.pb.operator.markedcontent.PBOp_BMC;
import org.verapdf.model.impl.pb.operator.markedcontent.PBOp_EMC;
import org.verapdf.model.impl.pb.operator.markedcontent.PBOp_MP;
import org.verapdf.model.impl.pb.operator.opclip.PBOp_WStar;
import org.verapdf.model.impl.pb.operator.opclip.PBOp_W_clip;
import org.verapdf.model.impl.pb.operator.opcompability.PBOp_BX;
import org.verapdf.model.impl.pb.operator.opcompability.PBOp_EX;
import org.verapdf.model.impl.pb.operator.pathconstruction.*;
import org.verapdf.model.impl.pb.operator.pathpaint.*;
import org.verapdf.model.impl.pb.operator.shading.PBOp_sh;
import org.verapdf.model.impl.pb.operator.specialgs.PBOp_Q_grestore;
import org.verapdf.model.impl.pb.operator.specialgs.PBOp_cm;
import org.verapdf.model.impl.pb.operator.specialgs.PBOp_q_gsave;
import org.verapdf.model.impl.pb.operator.xobject.PBOp_Do;
import org.verapdf.model.operator.Operator;
import org.verapdf.model.tools.constants.Operators;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Timur Kamalov
 */
public final class OperatorFactory {

    public static Operator parseOperator(org.apache.pdfbox.contentstream.operator.Operator pdfBoxOperator, List<COSBase> arguments) {
        String operatorName = pdfBoxOperator.getName();
        switch (operatorName) {
            // GENERAL GS
            case Operators.D_SET_DASH : return new PBOp_d(arguments);
            case Operators.GS : return new PBOp_gs(arguments);
            case Operators.I_SETFLAT : return new PBOp_i(arguments);
            case Operators.J_LINE_CAP : return new PBOp_J_line_cap(arguments);
            case Operators.J_LINE_JOIN : return new PBOp_j_line_join(arguments);
            case Operators.M_MITER_LIMIT : return new PBOp_M_miter_limit(arguments);
            case Operators.RI : return new PBOp_ri(arguments);
            case Operators.W_LINE_WIDTH : return new PBOp_w_line_width(arguments);

            // MARKED CONTENT
            case Operators.BMC : return new PBOp_BMC(arguments);
            case Operators.EMC : return new PBOp_EMC(arguments);
            case Operators.MP : return new PBOp_MP(arguments);

            // CLIP
            case Operators.W_CLIP : return new PBOp_W_clip(arguments);
            case Operators.W_STAR_EOCLIP : return new PBOp_WStar(arguments);

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
            case Operators.B_CLOSEPATH_FILL_STROKE : return new PBOp_b_closepath_fill_stroke(arguments);
            case Operators.B_FILL_STROKE : return new PBOp_B_fill_stroke(arguments);
            case Operators.B_STAR_CLOSEPATH_EOFILL_STROKE : return new PBOp_bstar_closepath_eofill_stroke(arguments);
            case Operators.B_STAR_EOFILL_STROKE : return new PBOp_BStar_eofill_stroke(arguments);
            case Operators.F_FILL : return new PBOp_f_fill(arguments);
            case Operators.F_FILL_OBSOLETE : return new PBOp_F_fill_obsolete(arguments);
            case Operators.F_STAR_FILL : return new PBOp_FStar(arguments);
            case Operators.N : return new PBOp_n(arguments);
            case Operators.S_CLOSE_STROKE : return new PBOp_s_close_stroke(arguments);
            case Operators.S_STROKE : return new PBOp_S_stroke(arguments);

            // SHADING
            case Operators.SH : return new PBOp_sh(arguments);

            // SPECIAL GS
            case Operators.CM_CONCAT : return new PBOp_cm(arguments);
            case Operators.Q_GRESTORE : return new PBOp_Q_grestore(arguments);
            case Operators.Q_GSAVE : return new PBOp_q_gsave(arguments);

            // XOBJECT
            case Operators.DO : return new PBOp_Do(arguments);

            default: return null;
        }
    }

    public static List<Operator> parseOperators(List<Object> pdfBoxTokens) {
        List<Operator> result = new ArrayList<>();
        List<COSBase> arguments = new ArrayList<>();
        for (Object pdfBoxToken : pdfBoxTokens) {
            if (pdfBoxToken instanceof COSBase) {
                arguments.add((COSBase) pdfBoxToken);
            } else if (pdfBoxToken instanceof org.apache.pdfbox.contentstream.operator.Operator) {
                parseOperator((org.apache.pdfbox.contentstream.operator.Operator) pdfBoxToken, arguments);
                arguments.clear();
            } else {
                //TODO : something unexpected encountered.
            }
        }
        return result;
    }

}

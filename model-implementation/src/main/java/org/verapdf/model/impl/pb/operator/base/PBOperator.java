package org.verapdf.model.impl.pb.operator.base;

import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSNumber;
import org.verapdf.model.GenericModelObject;
import org.verapdf.model.coslayer.CosReal;
import org.verapdf.model.impl.pb.cos.PBCosReal;
import org.verapdf.model.operator.Operator;
import org.verapdf.model.tools.IDGenerator;

import java.util.ArrayList;
import java.util.List;

/**
 * Base class for operator layer
 * @author Timur Kamalov
 */
public abstract class PBOperator extends GenericModelObject implements Operator {

    public static final Integer DEFAULT_ARRAY_SIZE = Integer.valueOf(16);

    protected List<COSBase> arguments = new ArrayList<>();

    private String id;
    private String type = "Operator";

    protected PBOperator(List<COSBase> arguments) {
        this.arguments.addAll(arguments);
        this.id = IDGenerator.generateID();
    }

    public String getID() {
        return id;
    }

    @Override
    public String getType() {
        return type;
    }

    protected void setType(String type) {
        this.type = type;
    }

    protected List<CosReal> getLastReal() {
        List<CosReal> cosReals = new ArrayList<>();
        COSBase base = !arguments.isEmpty() ? arguments.get(arguments.size() - 1) : null;
        if (base instanceof COSNumber) {
            cosReals.add(new PBCosReal((COSNumber) base));
        }
        return cosReals;
    }

}

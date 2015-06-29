package org.verapdf.model.impl.pb.operator.base;

import org.apache.pdfbox.cos.COSBase;
import org.verapdf.model.GenericModelObject;
import org.verapdf.model.operator.Operator;
import org.verapdf.model.tools.IDGenerator;

import java.util.List;

/**
 * Base class for operator layer
 * @author Timur Kamalov
 */
public abstract class PBOperator extends GenericModelObject implements Operator {

    protected List<COSBase> arguments;

    private String id;
    private String type = "Operator";

    protected PBOperator(List<COSBase> arguments) {
        this.arguments = arguments;
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

}

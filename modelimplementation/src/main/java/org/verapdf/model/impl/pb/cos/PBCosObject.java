package org.verapdf.model.impl.pb.cos;

import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSObject;
import org.verapdf.model.impl.GenericModelObject;
import org.verapdf.model.tools.IDGenerator;
import org.verapdf.model.coslayer.CosObject;
import org.verapdf.model.visitor.cos.pb.PBCosVisitor;

import java.io.IOException;

/**
 * Created by Evgeniy Muravitskiy on 4/27/15.
 * <p>
 *     Current class is representation of CosObject interface of abstract model.
 *     This class is analogue of COSBase in pdfbox.
 * </p>
 */
public class PBCosObject extends GenericModelObject implements CosObject {

    protected COSBase baseObject;

    private String type = "CosObject";
    private String id;

    public PBCosObject(COSBase baseObject) {
        this.baseObject = baseObject;
        id = IDGenerator.generateID(this.baseObject);
    }

    /** Get type of current object
     */
    @Override
    public String getType() {
        return type;
    }

    protected void setType(String type) {
        this.type = type;
    }

    /** Get personal id of current object
     */
    @Override
    public String getID() {
        return id;
    }

    public static CosObject getFromValue(COSBase base) {
        try {
            PBCosVisitor visitor = new PBCosVisitor();
            if (base instanceof COSObject) {
                return (CosObject) visitor.visitFromObject((COSObject) base);
            } else {
                return (CosObject) base.accept(visitor);
            }
        } catch (IOException ignore) {
            return null;
        }
    }
}

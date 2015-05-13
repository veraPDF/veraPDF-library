package org.verapdf.model.impl.pb.cos;

import org.apache.pdfbox.cos.COSBase;
import org.verapdf.model.ModelHelper;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.tools.IDGenerator;
import org.verapdf.model.coslayer.CosObject;

import java.util.List;

/**
 * Created by Evgeniy Muravitskiy on 4/27/15.
 * <p>
 *     Current class is representation of CosObject interface of abstract model.
 *     This class is analogue of COSBase in pdfbox.
 * </p>
 */
public class PBCosObject implements CosObject{

    protected COSBase baseObject;

    private String type = "CosObject";
    private Integer id = IDGenerator.generateID();

    public PBCosObject(COSBase baseObject) {
        this.baseObject = baseObject;
    }

    @Override
    public List<? extends Object> getLinkedObjects(String link) {
        throw new IllegalAccessError(this.get_type() + " has not access to this method or has not " + link + " link.");
    }

    @Override
    public List<String> getLinks() {
        return ModelHelper.getListOfLinks(this.get_type());
    }

    @Override
    public List<String> getProperties() {
        return ModelHelper.getListOfProperties(this.get_type());
    }

    @Override
    public List<String> getSuperTypes() {
        return ModelHelper.getListOfSuperNames(this.get_type());
    }

    /** Get type of current object
     */
    @Override
    public String get_type() {
        return type;
    }

    protected void setType(String type) {
        this.type = type;
    }

    /** Get personal id of current object
     */
    @Override
    public String get_id() {
        return String.valueOf(id);
    }

    public boolean compareTo(java.lang.Object object) {
        if (object instanceof COSBase) {
            return baseObject.equals(object);
        }
        else if (object instanceof CosObject){
            COSBase base = ((PBCosObject) object).baseObject;
            return  this.baseObject.equals(base);
        } else {
            return false;
        }
    }
}

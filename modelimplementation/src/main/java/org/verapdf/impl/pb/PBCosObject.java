package org.verapdf.impl.pb;

import org.apache.pdfbox.cos.COSBase;
import org.verapdf.model.ModelHelper;
import org.verapdf.model.baselayer.Object;
import org.verapdf.tools.IDGenerator;
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
    private String id = IDGenerator.generateID();

    public PBCosObject(COSBase baseObject) {
        this.baseObject = baseObject;
    }

    // TODO : wait ModelHelper realization
    @Override
    public List<Object> getLinkedObjects(String s) {
        return null;
    }

    @Override
    public List<String> getLinks() {
        //return ModelHelper.getListOfLinks(get_type);
        return null;
    }

    @Override
    public List<String> getProperties() {
        //return ModelHelper.getListOfProperties(get_type);
        return null;
    }

    @Override
    public List<String> getSuperTypes() {
        //return ModelHelper.getListOfSuperNames(get_type);
        return null;
    }

    /** Get type of current object
     */
    @Override
    public String get_type() {
        return this.getClass().getInterfaces()[0].getName();
    }

    /** Get personal id of current object
     */
    @Override
    public String get_id() {
        return id;
    }

    public boolean compareTo(java.lang.Object o) {
        if (o instanceof COSBase)
            return baseObject.equals(o);
        else
            return o instanceof CosObject && o.equals(baseObject);
    }
}

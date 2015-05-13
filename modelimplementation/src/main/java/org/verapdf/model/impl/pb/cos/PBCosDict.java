package org.verapdf.model.impl.pb.cos;

import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSName;
import org.verapdf.model.factory.cos.PBFactory;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.coslayer.CosDict;
import org.verapdf.model.coslayer.CosName;
import org.verapdf.model.coslayer.CosObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Evgeniy Muravitskiy on 4/29/15.
 * <p>
 *     Current class is representation of CosDict interface of abstract model.
 *     This class is analogue of COSDictionary in pdfbox.
 * </p>
 */
public class PBCosDict extends PBCosObject implements CosDict {

    public final static String KEYS = "keys";
    public final static String VALUES = "values";
    public final static String METADATA = "metadata";

    public PBCosDict(COSDictionary dictionary) {
        super(dictionary);
        setType("CosDict");
    }

    /**Get number of key/value pairs in the dictionary
     */
    @Override
    public Integer getsize() {
        return ((COSDictionary) baseObject).size();
    }

    @Override
    public List<? extends org.verapdf.model.baselayer.Object> getLinkedObjects(String link) {
        List<? extends Object> list;

        switch (link) {
            case KEYS:
                list = this.getkeys();
                break;
            case VALUES:
                list = this.getvalues();
                break;
            case METADATA:
                list = this.getmetadata();
                break;
            default:
                list = super.getLinkedObjects(link);
        }

        return list;
    }

    /** Get all keys of the dictionary
     */
    protected List<CosName> getkeys() {
        List<CosName> list = new ArrayList<>(this.getsize());
        for (COSName key : ((COSDictionary) baseObject).keySet()) {
            list.add((CosName) PBFactory.generateCosObject(key));
        }
        return list;
    }

    /** Get all values of the dictonary
     */
    protected List<CosObject> getvalues() {
        List<CosObject> list = new ArrayList<>(this.getsize());
        for (COSBase value : ((COSDictionary) baseObject).getValues()) {
            list.add(PBFactory.generateCosObject(value));
        }
        return list;
    }

    /** Get XMP metadata if it is present
     */
    // TODO : metadata support
    protected List<Object> getmetadata() {
        System.err.println("Current version not support metadata handler yet. Result is null.");
        return null;
    }
}

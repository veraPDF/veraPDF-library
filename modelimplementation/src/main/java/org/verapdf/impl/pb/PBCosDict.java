package org.verapdf.impl.pb;

import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSName;
import org.verapdf.factory.cos.PBFactory;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.coslayer.CosDict;

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

    private final static String KEYS = "keys";
    private final static String VALUES = "values";
    private final static String METADATA = "metadata";

    public PBCosDict(COSDictionary dictionary) {
        super(dictionary);
    }

    /**Get number of key/value pairs in the dictionary
     */
    @Override
    public Integer getsize() {
        return ((COSDictionary)baseObject).size();
    }

    @Override
    public List<org.verapdf.model.baselayer.Object> getLinkedObjects(String s) {
        List<Object> list;
        switch (s) {
            case KEYS:
                list = getkeys();
                break;
            case VALUES:
                list = getvalues();
                break;
            case METADATA:
                list = getmetadata();
                break;
            default:
                throw new IllegalArgumentException("Unknown link " + s + " for " + get_type());
        }

        return list;
    }

    /** Get all keys of the dictonary
     */
    protected List<Object> getkeys() {
        List<Object> list = new ArrayList<>(getsize());
        for (COSName key : ((COSDictionary)baseObject).keySet())
            list.add(PBFactory.generateCosObject(key));
        return list;
    }

    /** Get all values of the dictonary
     */
    protected List<Object> getvalues() {
        List<Object> list = new ArrayList<>(getsize());
        for (COSBase value : ((COSDictionary)baseObject).getValues())
            list.add(PBFactory.generateCosObject(value));
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

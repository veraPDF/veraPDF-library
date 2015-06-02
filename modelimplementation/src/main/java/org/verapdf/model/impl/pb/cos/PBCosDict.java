package org.verapdf.model.impl.pb.cos;

import org.apache.log4j.Logger;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSName;
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

    private final static Logger logger = Logger.getLogger(PBCosDict.class);

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
    public Long getsize() {
        return Long.valueOf(((COSDictionary) baseObject).size());
    }

    @Override
    public List<? extends org.verapdf.model.baselayer.Object> getLinkedObjects(String link) {
        List<? extends Object> list;

        switch (link) {
            case KEYS:
                list = this.getKeys();
                break;
            case VALUES:
                list = this.getValues();
                break;
            case METADATA:
                list = this.getMetadata();
                break;
            default:
                list = super.getLinkedObjects(link);
        }

        return list;
    }

    /** Get all keys of the dictionary
     */
    private List<CosName> getKeys() {
        List<CosName> list = new ArrayList<>(this.getsize().intValue());
        for (COSName key : ((COSDictionary) baseObject).keySet()) {
            list.add((CosName) getFromValue(key));
        }
        return list;
    }

    /** Get all values of the dictonary
     */
    private List<CosObject> getValues() {
        List<CosObject> list = new ArrayList<>(this.getsize().intValue());
        for (COSBase value : ((COSDictionary) baseObject).getValues()) {
            list.add(getFromValue(value));
        }
        return list;
    }

    /** Get XMP metadata if it is present
     */
    // TODO : metadata support
    private List<Object> getMetadata() {
        logger.warn("Current version not support metadata handler yet. Result is null.");
        return null;
    }
}

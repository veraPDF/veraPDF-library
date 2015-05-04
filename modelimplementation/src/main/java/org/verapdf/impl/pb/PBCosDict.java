package org.verapdf.impl.pb;

import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSName;
import org.verapdf.factory.cos.PBFactory;
import org.verapdf.model.coslayer.CosDict;
import org.verapdf.model.coslayer.CosName;
import org.verapdf.model.coslayer.CosObject;
import org.verapdf.model.pdlayer.PDMetadata;

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

    public PBCosDict(COSDictionary dictionary) {
        super(dictionary);
    }

    /**Get number of key/value pairs in the dictionary
     */
    @Override
    public Integer getsize() {
        return ((COSDictionary)baseObject).size();
    }

    /** Get all keys of the dictonary
     */
    @Override
    public List<CosName> getkeys() {
        List<CosName> list = new ArrayList<CosName>(getsize());
        for (COSName key : ((COSDictionary)baseObject).keySet())
            list.add((CosName) PBFactory.generateCosObject(key));
        return list;
    }

    /** Get all values of the dictonary
     */
    @Override
    public List<CosObject> getvalues() {
        List<CosObject> list = new ArrayList<CosObject>(getsize());
        for (COSBase value : ((COSDictionary)baseObject).getValues())
            list.add(PBFactory.generateCosObject(value));
        return list;
    }

    /** Get XMP metadata if it is present
     */
    // TODO : metadata support
    @Override
    public List<PDMetadata> getmetadata() {
        System.err.println("Current version not support metadata handler yet. Result is null.");
        return null;
    }
}

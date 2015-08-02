package org.verapdf.model.impl.pb.cos;

import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.cos.COSStream;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.coslayer.CosDict;
import org.verapdf.model.coslayer.CosName;
import org.verapdf.model.coslayer.CosObject;
import org.verapdf.model.impl.pb.pd.PBoxPDMetadata;
import org.verapdf.model.pdlayer.PDMetadata;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Current class is representation of CosDict interface of abstract model. This
 * class is analogue of COSDictionary in pdfbox.
 *
 * @author Evgeniy Muravitskiy
 */
public class PBCosDict extends PBCosObject implements CosDict {

    public static final String COS_DICT_TYPE = "CosDict";

    public static final String KEYS = "keys";
    public static final String VALUES = "values";
    public static final String METADATA = "metadata";

    private final int size;
    private final List<CosName> keys;
    private final List<CosObject> values;
    private final List<PDMetadata> metadata;

    public PBCosDict(COSDictionary dictionary) {
        this(dictionary, COS_DICT_TYPE);
    }

    public PBCosDict(COSDictionary dictionary, final String type) {
        super(dictionary, type);
        this.size = dictionary.size();
        this.keys = parseKeys(dictionary);
        this.values = parseValues(dictionary);
        this.metadata = parseMetadata(dictionary);
    }

    /**
     * Get number of key/value pairs in the dictionary
     */
    @Override
    public Long getsize() {
        return Long.valueOf(this.size);
    }

    @Override
    public List<? extends org.verapdf.model.baselayer.Object> getLinkedObjects(
            String link) {
        List<? extends Object> list;

        switch (link) {
        case KEYS:
            list = this.keys;
            break;
        case VALUES:
            list = this.values;
            break;
        case METADATA:
            list = this.metadata;
            break;
        default:
            list = super.getLinkedObjects(link);
        }

        return list;
    }

    /**
     * Get all keys of the dictionary
     */
    private static List<CosName> parseKeys(final COSDictionary dictionary) {
        List<CosName> list = new ArrayList<>(dictionary.size());
        for (COSName key : dictionary.keySet()) {
            if (key != null) {
                list.add((CosName) getFromValue(key));
            }
        }
        return Collections.unmodifiableList(list);
    }

    /**
     * Get all values of the dictonary
     */
    private static List<CosObject> parseValues(final COSDictionary dictionary) {
        List<CosObject> list = new ArrayList<>(dictionary.size());
        for (COSBase value : dictionary.getValues()) {
            if (value != null) {
                list.add(getFromValue(value));
            }
        }
        return Collections.unmodifiableList(list);
    }

    /**
     * Get XMP metadata if it is present
     */
    private static List<PDMetadata> parseMetadata(final COSDictionary dictionary) {
        ArrayList<PDMetadata> pdMetadatas = new ArrayList<>(1);
        COSBase meta = dictionary.getDictionaryObject(COSName.METADATA);
        COSName type = dictionary.getCOSName(COSName.TYPE);
        if (meta != null && meta instanceof COSStream
                && type != COSName.CATALOG) {
            org.apache.pdfbox.pdmodel.common.PDMetadata md = new org.apache.pdfbox.pdmodel.common.PDMetadata(
                    (COSStream) meta);
            pdMetadatas.add(new PBoxPDMetadata(md, Boolean.FALSE));
        }
        return Collections.unmodifiableList(pdMetadatas);
    }
}

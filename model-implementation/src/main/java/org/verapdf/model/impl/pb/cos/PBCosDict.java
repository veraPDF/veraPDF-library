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

    /**
     * Default constructor
     * @param dictionary pdfbox COSDictionary
     */
	public PBCosDict(COSDictionary dictionary) {
        this(dictionary, COS_DICT_TYPE);
    }

    /**
     * Constructor used by child classes
     * @param dictionary pdfbox COSDictionary
     * @param type type of child class
     */
    protected PBCosDict(COSDictionary dictionary, final String type) {
        super(dictionary, type);
        this.size = dictionary.size();
    }

    /**
     * Get number of key/value pairs in the dictionary
     */
    @Override
    public Long getsize() {
        return Long.valueOf(this.size);
    }

	@Override
	public List<? extends Object> getLinkedObjects(
			String link) {
		switch (link) {
			case KEYS:
				return this.getKeys();
			case VALUES:
				return this.getValues();
			case METADATA:
				return this.getMetadata();
			default:
				return super.getLinkedObjects(link);
		}
	}

    /**
     * Get all keys of the dictionary
     */
    private List<CosName> getKeys() {
		COSDictionary dictionary = (COSDictionary) this.baseObject;
		List<CosName> list = new ArrayList<>(dictionary.size());
        for (COSName key : dictionary.keySet()) {
            if (key != null) {
                list.add((CosName) getFromValue(key));
            }
        }
        return Collections.unmodifiableList(list);
    }

    /**
     * Get all values of the dictionary
     */
    private List<CosObject> getValues() {
		COSDictionary dictionary = (COSDictionary) this.baseObject;
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
    private List<PDMetadata> getMetadata() {
		COSDictionary dictionary = (COSDictionary) this.baseObject;
        COSBase meta = dictionary.getDictionaryObject(COSName.METADATA);
        COSName type = dictionary.getCOSName(COSName.TYPE);
        if (meta != null && meta instanceof COSStream
                && type != COSName.CATALOG) {
			ArrayList<PDMetadata> pdMetadatas = new ArrayList<>(MAX_NUMBER_OF_ELEMENTS);
			org.apache.pdfbox.pdmodel.common.PDMetadata md = new org.apache.pdfbox.pdmodel.common.PDMetadata(
                    (COSStream) meta);
            pdMetadatas.add(new PBoxPDMetadata(md, Boolean.FALSE));
			return pdMetadatas;
        }
        return Collections.emptyList();
    }
}

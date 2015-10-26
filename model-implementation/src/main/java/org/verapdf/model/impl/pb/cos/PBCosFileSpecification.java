package org.verapdf.model.impl.pb.cos;

import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.cos.COSString;
import org.verapdf.model.baselayer.*;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.coslayer.CosFileSpecification;
import org.verapdf.model.external.EmbeddedFile;
import org.verapdf.model.impl.pb.external.PBoxEmbeddedFile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represent a specific type of Dictionary - File Specification Dictionary.
 *
 * @author Evgeniy Muravitskiy
 */
public class PBCosFileSpecification extends PBCosDict implements
        CosFileSpecification {

    /** Type name for PBCosFileSpecification */
    public static final String COS_FILE_SPECIFICATION_TYPE = "CosFileSpecification";

	public static final String EF = "EF";

    /**
     * Default constructor
     * @param dictionary pdfbox COSDictionary
     */
    public PBCosFileSpecification(COSDictionary dictionary) {
        super(dictionary, COS_FILE_SPECIFICATION_TYPE);
    }

	public String getF() {
		return this.getStringValue(COSName.F);
	}

	public String getUF() {
		return this.getStringValue(COSName.UF);
	}

	@Override
	public List<? extends Object> getLinkedObjects(String link) {
		if (EF.equals(link)) {
			return this.getEFFile();
		}
		return super.getLinkedObjects(link);
	}

	// TODO : what the types can be here?
	private List<EmbeddedFile> getEFFile() {
		COSBase efDictionary = ((COSDictionary) this.baseObject)
				.getDictionaryObject(COSName.EF);
		if (efDictionary instanceof COSDictionary) {
			ArrayList<EmbeddedFile> list = new ArrayList<>(MAX_NUMBER_OF_ELEMENTS);
			list.add(new PBoxEmbeddedFile((COSDictionary) efDictionary));
			return Collections.unmodifiableList(list);
		}
		return Collections.emptyList();
	}

	private String getStringValue(COSName key) {
		COSBase value = ((COSDictionary) this.baseObject).getDictionaryObject(key);
		return value instanceof COSString ? ((COSString) value).getString() : null;
	}
}

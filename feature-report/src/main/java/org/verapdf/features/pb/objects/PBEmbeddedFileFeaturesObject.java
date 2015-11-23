package org.verapdf.features.pb.objects;

import org.apache.log4j.Logger;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.cos.COSString;
import org.apache.pdfbox.pdmodel.common.filespecification.PDComplexFileSpecification;
import org.apache.pdfbox.pdmodel.common.filespecification.PDEmbeddedFile;
import org.verapdf.core.FeatureParsingException;
import org.verapdf.features.EmbeddedFileFeaturesData;
import org.verapdf.features.FeaturesData;
import org.verapdf.features.FeaturesObjectTypesEnum;
import org.verapdf.features.IFeaturesObject;
import org.verapdf.features.pb.tools.PBCreateNodeHelper;
import org.verapdf.features.tools.ErrorsHelper;
import org.verapdf.features.tools.FeatureTreeNode;
import org.verapdf.features.tools.FeaturesCollection;

import java.io.IOException;
import java.util.List;

/**
 * Feature object for embedded file
 *
 * @author Maksim Bezrukov
 */
public class PBEmbeddedFileFeaturesObject implements IFeaturesObject {

	private static final Logger LOGGER = Logger
			.getLogger(PBEmbeddedFileFeaturesObject.class);

	private static final String CREATION_DATE = "creationDate";
	private static final String MOD_DATE = "modDate";

	private PDComplexFileSpecification embFile;
	private int index;

	/**
	 * Constructs new Embedded File Feature Object
	 *
	 * @param embFile pdfbox class represents Embedded File object
	 * @param index   page index
	 */
	public PBEmbeddedFileFeaturesObject(PDComplexFileSpecification embFile, int index) {
		this.embFile = embFile;
		this.index = index;
	}

	/**
	 * @return EMBEDDED_FILE instance of the FeaturesObjectTypesEnum enumeration
	 */
	@Override
	public FeaturesObjectTypesEnum getType() {
		return FeaturesObjectTypesEnum.EMBEDDED_FILE;
	}

	/**
	 * Reports all features from the object into the collection
	 *
	 * @param collection collection for feature report
	 * @return FeatureTreeNode class which represents a root node of the constructed collection tree
	 * @throws FeatureParsingException occurs when wrong features tree node constructs
	 */
	@Override
	public FeatureTreeNode reportFeatures(FeaturesCollection collection) throws FeatureParsingException {

		if (embFile != null) {
			FeatureTreeNode root = FeatureTreeNode.createRootNode("embeddedFile");
			root.setAttribute("id", "file" + index);

			PBCreateNodeHelper.addNotEmptyNode("fileName", embFile.getFilename(), root);
			PBCreateNodeHelper.addNotEmptyNode("description", embFile.getFileDescription(), root);

			PDEmbeddedFile ef = embFile.getEmbeddedFile();
			if (ef != null) {
				PBCreateNodeHelper.addNotEmptyNode("subtype", ef.getSubtype(), root);

				PBCreateNodeHelper.addNotEmptyNode("filter", getFilters(ef.getFilters()), root);

				try {
					PBCreateNodeHelper.createDateNode(CREATION_DATE, root, ef.getCreationDate(), collection);
				} catch (IOException e) {
					LOGGER.debug("PDFBox error obtaining creation date", e);
					FeatureTreeNode creationDate = FeatureTreeNode.createChildNode(CREATION_DATE, root);
					ErrorsHelper.addErrorIntoCollection(collection,
							creationDate,
							e.getMessage());
				}

				try {
					PBCreateNodeHelper.createDateNode(MOD_DATE, root, ef.getModDate(), collection);
				} catch (IOException e) {
					LOGGER.debug("PDFBox error obtaining modification date", e);
					FeatureTreeNode modDate = FeatureTreeNode.createChildNode(MOD_DATE, root);
					ErrorsHelper.addErrorIntoCollection(collection,
							modDate,
							e.getMessage());
				}

				COSBase baseParams = ef.getStream().getDictionaryObject(COSName.PARAMS);
				if (baseParams instanceof COSDictionary) {
					COSBase baseChecksum = ((COSDictionary) baseParams).getDictionaryObject(COSName.getPDFName("CheckSum"));
					if (baseChecksum instanceof COSString) {
						COSString str = (COSString) baseChecksum;
						if (str.isHex()) {
							PBCreateNodeHelper.addNotEmptyNode("checkSum", str.toHexString(), root);
						} else {
							PBCreateNodeHelper.addNotEmptyNode("checkSum", str.getString(), root);
						}
					}
				}
				PBCreateNodeHelper.addNotEmptyNode("size", String.valueOf(ef.getSize()), root);
			}

			collection.addNewFeatureTree(FeaturesObjectTypesEnum.EMBEDDED_FILE, root);
			return root;
		}
		return null;
	}

	/**
	 * @return null if it can not get embedded file stream and features data of the embedded file in other case.
	 */
	@Override
	public FeaturesData getData() {
		try {
			PDEmbeddedFile ef = embFile.getEmbeddedFile();
			if (ef == null) {
				LOGGER.error("Missed embedded file in PDComplexFileSpecification");
				return null;
			}
			byte[] stream = PBCreateNodeHelper.inputStreamToByteArray(ef.getStream().getUnfilteredStream());

			EmbeddedFileFeaturesData.Builder builder = new EmbeddedFileFeaturesData.Builder(stream);

			builder.name(embFile.getFilename());
			builder.description(embFile.getFileDescription());
			builder.subtype(ef.getSubtype());
			builder.creationDate(ef.getCreationDate());
			builder.modDate(ef.getModDate());
			builder.size(ef.getSize());

			COSBase baseParams = ef.getStream().getDictionaryObject(COSName.PARAMS);
			if (baseParams instanceof COSDictionary) {
				COSBase baseChecksum = ((COSDictionary) baseParams).getDictionaryObject(COSName.getPDFName("CheckSum"));
				if (baseChecksum instanceof COSString) {
					COSString str = (COSString) baseChecksum;
					if (str.isHex()) {
						builder.checkSum(str.toHexString());
					} else {
						builder.checkSum(str.getString());
					}
				}
			}

			return builder.build();
		} catch (IOException e) {
			LOGGER.error("Can not get embedded file stream", e);
			return null;
		}
	}

	private static String getFilters(List<COSName> list) {
		if (list != null) {
			StringBuilder builder = new StringBuilder();

			for (COSName filter : list) {
				if (filter != null && filter.getName() != null) {
					builder.append(filter.getName());
					builder.append(" ");
				}
			}

			return builder.toString().trim();
		}
		return null;
	}
}

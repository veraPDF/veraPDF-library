package org.verapdf.features.pb.objects;

import org.apache.log4j.Logger;
import org.apache.pdfbox.cos.*;
import org.verapdf.core.FeatureParsingException;
import org.verapdf.features.FeaturesData;
import org.verapdf.features.FeaturesObjectTypesEnum;
import org.verapdf.features.IFeaturesObject;
import org.verapdf.features.pb.tools.PBCreateNodeHelper;
import org.verapdf.features.tools.ErrorsHelper;
import org.verapdf.features.tools.FeatureTreeNode;
import org.verapdf.features.tools.FeaturesCollection;

import java.util.*;

/**
 * Feature object for low level info part of the features report
 *
 * @author Maksim Bezrukov
 */
public class PBLowLvlInfoFeaturesObject implements IFeaturesObject {

	private static final Logger LOGGER = Logger
			.getLogger(PBEmbeddedFileFeaturesObject.class);

	private COSDocument document;
	private static final Map<String, String> filtersAbbreviations;

	static {
		Map<String, String> filtersAbbreviationsTemp = new HashMap<>();

		filtersAbbreviationsTemp.put("AHx", "ASCIIHexDecode");
		filtersAbbreviationsTemp.put("A85", "ASCII85Decode");
		filtersAbbreviationsTemp.put("LZW", "LZWDecode");
		filtersAbbreviationsTemp.put("Fl", "FlateDecode");
		filtersAbbreviationsTemp.put("RL", "RunLengthDecode");
		filtersAbbreviationsTemp.put("CCF", "CCITTFaxDecode");
		filtersAbbreviationsTemp.put("DCT", "DCTDecode");
		filtersAbbreviations = Collections.unmodifiableMap(filtersAbbreviationsTemp);
	}

	/**
	 * Constructs new low level info feature object.
	 *
	 * @param document pdfbox class represents document object
	 */
	public PBLowLvlInfoFeaturesObject(COSDocument document) {
		this.document = document;
	}

	/**
	 * @return LOW_LVL_INFO instance of the FeaturesObjectTypesEnum enumeration
	 */
	@Override
	public FeaturesObjectTypesEnum getType() {
		return FeaturesObjectTypesEnum.LOW_LEVEL_INFO;
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
		if (document != null) {
			FeatureTreeNode root = FeatureTreeNode.createRootNode("lowLevelInfo");


			if (document.getObjects() != null) {
				FeatureTreeNode.createChildNode("indirectObjectsNumber", root).setValue(String.valueOf(document.getObjects().size()));
			}

			addDocumentId(root, collection);

			Set<String> filters = getAllFilters();

			if (!filters.isEmpty()) {
				FeatureTreeNode filtersNode = FeatureTreeNode.createChildNode("filters", root);

				for (String filter : filters) {
					if (filter != null) {
						FeatureTreeNode filterNode = FeatureTreeNode.createChildNode("filter", filtersNode);
						filterNode.setAttribute("name", filter);
					}
				}
			}

			collection.addNewFeatureTree(FeaturesObjectTypesEnum.LOW_LEVEL_INFO, root);
			return root;

		}
		return null;
	}

	/**
	 * @return null
	 */
	@Override
	public FeaturesData getData() {
		return null;
	}

	private Set<String> getAllFilters() {
		Set<String> res = new HashSet<>();

		for (COSBase base : document.getObjects()) {

			while (base instanceof COSObject) {
				base = ((COSObject) base).getObject();
			}

			if (base instanceof COSStream) {
				COSStream stream = (COSStream) base;

				COSBase baseFilter = stream.getFilters();

				if (baseFilter != null) {
					addFiltersFromBase(res, baseFilter);
				}
			}
		}

		return res;
	}

	private void addDocumentId(FeatureTreeNode root, FeaturesCollection collection) throws FeatureParsingException {
		COSArray ids = document.getDocumentID();
		if (ids != null) {
			String creationId = PBCreateNodeHelper.getStringFromBase(ids.get(0));
			String modificationId = PBCreateNodeHelper.getStringFromBase(ids.get(1));

			FeatureTreeNode documentId = FeatureTreeNode.createChildNode("documentId", root);

			if (creationId != null || modificationId != null) {
				if (creationId != null) {
					documentId.setAttribute("creationId", creationId);
				}
				if (modificationId != null) {
					documentId.setAttribute("modificationId", modificationId);
				}
			}

			if (ids.size() != 2 || creationId == null || modificationId == null) {
				ErrorsHelper.addErrorIntoCollection(collection,
						documentId,
						"Document's ID must be an array of two not null elements");
			}
		}
	}

	private static void addFiltersFromBase(Set<String> res, COSBase base) {
		if (base instanceof COSName) {
			String name = ((COSName) base).getName();
			if (filtersAbbreviations.keySet().contains(name)) {
				name = filtersAbbreviations.get(name);
			}
			res.add(name);

		} else if (base instanceof COSArray) {

			for (COSBase baseElement : (COSArray) base) {
				if (baseElement instanceof COSName) {
					String name = ((COSName) baseElement).getName();
					if (filtersAbbreviations.keySet().contains(name)) {
						name = filtersAbbreviations.get(name);
					}
					res.add(name);
				}
			}
		}
	}
}

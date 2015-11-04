package org.verapdf.features.pb.objects;

import org.apache.pdfbox.pdmodel.common.PDMetadata;
import org.verapdf.core.FeatureParsingException;
import org.verapdf.features.FeaturesData;
import org.verapdf.features.FeaturesObjectTypesEnum;
import org.verapdf.features.IFeaturesObject;
import org.verapdf.features.pb.tools.PBCreateNodeHelper;
import org.verapdf.features.tools.FeatureTreeNode;
import org.verapdf.features.tools.FeaturesCollection;

/**
 * Feature object for metadata
 *
 * @author Maksim Bezrukov
 */
public class PBMetadataFeaturesObject implements IFeaturesObject {

	private PDMetadata metadata;

	/**
	 * Constructs new Metadata Feature Object
	 *
	 * @param metadata pdfbox class represents metadata object
	 */
	public PBMetadataFeaturesObject(PDMetadata metadata) {
		this.metadata = metadata;
	}

	/**
	 * @return METADATA instance of the FeaturesObjectTypesEnum enumeration
	 */
	@Override
	public FeaturesObjectTypesEnum getType() {
		return FeaturesObjectTypesEnum.METADATA;
	}

	/**
	 * Reports featurereport into collection
	 *
	 * @param collection collection for feature report
	 * @return FeatureTreeNode class which represents a root node of the constructed collection tree
	 * @throws FeatureParsingException occurs when wrong features tree node constructs
	 */
	@Override
	public FeatureTreeNode reportFeatures(FeaturesCollection collection) throws FeatureParsingException {
		if (metadata != null) {
			FeatureTreeNode root = PBCreateNodeHelper.parseMetadata(metadata, "metadata", null, collection);

			if (root != null) {
				collection.addNewFeatureTree(FeaturesObjectTypesEnum.METADATA, root);
			}
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
}

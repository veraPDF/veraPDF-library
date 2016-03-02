package org.verapdf.features.pb.objects;

import org.apache.log4j.Logger;
import org.apache.pdfbox.pdmodel.common.PDMetadata;
import org.verapdf.core.FeatureParsingException;
import org.verapdf.features.FeaturesData;
import org.verapdf.features.FeaturesObjectTypesEnum;
import org.verapdf.features.IFeaturesObject;
import org.verapdf.features.MetadataFeaturesData;
import org.verapdf.features.pb.tools.PBCreateNodeHelper;
import org.verapdf.features.tools.FeatureTreeNode;
import org.verapdf.features.tools.FeaturesCollection;

import java.io.IOException;

/**
 * Feature object for metadata
 *
 * @author Maksim Bezrukov
 */
public class PBMetadataFeaturesObject implements IFeaturesObject {

	private static final Logger LOGGER = Logger
			.getLogger(PBMetadataFeaturesObject.class);

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
	 * @return null if it can not get metadata stream and features data of the metadata in other case.
	 */
	@Override
	public FeaturesData getData() {
		if (metadata == null) {
			return null;
		}
		try {
			byte[] meta = metadata.getByteArray();
			return MetadataFeaturesData.newInstance(meta);
		} catch (IOException e) {
			LOGGER.error("Error while obtaining unfiltered metadata stream", e);
			return null;
		}
	}
}

package org.verapdf.features.pb.objects;

import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.verapdf.core.FeatureParsingException;
import org.verapdf.features.FeaturesData;
import org.verapdf.features.FeaturesObjectTypesEnum;
import org.verapdf.features.IFeaturesObject;
import org.verapdf.features.pb.tools.PBCreateNodeHelper;
import org.verapdf.features.tools.FeatureTreeNode;
import org.verapdf.features.tools.FeaturesCollection;

import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

/**
 * Feature object for information dictionary
 *
 * @author Maksim Bezrukov
 */
public class PBInfoDictFeaturesObject implements IFeaturesObject {

	private static final String[] predefinedKeys = {"Title", "Author", "Subject", "Keywords", "Creator", "Producer", "CreationDate", "ModDate", "Trapped"};

	private static final String ENTRY = "entry";
	private static final String KEY = "key";

	private PDDocumentInformation info;

	/**
	 * Constructs new information dictionary feature object.
	 *
	 * @param info pdfbox class represents page object
	 */
	public PBInfoDictFeaturesObject(PDDocumentInformation info) {
		this.info = info;
	}

	/**
	 * @return INFORMATION_DICTIONARY instance of the FeaturesObjectTypesEnum enumeration
	 */
	@Override
	public FeaturesObjectTypesEnum getType() {
		return FeaturesObjectTypesEnum.INFORMATION_DICTIONARY;
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

		if (info != null) {
			FeatureTreeNode root = FeatureTreeNode.createRootNode("informationDict");

			addEntry("Title", info.getTitle(), root);
			addEntry("Author", info.getAuthor(), root);
			addEntry("Subject", info.getSubject(), root);
			addEntry("Keywords", info.getKeywords(), root);
			addEntry("Creator", info.getCreator(), root);
			addEntry("Producer", info.getProducer(), root);

			FeatureTreeNode creationDate = PBCreateNodeHelper.createDateNode(ENTRY, root, info.getCreationDate(), collection);
			if (creationDate != null) {
				creationDate.setAttribute(KEY, "CreationDate");
			}

			FeatureTreeNode modificationDate = PBCreateNodeHelper.createDateNode(ENTRY, root, info.getModificationDate(), collection);
			if (modificationDate != null) {
				modificationDate.setAttribute(KEY, "ModDate");
			}

			addEntry("Trapped", info.getTrapped(), root);

			if (info.getMetadataKeys() != null) {
				Set<String> keys = new TreeSet<>(info.getMetadataKeys());
				keys.removeAll(Arrays.asList(predefinedKeys));
				for (String key : keys) {
					addEntry(key, info.getCustomMetadataValue(key), root);
				}
			}

			collection.addNewFeatureTree(FeaturesObjectTypesEnum.INFORMATION_DICTIONARY, root);

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

	private static void addEntry(String name, String value, FeatureTreeNode root) throws FeatureParsingException {
		if (name != null && value != null) {
			FeatureTreeNode entry = FeatureTreeNode.createChildNode(ENTRY, root);
			entry.setValue(value);
			entry.setAttribute(KEY, name);
		}
	}
}

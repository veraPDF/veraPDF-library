package org.verapdf.features.pb.objects;

import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSName;
import org.verapdf.core.FeatureParsingException;
import org.verapdf.features.FeaturesData;
import org.verapdf.features.FeaturesObjectTypesEnum;
import org.verapdf.features.IFeaturesObject;
import org.verapdf.features.pb.tools.PBCreateNodeHelper;
import org.verapdf.features.tools.FeatureTreeNode;
import org.verapdf.features.tools.FeaturesCollection;

import java.util.Set;

/**
 * @author Maksim Bezrukov
 */
public class PBPropertiesDictFeaturesObject implements IFeaturesObject {

	private COSDictionary properties;
	private String id;
	private Set<String> pageParent;
	private Set<String> patternParent;
	private Set<String> xobjectParent;
	private Set<String> fontParent;

	/**
	 * Constructs new propertiesDict features object
	 *
	 * @param properties    COSDictionary which represents properties for feature report
	 * @param id            id of the object
	 * @param pageParent    set of page ids which contains the given properties as its resources
	 * @param patternParent set of pattern ids which contains the given properties as its resources
	 * @param xobjectParent set of xobject ids which contains the given properties as its resources
	 * @param fontParent    set of font ids which contains the given properties as its resources
	 */
	public PBPropertiesDictFeaturesObject(COSDictionary properties, String id, Set<String> pageParent, Set<String> patternParent, Set<String> xobjectParent, Set<String> fontParent) {
		this.properties = properties;
		this.id = id;
		this.pageParent = pageParent;
		this.patternParent = patternParent;
		this.xobjectParent = xobjectParent;
		this.fontParent = fontParent;
	}

	/**
	 * @return PROPERTIES instance of the FeaturesObjectTypesEnum enumeration
	 */
	@Override
	public FeaturesObjectTypesEnum getType() {
		return FeaturesObjectTypesEnum.PROPERTIES;
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
		if (properties != null) {
			FeatureTreeNode root = FeatureTreeNode.createRootNode("propertiesDict");
			root.setAttribute("id", id);

			parseParents(root);

			COSBase type = properties.getDictionaryObject(COSName.TYPE);
			if (type instanceof COSName) {
				PBCreateNodeHelper.addNotEmptyNode("type", ((COSName) type).getName(), root);
			}

			collection.addNewFeatureTree(FeaturesObjectTypesEnum.PROPERTIES, root);
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

	private void parseParents(FeatureTreeNode root) throws FeatureParsingException {
		if ((pageParent != null && !pageParent.isEmpty()) ||
				(patternParent != null && !patternParent.isEmpty()) ||
				(xobjectParent != null && !xobjectParent.isEmpty()) ||
				(fontParent != null && !fontParent.isEmpty())) {
			FeatureTreeNode parents = FeatureTreeNode.createChildNode("parents", root);

			PBCreateNodeHelper.parseIDSet(pageParent, "page", null, parents);
			PBCreateNodeHelper.parseIDSet(patternParent, "pattern", null, parents);
			PBCreateNodeHelper.parseIDSet(xobjectParent, "xobject", null, parents);
			PBCreateNodeHelper.parseIDSet(fontParent, "font", null, parents);
		}
	}
}

package org.verapdf.features.pb.objects;

import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.cos.COSNumber;
import org.apache.pdfbox.cos.COSObject;
import org.apache.pdfbox.pdmodel.PDPage;
import org.verapdf.exceptions.featurereport.FeaturesTreeNodeException;
import org.verapdf.features.FeaturesObjectTypesEnum;
import org.verapdf.features.IFeaturesObject;
import org.verapdf.features.pb.tools.PBCreateNodeHelper;
import org.verapdf.features.tools.ErrorsHelper;
import org.verapdf.features.tools.FeatureTreeNode;
import org.verapdf.features.tools.FeaturesCollection;

import java.util.Set;

/**
 * Feature object for page
 *
 * @author Maksim Bezrukov
 */
public class PBPageFeaturesObject implements IFeaturesObject {

	private static final String ID = "id";

	private PDPage page;
	private String thumb;
	private Set<String> annotsId;
	private Set<String> extGStateChild;
	private Set<String> colorSpaceChild;
	private Set<String> patternChild;
	private Set<String> shadingChild;
	private Set<String> xobjectChild;
	private Set<String> fontChild;
	private Set<String> procSetChild;
	private Set<String> propertiesChild;
	private String id;
	private int index;

	/**
	 * Constructs new Page Feature Object
	 *
	 * @param page            pdfbox class represents page object
	 * @param thumb           thumbnail image id
	 * @param annotsId        set of annotations id which contains in this page
	 * @param extGStateChild  set of external graphics state id which contains in resource dictionary of this page
	 * @param colorSpaceChild set of ColorSpace id which contains in resource dictionary of this page
	 * @param patternChild    set of pattern id which contains in resource dictionary of this page
	 * @param shadingChild    set of shading id which contains in resource dictionary of this page
	 * @param xobjectChild    set of XObject id which contains in resource dictionary of this page
	 * @param fontChild       set of font id which contains in resource dictionary of this page
	 * @param procSetChild    set of procedure set id which contains in resource dictionary of this page
	 * @param propertiesChild set of properties id which contains in resource dictionary of this page
	 * @param id              page id
	 * @param index           page index
	 */
	public PBPageFeaturesObject(PDPage page,
								String thumb,
								Set<String> annotsId,
								Set<String> extGStateChild,
								Set<String> colorSpaceChild,
								Set<String> patternChild,
								Set<String> shadingChild,
								Set<String> xobjectChild,
								Set<String> fontChild,
								Set<String> procSetChild,
								Set<String> propertiesChild,
								String id,
								int index) {
		this.page = page;
		this.thumb = thumb;
		this.annotsId = annotsId;
		this.extGStateChild = extGStateChild;
		this.colorSpaceChild = colorSpaceChild;
		this.patternChild = patternChild;
		this.shadingChild = shadingChild;
		this.xobjectChild = xobjectChild;
		this.fontChild = fontChild;
		this.procSetChild = procSetChild;
		this.propertiesChild = propertiesChild;
		this.id = id;
		this.index = index;
	}

	/**
	 * @return PAGE instance of the FeaturesObjectTypesEnum enumeration
	 */
	@Override
	public FeaturesObjectTypesEnum getType() {
		return FeaturesObjectTypesEnum.PAGE;
	}

	/**
	 * Reports featurereport into collection
	 *
	 * @param collection collection for feature report
	 * @return FeatureTreeNode class which represents a root node of the constructed collection tree
	 * @throws FeaturesTreeNodeException occurs when wrong features tree node constructs
	 */
	@Override
	public FeatureTreeNode reportFeatures(FeaturesCollection collection) throws FeaturesTreeNodeException {
		if (page != null) {
			FeatureTreeNode root = FeatureTreeNode.newRootInstance("page");

			root.addAttribute(ID, id);
			root.addAttribute("orderNumber", Integer.toString(index));

			PBCreateNodeHelper.addBoxFeature("mediaBox", page.getMediaBox(), root);
			PBCreateNodeHelper.addBoxFeature("cropBox", page.getCropBox(), root);
			PBCreateNodeHelper.addBoxFeature("trimBox", page.getTrimBox(), root);
			PBCreateNodeHelper.addBoxFeature("bleedBox", page.getBleedBox(), root);
			PBCreateNodeHelper.addBoxFeature("artBox", page.getArtBox(), root);

			FeatureTreeNode.newChildInstanceWithValue("rotation", Integer.toString(page.getRotation()), root);

			COSBase base = page.getCOSObject().getDictionaryObject(COSName.getPDFName("PZ"));
			if (base != null) {
				FeatureTreeNode scaling = FeatureTreeNode.newChildInstance("scaling", root);

				while (base instanceof COSObject) {
					base = ((COSObject) base).getObject();
				}

				if (base instanceof COSNumber) {
					COSNumber number = (COSNumber) base;
					scaling.setValue(String.valueOf(number.doubleValue()));
				} else {
					scaling.addAttribute(ErrorsHelper.ERRORID, ErrorsHelper.PAGESCALLING_ID);

					ErrorsHelper.addErrorIntoCollection(collection, ErrorsHelper.PAGESCALLING_ID, ErrorsHelper.PAGESCALLING_MESSAGE);
				}
			}

			if (thumb != null) {
				FeatureTreeNode thumbNode = FeatureTreeNode.newChildInstance("thumbnail", root);
				thumbNode.addAttribute(ID, thumb);
			}

			PBCreateNodeHelper.parseMetadata(page.getMetadata(), "metadata", root, collection);

			PBCreateNodeHelper.parseIDSet(annotsId, "annotation", "annotations", root);

			parseResources(root);

			collection.addNewFeatureTree(FeaturesObjectTypesEnum.PAGE, root);

			return root;

		}
		return null;
	}

	private void parseResources(FeatureTreeNode root) throws FeaturesTreeNodeException {
		if ((extGStateChild != null && !extGStateChild.isEmpty()) ||
				(colorSpaceChild != null && !colorSpaceChild.isEmpty()) ||
				(patternChild != null && !patternChild.isEmpty()) ||
				(shadingChild != null && !shadingChild.isEmpty()) ||
				(xobjectChild != null && !xobjectChild.isEmpty()) ||
				(fontChild != null && !fontChild.isEmpty()) ||
				(procSetChild != null && !procSetChild.isEmpty()) ||
				(propertiesChild != null && !propertiesChild.isEmpty())) {
			FeatureTreeNode resources = FeatureTreeNode.newChildInstance("resources", root);

			PBCreateNodeHelper.parseIDSet(extGStateChild, "graphicsState", "graphicsStates", resources);
			PBCreateNodeHelper.parseIDSet(colorSpaceChild, "colorSpace", "colorSpaces", resources);
			PBCreateNodeHelper.parseIDSet(patternChild, "pattern", "patterns", resources);
			PBCreateNodeHelper.parseIDSet(shadingChild, "shading", "shadings", resources);
			PBCreateNodeHelper.parseIDSet(xobjectChild, "xobject", "xobjects", resources);
			PBCreateNodeHelper.parseIDSet(fontChild, "font", "fonts", resources);
			PBCreateNodeHelper.parseIDSet(procSetChild, "procSet", "procSets", resources);
			PBCreateNodeHelper.parseIDSet(propertiesChild, "propertiesDict", "propertiesDicts", resources);
		}
	}
}

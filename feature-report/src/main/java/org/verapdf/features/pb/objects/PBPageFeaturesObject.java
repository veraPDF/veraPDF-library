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
    private Set<String> annotsId;
    private String id;
    private int index;

    /**
     * Constructs new Page Feature Object
     *
     * @param page  - pdfbox class represents page object
     * @param annotsId - set of annotations id which contains in this page
     * @param id - page id
     * @param index - page index
     */
    public PBPageFeaturesObject(PDPage page, Set<String> annotsId, String id, int index) {
        this.page = page;
        this.annotsId = annotsId;
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
     * @param collection - collection for feature report
     * @return FeatureTreeNode class which represents a root node of the constructed collection tree
     * @throws FeaturesTreeNodeException   - occurs when wrong features tree node constructs
     */
    @Override
    public FeatureTreeNode reportFeatures(FeaturesCollection collection) throws FeaturesTreeNodeException{
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

            FeatureTreeNode.newChildInstanceWithValue("thumbnail", Boolean.toString(page.getCOSObject().getDictionaryObject(COSName.getPDFName("Thumb")) != null), root);

            if (annotsId != null) {
                FeatureTreeNode annotations = FeatureTreeNode.newChildInstance("annotations", root);
                for (String annot : annotsId) {
                    if (annot != null) {
                        FeatureTreeNode annotNode = FeatureTreeNode.newChildInstance("annotation", annotations);
                        annotNode.addAttribute(ID, annot);
                    }
                }
            }

            // TODO: add <resources>

            collection.addNewFeatureTree(FeaturesObjectTypesEnum.PAGE, root);

            return root;

        }
        return null;
    }
}

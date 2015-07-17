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

/**
 * Feature object for page
 *
 * @author Maksim Bezrukov
 */
public class PBPageFeaturesObject implements IFeaturesObject {

    private static final String PAGE = "page";

    private PDPage page;
    private int index;

    /**
     * Constructs new Page Feature Object
     *
     * @param page  - pdfbox class represents page object
     * @param index - page index
     */
    public PBPageFeaturesObject(PDPage page, int index) {
        this.page = page;
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
            FeatureTreeNode root = FeatureTreeNode.newInstance(PAGE, null);

            root.addAttribute("id", PAGE + index);
            root.addAttribute("orderNumber", Integer.toString(index));

            PBCreateNodeHelper.addBoxFeature("mediaBox", page.getMediaBox(), root);
            PBCreateNodeHelper.addBoxFeature("cropBox", page.getCropBox(), root);
            PBCreateNodeHelper.addBoxFeature("trimBox", page.getTrimBox(), root);
            PBCreateNodeHelper.addBoxFeature("bleedBox", page.getBleedBox(), root);
            PBCreateNodeHelper.addBoxFeature("artBox", page.getArtBox(), root);

            FeatureTreeNode.newInstance("rotation", Integer.toString(page.getRotation()), root);

            COSBase base = page.getCOSObject().getDictionaryObject(COSName.getPDFName("PZ"));
            if (base != null) {
                FeatureTreeNode scaling = FeatureTreeNode.newInstance("scaling", root);

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

            FeatureTreeNode.newInstance("thumbnail", Boolean.toString(page.getCOSObject().getDictionaryObject(COSName.getPDFName("Thumb")) != null), root);

            // TODO: add <resources> and <annotations>

            collection.addNewFeatureTree(FeaturesObjectTypesEnum.PAGE, root);

            return root;

        } else {
            return null;
        }
    }
}

package org.verapdf.features.pb.objects;

import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.cos.COSNumber;
import org.apache.pdfbox.cos.COSObject;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.verapdf.exceptions.featurereport.FeaturesTreeNodeException;
import org.verapdf.features.FeaturesObjectTypesEnum;
import org.verapdf.features.IFeaturesObject;
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
    private static final String LLX = "llx";
    private static final String LLY = "lly";
    private static final String URX = "urx";
    private static final String URY = "ury";

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

            addBoxFeature("mediaBox", page.getMediaBox(), root);
            addBoxFeature("cropBox", page.getCropBox(), root);
            addBoxFeature("trimBox", page.getTrimBox(), root);
            addBoxFeature("bleedBox", page.getBleedBox(), root);
            addBoxFeature("artBox", page.getArtBox(), root);

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

    private void addBoxFeature(String name, PDRectangle box, FeatureTreeNode root) throws FeaturesTreeNodeException {
        if (box != null) {
            FeatureTreeNode boxNode = FeatureTreeNode.newInstance(name, root);
            boxNode.addAttribute(LLX, String.valueOf(box.getLowerLeftX()));
            boxNode.addAttribute(LLY, String.valueOf(box.getLowerLeftY()));
            boxNode.addAttribute(URX, String.valueOf(box.getUpperRightX()));
            boxNode.addAttribute(URY, String.valueOf(box.getUpperRightY()));
        }
    }

}

package org.verapdf.features.pb.objects;

import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.cos.COSNumber;
import org.apache.pdfbox.cos.COSObject;
import org.apache.pdfbox.pdmodel.PDPage;
import org.verapdf.exceptions.featurereport.FeatureValueException;
import org.verapdf.exceptions.featurereport.FeaturesTreeNodeException;
import org.verapdf.features.FeaturesObjectTypesEnum;
import org.verapdf.features.IFeaturesObject;
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
     * @throws FeatureValueException - occurs when wrong feature feature format found during features parser
     */
    @Override
    public FeatureTreeNode reportFeatures(FeaturesCollection collection) throws FeaturesTreeNodeException, FeatureValueException {

        FeatureTreeNode root = FeatureTreeNode.newInstance(PAGE, null);

        root.addAttribute("id", PAGE + index);
        root.addAttribute("orderNumber", Integer.toString(index));

        if (page.getMediaBox() != null) {
            FeatureTreeNode mediaBox = FeatureTreeNode.newInstance("mediaBox", root);
            mediaBox.addAttribute(LLX, String.valueOf(page.getMediaBox().getLowerLeftX()));
            mediaBox.addAttribute(LLY, String.valueOf(page.getMediaBox().getLowerLeftY()));
            mediaBox.addAttribute(URX, String.valueOf(page.getMediaBox().getUpperRightX()));
            mediaBox.addAttribute(URY, String.valueOf(page.getMediaBox().getUpperRightY()));
        }

        if (page.getCropBox() != null) {
            FeatureTreeNode cropBox = FeatureTreeNode.newInstance("cropBox", root);
            cropBox.addAttribute(LLX, String.valueOf(page.getCropBox().getLowerLeftX()));
            cropBox.addAttribute(LLY, String.valueOf(page.getCropBox().getLowerLeftY()));
            cropBox.addAttribute(URX, String.valueOf(page.getCropBox().getUpperRightX()));
            cropBox.addAttribute(URY, String.valueOf(page.getCropBox().getUpperRightY()));

        }

        if (page.getTrimBox() != null) {
            FeatureTreeNode trimBox = FeatureTreeNode.newInstance("trimBox", root);
            trimBox.addAttribute(LLX, String.valueOf(page.getTrimBox().getLowerLeftX()));
            trimBox.addAttribute(LLY, String.valueOf(page.getTrimBox().getLowerLeftY()));
            trimBox.addAttribute(URX, String.valueOf(page.getTrimBox().getUpperRightX()));
            trimBox.addAttribute(URY, String.valueOf(page.getTrimBox().getUpperRightY()));
        }

        if (page.getBleedBox() != null) {
            FeatureTreeNode bleedBox = FeatureTreeNode.newInstance("bleedBox", root);
            bleedBox.addAttribute(LLX, String.valueOf(page.getBleedBox().getLowerLeftX()));
            bleedBox.addAttribute(LLY, String.valueOf(page.getBleedBox().getLowerLeftY()));
            bleedBox.addAttribute(URX, String.valueOf(page.getBleedBox().getUpperRightX()));
            bleedBox.addAttribute(URY, String.valueOf(page.getBleedBox().getUpperRightY()));
        }

        if (page.getArtBox() != null) {
            FeatureTreeNode artBox = FeatureTreeNode.newInstance("artBox", root);
            artBox.addAttribute(LLX, String.valueOf(page.getArtBox().getLowerLeftX()));
            artBox.addAttribute(LLY, String.valueOf(page.getArtBox().getLowerLeftY()));
            artBox.addAttribute(URX, String.valueOf(page.getArtBox().getUpperRightX()));
            artBox.addAttribute(URY, String.valueOf(page.getArtBox().getUpperRightY()));
        }

        FeatureTreeNode rotation = FeatureTreeNode.newInstance("rotation", Integer.toString(page.getRotation()), root);

        COSBase base = page.getCOSObject().getDictionaryObject(COSName.getPDFName("PZ"));
        if (base != null) {
            COSNumber number = getScalingNumber(base);
            FeatureTreeNode scaling = FeatureTreeNode.newInstance("scaling", String.valueOf(number.doubleValue()), root);
        }

        FeatureTreeNode thumbnail = FeatureTreeNode.newInstance("thumbnail", Boolean.toString(page.getCOSObject().getDictionaryObject(COSName.getPDFName("Thumb")) != null), root);

        // TODO: add <resources> and <annotations>

        collection.addNewFeatureTree(FeaturesObjectTypesEnum.PAGE, root);

        return root;
    }

    private static COSNumber getScalingNumber(COSBase base) throws FeatureValueException {
        if (base instanceof COSNumber) {
            return (COSNumber) base;
        } else {
            throw new FeatureValueException("Page dictionary must contain number value for key \"PZ\"");
        }
    }
}

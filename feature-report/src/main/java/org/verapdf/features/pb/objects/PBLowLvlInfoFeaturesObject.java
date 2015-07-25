package org.verapdf.features.pb.objects;

import org.apache.pdfbox.contentstream.operator.Operator;
import org.apache.pdfbox.cos.*;
import org.apache.pdfbox.pdfparser.PDFStreamParser;
import org.verapdf.exceptions.featurereport.FeaturesTreeNodeException;
import org.verapdf.features.FeaturesObjectTypesEnum;
import org.verapdf.features.IFeaturesObject;
import org.verapdf.features.pb.tools.PBCreateNodeHelper;
import org.verapdf.features.tools.ErrorsHelper;
import org.verapdf.features.tools.FeatureTreeNode;
import org.verapdf.features.tools.FeaturesCollection;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Feature object for low level info part of the features report
 *
 * @author Maksim Bezrukov
 */
public class PBLowLvlInfoFeaturesObject implements IFeaturesObject {

    private static Map<String, String> filtersAbbreviations;

    private COSDocument document;

    static {
        filtersAbbreviations = new HashMap<>();

        filtersAbbreviations.put("AHx", "ASCIIHexDecode");
        filtersAbbreviations.put("A85", "ASCII85Decode");
        filtersAbbreviations.put("LZW", "LZWDecode");
        filtersAbbreviations.put("Fl", "FlateDecode");
        filtersAbbreviations.put("RL", "RunLengthDecode");
        filtersAbbreviations.put("CCF", "CCITTFaxDecode");
        filtersAbbreviations.put("DCT", "DCTDecode");
    }

    /**
     * Constructs new low level info feature object.
     *
     * @param document - pdfbox class represents document object
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
     * @param collection - collection for feature report
     * @return FeatureTreeNode class which represents a root node of the constructed collection tree
     * @throws FeaturesTreeNodeException   - occurs when wrong features tree node constructs
     */
    @Override
    public FeatureTreeNode reportFeatures(FeaturesCollection collection) throws FeaturesTreeNodeException {
        if (document != null) {
            FeatureTreeNode root = FeatureTreeNode.newRootInstance("lowLevelInfo");


            if (document.getObjects() != null) {
                FeatureTreeNode.newChildInstanceWithValue("indirectObjectsNumber", String.valueOf(document.getObjects().size()), root);
            }

            addDocumentId(root, collection);

            Set<String> filters = getAllFilters();

            if (filters.size() != 0) {
                FeatureTreeNode filtersNode = FeatureTreeNode.newChildInstance("filters", root);

                for (String filter : filters) {
                    if (filter != null) {
                        FeatureTreeNode filterNode = FeatureTreeNode.newChildInstance("filter", filtersNode);
                        filterNode.addAttribute("name", filter);
                    }
                }
            }

            collection.addNewFeatureTree(FeaturesObjectTypesEnum.LOW_LEVEL_INFO, root);
            return root;

        } else {
            return null;
        }
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

                try {
                    PDFStreamParser streamParser = new PDFStreamParser(stream);
                    streamParser.parse();

                    for (Object token : streamParser.getTokens()) {
                        if (token instanceof Operator) {
                            COSDictionary dict = ((Operator) token).getImageParameters();

                            if (dict != null) {
                                COSBase baseOpItem = dict.getDictionaryObject(COSName.F);

                                while (baseOpItem instanceof COSObject) {
                                    baseOpItem = ((COSObject) baseOpItem).getObject();
                                }

                                addFiltersFromBase(res, baseOpItem);
                            }
                        }
                    }

                } catch (IOException ignore) {
                    // Some error with initialising or parsing a stream.
                    // In this case we can't get any filters from it.
                }

            }
        }

        return res;
    }

    private void addDocumentId(FeatureTreeNode root, FeaturesCollection collection) throws FeaturesTreeNodeException {
        COSArray ids = document.getDocumentID();
        if (ids != null) {
            String creationId = PBCreateNodeHelper.getStringFromBase(ids.get(0));
            String modificationId = PBCreateNodeHelper.getStringFromBase(ids.get(1));

            FeatureTreeNode documentId = FeatureTreeNode.newChildInstance("documentId", root);

            if (creationId != null || modificationId != null) {
                if (creationId != null) {
                    documentId.addAttribute("creationId", creationId);
                }
                if (modificationId != null) {
                    documentId.addAttribute("modificationId", modificationId);
                }
            }

            if (ids.size() != 2 || creationId == null || modificationId == null) {
                documentId.addAttribute(ErrorsHelper.ERRORID, ErrorsHelper.LOWLVLINFODOCUMENTID_ID);
                ErrorsHelper.addErrorIntoCollection(collection, ErrorsHelper.LOWLVLINFODOCUMENTID_ID, ErrorsHelper.LOWLVLINFODOCUMENTID_MESSAGE);
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

            for (COSBase baseElement : ((COSArray) base)) {
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

package org.verapdf.features.objects;

import org.verapdf.core.FeatureParsingException;
import org.verapdf.features.FeatureObjectType;
import org.verapdf.features.FeaturesData;
import org.verapdf.features.ImageFeaturesData;
import org.verapdf.features.tools.CreateNodeHelper;
import org.verapdf.features.tools.FeatureTreeNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Features object for image xobject.
 *
 * @author Sergey Shemyakov
 */
public class ImageXObjectFeaturesObject extends FeaturesObject {

    private static final Logger LOGGER = Logger.getLogger(ImageXObjectFeaturesObject.class.getCanonicalName());

    private static final String X_OBJECT = "xobject";
    private static final String ID = "id";

    /**
     * Constructs new Image XObject Feature Object
     *
     * @param adapter class represents annotation adapter
     */
    public ImageXObjectFeaturesObject(ImageXObjectFeaturesObjectAdapter adapter) {
        super(adapter);
    }

    @Override
    public FeatureObjectType getType() {
        return FeatureObjectType.IMAGE_XOBJECT;
    }

    @Override
    protected FeatureTreeNode collectFeatures() throws FeatureParsingException {
        ImageXObjectFeaturesObjectAdapter xImageAdapter = (ImageXObjectFeaturesObjectAdapter) this.adapter;
        FeatureTreeNode root = FeatureTreeNode.createRootNode(X_OBJECT);
        root.setAttribute("type", "image");
        String id = xImageAdapter.getID();
        if (id != null) {
            root.setAttribute(ID, id);
        }
        Long width = xImageAdapter.getWidth();
        if (width != null) {
            CreateNodeHelper.addNotEmptyNode("width", String.valueOf(width.longValue()), root);
        }
        Long height = xImageAdapter.getHeight();
        if (height != null) {
            CreateNodeHelper.addNotEmptyNode("height", String.valueOf(height.longValue()), root);
        }

        String colorSpaceChild = xImageAdapter.getColorSpaceChild();
        if (colorSpaceChild != null) {
            FeatureTreeNode shading = root.addChild("colorSpace");
            shading.setAttribute(ID, colorSpaceChild);
        }

        Long bitsPerComponent = xImageAdapter.getBitsPerComponent();
        if (bitsPerComponent != null) {
            CreateNodeHelper.addNotEmptyNode("bitsPerComponent", String.valueOf(bitsPerComponent.longValue()), root);
        }

        root.addChild("imageMask").setValue(String.valueOf(xImageAdapter.getImageMask()));

        String maskChild = xImageAdapter.getMaskChild();
        if (maskChild != null) {
            FeatureTreeNode mask = root.addChild("mask");
            mask.setAttribute(ID, maskChild);
        }

        CreateNodeHelper.addNotEmptyNode("interpolate", String.valueOf(xImageAdapter.isInterpolate()), root);
        CreateNodeHelper.parseIDSet(xImageAdapter.getAlternatesChild(),
                "alternate", "alternates", root);
        String sMaskChild = xImageAdapter.getSMaskChild();
        if (sMaskChild != null) {
            FeatureTreeNode mask = root.addChild("sMask");
            mask.setAttribute(ID, sMaskChild);
        }

        Long struct = xImageAdapter.getStructParent();
        if (struct != null) {
            CreateNodeHelper.addNotEmptyNode("structParent", String.valueOf(struct.longValue()), root);
        }

        List<String> filtersList = xImageAdapter.getFilters();
        if (!filtersList.isEmpty()) {
            FeatureTreeNode filters = root.addChild("filters");
            for (String name : filtersList) {
                CreateNodeHelper.addNotEmptyNode("filter", name, filters);
            }
        }

        CreateNodeHelper.parseMetadata(xImageAdapter.getMetadata(), "metadata", root, this);
        return root;
    }

    @Override
    public FeaturesData getData() {
        ImageXObjectFeaturesObjectAdapter xImageAdapter = (ImageXObjectFeaturesObjectAdapter) this.adapter;
        if (adapter != null) {
            List<ImageFeaturesData.Filter> resFilters = new ArrayList<>();
            List<String> filterNames = xImageAdapter.getFilters();
            if (!filterNames.isEmpty()) {
                List<ImageXObjectFeaturesObjectAdapter.StreamFilterAdapter> filterAdapters =
                        xImageAdapter.getFilterAdapters();
                for (int i = 0; i < filterNames.size(); ++i) {
                    String filterNameValue = filterNames.get(i);
                    ImageXObjectFeaturesObjectAdapter.StreamFilterAdapter adapter =
                            filterAdapters.get(i);
                    switch (filterNameValue) {
                        case "LZWDecode":
                            resFilters.add(ImageFeaturesData.Filter.newInstance(filterNameValue, createLZWFilterMap(adapter),
                                    null));
                            break;
                        case "FlateDecode":
                            resFilters.add(ImageFeaturesData.Filter.newInstance(filterNameValue, createFlatFilterMap(adapter),
                                    null));
                            break;
                        case "CCITTFaxDecode":
                            resFilters.add(ImageFeaturesData.Filter.newInstance(filterNameValue, getCCITTFaxFiltersMap(adapter), null));
                            break;
                        case "DCTDecode":
                            resFilters.add(ImageFeaturesData.Filter.newInstance(filterNameValue, getDCTFiltersMap(adapter), null));
                            break;
                        case "JBIG2Decode":
                            resFilters.add(ImageFeaturesData.Filter.newInstance(filterNameValue, new HashMap<String, String>(),
                                    adapter.getJBIG2Global()));
                            break;
                        case "Crypt":
                            if (adapter.hasCryptFilter()) {
                                LOGGER.log(Level.FINE, "An Image has a Crypt filter");
                                return null;
                            }
                            //$FALL-THROUGH$
                        default:
                            resFilters.add(
                                    ImageFeaturesData.Filter.newInstance(filterNameValue, new HashMap<String, String>(), null));
                    }
                }
            }

            Integer width = getIntegerWithDefault(xImageAdapter.getWidth(), null);
            Integer height = getIntegerWithDefault(xImageAdapter.getHeight(), null);

            return ImageFeaturesData.newInstance(xImageAdapter.getMetadata(),
                    xImageAdapter.getRawStreamData(), width, height, resFilters);
        }
        return null;
    }

    private static Map<String, String> getCCITTFaxFiltersMap(
            ImageXObjectFeaturesObjectAdapter.StreamFilterAdapter filter) {
        Map<String, String> res = new HashMap<>();
        if (filter != null) {
            putIntegerAsStringWithDefault(res, "K", filter.getCCITTK(), Integer.valueOf(0));
            putBooleanAsStringWithDefault(res, "EndOfLine", filter.getCCITTEndOfLine(), Boolean.FALSE);
            putBooleanAsStringWithDefault(res, "EncodedByteAlign", filter.getCCITTEncodedByteAlign(),
                    Boolean.FALSE);
            putIntegerAsStringWithDefault(res, "Columns", filter.getCCITTColumns(),
                    Integer.valueOf(1728));
            putIntegerAsStringWithDefault(res, "Rows", filter.getCCITTRows(), Integer.valueOf(0));
            putBooleanAsStringWithDefault(res, "EndOfBlock", filter.getCCITTEndOfBlock(),
                    Boolean.TRUE);
            putBooleanAsStringWithDefault(res, "BlackIs1", filter.getCCITTBlackIs1(), Boolean.FALSE);
            putIntegerAsStringWithDefault(res, "DamagedRowsBeforeError",
                    filter.getCCITTDamagedRowsBeforeError(), Integer.valueOf(0));
        } else {
            res.put("K", "0");
            res.put("EndOfLine", "false");
            res.put("EncodedByteAlign", "false");
            res.put("Columns", "1728");
            res.put("Rows", "0");
            res.put("EndOfBlock", "true");
            res.put("BlackIs1", "false");
            res.put("DamagedRowsBeforeError", "0");
        }

        return res;
    }

    private static Map<String, String> getDCTFiltersMap(
            ImageXObjectFeaturesObjectAdapter.StreamFilterAdapter filter) {
        Map<String, String> res = new HashMap<>();
        if (filter != null) {
            Long colorTransform = filter.getDCTColorTransform();
            if (colorTransform != null) {
                res.put("ColorTransform", String.valueOf(colorTransform.longValue()));
            }
        }
        return res;
    }

    private static Map<String, String> createLZWFilterMap(
            ImageXObjectFeaturesObjectAdapter.StreamFilterAdapter filter) {
        if (filter == null) {
            Map<String, String> retVal = createDefaultFlatFilterMap();
            retVal.put("EarlyChange", "1");
            return retVal;
        }

        Map<String, String> retVal = createFlatFilterMap(filter);
        putIntegerAsStringWithDefault(retVal, "EarlyChange", filter.getLZWEarlyChange(),
                Integer.valueOf(1));
        return retVal;

    }

    private static Map<String, String> createFlatFilterMap(
            ImageXObjectFeaturesObjectAdapter.StreamFilterAdapter filter) {
        if (filter == null) {
            return createDefaultFlatFilterMap();
        }

        Map<String, String> res = new HashMap<>();

        putIntegerAsStringWithDefault(res, "Predictor", filter.getFlatePredictor(),
                Integer.valueOf(1));
        putIntegerAsStringWithDefault(res, "Colors", filter.getFlateColors(), Integer.valueOf(1));
        putIntegerAsStringWithDefault(res, "BitsPerComponent", filter.getFlateBitsPerComponent(),
                Integer.valueOf(8));
        putIntegerAsStringWithDefault(res, "Columns", filter.getFlateColumns(), Integer.valueOf(1));
        return res;
    }

    private static Map<String, String> createDefaultFlatFilterMap() {
        Map<String, String> res = new HashMap<>();
        res.put("Predictor", "1");
        res.put("Colors", "1");
        res.put("BitsPerComponent", "8");
        res.put("Columns", "1");
        return res;
    }

    private static Integer getIntegerWithDefault(Long value, Integer defaultValue) {
        if (value != null) {
            return Integer.valueOf(value.intValue());
        }
        return defaultValue;
    }

    private static void putIntegerAsStringWithDefault(Map<String, String> map, String key, Long value,
                                                      Integer defaultValue) {
        if (value != null) {
            map.put(key, String.valueOf(value.longValue()));
        } else {
            if (defaultValue != null) {
                map.put(key, defaultValue.toString());
            }
        }
    }

    private static void putBooleanAsStringWithDefault(Map<String, String> map, String key, Boolean value,
                                                      Boolean defaultValue) {
        if (value != null) {
            map.put(key, String.valueOf(value.booleanValue()));
        } else {
            if (defaultValue != null) {
                map.put(key, defaultValue.toString());
            }
        }
    }
}

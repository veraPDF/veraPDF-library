package org.verapdf.features.pb.objects;

import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.verapdf.exceptions.featurereport.FeaturesTreeNodeException;
import org.verapdf.features.FeaturesObjectTypesEnum;
import org.verapdf.features.IFeaturesObject;
import org.verapdf.features.tools.ErrorsHelper;
import org.verapdf.features.tools.FeatureTreeNode;
import org.verapdf.features.tools.FeaturesCollection;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.util.*;

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
     * @param info - pdfbox class represents page object
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
     * @param collection - collection for feature report
     * @return FeatureTreeNode class which represents a root node of the constructed collection tree
     * @throws FeaturesTreeNodeException   - occurs when wrong features tree node constructs
     */
    @Override
    public FeatureTreeNode reportFeatures(FeaturesCollection collection) throws FeaturesTreeNodeException{

        if (info != null) {
            FeatureTreeNode root = FeatureTreeNode.newInstance("informationDict", null);

            addEntry("Title", info.getTitle(), root);
            addEntry("Author", info.getAuthor(), root);
            addEntry("Subject", info.getSubject(), root);
            addEntry("Keywords", info.getKeywords(), root);
            addEntry("Creator", info.getCreator(), root);
            addEntry("Producer", info.getProducer(), root);

            if (info.getCreationDate() != null) {
                FeatureTreeNode creationDate = FeatureTreeNode.newInstance(ENTRY, root);
                creationDate.addAttribute(KEY, "CreationDate");
                try {
                    creationDate.setValue(getXMLFormat(info.getCreationDate()));
                } catch (DatatypeConfigurationException e) {
                    creationDate.addAttribute(ErrorsHelper.ERRORID, ErrorsHelper.INFODICTCONFCREATIONDATE_ID);
                    ErrorsHelper.addErrorIntoCollection(collection, ErrorsHelper.INFODICTCONFCREATIONDATE_ID, ErrorsHelper.INFODICTCONFCREATIONDATE_MESSAGE);
                }
            }

            if (info.getModificationDate() != null) {
                FeatureTreeNode modificationDate = FeatureTreeNode.newInstance(ENTRY, root);
                modificationDate.addAttribute(KEY, "ModDate");
                try {
                    modificationDate.setValue(getXMLFormat(info.getModificationDate()));
                } catch (DatatypeConfigurationException e) {
                    modificationDate.addAttribute(ErrorsHelper.ERRORID, ErrorsHelper.INFODICTCONFMODDATE_ID);
                    ErrorsHelper.addErrorIntoCollection(collection, ErrorsHelper.INFODICTCONFMODDATE_ID, ErrorsHelper.INFODICTCONFMODDATE_MESSAGE);
                }
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
        } else {
            return null;
        }
    }

    private void addEntry(String name, String value, FeatureTreeNode root) throws FeaturesTreeNodeException {
        if (name != null && value != null) {
            FeatureTreeNode entry = FeatureTreeNode.newInstance(ENTRY, value, root);
            entry.addAttribute(KEY, name);
        }
    }

    private String getXMLFormat(Calendar calendar) throws DatatypeConfigurationException {
        GregorianCalendar greg = new GregorianCalendar();
        greg.setTime(calendar.getTime());
        greg.setTimeZone(calendar.getTimeZone());
        XMLGregorianCalendar xmlCalendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(greg);
        return xmlCalendar.toXMLFormat();
    }
}

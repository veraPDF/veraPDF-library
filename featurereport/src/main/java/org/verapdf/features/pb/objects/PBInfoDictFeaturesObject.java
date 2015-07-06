package org.verapdf.features.pb.objects;

import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.verapdf.exceptions.featurereport.FeatureValueException;
import org.verapdf.exceptions.featurereport.FeaturesTreeNodeException;
import org.verapdf.features.FeaturesObjectTypesEnum;
import org.verapdf.features.IFeaturesObject;
import org.verapdf.features.tools.FeatureTreeNode;
import org.verapdf.features.tools.FeaturesCollection;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.Set;
import java.util.TreeSet;

/**
 * Feature object for information dictionary
 *
 * @author Maksim Bezrukov
 */
public class PBInfoDictFeaturesObject implements IFeaturesObject {

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
     * @throws FeatureValueException - occurs when wrong feature feature format found during features parser
     */
    @Override
    public FeatureTreeNode reportFeatures(FeaturesCollection collection) throws FeaturesTreeNodeException, FeatureValueException {

        FeatureTreeNode root = FeatureTreeNode.newInstance("informationDict", null);

        if (info.getTitle() != null) {
            FeatureTreeNode title = FeatureTreeNode.newInstance(ENTRY, info.getTitle(), root);
            title.addAttribute(KEY, "Title");
        }

        if (info.getAuthor() != null) {
            FeatureTreeNode author = FeatureTreeNode.newInstance(ENTRY, info.getAuthor(), root);
            author.addAttribute(KEY, "Author");
        }

        if (info.getSubject() != null) {
            FeatureTreeNode subject = FeatureTreeNode.newInstance(ENTRY, info.getSubject(), root);
            subject.addAttribute(KEY, "Subject");
        }

        if (info.getKeywords() != null) {
            FeatureTreeNode keywords = FeatureTreeNode.newInstance(ENTRY, info.getKeywords(), root);
            keywords.addAttribute(KEY, "Keywords");
        }

        if (info.getCreator() != null) {
            FeatureTreeNode creator = FeatureTreeNode.newInstance(ENTRY, info.getCreator(), root);
            creator.addAttribute(KEY, "Creator");
        }

        if (info.getProducer() != null) {
            FeatureTreeNode producer = FeatureTreeNode.newInstance(ENTRY, info.getProducer(), root);
            producer.addAttribute(KEY, "Producer");
        }

        if (info.getCreationDate() != null) {
            try {
                GregorianCalendar greg = new GregorianCalendar();
                greg.setTime(info.getCreationDate().getTime());
                greg.setTimeZone(info.getCreationDate().getTimeZone());
                XMLGregorianCalendar creationCalendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(greg);
                FeatureTreeNode creationDate = FeatureTreeNode.newInstance(ENTRY, creationCalendar.toXMLFormat(), root);
                creationDate.addAttribute(KEY, "CreationDate");
            } catch (DatatypeConfigurationException e) {
                throw new FeatureValueException("A serious configuration error while creating creationDate field in information dictionary features.", e);
            }
        }

        if (info.getModificationDate() != null) {
            try {
                GregorianCalendar greg = new GregorianCalendar();
                greg.setTime(info.getModificationDate().getTime());
                greg.setTimeZone(info.getModificationDate().getTimeZone());
                XMLGregorianCalendar modCalendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(greg);
                FeatureTreeNode modificationDate = FeatureTreeNode.newInstance(ENTRY, modCalendar.toXMLFormat(), root);
                modificationDate.addAttribute(KEY, "ModDate");
            } catch (DatatypeConfigurationException e) {
                throw new FeatureValueException("A serious configuration error while creating creationDate field in information dictionary features.", e);
            }
        }

        if (info.getTrapped() != null) {
            FeatureTreeNode trapped = FeatureTreeNode.newInstance(ENTRY, info.getTrapped(), root);
            trapped.addAttribute(KEY, "Trapped");
        }

        Set<String> keys = new TreeSet<>(info.getMetadataKeys());
        String[] predefinedKeys = {"Title", "Author", "Subject", "Keywords", "Creator", "Producer", "CreationDate", "ModDate", "Trapped"};
        keys.removeAll(Arrays.asList(predefinedKeys));

        for (String key : keys) {
            if (info.getCustomMetadataValue(key) != null) {
                FeatureTreeNode customValue = FeatureTreeNode.newInstance(ENTRY, info.getCustomMetadataValue(key), root);
                customValue.addAttribute(KEY, key);
            }
        }

        collection.addNewFeatureTree(FeaturesObjectTypesEnum.INFORMATION_DICTIONARY, root);

        return root;
    }
}

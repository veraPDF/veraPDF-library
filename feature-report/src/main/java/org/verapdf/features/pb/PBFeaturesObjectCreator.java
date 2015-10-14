package org.verapdf.features.pb;

import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.cos.COSStream;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDMetadata;
import org.apache.pdfbox.pdmodel.common.filespecification.PDComplexFileSpecification;
import org.apache.pdfbox.pdmodel.encryption.PDEncryption;
import org.apache.pdfbox.pdmodel.font.PDFontLike;
import org.apache.pdfbox.pdmodel.graphics.color.PDColorSpace;
import org.apache.pdfbox.pdmodel.graphics.color.PDOutputIntent;
import org.apache.pdfbox.pdmodel.graphics.form.PDFormXObject;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.pdmodel.graphics.pattern.PDShadingPattern;
import org.apache.pdfbox.pdmodel.graphics.pattern.PDTilingPattern;
import org.apache.pdfbox.pdmodel.graphics.shading.PDShading;
import org.apache.pdfbox.pdmodel.graphics.state.PDExtendedGraphicsState;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotation;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDDocumentOutline;
import org.verapdf.features.pb.objects.*;

import java.util.Set;

/**
 * Creates Feature Objects and report them to Features Reporter
 *
 * @author Maksim Bezrukov
 */
public final class PBFeaturesObjectCreator {

	private PBFeaturesObjectCreator() {
	}

	/**
	 * Creates new PBInfoDictFeaturesObject
	 *
	 * @param info PDDocumentInformation class from pdfbox, which represents a document info dictionary for feature report
	 * @return created PBInfoDictFeaturesObject
	 */
	public static PBInfoDictFeaturesObject createInfoDictFeaturesObject(PDDocumentInformation info) {
		return new PBInfoDictFeaturesObject(info);
	}

	/**
	 * Creates new PBMetadataFeaturesObject
	 *
	 * @param metadata PDMetadata class from pdfbox, which represents a metadata for feature report
	 * @return created PBMetadataFeaturesObject
	 */
	public static PBMetadataFeaturesObject createMetadataFeaturesObject(PDMetadata metadata) {
		return new PBMetadataFeaturesObject(metadata);
	}

	/**
	 * Creates new PBDocSecurityFeaturesObject
	 *
	 * @param encryption PDEncryption class from pdfbox, which represents an encryption for feature report
	 * @return created PBDocSecurityFeaturesObject
	 */
	public static PBDocSecurityFeaturesObject createDocSecurityFeaturesObject(PDEncryption encryption) {
		return new PBDocSecurityFeaturesObject(encryption);
	}

	/**
	 * Creates new PBLowLvlInfoFeaturesObject
	 *
	 * @param document COSDocument class from pdfbox, which represents a document for feature report
	 * @return created PBLowLvlInfoFeaturesObject
	 */
	public static PBLowLvlInfoFeaturesObject createLowLvlInfoFeaturesObject(COSDocument document) {
		return new PBLowLvlInfoFeaturesObject(document);
	}

	/**
	 * Creates new PBEmbeddedFileFeaturesObject
	 *
	 * @param embFile PDComplexFileSpecification class from pdfbox, which represents a file specification with embedded
	 *                file for feature report
	 * @return created PBEmbeddedFileFeaturesObject
	 */
	public static PBEmbeddedFileFeaturesObject createEmbeddedFileFeaturesObject(PDComplexFileSpecification embFile, int index) {
		return new PBEmbeddedFileFeaturesObject(embFile, index);
	}

	/**
	 * Creates new PBOutputIntentsFeaturesObject
	 *
	 * @param outInt       PDOutputIntent class from pdfbox, which represents an outputIntent for feature report
	 * @param id           id of the outputIntent
	 * @param iccProfileID id of the icc profile which use in this outputIntent
	 * @return created PBOutputIntentsFeaturesObject
	 */
	public static PBOutputIntentsFeaturesObject createOutputIntentFeaturesObject(PDOutputIntent outInt, String id, String iccProfileID) {
		return new PBOutputIntentsFeaturesObject(outInt, id, iccProfileID);
	}

	/**
	 * Creates new PBOutlinesFeaturesObject
	 *
	 * @param outlines PDPage class from pdfbox, which represents a page for feature report
	 * @return created PBOutlinesFeaturesObject
	 */
	public static PBOutlinesFeaturesObject createOutlinesFeaturesObject(PDDocumentOutline outlines) {
		return new PBOutlinesFeaturesObject(outlines);
	}

	/**
	 * Creates new PBAnnotationFeaturesObject
	 *
	 * @param annot        PDAnnotation class from pdfbox, which represents an annotation for feature report
	 * @param id           page id
	 * @param pages        set of pages for this annotation
	 * @param parentId     parent annotation for this annotation
	 * @param popupId      id of the popup annotation for this annotation
	 * @param formXObjects set of id of the form XObjects which used in appearance stream of this annotation
	 * @return created PBAnnotationFeaturesObject
	 */
	public static PBAnnotationFeaturesObject createAnnotFeaturesObject(PDAnnotation annot, String id, Set<String> pages,
																	   String parentId, String popupId, Set<String> formXObjects) {
		return new PBAnnotationFeaturesObject(annot, id, pages, parentId, popupId, formXObjects);
	}

	/**
	 * Creates new PBPageFeaturesObject
	 *
	 * @param page            pdfbox class represents page object
	 * @param thumb           thumbnail image id
	 * @param annotsId        set of annotations id which contains in this page
	 * @param extGStateChild  set of extGState id which contains in resource dictionary of this page
	 * @param colorSpaceChild set of ColorSpace id which contains in resource dictionary of this page
	 * @param patternChild    set of pattern id which contains in resource dictionary of this page
	 * @param shadingChild    set of shading id which contains in resource dictionary of this page
	 * @param xobjectChild    set of XObject id which contains in resource dictionary of this page
	 * @param fontChild       set of font id which contains in resource dictionary of this page
	 * @param propertiesChild set of properties id which contains in resource dictionary of this page
	 * @param id              page id
	 * @param index           page index
	 * @return created PBPageFeaturesObject
	 */
	public static PBPageFeaturesObject createPageFeaturesObject(PDPage page,
																String thumb,
																Set<String> annotsId,
																Set<String> extGStateChild,
																Set<String> colorSpaceChild,
																Set<String> patternChild,
																Set<String> shadingChild,
																Set<String> xobjectChild,
																Set<String> fontChild,
																Set<String> propertiesChild,
																String id,
																int index) {
		return new PBPageFeaturesObject(page, thumb, annotsId, extGStateChild,
				colorSpaceChild, patternChild, shadingChild, xobjectChild,
				fontChild, propertiesChild, id, index);
	}

	/**
	 * Creates new PBICCProfileFeaturesObject
	 *
	 * @param profile   COSStream which represents the icc profile for feature report
	 * @param id        id of the profile
	 * @param outInts   set of ids of all parent output intents for this icc profile
	 * @param iccBaseds set of ids of all parent icc based color spaces for this icc profile
	 * @return created PBICCProfileFeaturesObject
	 */
	public static PBICCProfileFeaturesObject createICCProfileFeaturesObject(COSStream profile, String id,
																			Set<String> outInts, Set<String> iccBaseds) {
		return new PBICCProfileFeaturesObject(profile, id, outInts, iccBaseds);
	}

	/**
	 * Creates new PBExtGStateFeaturesObject
	 *
	 * @param exGState         PDExtendedGraphicsState which represents extended graphics state for feature report
	 * @param id               id of the object
	 * @param fontChildID      id of the font child
	 * @param pageParentsID    set of page ids which contains the given extended graphics state as its resources
	 * @param patternParentsID set of pattern ids which contains the given extended graphics state as its resources
	 * @param xobjectParentsID set of xobject ids which contains the given extended graphics state as its resources
	 * @param fontParentsID    set of font ids which contains the given extended graphics state as its resources
	 * @return created PBExtGStateFeaturesObject
	 */
	public static PBExtGStateFeaturesObject createExtGStateFeaturesObject(PDExtendedGraphicsState exGState,
																		  String id,
																		  String fontChildID,
																		  Set<String> pageParentsID,
																		  Set<String> patternParentsID,
																		  Set<String> xobjectParentsID,
																		  Set<String> fontParentsID) {
		return new PBExtGStateFeaturesObject(exGState, id, fontChildID, pageParentsID, patternParentsID, xobjectParentsID, fontParentsID);
	}

	/**
	 * Constructs new PBColorSpaceFeaturesObject
	 *
	 * @param colorSpace        PDColorSpace which represents colorspace for feature report
	 * @param id                id of the object
	 * @param iccProfileChild   id of the iccprofile child
	 * @param colorSpaceChild   id of the colorspace child
	 * @param pageParents       set of page ids which contains the given colorspace as its resources
	 * @param colorSpaceParents set of colorspace ids which contains the given colorspace as its resources
	 * @param patternParents    set of pattern ids which contains the given colorspace as its resources
	 * @param shadingParents    set of shading ids which contains the given colorspace as its resources
	 * @param xobjectParents    set of xobject ids which contains the given colorspace as its resources
	 * @param fontParents       set of font ids which contains the given colorspace as its resources
	 * @return created PBColorSpaceFeaturesObject
	 */
	public static PBColorSpaceFeaturesObject createColorSpaceFeaturesObject(PDColorSpace colorSpace,
																			String id,
																			String iccProfileChild,
																			String colorSpaceChild,
																			Set<String> pageParents,
																			Set<String> colorSpaceParents,
																			Set<String> patternParents,
																			Set<String> shadingParents,
																			Set<String> xobjectParents,
																			Set<String> fontParents) {
		return new PBColorSpaceFeaturesObject(colorSpace, id, iccProfileChild, colorSpaceChild, pageParents, colorSpaceParents, patternParents, shadingParents, xobjectParents, fontParents);
	}

	/**
	 * Constructs new PBTilingPatternFeaturesObject
	 *
	 * @param tilingPattern   PDTilingPattern which represents tilling pattern for feature report
	 * @param id              id of the object
	 * @param extGStateChild  set of external graphics state id which contains in resource dictionary of this pattern
	 * @param colorSpaceChild set of ColorSpace id which contains in resource dictionary of this pattern
	 * @param patternChild    set of pattern id which contains in resource dictionary of this pattern
	 * @param shadingChild    set of shading id which contains in resource dictionary of this pattern
	 * @param xobjectChild    set of XObject id which contains in resource dictionary of this pattern
	 * @param fontChild       set of font id which contains in resource dictionary of this pattern
	 * @param propertiesChild set of properties id which contains in resource dictionary of this pattern
	 * @param pageParent      set of page ids which contains the given pattern as its resources
	 * @param patternParent   set of pattern ids which contains the given pattern as its resources
	 * @param xobjectParent   set of xobject ids which contains the given pattern as its resources
	 * @param fontParent      set of font ids which contains the given pattern as its resources
	 * @return created PBTilingPatternFeaturesObject
	 */
	public static PBTilingPatternFeaturesObject createTilingPatternFeaturesObject(PDTilingPattern tilingPattern,
																				  String id,
																				  Set<String> extGStateChild,
																				  Set<String> colorSpaceChild,
																				  Set<String> patternChild,
																				  Set<String> shadingChild,
																				  Set<String> xobjectChild,
																				  Set<String> fontChild,
																				  Set<String> propertiesChild,
																				  Set<String> pageParent,
																				  Set<String> patternParent,
																				  Set<String> xobjectParent,
																				  Set<String> fontParent) {
		return new PBTilingPatternFeaturesObject(tilingPattern, id, extGStateChild, colorSpaceChild, patternChild, shadingChild, xobjectChild, fontChild, propertiesChild, pageParent, patternParent, xobjectParent, fontParent);
	}

	/**
	 * Constructs new PBShadingPatternFeaturesObject
	 *
	 * @param shadingPattern PDShadingPattern which represents shading pattern for feature report
	 * @param id             id of the object
	 * @param extGStateChild external graphics state id which contains in this shading pattern
	 * @param shadingChild   shading id which contains in this shading pattern
	 * @param pageParent     set of page ids which contains the given pattern as its resources
	 * @param patternParent  set of pattern ids which contains the given pattern as its resources
	 * @param xobjectParent  set of xobject ids which contains the given pattern as its resources
	 * @param fontParent     set of font ids which contains the given pattern as its resources
	 * @return created PBShadingPatternFeaturesObject
	 */
	public static PBShadingPatternFeaturesObject createShadingPatternFeaturesObject(PDShadingPattern shadingPattern,
																					String id,
																					String shadingChild,
																					String extGStateChild,
																					Set<String> pageParent,
																					Set<String> patternParent,
																					Set<String> xobjectParent,
																					Set<String> fontParent) {
		return new PBShadingPatternFeaturesObject(shadingPattern, id, shadingChild, extGStateChild, pageParent, patternParent, xobjectParent, fontParent);
	}

	/**
	 * Constructs new PBShadingFeaturesObject
	 *
	 * @param shading         PDShading which represents shading for feature report
	 * @param id              id of the object
	 * @param colorSpaceChild colorSpace id which contains in this shading pattern
	 * @param pageParent      set of page ids which contains the given shading as its resources
	 * @param patternParent   set of pattern ids which contains the given shading as its resources
	 * @param xobjectParent   set of xobject ids which contains the given shading as its resources
	 * @param fontParent      set of font ids which contains the given shading as its resources
	 * @return created PBShadingFeaturesObject
	 */
	public static PBShadingFeaturesObject createShadingFeaturesObject(PDShading shading, String id, String colorSpaceChild, Set<String> pageParent, Set<String> patternParent, Set<String> xobjectParent, Set<String> fontParent) {
		return new PBShadingFeaturesObject(shading, id, colorSpaceChild, pageParent, patternParent, xobjectParent, fontParent);
	}

	/**
	 * Constructs new PBImageXObjectFeaturesObject
	 *
	 * @param imageXObject    PDImageXObject which represents image xobject for feature report
	 * @param id              id of the object
	 * @param colorSpaceChild colorSpace id which contains in this image xobject
	 * @param maskChild       image xobject id which contains in this image xobject as it's mask
	 * @param sMaskChild      image xobject id which contains in this image xobject as it's smask
	 * @param alternatesChild set of image xobject ids which contains in this image xobject as alternates
	 * @param pageParent      set of page ids which contains the given image xobject as its resources
	 * @param patternParent   set of pattern ids which contains the given image xobject state as its resources
	 * @param xobjectParent   set of xobject ids which contains the given image xobject state as its resources
	 * @param fontParent      set of font ids which contains the given image xobject state as its resources
	 * @return created PBImageXObjectFeaturesObject
	 */
	public static PBImageXObjectFeaturesObject createImageXObjectFeaturesObject(PDImageXObject imageXObject, String id, String colorSpaceChild, String maskChild, String sMaskChild, Set<String> alternatesChild, Set<String> pageParent, Set<String> patternParent, Set<String> xobjectParent, Set<String> fontParent) {
		return new PBImageXObjectFeaturesObject(imageXObject, id, colorSpaceChild, maskChild, sMaskChild, alternatesChild, pageParent, patternParent, xobjectParent, fontParent);
	}

	/**
	 * Constructs new PBFormXObjectFeaturesObject
	 *
	 * @param formXObject      PDFormXObject which represents form xobject for feature report
	 * @param id               id of the object
	 * @param groupChild       id of the group xobject which contains in the given form xobject
	 * @param extGStateChild   set of external graphics state id which contains in resource dictionary of this xobject
	 * @param colorSpaceChild  set of ColorSpace id which contains in resource dictionary of this xobject
	 * @param patternChild     set of pattern id which contains in resource dictionary of this xobject
	 * @param shadingChild     set of shading id which contains in resource dictionary of this xobject
	 * @param xobjectChild     set of XObject id which contains in resource dictionary of this xobject
	 * @param fontChild        set of font id which contains in resource dictionary of this pattern
	 * @param propertiesChild  set of properties id which contains in resource dictionary of this xobject
	 * @param pageParent       set of page ids which contains the given xobject as its resources
	 * @param annotationParent set of annotation ids which contains the given xobject in its appearance dictionary
	 * @param patternParent    set of pattern ids which contains the given xobject as its resources
	 * @param xobjectParent    set of xobject ids which contains the given xobject as its resources
	 * @param fontParent       set of font ids which contains the given xobject as its resources
	 * @return created PBFormXObjectFeaturesObject
	 */
	public static PBFormXObjectFeaturesObject createFormXObjectFeaturesObject(PDFormXObject formXObject, String id, String groupChild, Set<String> extGStateChild, Set<String> colorSpaceChild, Set<String> patternChild, Set<String> shadingChild, Set<String> xobjectChild, Set<String> fontChild, Set<String> propertiesChild, Set<String> pageParent, Set<String> annotationParent, Set<String> patternParent, Set<String> xobjectParent, Set<String> fontParent) {
		return new PBFormXObjectFeaturesObject(formXObject, id, groupChild, extGStateChild, colorSpaceChild, patternChild, shadingChild, xobjectChild, fontChild, propertiesChild, pageParent, annotationParent, patternParent, xobjectParent, fontParent);
	}

	/**
	 * Constructs new PBFontFeaturesObject
	 *
	 * @param fontLike        PDFontLike which represents font for feature report
	 * @param id              id of the object
	 * @param extGStateChild  set of external graphics state id which contains in resource dictionary of this font
	 * @param colorSpaceChild set of ColorSpace id which contains in resource dictionary of this font
	 * @param patternChild    set of pattern id which contains in resource dictionary of this font
	 * @param shadingChild    set of shading id which contains in resource dictionary of this font
	 * @param xobjectChild    set of XObject id which contains in resource dictionary of this font
	 * @param fontChild       set of font id which contains in resource dictionary of this font
	 * @param propertiesChild set of properties id which contains in resource dictionary of this font
	 * @param pageParent      set of page ids which contains the given font as its resources
	 * @param extGStateParent set of graphicsState ids which contains the given font as their resource
	 * @param patternParent   set of pattern ids which contains the given font as its resources
	 * @param xobjectParent   set of xobject ids which contains the given font as its resources
	 * @param fontParent      set of font ids which contains the given font as its resources
	 * @return created PBFontFeaturesObject
	 */
	public static PBFontFeaturesObject createFontFeaturesObject(PDFontLike fontLike, String id, Set<String> extGStateChild, Set<String> colorSpaceChild, Set<String> patternChild, Set<String> shadingChild, Set<String> xobjectChild, Set<String> fontChild, Set<String> propertiesChild, Set<String> extGStateParent, Set<String> pageParent, Set<String> patternParent, Set<String> xobjectParent, Set<String> fontParent) {
		return new PBFontFeaturesObject(fontLike, id, extGStateChild, colorSpaceChild, patternChild, shadingChild, xobjectChild, fontChild, propertiesChild, extGStateParent, pageParent, patternParent, xobjectParent, fontParent);
	}

	/**
	 * Constructs new PBPropertiesDictFeaturesObject
	 *
	 * @param properties    COSDictionary which represents properties for feature report
	 * @param id            id of the object
	 * @param pageParent    set of page ids which contains the given properties as its resources
	 * @param patternParent set of pattern ids which contains the given properties as its resources
	 * @param xobjectParent set of xobject ids which contains the given properties as its resources
	 * @param fontParent    set of font ids which contains the given properties as its resources
	 * @return created PBPropertiesDictFeaturesObject
	 */
	public static PBPropertiesDictFeaturesObject createPropertiesDictFeaturesObject(COSDictionary properties, String id, Set<String> pageParent, Set<String> patternParent, Set<String> xobjectParent, Set<String> fontParent) {
		return new PBPropertiesDictFeaturesObject(properties, id, pageParent, patternParent, xobjectParent, fontParent);
	}

	/**
	 * Constructs new PBPostScriptXObjectFeaturesObject
	 *
	 * @param id            id of the object
	 * @param pageParent    set of page ids which contains the given xobject as its resources
	 * @param patternParent set of pattern ids which contains the given xobject as its resources
	 * @param xobjectParent set of xobject ids which contains the given xobject as its resources
	 * @param fontParent    set of font ids which contains the given xobject as its resources
	 * @return created PBPostScriptXObjectFeaturesObject
	 */
	public static PBPostScriptXObjectFeaturesObject createPostScriptXObjectFeaturesObject(String id, Set<String> pageParent, Set<String> patternParent, Set<String> xobjectParent, Set<String> fontParent) {
		return new PBPostScriptXObjectFeaturesObject(id, pageParent, patternParent, xobjectParent, fontParent);
	}
}

package org.verapdf.features.pb;

import org.apache.log4j.Logger;
import org.apache.pdfbox.cos.*;
import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.common.PDNameTreeNode;
import org.apache.pdfbox.pdmodel.common.PDStream;
import org.apache.pdfbox.pdmodel.common.filespecification.PDComplexFileSpecification;
import org.apache.pdfbox.pdmodel.documentinterchange.markedcontent.PDPropertyList;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDFontLike;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.font.PDType3Font;
import org.apache.pdfbox.pdmodel.graphics.PDPostScriptXObject;
import org.apache.pdfbox.pdmodel.graphics.PDXObject;
import org.apache.pdfbox.pdmodel.graphics.color.*;
import org.apache.pdfbox.pdmodel.graphics.form.PDFormXObject;
import org.apache.pdfbox.pdmodel.graphics.form.PDGroup;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.pdmodel.graphics.pattern.PDAbstractPattern;
import org.apache.pdfbox.pdmodel.graphics.pattern.PDShadingPattern;
import org.apache.pdfbox.pdmodel.graphics.pattern.PDTilingPattern;
import org.apache.pdfbox.pdmodel.graphics.shading.PDShading;
import org.apache.pdfbox.pdmodel.graphics.state.PDExtendedGraphicsState;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotation;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAppearanceDictionary;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAppearanceEntry;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAppearanceStream;
import org.verapdf.core.FeatureParsingException;
import org.verapdf.features.FeaturesObjectTypesEnum;
import org.verapdf.features.FeaturesReporter;
import org.verapdf.features.tools.ErrorsHelper;
import org.verapdf.features.tools.FeatureTreeNode;
import org.verapdf.features.tools.FeaturesCollection;

import java.io.IOException;
import java.util.*;

/**
 * Parses PDFBox PDDocument to generate features collection
 *
 * @author Maksim Bezrukov
 */
public final class PBFeatureParser {

	private static final Logger LOGGER = Logger
			.getLogger(PBFeatureParser.class);
	private static final String PAGE = "page";
	private static final String ICCPROFILE = "iccProfile";
	private static final String ID = "id";
	private static final String ANNOTATION = "annotation";
	private static final String ANNOT = "annot";
	private static final String OUTINT = "outInt";
	private static final String EMBEDDEDFILE = "embeddedFile";
	private static final String FONT = "font";
	private static final String SHADING = "shading";
	private static final String XOBJECT = "xobject";
	private static final String PATTERN = "pattern";
	private static final String COLORSPACE = "colorSpace";
	private static final String EXTGSTATE_ID = "exGSt";
	private static final String COLORSPACE_ID = "clrsp";
	private static final String PATTERN_ID = "ptrn";
	private static final String SHADING_ID = "shdng";
	private static final String XOBJECT_ID = "xobj";
	private static final String FONT_ID = "fnt";
	private static final String PROPERTIES_ID = "prop";
	private static final String DEVICEGRAY_ID = "devgray";
	private static final String DEVICERGB_ID = "devrgb";
	private static final String DEVICECMYK_ID = "devcmyk";

	private FeaturesReporter reporter;

	private Map<String, COSStream> iccProfiles = new HashMap<>();
	private Map<String, Set<String>> iccProfileOutInts = new HashMap<>();
	private Map<String, Set<String>> iccProfileICCBased = new HashMap<>();

	private Map<String, Set<String>> pageExtGStateChild = new HashMap<>();
	private Map<String, Set<String>> pageColorSpaceChild = new HashMap<>();
	private Map<String, Set<String>> pagePatternChild = new HashMap<>();
	private Map<String, Set<String>> pageShadingChild = new HashMap<>();
	private Map<String, Set<String>> pageXObjectChild = new HashMap<>();
	private Map<String, Set<String>> pageFontChild = new HashMap<>();
	private Map<String, Set<String>> pagePropertiesChild = new HashMap<>();

	private Map<String, PDAnnotation> annots = new HashMap<>();
	private Map<String, String> annotChild = new HashMap<>();
	private Map<String, Set<String>> annotXObjectsChild = new HashMap<>();
	private Map<String, Set<String>> annotPagesParent = new HashMap<>();
	private Map<String, String> annotParent = new HashMap<>();

	private Map<String, PDExtendedGraphicsState> exGStates = new HashMap<>();
	private Map<String, String> exGStateFontChild = new HashMap<>();
	private Map<String, Set<String>> exGStatePageParent = new HashMap<>();
	private Map<String, Set<String>> exGStatePatternParent = new HashMap<>();
	private Map<String, Set<String>> exGStateXObjectParent = new HashMap<>();
	private Map<String, Set<String>> exGStateFontParent = new HashMap<>();

	private Map<String, PDColorSpace> colorSpaces = new HashMap<>();
	private Map<String, String> colorSpaceIccProfileChild = new HashMap<>();
	private Map<String, String> colorSpaceColorSpaceChild = new HashMap<>();
	private Map<String, Set<String>> colorSpacePageParent = new HashMap<>();
	private Map<String, Set<String>> colorSpaceColorSpaceParent = new HashMap<>();
	private Map<String, Set<String>> colorSpacePatternParent = new HashMap<>();
	private Map<String, Set<String>> colorSpaceShadingParent = new HashMap<>();
	private Map<String, Set<String>> colorSpaceXObjectParent = new HashMap<>();
	private Map<String, Set<String>> colorSpaceFontParent = new HashMap<>();

	private Map<String, PDTilingPattern> tilingPatterns = new HashMap<>();
	private Map<String, Set<String>> tilingPatternExtGStateChild = new HashMap<>();
	private Map<String, Set<String>> tilingPatternColorSpaceChild = new HashMap<>();
	private Map<String, Set<String>> tilingPatternPatternChild = new HashMap<>();
	private Map<String, Set<String>> tilingPatternShadingChild = new HashMap<>();
	private Map<String, Set<String>> tilingPatternXObjectChild = new HashMap<>();
	private Map<String, Set<String>> tilingPatternFontChild = new HashMap<>();
	private Map<String, Set<String>> tilingPatternPropertiesChild = new HashMap<>();
	private Map<String, Set<String>> tilingPatternPageParent = new HashMap<>();
	private Map<String, Set<String>> tilingPatternPatternParent = new HashMap<>();
	private Map<String, Set<String>> tilingPatternXObjectParent = new HashMap<>();
	private Map<String, Set<String>> tilingPatternFontParent = new HashMap<>();

	private Map<String, PDShadingPattern> shadingPatterns = new HashMap<>();
	private Map<String, String> shadingPatternShadingChild = new HashMap<>();
	private Map<String, String> shadingPatternExtGStateChild = new HashMap<>();
	private Map<String, Set<String>> shadingPatternPageParent = new HashMap<>();
	private Map<String, Set<String>> shadingPatternPatternParent = new HashMap<>();
	private Map<String, Set<String>> shadingPatternXObjectParent = new HashMap<>();
	private Map<String, Set<String>> shadingPatternFontParent = new HashMap<>();

	private Map<String, PDShading> shadings = new HashMap<>();
	private Map<String, String> shadingColorSpaceChild = new HashMap<>();
	private Map<String, Set<String>> shadingPageParent = new HashMap<>();
	private Map<String, Set<String>> shadingPatternParent = new HashMap<>();
	private Map<String, Set<String>> shadingXObjectParent = new HashMap<>();
	private Map<String, Set<String>> shadingFontParent = new HashMap<>();

	private Map<String, PDImageXObject> imageXObjects = new HashMap<>();
	private Map<String, String> imageXObjectColorSpaceChild = new HashMap<>();
	private Map<String, String> imageXObjectMaskChild = new HashMap<>();
	private Map<String, String> imageXObjectSMaskChild = new HashMap<>();
	private Map<String, Set<String>> imageXObjectAlternatesChild = new HashMap<>();
	private Map<String, Set<String>> imageXObjectPageParent = new HashMap<>();
	private Map<String, Set<String>> imageXObjectPatternParent = new HashMap<>();
	private Map<String, Set<String>> imageXObjectXObjectParent = new HashMap<>();
	private Map<String, Set<String>> imageXObjectFontParent = new HashMap<>();

	private Map<String, PDFormXObject> formXObjects = new HashMap<>();
	private Map<String, String> groupXObjectColorSpaceChild = new HashMap<>();
	private Map<String, Set<String>> formXObjectExtGStateChild = new HashMap<>();
	private Map<String, Set<String>> formXObjectColorSpaceChild = new HashMap<>();
	private Map<String, Set<String>> formXObjectPatternChild = new HashMap<>();
	private Map<String, Set<String>> formXObjectShadingChild = new HashMap<>();
	private Map<String, Set<String>> formXObjectXObjectChild = new HashMap<>();
	private Map<String, Set<String>> formXObjectFontChild = new HashMap<>();
	private Map<String, Set<String>> formXObjectPropertiesChild = new HashMap<>();
	private Map<String, Set<String>> formXObjectPageParent = new HashMap<>();
	private Map<String, Set<String>> formXObjectAnnotationParent = new HashMap<>();
	private Map<String, Set<String>> formXObjectPatternParent = new HashMap<>();
	private Map<String, Set<String>> formXObjectXObjectParent = new HashMap<>();
	private Map<String, Set<String>> formXObjectFontParent = new HashMap<>();

	private Map<String, PDFontLike> fonts = new HashMap<>();
	private Map<String, Set<String>> fontExtGStateChild = new HashMap<>();
	private Map<String, Set<String>> fontColorSpaceChild = new HashMap<>();
	private Map<String, Set<String>> fontPatternChild = new HashMap<>();
	private Map<String, Set<String>> fontShadingChild = new HashMap<>();
	private Map<String, Set<String>> fontXObjectChild = new HashMap<>();
	private Map<String, Set<String>> fontFontChild = new HashMap<>();
	private Map<String, Set<String>> fontPropertiesChild = new HashMap<>();
	private Map<String, Set<String>> fontExtGStateParent = new HashMap<>();
	private Map<String, Set<String>> fontPageParent = new HashMap<>();
	private Map<String, Set<String>> fontPatternParent = new HashMap<>();
	private Map<String, Set<String>> fontXObjectParent = new HashMap<>();
	private Map<String, Set<String>> fontFontParent = new HashMap<>();

	private Map<String, COSDictionary> properties = new HashMap<>();
	private Map<String, Set<String>> propertyPageParent = new HashMap<>();
	private Map<String, Set<String>> propertyPatternParent = new HashMap<>();
	private Map<String, Set<String>> propertyXObjectParent = new HashMap<>();
	private Map<String, Set<String>> propertyFontParent = new HashMap<>();

	private Set<String> postscripts = new HashSet<>();
	private Map<String, Set<String>> postscriptPageParent = new HashMap<>();
	private Map<String, Set<String>> postscriptPatternParent = new HashMap<>();
	private Map<String, Set<String>> postscriptXObjectParent = new HashMap<>();
	private Map<String, Set<String>> postscriptFontParent = new HashMap<>();

	private PBFeatureParser(FeaturesReporter reporter) {
		this.reporter = reporter;
	}

	/**
	 * Parses the document and returns Feature collection by using given
	 * Features Reporter
	 *
	 * @param document         the document for parsing
	 * @return FeaturesCollection class with information about all featurereport
	 */
	public static FeaturesCollection getFeaturesCollection(
			final PDDocument document) {

		FeaturesReporter reporter = new FeaturesReporter();

		if (document != null) {
			PBFeatureParser parser = new PBFeatureParser(reporter);
			parser.parseDocumentFeatures(document);
		}

		return reporter.getCollection();
	}

	private void parseDocumentFeatures(PDDocument document) {
		reporter.report(PBFeaturesObjectCreator
				.createInfoDictFeaturesObject(document.getDocumentInformation()));

		reporter.report(PBFeaturesObjectCreator
				.createDocSecurityFeaturesObject(document.getEncryption()));

		PDDocumentCatalog catalog = document.getDocumentCatalog();
		if (catalog != null) {
			getCatalogFeatures(catalog);
		}

		reporter.report(PBFeaturesObjectCreator
				.createLowLvlInfoFeaturesObject(document.getDocument()));

	}

	private void getCatalogFeatures(PDDocumentCatalog catalog) {
		reporter.report(PBFeaturesObjectCreator
				.createMetadataFeaturesObject(catalog.getMetadata()));
		reporter.report(PBFeaturesObjectCreator
				.createOutlinesFeaturesObject(catalog.getDocumentOutline()));

		if (catalog.getNames() != null
				&& catalog.getNames().getEmbeddedFiles() != null) {
			reportEmbeddedFiles(catalog);
		}

		if (catalog.getOutputIntents() != null) {
			int outIntNumber = 0;
			for (PDOutputIntent outInt : catalog.getOutputIntents()) {
				String outIntID = getId(outInt.getCOSObject(), OUTINT, outIntNumber++);
				String iccProfileID = addICCProfileFromOutputIntent(outInt, outIntID);
				reporter.report(PBFeaturesObjectCreator
						.createOutputIntentFeaturesObject(outInt, outIntID, iccProfileID));
			}
		}

		PDPageTree pageTree = catalog.getPages();
		if (pageTree != null) {
			getPageTreeFeatures(pageTree);
		}

		for (Map.Entry<String, COSStream> iccProfileEntry : iccProfiles.entrySet()) {
			if (iccProfileEntry.getValue() != null) {
				String id = iccProfileEntry.getKey();
				reporter.report(PBFeaturesObjectCreator
						.createICCProfileFeaturesObject(iccProfileEntry.getValue(), id,
								iccProfileOutInts.get(id), iccProfileICCBased.get(id)));
			}
		}

		for (Map.Entry<String, PDAnnotation> annotEntry : annots.entrySet()) {
			if (annotEntry.getValue() != null) {
				String id = annotEntry.getKey();
				getAnnotationResourcesDependencies(annotEntry.getValue(), id);
				reporter.report(PBFeaturesObjectCreator
						.createAnnotFeaturesObject(annotEntry.getValue(), id,
								annotPagesParent.get(id), annotParent.get(id),
								annotChild.get(id), annotXObjectsChild.get(id)));
			}
		}

		getResourcesFeatures();
	}

	private void getResourcesFeatures() {
		for (Map.Entry<String, PDExtendedGraphicsState> exGStateEntry : exGStates.entrySet()) {
			if (exGStateEntry.getValue() != null) {
				String id = exGStateEntry.getKey();
				reporter.report(PBFeaturesObjectCreator
						.createExtGStateFeaturesObject(exGStateEntry.getValue(),
								id,
								exGStateFontChild.get(id),
								exGStatePageParent.get(id),
								exGStatePatternParent.get(id),
								exGStateXObjectParent.get(id),
								exGStateFontParent.get(id)));
			}
		}

		for (Map.Entry<String, PDColorSpace> colorSpaceEntry : colorSpaces.entrySet()) {
			if (colorSpaceEntry.getValue() != null) {
				String id = colorSpaceEntry.getKey();
				reporter.report(PBFeaturesObjectCreator
						.createColorSpaceFeaturesObject(colorSpaceEntry.getValue(),
								id,
								colorSpaceIccProfileChild.get(id),
								colorSpaceColorSpaceChild.get(id),
								colorSpacePageParent.get(id),
								colorSpaceColorSpaceParent.get(id),
								colorSpacePatternParent.get(id),
								colorSpaceShadingParent.get(id),
								colorSpaceXObjectParent.get(id),
								colorSpaceFontParent.get(id)));
			}
		}

		for (Map.Entry<String, PDTilingPattern> tilingPatternEntry : tilingPatterns.entrySet()) {
			if (tilingPatternEntry.getValue() != null) {
				String id = tilingPatternEntry.getKey();
				reporter.report(PBFeaturesObjectCreator
						.createTilingPatternFeaturesObject(tilingPatternEntry.getValue(),
								id,
								tilingPatternExtGStateChild.get(id),
								tilingPatternColorSpaceChild.get(id),
								tilingPatternPatternChild.get(id),
								tilingPatternShadingChild.get(id),
								tilingPatternXObjectChild.get(id),
								tilingPatternFontChild.get(id),
								tilingPatternPropertiesChild.get(id),
								tilingPatternPageParent.get(id),
								tilingPatternPatternParent.get(id),
								tilingPatternXObjectParent.get(id),
								tilingPatternFontParent.get(id)));
			}
		}

		for (Map.Entry<String, PDShadingPattern> shadingPatternEntry : shadingPatterns.entrySet()) {
			if (shadingPatternEntry.getValue() != null) {
				String id = shadingPatternEntry.getKey();
				reporter.report(PBFeaturesObjectCreator
						.createShadingPatternFeaturesObject(shadingPatternEntry.getValue(),
								id,
								shadingPatternShadingChild.get(id),
								shadingPatternExtGStateChild.get(id),
								shadingPatternPageParent.get(id),
								shadingPatternPatternParent.get(id),
								shadingPatternXObjectParent.get(id),
								shadingPatternFontParent.get(id)));
			}
		}

		for (Map.Entry<String, PDShading> shadingEntry : shadings.entrySet()) {
			if (shadingEntry.getValue() != null) {
				String id = shadingEntry.getKey();
				reporter.report(PBFeaturesObjectCreator
						.createShadingFeaturesObject(shadingEntry.getValue(),
								id,
								shadingColorSpaceChild.get(id),
								shadingPageParent.get(id),
								shadingPatternParent.get(id),
								shadingXObjectParent.get(id),
								shadingFontParent.get(id)));
			}
		}

		for (Map.Entry<String, PDImageXObject> imageXObjectEntry : imageXObjects.entrySet()) {
			if (imageXObjectEntry.getValue() != null) {
				String id = imageXObjectEntry.getKey();
				reporter.report(PBFeaturesObjectCreator
						.createImageXObjectFeaturesObject(imageXObjectEntry.getValue(),
								id,
								imageXObjectColorSpaceChild.get(id),
								imageXObjectMaskChild.get(id),
								imageXObjectSMaskChild.get(id),
								imageXObjectAlternatesChild.get(id),
								imageXObjectPageParent.get(id),
								imageXObjectPatternParent.get(id),
								imageXObjectXObjectParent.get(id),
								imageXObjectFontParent.get(id)));
			}
		}

		for (Map.Entry<String, PDFormXObject> formXObjectEntry : formXObjects.entrySet()) {
			if (formXObjectEntry.getValue() != null) {
				String id = formXObjectEntry.getKey();
				reporter.report(PBFeaturesObjectCreator
						.createFormXObjectFeaturesObject(formXObjectEntry.getValue(),
								id,
								groupXObjectColorSpaceChild.get(id),
								formXObjectExtGStateChild.get(id),
								formXObjectColorSpaceChild.get(id),
								formXObjectPatternChild.get(id),
								formXObjectShadingChild.get(id),
								formXObjectXObjectChild.get(id),
								formXObjectFontChild.get(id),
								formXObjectPropertiesChild.get(id),
								formXObjectPageParent.get(id),
								formXObjectAnnotationParent.get(id),
								formXObjectPatternParent.get(id),
								formXObjectXObjectParent.get(id),
								formXObjectFontParent.get(id)));
			}
		}

		for (String postscript : postscripts) {
			if (postscript != null) {
				reporter.report(PBFeaturesObjectCreator
						.createPostScriptXObjectFeaturesObject(postscript,
								postscriptPageParent.get(postscript),
								postscriptPatternParent.get(postscript),
								postscriptXObjectParent.get(postscript),
								postscriptFontParent.get(postscript)));
			}
		}

		for (Map.Entry<String, PDFontLike> fontEntry : fonts.entrySet()) {
			if (fontEntry.getValue() != null) {
				String id = fontEntry.getKey();
				reporter.report(PBFeaturesObjectCreator
						.createFontFeaturesObject(fontEntry.getValue(),
								id,
								fontExtGStateChild.get(id),
								fontColorSpaceChild.get(id),
								fontPatternChild.get(id),
								fontShadingChild.get(id),
								fontXObjectChild.get(id),
								fontFontChild.get(id),
								fontPropertiesChild.get(id),
								fontExtGStateParent.get(id),
								fontPageParent.get(id),
								fontPatternParent.get(id),
								fontXObjectParent.get(id),
								fontFontParent.get(id)));
			}
		}

		for (Map.Entry<String, COSDictionary> propertiesEntry : properties.entrySet()) {
			if (propertiesEntry.getValue() != null) {
				String id = propertiesEntry.getKey();
				reporter.report(PBFeaturesObjectCreator
						.createPropertiesDictFeaturesObject(propertiesEntry.getValue(),
								id,
								propertyPageParent.get(id),
								propertyPatternParent.get(id),
								propertyXObjectParent.get(id),
								propertyFontParent.get(id)));
			}
		}
	}

	private void getPageTreeFeatures(PDPageTree pageTree) {
		for (PDPage page : pageTree) {

			int pageIndex = pageTree.indexOf(page) + 1;
			Set<String> annotsId = addAnnotsDependencies(page, pageIndex);
			String thumbID = null;

			if (page.getCOSObject().getDictionaryObject(COSName.getPDFName("Thumb")) != null) {
				COSBase baseThumb = page.getCOSObject().getItem(COSName.getPDFName("Thumb"));
				thumbID = getId(baseThumb, XOBJECT_ID, imageXObjects.size() + formXObjects.size() + postscripts.size());
				if (imageXObjectPageParent.get(thumbID) == null) {
					imageXObjectPageParent.put(thumbID, new HashSet<String>());
				}
				imageXObjectPageParent.get(thumbID).add(PAGE + pageIndex);
				if (!imageXObjects.containsKey(thumbID)) {
					COSBase base = getBase(baseThumb);
					if (base instanceof COSStream) {
						try {
							PDImageXObject img = new PDImageXObject(new PDStream((COSStream) base), null);
							imageXObjects.put(thumbID, img);
							parseImageXObject(img, thumbID);
						} catch (IOException e) {
							LOGGER.error(e);
							xobjectCreationProblem(thumbID, e.getMessage());
						}
					} else {
						xobjectCreationProblem(thumbID, "Thumb is not a stream");
					}
				}
			}

			getResourceDictionaryDependencies(page.getResources(),
					PAGE + pageIndex,
					pageExtGStateChild,
					pageColorSpaceChild,
					pagePatternChild,
					pageShadingChild,
					pageXObjectChild,
					pageFontChild,
					pagePropertiesChild,
					exGStatePageParent,
					colorSpacePageParent,
					tilingPatternPageParent,
					shadingPatternPageParent,
					shadingPageParent,
					imageXObjectPageParent,
					formXObjectPageParent,
					postscriptPageParent,
					fontPageParent,
					propertyPageParent);

			reporter.report(PBFeaturesObjectCreator
					.createPageFeaturesObject(page,
							thumbID,
							annotsId,
							pageExtGStateChild.get(PAGE + pageIndex),
							pageColorSpaceChild.get(PAGE + pageIndex),
							pagePatternChild.get(PAGE + pageIndex),
							pageShadingChild.get(PAGE + pageIndex),
							pageXObjectChild.get(PAGE + pageIndex),
							pageFontChild.get(PAGE + pageIndex),
							pagePropertiesChild.get(PAGE + pageIndex),
							PAGE + pageIndex,
							pageIndex));
		}
	}

	private Set<String> addAnnotsDependencies(PDPage page, int pageIndex) {

		COSArray annotsArray = (COSArray) page.getCOSObject()
				.getDictionaryObject(COSName.ANNOTS);

		if (annotsArray == null) {
			return Collections.emptySet();
		}
		Set<String> annotsId = new HashSet<>();

		for (int i = 0; i < annotsArray.size(); ++i) {
			COSBase item = annotsArray.get(i);
			if (item != null) {
				String id = getId(item, ANNOT, annots.keySet().size());
				annotsId.add(id);

				if (annotPagesParent.get(id) == null) {
					annotPagesParent.put(id, new HashSet<String>());
				}
				annotPagesParent.get(id).add(PAGE + pageIndex);

				COSBase base = getBase(item);

				try {
					PDAnnotation annotation = PDAnnotation
							.createAnnotation(base);
					annots.put(id, annotation);
					COSBase pop = annotation.getCOSObject().getItem(
							COSName.getPDFName("Popup"));

					if (pop != null) {
						addPopup(pop, id);
					}
				} catch (IOException e) {
					LOGGER.debug("Unknown annotation type detected.", e);
					generateUnknownAnnotation(id);
				}
			}
		}

		return annotsId;
	}

	private void addPopup(COSBase item, String parentId) {
		String id = getId(item, ANNOT, annots.keySet().size());
		COSBase base = getBase(item);

		annotChild.put(parentId, id);
		annotParent.put(id, parentId);

		try {
			PDAnnotation annotation = PDAnnotation.createAnnotation(base);
			annots.put(id, annotation);
		} catch (IOException e) {
			LOGGER.debug("Unknown annotation type detected.", e);
			generateUnknownAnnotation(id);
		}
	}

	private void generateUnknownAnnotation(String id) {
		try {
			FeatureTreeNode annot = FeatureTreeNode
					.createRootNode(ANNOTATION);
			annot.setAttribute(ID, id);
			ErrorsHelper.addErrorIntoCollection(reporter.getCollection(),
					annot,
					"Unknown annotation type");
			reporter.getCollection().addNewFeatureTree(FeaturesObjectTypesEnum.ANNOTATION,
					annot);
		} catch (FeatureParsingException e) {
			// This exception occurs when wrong node creates for feature tree.
			// The logic of the method guarantees this doesn't occur.
			String message = "PBFeatureParser.generateUnknownAnnotation logic failure.";
			LOGGER.fatal(message, e);
			throw new IllegalStateException(message, e);
		}

	}

	private void reportEmbeddedFiles(PDDocumentCatalog catalog) {
		int index = 0;
		PDEmbeddedFilesNameTreeNode efTree = catalog.getNames()
				.getEmbeddedFiles();

		try {
			if (efTree.getNames() != null) {
				for (PDComplexFileSpecification file : efTree.getNames()
						.values()) {
					reporter.report(PBFeaturesObjectCreator
							.createEmbeddedFileFeaturesObject(file, ++index));
				}
			}
		} catch (IOException e) {
			LOGGER.debug("Error creating PDFBox SubType.", e);
			handleSubtypeCreationProblem(e.getMessage());
		}

		if (efTree.getKids() != null) {
			for (PDNameTreeNode<PDComplexFileSpecification> tree : efTree
					.getKids()) {
				if (tree != null) {
					index = reportEmbeddedFileNode(tree, index);
				}
			}
		}
	}

	private int reportEmbeddedFileNode(
			final PDNameTreeNode<PDComplexFileSpecification> node,
			final int index) {
		int res = index;

		try {
			if (node.getNames() != null) {
				for (PDComplexFileSpecification file : node.getNames().values()) {
					if (file != null) {
						reporter.report(PBFeaturesObjectCreator
								.createEmbeddedFileFeaturesObject(file, ++res));
					}
				}
			}
		} catch (IOException e) {
			LOGGER.debug("Subtype creation exception caught", e);
			handleSubtypeCreationProblem(e.getMessage());
		}

		if (node.getKids() != null) {
			for (PDNameTreeNode<PDComplexFileSpecification> tree : node
					.getKids()) {
				res = reportEmbeddedFileNode(tree, res);
			}
		}

		return res;
	}

	private String addICCProfileFromOutputIntent(PDOutputIntent outInt, String outIntID) {
		COSBase outIntBase = outInt.getCOSObject();

		if (outIntBase instanceof COSDictionary) {
			COSDictionary outIntDict = (COSDictionary) outIntBase;
			String iccProfileID = getId(outIntDict.getItem(COSName.DEST_OUTPUT_PROFILE), ICCPROFILE, iccProfiles.size());

			if (!iccProfiles.containsKey(iccProfileID)) {

				iccProfiles.put(iccProfileID, outInt.getDestOutputIntent());
			}

			if (!iccProfileOutInts.containsKey(iccProfileID)) {
				iccProfileOutInts.put(iccProfileID, new HashSet<String>());
			}

			iccProfileOutInts.get(iccProfileID).add(outIntID);

			return iccProfileID;
		}

		return null;
	}

	private void handleSubtypeCreationProblem(String errorMessage) {
		creationProblem(EMBEDDEDFILE,
				null,
				errorMessage,
				FeaturesObjectTypesEnum.EMBEDDED_FILE,
				"PBFeatureParser.reportEmbeddedFileNode logic failure.", true);
	}

	private void fontCreationProblem(final String nodeID, String errorMessage) {
		creationProblem(FONT,
				nodeID,
				errorMessage,
				FeaturesObjectTypesEnum.FONT,
				"PBFeatureParser.fontCreationProblem logic failure.", false);
	}

	private void patternCreationProblem(final String nodeID, String errorMessage) {
		creationProblem(PATTERN,
				nodeID,
				errorMessage,
				FeaturesObjectTypesEnum.PATTERN,
				"PBFeatureParser.patternCreationProblem logic failure.", false);
	}

	private void colorSpaceCreationProblem(final String nodeID, String errorMessage) {
		creationProblem(COLORSPACE,
				nodeID,
				errorMessage,
				FeaturesObjectTypesEnum.COLORSPACE,
				"PBFeatureParser.colorSpaceCreationProblem logic failure.", false);
	}

	private void shadingCreationProblem(final String nodeID, String errorMessage) {
		creationProblem(SHADING,
				nodeID,
				errorMessage,
				FeaturesObjectTypesEnum.SHADING,
				"PBFeatureParser.shadingCreationProblem logic failure.", false);
	}

	private void xobjectCreationProblem(final String nodeID, String errorMessage) {
		creationProblem(XOBJECT,
				nodeID,
				errorMessage,
				FeaturesObjectTypesEnum.FAILED_XOBJECT,
				"PBFeatureParser.xobjectCreationProblem logic failure.", false);
	}

	private void creationProblem(
			final String nodeName,
			final String nodeID,
			final String errorMessage,
			final FeaturesObjectTypesEnum type,
			final String loggerMessage,
			final boolean isTypeError) {
		try {
			if (!isTypeError) {
				FeatureTreeNode node = FeatureTreeNode.createRootNode(nodeName);
				if (nodeID != null) {
					node.setAttribute(ID, nodeID);
				}
				reporter.getCollection().addNewFeatureTree(type, node);
				ErrorsHelper.addErrorIntoCollection(reporter.getCollection(),
						node,
						errorMessage);
			} else {
				String id = ErrorsHelper.addErrorIntoCollection(reporter.getCollection(),
						null,
						errorMessage);
				reporter.getCollection().addNewError(type, id);

			}
		} catch (FeatureParsingException e) {
			// This exception occurs when wrong node creates for feature
			// tree.
			// The logic of the method guarantees this doesn't occur.
			// In which case we throw an IllegalStateException as if this
			// occurs
			// we want to know there's something wrong with our logic
			LOGGER.fatal(loggerMessage, e);
			throw new IllegalStateException(loggerMessage, e);
		}
	}

	private void getAnnotationResourcesDependencies(PDAnnotation annot, String annotationID) {
		PDAppearanceDictionary dic = annot.getAppearance();

		if (dic != null) {
			COSBase baseNormal = dic.getCOSObject().getItem(COSName.N);
			if (baseNormal != null) {
				getAppearanceEntryDependencies(dic.getNormalAppearance(), baseNormal, annotationID);
			}

			COSBase baseRollover = dic.getCOSObject().getItem(COSName.R);
			if (baseRollover != null) {
				getAppearanceEntryDependencies(dic.getRolloverAppearance(), baseRollover, annotationID);
			}

			COSBase baseDown = dic.getCOSObject().getItem(COSName.D);
			if (baseDown != null) {
				getAppearanceEntryDependencies(dic.getDownAppearance(), baseDown, annotationID);
			}
		}
	}

	private void getAppearanceEntryDependencies(PDAppearanceEntry entry, COSBase entryLink, String annotationID) {
		if (entry.isStream()) {
			getAppearanceStreamDependencies(entry.getAppearanceStream(), entryLink, annotationID);
		} else {
			for (Map.Entry<COSName, PDAppearanceStream> mapEntry : entry.getSubDictionary().entrySet()) {
				getAppearanceStreamDependencies(mapEntry.getValue(),
						((COSDictionary) entry.getCOSObject()).getItem(mapEntry.getKey()), annotationID);
			}
		}
	}

	private void getAppearanceStreamDependencies(PDAppearanceStream stream, COSBase entryLink, String annotationID) {
		String id = getId(entryLink, XOBJECT_ID, formXObjects.size());
		makePairDependence(id, annotationID, formXObjectAnnotationParent, annotXObjectsChild);

		if (!formXObjects.containsKey(id)) {
			formXObjects.put(id, stream);
			getResourceDictionaryDependencies(stream.getResources(),
					id,
					formXObjectExtGStateChild,
					formXObjectColorSpaceChild,
					formXObjectPatternChild,
					formXObjectShadingChild,
					formXObjectXObjectChild,
					formXObjectFontChild,
					formXObjectPropertiesChild,
					exGStateXObjectParent,
					colorSpaceXObjectParent,
					tilingPatternXObjectParent,
					shadingPatternXObjectParent,
					shadingXObjectParent,
					imageXObjectXObjectParent,
					formXObjectXObjectParent,
					postscriptXObjectParent,
					fontXObjectParent,
					propertyXObjectParent);
		}
	}

	private void getResourceDictionaryDependencies(PDResources resources,
												   String parentID,
												   Map<String, Set<String>> exGStateChildMap,
												   Map<String, Set<String>> colorSpaceChildMap,
												   Map<String, Set<String>> patternChildMap,
												   Map<String, Set<String>> shadingChildMap,
												   Map<String, Set<String>> xobjectChildMap,
												   Map<String, Set<String>> fontChildMap,
												   Map<String, Set<String>> propertiesChildMap,
												   Map<String, Set<String>> exGStateParentMap,
												   Map<String, Set<String>> colorSpaceParentMap,
												   Map<String, Set<String>> tilingPatternParentMap,
												   Map<String, Set<String>> shadingPatternParentMap,
												   Map<String, Set<String>> shadingParentMap,
												   Map<String, Set<String>> imageXObjectParentMap,
												   Map<String, Set<String>> formXObjectParentMap,
												   Map<String, Set<String>> postscriptParentMap,
												   Map<String, Set<String>> fontParentMap,
												   Map<String, Set<String>> propertiesParentMap) {
		parseExGStateFromResource(resources, parentID, exGStateChildMap, exGStateParentMap);

		if (resources == null) {
			return;
		}

		if (resources.getColorSpaceNames() != null) {
			for (COSName name : resources.getColorSpaceNames()) {

				COSDictionary dict = (COSDictionary) resources.getCOSObject().getDictionaryObject(COSName.COLORSPACE);
				COSBase base = dict.getItem(name);
				String id = getId(base, COLORSPACE_ID, colorSpaces.size());

				try {
					PDColorSpace colorSpace = resources.getColorSpace(name);

					id = checkColorSpaceID(id, colorSpace);

					makePairDependence(id, parentID, colorSpaceParentMap, colorSpaceChildMap);

					if (!colorSpaces.containsKey(id)) {
						colorSpaces.put(id, colorSpace);
						parseColorSpace(colorSpace, id);
					}
				} catch (IOException e) {
					LOGGER.info(e);
					if (!xobjectChildMap.containsKey(parentID)) {
						xobjectChildMap.put(parentID, new HashSet<String>());
					}
					xobjectChildMap.get(parentID).add(id);
					colorSpaceCreationProblem(id, e.getMessage());
				}
			}
		}

		parsePatternFromResource(resources, parentID, patternChildMap, tilingPatternParentMap, shadingPatternParentMap);

		parseShadingFromResource(resources, parentID, shadingChildMap, shadingParentMap);

		parseXObjectFromResources(resources, parentID, xobjectChildMap, imageXObjectParentMap, formXObjectParentMap, postscriptParentMap);

		parseFontFromResources(resources, parentID, fontChildMap, fontParentMap);

		parsePropertiesFromResources(resources, parentID, propertiesChildMap, propertiesParentMap);
	}

	private void parseImageXObject(PDImageXObject xobj, String id) {
		COSBase baseColorSpace = ((COSStream) xobj.getCOSObject()).getItem(COSName.CS);
		if (baseColorSpace == null) {
			baseColorSpace = ((COSStream) xobj.getCOSObject()).getItem(COSName.COLORSPACE);
		}

		String idColorSpace = getId(baseColorSpace, COLORSPACE_ID, colorSpaces.size());

		try {
			PDColorSpace colorSpace = xobj.getColorSpace();

			idColorSpace = checkColorSpaceID(idColorSpace, colorSpace);

			if (colorSpaceXObjectParent.get(idColorSpace) == null) {
				colorSpaceXObjectParent.put(idColorSpace, new HashSet<String>());
			}
			colorSpaceXObjectParent.get(idColorSpace).add(id);
			imageXObjectColorSpaceChild.put(id, idColorSpace);

			if (!colorSpaces.containsKey(idColorSpace)) {
				colorSpaces.put(idColorSpace, colorSpace);
				parseColorSpace(colorSpace, idColorSpace);
			}
		} catch (IOException e) {
			if (colorSpaceXObjectParent.get(idColorSpace) == null) {
				colorSpaceXObjectParent.put(idColorSpace, new HashSet<String>());
			}
			colorSpaceXObjectParent.get(idColorSpace).add(id);
			imageXObjectColorSpaceChild.put(id, idColorSpace);
			LOGGER.info(e);
			colorSpaceCreationProblem(idColorSpace, e.getMessage());
		}

		COSBase mask = xobj.getCOSStream().getDictionaryObject(COSName.MASK);
		if (mask instanceof COSStream) {
			COSBase maskBase = ((COSStream) xobj.getCOSObject()).getItem(COSName.MASK);
			String idMask = getId(maskBase, XOBJECT_ID, imageXObjects.size());

			if (imageXObjectXObjectParent.get(idMask) == null) {
				imageXObjectXObjectParent.put(idMask, new HashSet<String>());
			}
			imageXObjectXObjectParent.get(idMask).add(id);
			imageXObjectMaskChild.put(id, idMask);

			if (!imageXObjects.containsKey(idMask)) {
				try {
					PDImageXObject imxobj = xobj.getMask();
					imageXObjects.put(idMask, imxobj);
					parseImageXObject(imxobj, idMask);
				} catch (IOException e) {
					LOGGER.info(e);
					xobjectCreationProblem(idMask, e.getMessage());
				}
			}
		}

		COSBase sMask = xobj.getCOSStream().getDictionaryObject(COSName.SMASK);
		if (sMask instanceof COSStream) {
			COSBase sMaskBase = ((COSStream) xobj.getCOSObject()).getItem(COSName.SMASK);
			String idMask = getId(sMaskBase, XOBJECT_ID, imageXObjects.size());

			if (imageXObjectXObjectParent.get(idMask) == null) {
				imageXObjectXObjectParent.put(idMask, new HashSet<String>());
			}
			imageXObjectXObjectParent.get(idMask).add(id);
			imageXObjectSMaskChild.put(id, idMask);

			if (!imageXObjects.containsKey(idMask)) {
				try {
					PDImageXObject imxobj = xobj.getSoftMask();
					imageXObjects.put(idMask, imxobj);
					parseImageXObject(imxobj, idMask);
				} catch (IOException e) {
					LOGGER.info(e);
					xobjectCreationProblem(idMask, e.getMessage());
				}
			}
		}

		COSBase alternates = xobj.getCOSStream().getDictionaryObject(COSName.getPDFName("Alternates"));
		alternates = getBase(alternates);
		if (alternates instanceof COSArray) {
			COSArray alternatesArray = (COSArray) alternates;
			for (COSBase entry : alternatesArray) {
				COSBase base = getBase(entry);
				if (base instanceof COSDictionary) {
					COSDictionary altDict = (COSDictionary) base;
					COSBase baseImage = altDict.getItem(COSName.IMAGE);
					String idImage = getId(baseImage, XOBJECT_ID, imageXObjects.size());
					baseImage = getBase(baseImage);
					if (baseImage instanceof COSStream) {
						makePairDependence(idImage, id, imageXObjectXObjectParent, imageXObjectAlternatesChild);
						if (!imageXObjects.containsKey(idImage)) {
							try {
								PDImageXObject im = new PDImageXObject(new PDStream((COSStream) baseImage), null);
								imageXObjects.put(idImage, im);
								parseImageXObject(im, idImage);
							} catch (IOException e) {
								LOGGER.info(e);
								xobjectCreationProblem(idImage, e.getMessage());
							}
						}
					}
				}
			}
		}
	}

	private void parseXObjectFromResources(PDResources resources,
										   String parentID,
										   Map<String, Set<String>> xobjectChildMap,
										   Map<String, Set<String>> imageXObjectParentMap,
										   Map<String, Set<String>> formXObjectParentMap,
										   Map<String, Set<String>> postscriptParentMap) {
		if (resources == null || resources.getXObjectNames() == null) {
			return;
		}

		for (COSName name : resources.getXObjectNames()) {
			COSDictionary dict = (COSDictionary) resources.getCOSObject().getDictionaryObject(COSName.XOBJECT);
			COSBase base = dict.getItem(name);
			String id = getId(base, XOBJECT_ID, imageXObjects.size() + formXObjects.size() + postscripts.size());

			try {
				PDXObject xobj = resources.getXObject(name);

				if (xobj instanceof PDImageXObject) {
					makePairDependence(id, parentID, imageXObjectParentMap, xobjectChildMap);
					if (!imageXObjects.containsKey(id)) {
						imageXObjects.put(id, (PDImageXObject) xobj);

						parseImageXObject((PDImageXObject) xobj, id);

					}
				} else if (xobj instanceof PDFormXObject) {
					makePairDependence(id, parentID, formXObjectParentMap, xobjectChildMap);
					if (!formXObjects.containsKey(id)) {
						formXObjects.put(id, (PDFormXObject) xobj);

						PDGroup group = ((PDFormXObject) xobj).getGroup();
						if (group != null && COSName.TRANSPARENCY.equals(group.getSubType())) {
							COSBase baseColorSpace = group.getCOSObject().getItem(COSName.CS);
							String idColorSpace = getId(baseColorSpace, COLORSPACE_ID, colorSpaces.size());

							try {
								PDColorSpace colorSpace = group.getColorSpace();

								idColorSpace = checkColorSpaceID(idColorSpace, colorSpace);

								if (colorSpaceXObjectParent.get(idColorSpace) == null) {
									colorSpaceXObjectParent.put(idColorSpace, new HashSet<String>());
								}
								colorSpaceXObjectParent.get(idColorSpace).add(id);
								groupXObjectColorSpaceChild.put(id, idColorSpace);

								if (!colorSpaces.containsKey(idColorSpace)) {
									colorSpaces.put(idColorSpace, colorSpace);
									parseColorSpace(colorSpace, idColorSpace);
								}
							} catch (IOException e) {
								if (colorSpaceXObjectParent.get(idColorSpace) == null) {
									colorSpaceXObjectParent.put(idColorSpace, new HashSet<String>());
								}
								colorSpaceXObjectParent.get(idColorSpace).add(id);
								groupXObjectColorSpaceChild.put(id, idColorSpace);
								LOGGER.info(e);
								colorSpaceCreationProblem(idColorSpace, e.getMessage());
							}
						}

						getResourceDictionaryDependencies(((PDFormXObject) xobj).getResources(),
								id,
								formXObjectExtGStateChild,
								formXObjectColorSpaceChild,
								formXObjectPatternChild,
								formXObjectShadingChild,
								formXObjectXObjectChild,
								formXObjectFontChild,
								formXObjectPropertiesChild,
								exGStateXObjectParent,
								colorSpaceXObjectParent,
								tilingPatternXObjectParent,
								shadingPatternXObjectParent,
								shadingXObjectParent,
								imageXObjectXObjectParent,
								formXObjectXObjectParent,
								postscriptXObjectParent,
								fontXObjectParent,
								propertyXObjectParent);

					}
				} else if (xobj instanceof PDPostScriptXObject) {
					makePairDependence(id, parentID, postscriptParentMap, xobjectChildMap);
					postscripts.add(id);
				}
			} catch (IOException e) {
				LOGGER.info(e);
				xobjectCreationProblem(id, e.getMessage());
			}
		}
	}

	private void parsePropertiesFromResources(PDResources resources,
											  String parentID,
											  Map<String, Set<String>> propertiesChildMap,
											  Map<String, Set<String>> propertiesParentMap) {
		if (resources == null || resources.getPropertiesNames() == null) {
			return;
		}

		for (COSName name : resources.getPropertiesNames()) {
			COSDictionary dict = (COSDictionary) resources.getCOSObject().getDictionaryObject(COSName.PROPERTIES);
			COSBase base = dict.getItem(name);
			String id = getId(base, PROPERTIES_ID, properties.size());

			makePairDependence(id, parentID, propertiesParentMap, propertiesChildMap);

			if (!properties.containsKey(id)) {
				PDPropertyList property = resources.getProperties(name);
				properties.put(id, property.getCOSObject());
			}
		}
	}

	private void parseFontFromResources(PDResources resources,
										String parentID,
										Map<String, Set<String>> fontChildMap,
										Map<String, Set<String>> fontParentMap) {
		if (resources == null || resources.getFontNames() == null) {
			return;
		}

		for (COSName name : resources.getFontNames()) {
			COSDictionary dict = (COSDictionary) resources.getCOSObject().getDictionaryObject(COSName.FONT);
			COSBase base = dict.getItem(name);
			String id = getId(base, FONT_ID, fonts.size());

			makePairDependence(id, parentID, fontParentMap, fontChildMap);

			if (!fonts.containsKey(id)) {
				try {
					PDFont font = resources.getFont(name);
					fonts.put(id, font);
					parseFont(font, id);
				} catch (IOException e) {
					LOGGER.info(e);
					fontCreationProblem(id, e.getMessage());
				}

			}
		}
	}

	private void parseExGStateFromResource(PDResources resources,
										   String parentID,
										   Map<String, Set<String>> exGStateChildMap,
										   Map<String, Set<String>> exGStateParentMap) {
		if (resources == null || resources.getExtGStateNames() == null) {
			return;
		}

		for (COSName name : resources.getExtGStateNames()) {
			COSDictionary dict = (COSDictionary) resources.getCOSObject().getDictionaryObject(COSName.EXT_G_STATE);
			COSBase base = dict.getItem(name);
			String id = getId(base, EXTGSTATE_ID, exGStates.size());

			makePairDependence(id, parentID, exGStateParentMap, exGStateChildMap);

			if (!exGStates.containsKey(id)) {
				PDExtendedGraphicsState exGState = resources.getExtGState(name);
				exGStates.put(id, exGState);

				if (exGState.getFontSetting() == null || !(exGState.getFontSetting().getCOSObject() instanceof COSArray)) {
					return;
				}

				String fontID = getId(((COSArray) exGState.getFontSetting().getCOSObject()).get(0), FONT_ID, fonts.size());

				if (fontExtGStateParent.get(fontID) == null) {
					fontExtGStateParent.put(fontID, new HashSet<String>());
				}
				fontExtGStateParent.get(fontID).add(id);
				exGStateFontChild.put(id, fontID);

				if (!fonts.containsKey(fontID)) {
					try {
						PDFont font = exGState.getFontSetting().getFont();
						fonts.put(fontID, font);
						parseFont(font, fontID);
					} catch (IOException e) {
						LOGGER.info(e);
						fontCreationProblem(fontID, e.getMessage());
					}
				}
			}
		}
	}

	private void parsePatternFromResource(PDResources resources,
										  String parentID,
										  Map<String, Set<String>> patternChildMap,
										  Map<String, Set<String>> tilingPatternParentMap,
										  Map<String, Set<String>> shadingPatternParentMap) {
		if (resources == null || resources.getPatternNames() == null) {
			return;
		}

		for (COSName name : resources.getPatternNames()) {
			COSDictionary dict = (COSDictionary) resources.getCOSObject().getDictionaryObject(COSName.PATTERN);
			COSBase base = dict.getItem(name);

			String id = getId(base, PATTERN_ID, shadingPatterns.size() + tilingPatterns.size());

			if (patternChildMap.get(parentID) == null) {
				patternChildMap.put(parentID, new HashSet<String>());
			}
			patternChildMap.get(parentID).add(id);

			try {
				PDAbstractPattern pattern = resources.getPattern(name);

				if (pattern instanceof PDTilingPattern) {
					if (tilingPatternParentMap.get(id) == null) {
						tilingPatternParentMap.put(id, new HashSet<String>());
					}
					tilingPatternParentMap.get(id).add(parentID);

					if (!tilingPatterns.containsKey(id)) {
						PDTilingPattern tilingPattern = (PDTilingPattern) pattern;
						tilingPatterns.put(id, tilingPattern);

						getResourceDictionaryDependencies(
								tilingPattern.getResources(),
								id,
								tilingPatternExtGStateChild,
								tilingPatternColorSpaceChild,
								tilingPatternPatternChild,
								tilingPatternShadingChild,
								tilingPatternXObjectChild,
								tilingPatternFontChild,
								tilingPatternPropertiesChild,
								exGStatePatternParent,
								colorSpacePatternParent,
								tilingPatternPatternParent,
								shadingPatternPatternParent,
								shadingPatternParent,
								imageXObjectPatternParent,
								formXObjectPatternParent,
								postscriptPatternParent,
								fontPatternParent,
								propertyPatternParent);
					}
				} else {
					if (shadingPatternParentMap.get(id) == null) {
						shadingPatternParentMap.put(id, new HashSet<String>());
					}
					shadingPatternParentMap.get(id).add(parentID);

					if (!shadingPatterns.containsKey(id)) {
						PDShadingPattern shadingPattern = (PDShadingPattern) pattern;
						shadingPatterns.put(id, shadingPattern);

						COSBase baseShading = shadingPattern.getCOSObject().getItem(COSName.SHADING);
						String shadingID = getId(baseShading, SHADING_ID, shadings.size());

						shadingPatternShadingChild.put(id, shadingID);
						if (shadingPatternParent.get(shadingID) == null) {
							shadingPatternParent.put(shadingID, new HashSet<String>());
						}
						shadingPatternParent.get(shadingID).add(id);

						if (!shadings.containsKey(shadingID) && shadingPattern.getShading() != null) {
							shadings.put(shadingID, shadingPattern.getShading());

							parseShading(shadingPattern.getShading(), shadingID);
						}

						COSBase baseExGState = shadingPattern.getCOSObject().getItem(COSName.EXT_G_STATE);
						String exGStateID = getId(baseExGState, EXTGSTATE_ID, exGStates.size());

						shadingPatternExtGStateChild.put(id, exGStateID);
						if (exGStatePatternParent.get(exGStateID) == null) {
							exGStatePatternParent.put(exGStateID, new HashSet<String>());
						}
						exGStatePatternParent.get(exGStateID).add(id);

						if (!exGStates.containsKey(exGStateID) && shadingPattern.getExtendedGraphicsState() != null) {
							exGStates.put(exGStateID, shadingPattern.getExtendedGraphicsState());

							if (shadingPattern.getExtendedGraphicsState().getFontSetting() == null) {
								return;
							}

							String fontID = getId(((COSArray) shadingPattern.getExtendedGraphicsState().getFontSetting().getCOSObject()).get(0), FONT_ID, fonts.size());

							if (fontExtGStateParent.get(fontID) == null) {
								fontExtGStateParent.put(fontID, new HashSet<String>());
							}
							fontExtGStateParent.get(fontID).add(exGStateID);
							exGStateFontChild.put(exGStateID, fontID);

							if (!fonts.containsKey(fontID)) {
								try {
									PDFont font = shadingPattern.getExtendedGraphicsState().getFontSetting().getFont();
									fonts.put(fontID, font);
									parseFont(font, fontID);
								} catch (IOException e) {
									LOGGER.info(e);
									fontCreationProblem(fontID, e.getMessage());
								}
							}
						}
					}
				}
			} catch (IOException e) {
				LOGGER.info(e);
				patternCreationProblem(id, e.getMessage());
			}
		}
	}

	private void parseShadingFromResource(PDResources resources,
										  String parentID,
										  Map<String, Set<String>> shadingChildMap,
										  Map<String, Set<String>> shadingParentMap) {

		if (resources == null || resources.getShadingNames() == null) {
			return;
		}

		for (COSName name : resources.getShadingNames()) {
			COSDictionary dict = (COSDictionary) resources.getCOSObject().getDictionaryObject(COSName.SHADING);
			COSBase base = dict.getItem(name);
			String id = getId(base, SHADING_ID, shadings.size());
			makePairDependence(id, parentID, shadingParentMap, shadingChildMap);

			if (!shadings.containsKey(id)) {
				try {
					PDShading shading = resources.getShading(name);
					parseShading(shading, id);
				} catch (IOException e) {
					LOGGER.info(e);
					shadingCreationProblem(id, e.getMessage());
				}
			}
		}
	}

	private void parseShading(PDShading shading, String parentID) {
		COSBase base = shading.getCOSObject().getItem(COSName.CS);
		if (base == null) {
			base = shading.getCOSObject().getItem(COSName.COLORSPACE);
		}

		String id = getId(base, COLORSPACE_ID, colorSpaces.size());

		try {
			PDColorSpace colorSpace = shading.getColorSpace();

			id = checkColorSpaceID(id, colorSpace);

			if (colorSpaceShadingParent.get(id) == null) {
				colorSpaceShadingParent.put(id, new HashSet<String>());
			}
			colorSpaceShadingParent.get(id).add(parentID);
			shadingColorSpaceChild.put(parentID, id);

			if (!colorSpaces.containsKey(id)) {
				colorSpaces.put(id, colorSpace);
				parseColorSpace(colorSpace, id);
			}
		} catch (IOException e) {
			if (colorSpaceShadingParent.get(id) == null) {
				colorSpaceShadingParent.put(id, new HashSet<String>());
			}
			colorSpaceShadingParent.get(id).add(parentID);
			shadingColorSpaceChild.put(parentID, id);
			LOGGER.info(e);
			colorSpaceCreationProblem(id, e.getMessage());
		}
	}

	private void parseFont(PDFont font, String parentID) {
		if (font instanceof PDType3Font) {
			getResourceDictionaryDependencies(
					((PDType3Font) font).getResources(),
					parentID,
					fontExtGStateChild,
					fontColorSpaceChild,
					fontPatternChild,
					fontShadingChild,
					fontXObjectChild,
					fontFontChild,
					fontPropertiesChild,
					exGStateFontParent,
					colorSpaceFontParent,
					tilingPatternFontParent,
					shadingPatternFontParent,
					shadingFontParent,
					imageXObjectFontParent,
					formXObjectFontParent,
					postscriptFontParent,
					fontFontParent,
					propertyFontParent);
		} else if (font instanceof PDType0Font) {
			PDType0Font type0 = (PDType0Font) font;

			COSBase descendantFontsBase = type0.getCOSObject().getDictionaryObject(COSName.DESCENDANT_FONTS);
			if (descendantFontsBase instanceof COSArray) {
				COSBase descendantFontDictionaryBase = ((COSArray) descendantFontsBase).getObject(0);
				String id = getId(descendantFontDictionaryBase, FONT_ID, fonts.size());
				makePairDependence(id, parentID, fontFontParent, fontFontChild);

				if (!fonts.containsKey(id)) {
					fonts.put(id, type0.getDescendantFont());
				}
			}
		}
	}

	private void parseColorSpace(PDColorSpace colorSpace, String parentID) {
		if (colorSpace instanceof PDICCBased) {
			PDICCBased iccBased = (PDICCBased) colorSpace;
			COSArray array = (COSArray) iccBased.getCOSObject();
			COSBase base = array.get(1);
			String id = getId(base, ICCPROFILE, iccProfiles.size());

			if (iccProfileICCBased.get(id) == null) {
				iccProfileICCBased.put(id, new HashSet<String>());
			}
			iccProfileICCBased.get(id).add(parentID);
			colorSpaceIccProfileChild.put(parentID, id);

			if (!iccProfiles.containsKey(id)) {
				iccProfiles.put(id, iccBased.getPDStream().getStream());
			}

			COSBase baseAlt = iccBased.getPDStream().getStream().getItem(COSName.ALTERNATE);
			String idAlt = getId(baseAlt, COLORSPACE_ID, colorSpaces.size());

			try {
				PDColorSpace altclr = iccBased.getAlternateColorSpace();
				idAlt = checkColorSpaceID(idAlt, altclr);

				if (colorSpaceColorSpaceParent.get(idAlt) == null) {
					colorSpaceColorSpaceParent.put(idAlt, new HashSet<String>());
				}
				colorSpaceColorSpaceParent.get(idAlt).add(parentID);
				colorSpaceColorSpaceChild.put(parentID, idAlt);

				if (!colorSpaces.containsKey(idAlt)) {
					colorSpaces.put(idAlt, iccBased.getAlternateColorSpace());
					parseColorSpace(iccBased.getAlternateColorSpace(), idAlt);

				}
			} catch (IOException e) {
				LOGGER.info(e);
				colorSpaceCreationProblem(idAlt, e.getMessage());
			}
		} else if (colorSpace instanceof PDIndexed ||
				colorSpace instanceof PDSeparation ||
				colorSpace instanceof PDDeviceN) {

			int number;
			if (colorSpace instanceof PDIndexed) {
				number = 1;
			} else {
				number = 2;
			}

			COSArray array = (COSArray) colorSpace.getCOSObject();
			COSBase base = array.get(number);
			String id = getId(base, COLORSPACE_ID, colorSpaces.size());

			try {
				PDColorSpace alt;
				if (colorSpace instanceof PDIndexed) {
					alt = ((PDIndexed) colorSpace).getBaseColorSpace();
				} else if (colorSpace instanceof PDSeparation) {
					alt = ((PDSeparation) colorSpace).getAlternateColorSpace();
				} else {
					alt = ((PDDeviceN) colorSpace).getAlternateColorSpace();
				}

				id = checkColorSpaceID(id, alt);

				if (colorSpaceColorSpaceParent.get(id) == null) {
					colorSpaceColorSpaceParent.put(id, new HashSet<String>());
				}
				colorSpaceColorSpaceParent.get(id).add(parentID);
				colorSpaceColorSpaceChild.put(parentID, id);

				if (!colorSpaces.containsKey(id)) {
					colorSpaces.put(id, alt);
					parseColorSpace(alt, id);
				}
			} catch (IOException e) {
				LOGGER.info(e);
				colorSpaceCreationProblem(id, e.getMessage());
			}

		}
	}

	private static String checkColorSpaceID(String prevID, PDColorSpace colorSpace) {
		String id;
		if (colorSpace instanceof PDDeviceGray) {
			id = DEVICEGRAY_ID;
		} else if (colorSpace instanceof PDDeviceRGB) {
			id = DEVICERGB_ID;
		} else if (colorSpace instanceof PDDeviceCMYK) {
			id = DEVICECMYK_ID;
		} else {
			id = prevID;
		}
		return id;
	}

	private static void makePairDependence(String childID,
										   String parentID,
										   Map<String, Set<String>> childParentMap,
										   Map<String, Set<String>> parentChildMap) {
		if (parentChildMap.get(parentID) == null) {
			parentChildMap.put(parentID, new HashSet<String>());
		}
		parentChildMap.get(parentID).add(childID);

		if (childParentMap.get(childID) == null) {
			childParentMap.put(childID, new HashSet<String>());
		}
		childParentMap.get(childID).add(parentID);
	}

	private static COSBase getBase(final COSBase base) {
		COSBase item = base;

		while (item instanceof COSObject) {
			item = ((COSObject) item).getObject();
		}

		return item;
	}

	private static String getId(final COSBase base, final String prefix,
								final long number) {
		long numb = number;
		COSBase item = base;
		String type = "Dir";

		while (item instanceof COSObject) {
			numb = ((COSObject) item).getObjectNumber();
			type = "Indir";
			item = ((COSObject) item).getObject();
		}

		return prefix + type + numb;
	}
}

/**
 * This file is part of veraPDF Library core, a module of the veraPDF project.
 * Copyright (c) 2015, veraPDF Consortium <info@verapdf.org>
 * All rights reserved.
 * <p>
 * veraPDF Library core is free software: you can redistribute it and/or modify
 * it under the terms of either:
 * <p>
 * The GNU General public license GPLv3+.
 * You should have received a copy of the GNU General Public License
 * along with veraPDF Library core as the LICENSE.GPL file in the root of the source
 * tree.  If not, see http://www.gnu.org/licenses/ or
 * https://www.gnu.org/licenses/gpl-3.0.en.html.
 * <p>
 * The Mozilla Public License MPLv2+.
 * You should have received a copy of the Mozilla Public License along with
 * veraPDF Library core as the LICENSE.MPL file in the root of the source tree.
 * If a copy of the MPL was not distributed with this file, you can obtain one at
 * http://mozilla.org/MPL/2.0/.
 */
package org.verapdf.features.objects;

import org.verapdf.core.FeatureParsingException;
import org.verapdf.features.FeatureObjectType;
import org.verapdf.features.FeaturesData;
import org.verapdf.features.tools.CreateNodeHelper;
import org.verapdf.features.tools.ErrorsHelper;
import org.verapdf.features.tools.FeatureTreeNode;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

/**
 * Feature object for info dict
 *
 * @author Maksim Bezrukov
 */
public class InfoDictFeaturesObject extends FeaturesObject {

	private static final String SUB_TYPE = "subType";
	private static final String INFORMATION_DICT = "informationDict";
	private static final String TITLE = "Title";
	private static final String AUTHOR = "Author";
	private static final String SUBJECT = "Subject";
	private static final String KEYWORDS = "Keywords";
	private static final String CREATOR = "Creator";
	private static final String PRODUCER = "Producer";
	private static final String CREATION_DATE = "CreationDate";
	private static final String MOD_DATE = "ModDate";
	private static final String TRAPPED = "Trapped";

	private static final String ENTRY = "entry";
	private static final String KEY = "key";

	/**
	 * Constructs new info dict Feature Object
	 *
	 * @param adapter class represents info dict adapter
	 */
	public InfoDictFeaturesObject(InfoDictFeaturesObjectAdapter adapter) {
		super(adapter);
	}

	/**
	 * @return INFORMATION_DICTIONARY instance of the FeatureObjectType enumeration
	 */
	@Override
	public FeatureObjectType getType() {
		return FeatureObjectType.INFORMATION_DICTIONARY;
	}

	/**
	 * Reports all features from the object into the collection
	 *
	 * @return FeatureTreeNode class which represents a root node of the
	 * constructed collection tree
	 * @throws FeatureParsingException occurs when wrong features tree node constructs
	 */
	@Override
	public FeatureTreeNode collectFeatures() throws FeatureParsingException {
		InfoDictFeaturesObjectAdapter infoDictAdapter = (InfoDictFeaturesObjectAdapter) this.adapter;
		FeatureTreeNode root = FeatureTreeNode.createRootNode(INFORMATION_DICT);

		addEntry(TITLE, infoDictAdapter.getTitle(), root);
		addEntry(AUTHOR, infoDictAdapter.getAuthor(), root);
		addEntry(SUBJECT, infoDictAdapter.getSubject(), root);
		addEntry(KEYWORDS, infoDictAdapter.getKeywords(), root);
		addEntry(CREATOR, infoDictAdapter.getCreator(), root);
		addEntry(PRODUCER, infoDictAdapter.getProducer(), root);

		Calendar crDate = infoDictAdapter.getCreationDate();
		if (crDate != null) {
			FeatureTreeNode creationDate = CreateNodeHelper.createDateNode(ENTRY, root, crDate, this);
			if (creationDate != null) {
				creationDate.setAttribute(KEY, CREATION_DATE);
			}
		}

		Calendar mdDate = infoDictAdapter.getModDate();
		if (mdDate != null) {
			FeatureTreeNode modDate = CreateNodeHelper.createDateNode(ENTRY, root, mdDate, this);
			if (modDate != null) {
				modDate.setAttribute(KEY, MOD_DATE);
			}
		}

		addEntry(TRAPPED, infoDictAdapter.getTrapped(), root);

		Map<String, String> elements = infoDictAdapter.getCustomValues();
		for (Map.Entry<String, String> elem : elements.entrySet()) {
			addEntry(elem.getKey(), elem.getValue(), root);
		}
		return root;
	}

	private static void addEntry(String name, String value, FeatureTreeNode root) throws FeatureParsingException {
		if (name != null && value != null) {
			FeatureTreeNode entry = root.addChild(ENTRY);
			entry.setValue(value);
			entry.setAttribute(KEY, name);
		}
	}

	private static String getXPath(String key) {
		return INFORMATION_DICT + "/" + ENTRY + "[@key='" + key + "']";
	}

	/**
	 * @return null
	 */
	@Override
	public FeaturesData getData() {
		return null;
	}

	static List<Feature> getFeaturesList() {
		// All fields are present
		List<Feature> featuresList = new ArrayList<>();
		featuresList.add(new Feature("Title", getXPath(TITLE), Feature.FeatureType.STRING));
		featuresList.add(new Feature("Author", getXPath(AUTHOR), Feature.FeatureType.STRING));
		featuresList.add(new Feature("Subject", getXPath(SUBJECT), Feature.FeatureType.STRING));
		featuresList.add(new Feature("Keywords", getXPath(KEYWORDS), Feature.FeatureType.STRING));
		featuresList.add(new Feature("Creator", getXPath(CREATOR), Feature.FeatureType.STRING));
		featuresList.add(new Feature("Producer", getXPath(PRODUCER), Feature.FeatureType.STRING));
		featuresList.add(new Feature("Creation Date", getXPath(CREATION_DATE), Feature.FeatureType.STRING));
		featuresList.add(new Feature("Modification Date", getXPath(MOD_DATE), Feature.FeatureType.STRING));
		featuresList.add(new Feature("Trapped", getXPath(TRAPPED), Feature.FeatureType.STRING));

		featuresList.add(new Feature("Error IDs",
				generateAttributeXPath(INFORMATION_DICT, ErrorsHelper.ERRORID), Feature.FeatureType.STRING));
		return featuresList;
	}
}

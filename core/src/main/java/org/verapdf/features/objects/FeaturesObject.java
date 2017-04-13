/**
 * This file is part of veraPDF Library core, a module of the veraPDF project.
 * Copyright (c) 2015, veraPDF Consortium <info@verapdf.org>
 * All rights reserved.
 *
 * veraPDF Library core is free software: you can redistribute it and/or modify
 * it under the terms of either:
 *
 * The GNU General public license GPLv3+.
 * You should have received a copy of the GNU General Public License
 * along with veraPDF Library core as the LICENSE.GPL file in the root of the source
 * tree.  If not, see http://www.gnu.org/licenses/ or
 * https://www.gnu.org/licenses/gpl-3.0.en.html.
 *
 * The Mozilla Public License MPLv2+.
 * You should have received a copy of the Mozilla Public License along with
 * veraPDF Library core as the LICENSE.MPL file in the root of the source tree.
 * If a copy of the MPL was not distributed with this file, you can obtain one at
 * http://mozilla.org/MPL/2.0/.
 */
package org.verapdf.features.objects;

import org.verapdf.core.FeatureParsingException;
import org.verapdf.features.FeatureExtractionResult;
import org.verapdf.features.FeatureObjectType;
import org.verapdf.features.FeaturesData;
import org.verapdf.features.tools.ErrorsHelper;
import org.verapdf.features.tools.FeatureTreeNode;

import java.util.ArrayList;
import java.util.List;

/**
 * Main interface for all features objects
 *
 * @author Maksim Bezrukov
 */
public abstract class FeaturesObject {

	protected FeaturesObjectAdapter adapter;
	private final List<String> errors = new ArrayList<>();

	FeaturesObject(FeaturesObjectAdapter adapter) {
		this.adapter = adapter;
	}

	public void registerNewError(String error) {
		errors.add(error);
	}

	/**
	 * @return enum type of the current feature object
	 */
	public abstract FeatureObjectType getType();

	/**
	 * Reports all features from the object into the collection
	 *
	 * @param collection collection for feature report
	 * @return FeatureTreeNode class which represents a root node of the constructed collection tree
	 * @throws FeatureParsingException occurs when wrong features tree node constructs
	 */
	public final FeatureTreeNode reportFeatures(FeatureExtractionResult collection) throws FeatureParsingException {
		this.errors.clear();
		if (this.adapter.isPDFObjectPresent()) {
			FeatureTreeNode root = collectFeatures();
			this.errors.addAll(adapter.getErrors());
			if (!errors.isEmpty()) {
				for (String error : errors) {
					ErrorsHelper.addErrorIntoCollection(collection, root, error);
				}
			}
			collection.addNewFeatureTree(getType(), root);
			return root;
		}
		return null;
	}

	protected abstract FeatureTreeNode collectFeatures() throws FeatureParsingException;

	/**
	 * @return features data for object
	 */
	public abstract FeaturesData getData();

	protected static String generateVariableXPath(String... node) {
		if (node == null || node.length == 0) {
			throw new IllegalArgumentException("There should be at least one node");
		}
		StringBuilder builder = new StringBuilder(node[0]);
		for (int i = 1; i < node.length; ++i) {
			builder.append("/").append(node[i]);
		}
		return builder.toString();
	}

	protected static String generateAttributeXPath(String... node) {
		if (node == null || node.length < 2) {
			throw new IllegalArgumentException("There should be at least two nodes for attribute path");
		}
		StringBuilder builder = new StringBuilder(node[0]);
		for (int i = 1; i < node.length - 1; ++i) {
			builder.append("/").append(node[i]);
		}
		builder.append("/@").append(node[node.length - 1]);
		return builder.toString();
	}
}

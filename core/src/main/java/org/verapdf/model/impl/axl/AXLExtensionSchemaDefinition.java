/**
 * This file is part of veraPDF Library core, a module of the veraPDF project.
 * Copyright (c) 2015-2024, veraPDF Consortium <info@verapdf.org>
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
package org.verapdf.model.impl.axl;

import org.verapdf.xmp.XMPConst;
import org.verapdf.xmp.impl.VeraPDFXMPNode;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.tools.xmp.ValidatorsContainer;
import org.verapdf.model.tools.xmp.validators.SimpleTypeValidator;
import org.verapdf.model.tools.xmp.validators.URITypeValidator;
import org.verapdf.model.xmplayer.ExtensionSchemaDefinition;

import java.util.*;

/**
 * @author Maksim Bezrukov
 */
public class AXLExtensionSchemaDefinition extends AXLExtensionSchemaObject implements ExtensionSchemaDefinition {

	public static final String EXTENSION_SCHEMA_DEFINITION = "ExtensionSchemaDefinition";

	public static final String EXTENSION_SCHEMA_PROPERTIES = "ExtensionSchemaProperties";
	public static final String EXTENSION_SCHEMA_VALUE_TYPES = "ExtensionSchemaValueTypes";
	public static final String NAMESPACE_URI = "namespaceURI";
	private static final String PREFIX = "prefix";
	private static final String PROPERTY = "property";
	private static final String SCHEMA = "schema";
	private static final String VALUE_TYPE = "valueType";
	private static final Set<String> validChildNames = new HashSet<>();

	public AXLExtensionSchemaDefinition(VeraPDFXMPNode xmpNode, ValidatorsContainer containerForPDFA_1, ValidatorsContainer containerForPDFA_2_3) {
		super(EXTENSION_SCHEMA_DEFINITION, xmpNode, containerForPDFA_1, containerForPDFA_2_3);
	}

	/**
	 * @param link name of the link
	 * @return List of all objects with link name
	 */
	@Override
	public List<? extends Object> getLinkedObjects(String link) {
		switch (link) {
			case EXTENSION_SCHEMA_PROPERTIES:
				return this.getExtensionSchemaProperties();
			case EXTENSION_SCHEMA_VALUE_TYPES:
				return this.getExtensionSchemaValueType();
			default:
				return super.getLinkedObjects(link);
		}
	}

	private List<AXLExtensionSchemaValueType> getExtensionSchemaValueType() {
		if (this.xmpNode != null) {
			List<AXLExtensionSchemaValueType> res = new ArrayList<>();
			for (VeraPDFXMPNode child : this.xmpNode.getChildren()) {
				if (XMPConst.NS_PDFA_SCHEMA.equals(child.getNamespaceURI()) && VALUE_TYPE.equals(child.getName())) {
					if (child.getOptions().isArray()) {
						for (VeraPDFXMPNode node : child.getChildren()) {
							res.add(new AXLExtensionSchemaValueType(node, this.containerForPDFA_1, this.containerForPDFA_2_3));
						}
					}
					break;
				}
			}
			return Collections.unmodifiableList(res);
		}
		return Collections.emptyList();
	}

	private List<AXLExtensionSchemaProperty> getExtensionSchemaProperties() {
		if (this.xmpNode != null) {
			List<AXLExtensionSchemaProperty> res = new ArrayList<>();
			for (VeraPDFXMPNode child : this.xmpNode.getChildren()) {
				if (XMPConst.NS_PDFA_SCHEMA.equals(child.getNamespaceURI()) && PROPERTY.equals(child.getName())) {
					if (child.getOptions().isArray()) {
						for (VeraPDFXMPNode node : child.getChildren()) {
							res.add(new AXLExtensionSchemaProperty(node, this.containerForPDFA_1, this.containerForPDFA_2_3));
						}
					}
					break;
				}
			}
			return Collections.unmodifiableList(res);
		}
		return Collections.emptyList();
	}

	@Override
	public Boolean getisNamespaceURIValidURI() {
		for (VeraPDFXMPNode child : this.xmpNode.getChildren()) {
			if (XMPConst.NS_PDFA_SCHEMA.equals(child.getNamespaceURI()) && NAMESPACE_URI.equals(child.getName())) {
				return new URITypeValidator().isCorresponding(child);
			}
		}
		return Boolean.TRUE;
	}

	@Override
	public Boolean getisPrefixValidText() {
		for (VeraPDFXMPNode child : this.xmpNode.getChildren()) {
			if (XMPConst.NS_PDFA_SCHEMA.equals(child.getNamespaceURI()) && PREFIX.equals(child.getName())) {
				return SimpleTypeValidator.fromValue(SimpleTypeValidator.SimpleTypeEnum.TEXT).isCorresponding(child);
			}
		}
		return Boolean.TRUE;
	}

	@Override
	public Boolean getisPropertyValidSeq() {
		for (VeraPDFXMPNode child : this.xmpNode.getChildren()) {
			if (XMPConst.NS_PDFA_SCHEMA.equals(child.getNamespaceURI()) && PROPERTY.equals(child.getName())) {
				return child.getOptions().isArrayOrdered();
			}
		}
		return Boolean.TRUE;
	}

	@Override
	public Boolean getisSchemaValidText() {
		for (VeraPDFXMPNode child : this.xmpNode.getChildren()) {
			if (XMPConst.NS_PDFA_SCHEMA.equals(child.getNamespaceURI()) && SCHEMA.equals(child.getName())) {
				return SimpleTypeValidator.fromValue(SimpleTypeValidator.SimpleTypeEnum.TEXT).isCorresponding(child);
			}
		}
		return Boolean.TRUE;
	}

	@Override
	public Boolean getisValueTypeValidSeq() {
		for (VeraPDFXMPNode child : this.xmpNode.getChildren()) {
			if (XMPConst.NS_PDFA_SCHEMA.equals(child.getNamespaceURI()) && VALUE_TYPE.equals(child.getName())) {
				return child.getOptions().isArrayOrdered();
			}
		}
		return Boolean.TRUE;
	}

	@Override
	public String getnamespaceURIPrefix() {
		for (VeraPDFXMPNode child : this.xmpNode.getChildren()) {
			if (XMPConst.NS_PDFA_SCHEMA.equals(child.getNamespaceURI()) && NAMESPACE_URI.equals(child.getName())) {
				return child.getPrefix();
			}
		}
		return null;
	}

	@Override
	public String getprefixPrefix() {
		for (VeraPDFXMPNode child : this.xmpNode.getChildren()) {
			if (XMPConst.NS_PDFA_SCHEMA.equals(child.getNamespaceURI()) && PREFIX.equals(child.getName())) {
				return child.getPrefix();
			}
		}
		return null;
	}

	@Override
	public String getpropertyPrefix() {
		for (VeraPDFXMPNode child : this.xmpNode.getChildren()) {
			if (XMPConst.NS_PDFA_SCHEMA.equals(child.getNamespaceURI()) && PROPERTY.equals(child.getName())) {
				return child.getPrefix();
			}
		}
		return null;
	}

	@Override
	public String getschemaPrefix() {
		for (VeraPDFXMPNode child : this.xmpNode.getChildren()) {
			if (XMPConst.NS_PDFA_SCHEMA.equals(child.getNamespaceURI()) && SCHEMA.equals(child.getName())) {
				return child.getPrefix();
			}
		}
		return null;
	}

	@Override
	public String getvalueTypePrefix() {
		for (VeraPDFXMPNode child : this.xmpNode.getChildren()) {
			if (XMPConst.NS_PDFA_SCHEMA.equals(child.getNamespaceURI()) && VALUE_TYPE.equals(child.getName())) {
				return child.getPrefix();
			}
		}
		return null;
	}

	@Override
	protected String getValidNamespaceURI() {
		return XMPConst.NS_PDFA_SCHEMA;
	}

	@Override
	protected Set<String> getValidChildNames() {
		return validChildNames;
	}

	static {
		validChildNames.add(NAMESPACE_URI);
		validChildNames.add(PREFIX);
		validChildNames.add(PROPERTY);
		validChildNames.add(SCHEMA);
		validChildNames.add(VALUE_TYPE);
	}

}

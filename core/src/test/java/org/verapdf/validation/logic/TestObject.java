package org.verapdf.validation.logic;

import org.verapdf.model.baselayer.Object;
import org.verapdf.model.coslayer.CosDict;
import org.verapdf.model.pdlayer.PDAnnot;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Maksim Bezrukov
 */
public class TestObject implements Object {
	private String type;
	private String id;
	private List<CosDict> cosDicts;
	private List<PDAnnot> pdAnnots;

	@Override
	public Boolean isContextDependent() {
		return Boolean.FALSE;
	}

	public TestObject(String id, List<CosDict> cosDicts, List<PDAnnot> pdAnnots) {
		this.type = "Object";
		this.id = id;
		this.cosDicts = cosDicts;
		this.pdAnnots = pdAnnots;
	}

	@Override
	public List<String> getLinks() {
		List<String> res = new ArrayList<>();
		res.add("CosDict");
		res.add("PDAnnot");
		return res;
	}

	@Override
	public List<? extends Object> getLinkedObjects(String s) {
		switch (s) {
			case "CosDict":
				return cosDicts;
			case "PDAnnot":
				return pdAnnots;
			default:
				return new ArrayList<>();
		}
	}

	@Override
	public List<String> getSuperTypes() {
		return new ArrayList<>();
	}

	@Override
	public List<String> getProperties() {
		return new ArrayList<String>();
	}

	@Override
	public String getObjectType() {
		return type;
	}

	@Override
	public String getID() {
		return id;
	}
}

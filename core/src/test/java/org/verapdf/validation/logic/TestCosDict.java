package org.verapdf.validation.logic;

import org.verapdf.model.baselayer.Object;
import org.verapdf.model.coslayer.CosDict;
import org.verapdf.model.coslayer.CosInteger;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Maksim Bezrukov
 */
public class TestCosDict implements CosDict {

	private long size;
	private String type;
	private String id;

	private List<CosDict> cosDicts;
	private List<CosInteger> cosIntegers;
	private List<Object> objects;

	public TestCosDict(int size, String id, List<CosDict> cosDicts,
					   List<CosInteger> cosIntegers, List<Object> objects) {
		this.size = size;
		this.id = id;
		this.cosDicts = cosDicts;
		this.cosIntegers = cosIntegers;
		this.objects = objects;
		this.type = "CosDict";
	}

	@Override
	public Long getsize() {
		return Long.valueOf(size);
	}

	@Override
	public List<String> getLinks() {
		List<String> res = new ArrayList<>();
		res.add("keys");
		res.add("CosInteger");
		res.add("Object");
		return res;
	}

	@Override
	public List<? extends Object> getLinkedObjects(String s) {
		switch (s) {
			case "keys":
				return cosDicts;
			case "CosInteger":
				return cosIntegers;
			case "Object":
				return objects;
			default:
				return new ArrayList<>();
		}
	}

	@Override
	public List<String> getSuperTypes() {
		List<String> res = new ArrayList<>();
		res.add("CosObject");
		res.add("Object");
		return res;
	}

	@Override
	public List<String> getProperties() {
		List<String> res = new ArrayList<>();
		res.add("size");
		return res;
	}

	@Override
	public Boolean isContextDependent() {
		return Boolean.FALSE;
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

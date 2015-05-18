package org.verapdf.validation.logic;

import org.verapdf.model.baselayer.*;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.coslayer.CosInteger;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by bezrukov on 5/7/15.
 */
public class TestCosInteger implements CosInteger {

    private int value;
    private String type;
    private String id;

    public TestCosInteger(int value, String id) {
        this.value = value;
        this.id = id;
        this.type = "CosInteger";
    }

    @Override
    public String getstringValue() {
        return "" + value;
    }

    @Override
    public Integer getintValue() {
        return value;
    }

    @Override
    public BigDecimal getrealValue() {
        return new BigDecimal(value);
    }

    @Override
    public List<String> getLinks() {
        return new ArrayList<>();
    }

    @Override
    public List<? extends Object> getLinkedObjects(String s) {
        return null;
    }

    @Override
    public List<String> getSuperTypes() {
        List<String> res = new ArrayList<>();
        res.add("CosNumber");
        res.add("CosObject");
        res.add("Object");
        return res;
    }

    @Override
    public List<String> getProperties() {
        List<String> res = new ArrayList<>();
        res.add("stringValue");
        res.add("intValue");
        res.add("realValue");
        return res;
    }

    public Boolean isContextDependent(){
        return false;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public String getID() {
        return id;
    }
}

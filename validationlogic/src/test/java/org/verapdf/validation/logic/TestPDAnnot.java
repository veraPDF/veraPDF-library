package org.verapdf.validation.logic;

import org.verapdf.model.baselayer.*;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.pdlayer.PDAnnot;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bezrukov on 5/7/15.
 */
public class TestPDAnnot implements PDAnnot {
    private String Subtype;
    private String type;
    private String id;

    public TestPDAnnot(String subtype, String id) {
        this.Subtype = subtype;
        this.type = "PDAnnot";
        this.id = id;
    }

    public Boolean isContextDependent(){
        return false;
    }

    @Override
    public String getSubtype() {
        return Subtype;
    }

    @Override
    public List<String> getLinks() {
        return new ArrayList<>();
    }

    @Override
    public List<? extends Object> getLinkedObjects(String s) {
        return new ArrayList<>();
    }

    @Override
    public List<String> getSuperTypes() {
        List<String> res = new ArrayList<>();
        res.add("Object");
        res.add("PDObject");
        return res;
    }

    @Override
    public List<String> getProperties() {
        List<String> res = new ArrayList<>();
        res.add("Subtype");
        return res;
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

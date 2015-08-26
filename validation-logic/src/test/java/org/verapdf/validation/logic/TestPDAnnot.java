package org.verapdf.validation.logic;

import org.verapdf.model.baselayer.Object;
import org.verapdf.model.pdlayer.PDAnnot;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Maksim Bezrukov
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

    @Override
    public Boolean isContextDependent() {
        return Boolean.FALSE;
    }

    @Override
    public String getAP() {
      return "";
    }

    @Override
    public Double getCA() {
      return Double.valueOf(0.0);
    }

    @Override
    public Long getF() {
      return Long.valueOf(0L);
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
    public String getObjectType() {
        return type;
    }

    @Override
    public String getID() {
        return id;
    }
}

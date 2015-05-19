package org.verapdf.model.impl;

import org.verapdf.model.ModelHelper;
import org.verapdf.model.baselayer.Object;

import java.util.List;

/**
 * Created by Evgeniy Muravitskiy on 5/16/2015.
 * <p>
 *     Current class is implementation of {@link org.verapdf.model.baselayer.Object} and
 *     implements next methods: {@link GenericModelObject#getSuperTypes()}, {@link GenericModelObject#getProperties()},
 *     {@link GenericModelObject#getLinks()}, {@link GenericModelObject#getLinkedObjects(String)}. For
 *     implement this four methods using auxiliary class {@code org.verapdf.model.ModelHelper}
 *
 *     @see org.verapdf.model.ModelHelper
 */
public abstract class GenericModelObject implements org.verapdf.model.baselayer.Object {

    private Boolean contextDependent = false;

    /**
     * Try to return link for corresponding {@code link} or throws exception when corresponding link
     * not exists. For getting list of links for current object use {@link GenericModelObject#getLinks()}
     * method
     */
    @Override
    public List<? extends Object> getLinkedObjects(String link) {
        throw new IllegalAccessError(this.getType() + " has not access to this method or has not " + link + " link`.");
    }

    /**
     * @return list of links for current type of object
     */
    @Override
    public List<String> getLinks() {
        return ModelHelper.getListOfLinks(this.getType());
    }

    /**
     * @return list of properties for current type of object
     */
    @Override
    public List<String> getProperties() {
        return ModelHelper.getListOfProperties(this.getType());
    }

    /**
     * @return list of super types for current type of object
     */
    @Override
    public List<String> getSuperTypes() {
        return ModelHelper.getListOfSuperNames(this.getType());
    }

    /** It tells whether to take into account the context
     */
    @Override
    public Boolean isContextDependent() {
        return contextDependent;
    }
}

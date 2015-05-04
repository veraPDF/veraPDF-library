package org.verapdf.factory.cos;

import org.apache.pdfbox.cos.*;
import org.verapdf.model.coslayer.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Evgeniy Muravitskiy on 4/27/15.
 * <p>
 *     Factory class for transforming objects of pdfbox to objects for abstract model.
 * </p>
 */
public final class PBFactory {

    private final static Map<Class, PBCosFactory> undefinedType = new HashMap<Class, PBCosFactory>();
    private final static Map<Class, PBCosFactory> definedType = new HashMap<Class, PBCosFactory>();

    private PBFactory(){}

    /** This method transform some pdf box object to correspond object of abstract model
     */
    public static CosObject generateCosObject(COSBase object) {
        return generateCosObject(object, new ArrayList<CosObject>());
    }

    public static CosObject generateCosObject(COSBase object, List<CosObject> parents) {
        PBCosFactory factory = undefinedType.get(object.getClass());
        return generateCosObject(object, factory, parents);
    }

    /** This method transform some pdf box object to specific object of abstract model
     */
    public static CosObject generateCosObject(Class type, COSBase object) {
        return generateCosObject(type, object, new ArrayList<CosObject>());
    }

    public static CosObject generateCosObject(Class type, COSBase object, List<CosObject> parents) {
        PBCosFactory factory = definedType.get(type);
        return generateCosObject(object, factory, parents);
    }

    private static CosObject generateCosObject(COSBase object, PBCosFactory factory, List<CosObject> parents) {
        if (factory == null)
            throw new IllegalArgumentException("Unknown type of object: " + object.getClass() + ". Maybe current type" +
                    " not supported yet.");

        CosObject result = factory.generateCosObject(parents, object);
        if (result != null)
            return result;
        throw new IllegalArgumentException("Can`t transform PDFBOX`s object: " + object.getClass());
    }

    static {
        undefinedType.put(COSBase.class, new PBCosObjectFactory());
        undefinedType.put(COSObject.class, new PBCosIndirectFactory());
        undefinedType.put(COSDictionary.class, new PBCosDictFactory());
        undefinedType.put(COSStream.class, new PBCosStreamFactory());
        undefinedType.put(COSDocument.class, new PBCosDocumentFactory());
        undefinedType.put(COSArray.class, new PBCosArrayFactory());
        undefinedType.put(COSNull.class, new PBCosNullFactory());
        undefinedType.put(COSBoolean.class, new PBCosBoolFactory());
        undefinedType.put(COSString.class, new PBCosStringFactory());
        undefinedType.put(COSFloat.class, new PBCosRealFactory());
        undefinedType.put(COSInteger.class, new PBCosIntegerFactory());
        undefinedType.put(COSName.class, new PBCosNameFactory());
    }

    static {
        definedType.put(CosObject.class, new PBCosObjectFactory());
        definedType.put(CosIndirect.class, new PBCosIndirectFactory());
        definedType.put(CosDict.class, new PBCosDictFactory());
        definedType.put(CosStream.class, new PBCosStreamFactory());
        definedType.put(CosDocument.class, new PBCosDocumentFactory());
        definedType.put(CosTrailer.class, new PBCosTrailerFactory());
        definedType.put(CosArray.class, new PBCosArrayFactory());
        definedType.put(CosNull.class, new PBCosNullFactory());
        definedType.put(CosBool.class, new PBCosBoolFactory());
        definedType.put(CosString.class, new PBCosStringFactory());
        definedType.put(CosReal.class, new PBCosRealFactory());
        definedType.put(CosInteger.class, new PBCosIntegerFactory());
        definedType.put(CosName.class, new PBCosNameFactory());
    }
}
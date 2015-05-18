package org.verapdf.model.visitor.cos.pb;

import org.apache.pdfbox.cos.*;
import org.verapdf.model.impl.pb.cos.*;

import java.io.IOException;

/**
 * Created by Evgeniy on 5/16/2015.
 */
public final class PBCosVisitor implements ICOSVisitor {

    @Override
    public Object visitFromArray(COSArray obj) throws IOException {
        return new PBCosArray(obj);
    }

    @Override
    public Object visitFromBoolean(COSBoolean obj) throws IOException {
        return new PBCosBool(obj);
    }

    @Override
    public Object visitFromDictionary(COSDictionary obj) throws IOException {
        return new PBCosDict(obj);
    }

    @Override
    public Object visitFromDocument(COSDocument obj) throws IOException {
        return new PBCosDocument(obj);
    }

    @Override
    public Object visitFromFloat(COSFloat obj) throws IOException {
        return new PBCosReal(obj);
    }

    @Override
    public Object visitFromInt(COSInteger obj) throws IOException {
        return new PBCosInteger(obj);
    }

    @Override
    public Object visitFromName(COSName obj) throws IOException {
        return new PBCosName(obj);
    }

    @Override
    public Object visitFromNull(COSNull obj) throws IOException {
        return PBCosNull.NULL;
    }

    @Override
    public Object visitFromStream(COSStream obj) throws IOException {
        return new PBCosStream(obj);
    }

    @Override
    public Object visitFromString(COSString obj) throws IOException {
        return new PBCosString(obj, false);
    }

    public Object visitFromObject(COSObject obj) throws IOException {
        return new PBCosIndirect(obj, true);
    }
}
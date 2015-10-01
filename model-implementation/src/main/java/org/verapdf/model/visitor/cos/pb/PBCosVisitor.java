package org.verapdf.model.visitor.cos.pb;

import org.apache.pdfbox.cos.*;
import org.verapdf.model.impl.pb.cos.*;

import java.io.IOException;

/**
 * Implementation of {@link ICOSVisitor} which realize Visitor pattern. Implements singleton pattern.
 * Current implementation create objects of abstract model implementation for corresponding objects
 * of pdf box. Methods call from {@code <? extends COSBase>} objects using accept() method.
 *
 * @author Evgeniy Muravitskiy
 */
public final class PBCosVisitor implements ICOSVisitor {

    private static final PBCosVisitor visitor = new PBCosVisitor();

    private PBCosVisitor() {
        // Disable default constructor
    }

    public static PBCosVisitor getInstance() {
        return visitor;
    }

    /** {@inheritDoc} Create a PBCosArray for corresponding COSArray.
     * @return PBCosArray object
     * @see PBCosArray
     */
    @Override
    public Object visitFromArray(COSArray obj) throws IOException {
        return new PBCosArray(obj);
    }

    /** {@inheritDoc} Create a PBCosBool for corresponding COSBoolean.
     * @return PBCosBool object
     * @see PBCosBool
     */
    @Override
    public Object visitFromBoolean(COSBoolean obj) throws IOException {
        return PBCosBool.valueOf(obj);
    }

    /** {@inheritDoc} Create a PBCosFileSpecification COSDictionary if
	 * value of type key of {@code obj} is file specification. Otherwise
	 * create PBCosDict
     * @return PBCosFileSpecification or PBCosDict
     * @see PBCosDict
	 * @see PBCosFileSpecification
     */
    @Override
    public Object visitFromDictionary(COSDictionary obj) throws IOException {
		COSName type = obj.getCOSName(COSName.TYPE);
		boolean isFileSpec = type != null && COSName.FILESPEC.equals(type);
		return isFileSpec ? new PBCosFileSpecification(obj) : new PBCosDict(obj);
    }

    /** {@inheritDoc} Create a PBCosDocument for corresponding COSDocument.
     * @return PBCosDocument object
     * @see PBCosDocument
     */
    @Override
    public Object visitFromDocument(COSDocument obj) throws IOException {
        return new PBCosDocument(obj);
    }

    /** {@inheritDoc} Create a PBCosReal for corresponding COSFloat.
     * @return PBCosReal object
     * @see PBCosReal
     */
    @Override
    public Object visitFromFloat(COSFloat obj) throws IOException {
        return new PBCosReal(obj);
    }

    /** {@inheritDoc} Create a PBCosInteger for corresponding COSInteger.
     * @return PBCosInteger object
     * @see PBCosInteger
     */
    @Override
    public Object visitFromInt(COSInteger obj) throws IOException {
        return new PBCosInteger(obj);
    }

    /** {@inheritDoc} Create a PBCosName for corresponding COSName.
     * @return PBCosName object
     * @see PBCosName
     */
    @Override
    public Object visitFromName(COSName obj) throws IOException {
        return new PBCosName(obj);
    }

    /** {@inheritDoc} Create a PBCosNull for corresponding COSNull.
     * @return PBCosNull object
     * @see PBCosNull
     */
    @Override
    public Object visitFromNull(COSNull obj) throws IOException {
        return PBCosNull.getInstance();
    }

    /** {@inheritDoc} Create a PBCosStream for corresponding COSStream.
     * @return PBCosStream object
     * @see PBCosStream
     */
    @Override
    public Object visitFromStream(COSStream obj) throws IOException {
        return new PBCosStream(obj);
    }

    /** {@inheritDoc} Create a PBCosString for corresponding COSString.
     * @return PBCosString object
     * @see PBCosString
     */
    @Override
    public Object visitFromString(COSString obj) throws IOException {
        return new PBCosString(obj);
    }

    /** Notification of visiting in indirect object. Create a PBCosIndirect for corresponding
     * COSObject. {@code COSObject#accept(ICOSVisitor)} not accept indirect objects - its get
     * direct content and accepting it.
     * @return {@link PBCosIndirect} object
     * @see PBCosIndirect
     * @see COSObject#accept(ICOSVisitor)
     */
    public static Object visitFromObject(COSObject obj) {
        return new PBCosIndirect(obj);
    }
}
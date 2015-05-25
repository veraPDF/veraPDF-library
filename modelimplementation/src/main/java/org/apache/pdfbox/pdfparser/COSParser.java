/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.pdfbox.pdfparser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Set;
import java.util.TreeMap;
import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.pdfbox.cos.COSArray;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.cos.COSNull;
import org.apache.pdfbox.cos.COSNumber;
import org.apache.pdfbox.cos.COSObject;
import org.apache.pdfbox.cos.COSObjectKey;
import org.apache.pdfbox.cos.COSStream;
import org.apache.pdfbox.io.IOUtils;
import org.apache.pdfbox.pdfparser.XrefTrailerResolver.XRefType;
import org.apache.pdfbox.pdmodel.encryption.SecurityHandler;
import static org.apache.pdfbox.util.Charsets.ISO_8859_1;

/**
 * PDF-Parser which first reads startxref and xref tables in order to know valid objects and parse only these objects.
 *
 * First {@link PDFParser#parse()} or  {@link FDFParser#parse()} must be called before page objects
 * can be retrieved, e.g. {@link PDFParser#getPDDocument()}.
 *
 * This class is a much enhanced version of <code>QuickParser</code> presented in <a
 * href="https://issues.apache.org/jira/browse/PDFBOX-1104">PDFBOX-1104</a> by Jeremy Villalobos.
 */
public class COSParser extends BaseParser
{
    private static final String PDF_HEADER = "%PDF-";
    private static final String FDF_HEADER = "%FDF-";

    private static final String PDF_DEFAULT_VERSION = "1.4";
    private static final String FDF_DEFAULT_VERSION = "1.0";

    private static final char[] XREF_TABLE = new char[] { 'x', 'r', 'e', 'f' };
    private static final char[] XREF_STREAM = new char[] { '/', 'X', 'R', 'e', 'f' };
    private static final char[] STARTXREF = new char[] { 's','t','a','r','t','x','r','e','f' };

    private static final long MINIMUM_SEARCH_OFFSET = 6;

    private static final int X = 'x';

    /**
     * Only parse the PDF file minimally allowing access to basic information.
     */
    public static final String SYSPROP_PARSEMINIMAL =
            "org.apache.pdfbox.pdfparser.nonSequentialPDFParser.parseMinimal";

    /**
     * The range within the %%EOF marker will be searched.
     * Useful if there are additional characters after %%EOF within the PDF. 
     */
    public static final String SYSPROP_EOFLOOKUPRANGE =
            "org.apache.pdfbox.pdfparser.nonSequentialPDFParser.eofLookupRange";

    /**
     * How many trailing bytes to read for EOF marker.
     */
    private static final int DEFAULT_TRAIL_BYTECOUNT = 2048;
    /**
     * EOF-marker.
     */
    protected static final char[] EOF_MARKER = new char[] { '%', '%', 'E', 'O', 'F' };
    /**
     * obj-marker.
     */
    protected static final char[] OBJ_MARKER = new char[] { 'o', 'b', 'j' };

    private long trailerOffset;

    /**
     * file length.
     */
    protected long fileLen;

    /**
     * is parser using auto healing capacity ?
     */
    private boolean isLenient = true;

    protected boolean initialParseDone = false;
    /**
     * Contains all found objects of a brute force search.
     */
    private Map<COSObjectKey, Long> bfSearchCOSObjectKeyOffsets = null;
    private List<Long> bfSearchXRefTablesOffsets = null;
    private List<Long> bfSearchXRefStreamsOffsets = null;

    /**
     * The security handler.
     */
    protected SecurityHandler securityHandler = null;

    /**
     *  how many trailing bytes to read for EOF marker.
     */
    private int readTrailBytes = DEFAULT_TRAIL_BYTECOUNT;

    private static final Log LOG = LogFactory.getLog(COSParser.class);

    /**
     * Collects all Xref/trailer objects and resolves them into single
     * object using startxref reference. 
     */
    protected XrefTrailerResolver xrefTrailerResolver = new XrefTrailerResolver();


    /**
     * The prefix for the temp file being used. 
     */
    public static final String TMP_FILE_PREFIX = "tmpPDF";

    /**
     * Default constructor.
     */
    public COSParser()
    {
    }

    /**
     * Constructor.
     *
     * @param input inputStream of the pdf to be read
     * @throws IOException if something went wrong
     */
    public COSParser(InputStream input) throws IOException
    {
        super(input);
    }

    /**
     * Sets how many trailing bytes of PDF file are searched for EOF marker and 'startxref' marker. If not set we use
     * default value {@link #DEFAULT_TRAIL_BYTECOUNT}.
     *
     * <p>We check that new value is at least 16. However for practical use cases this value should not be lower than
     * 1000; even 2000 was found to not be enough in some cases where some trailing garbage like HTML snippets followed
     * the EOF marker.</p>
     *
     * <p>
     * In case system property {@link #SYSPROP_EOFLOOKUPRANGE} is defined this value will be set on initialization but
     * can be overwritten later.
     * </p>
     *
     * @param byteCount number of trailing bytes
     */
    public void setEOFLookupRange(int byteCount)
    {
        if (byteCount > 15)
        {
            readTrailBytes = byteCount;
        }
    }

    /**
     * Parses cross reference tables.
     *
     * @param startXRefOffset start offset of the first table
     * @return the trailer dictionary
     * @throws IOException if something went wrong
     */
    protected COSDictionary parseXref(long startXRefOffset) throws IOException
    {
        pdfSource.seek(startXRefOffset);
        long startXrefOffset = Math.max(0, parseStartXref());
        // check the startxref offset
        long fixedOffset = checkXRefOffset(startXrefOffset);
        if (fixedOffset > -1)
        {
            startXrefOffset = fixedOffset;
        }
        document.setStartXref(startXrefOffset);
        long prev = startXrefOffset;
        // ---- parse whole chain of xref tables/object streams using PREV reference
        while (prev > 0)
        {
            // seek to xref table
            pdfSource.seek(prev);

            // skip white spaces
            skipSpaces();
            // -- parse xref
            if (pdfSource.peek() == X)
            {
                // xref table and trailer
                // use existing parser to parse xref table
                parseXrefTable(prev);
                // parse the last trailer.
                trailerOffset = pdfSource.getOffset();
                // PDFBOX-1739 skip extra xref entries in RegisSTAR documents
                while (isLenient && pdfSource.peek() != 't')
                {
                    if (pdfSource.getOffset() == trailerOffset)
                    {
                        // warn only the first time
                        LOG.warn("Expected trailer object at position " + trailerOffset
                                + ", keep trying");
                    }
                    readLine();
                }
                if (!parseTrailer())
                {
                    throw new IOException("Expected trailer object at position: "
                            + pdfSource.getOffset());
                }
                COSDictionary trailer = xrefTrailerResolver.getCurrentTrailer();
                // check for a XRef stream, it may contain some object ids of compressed objects 
                if(trailer.containsKey(COSName.XREF_STM))
                {
                    int streamOffset = trailer.getInt(COSName.XREF_STM);
                    // check the xref stream reference
                    fixedOffset = checkXRefStreamOffset(streamOffset, false);
                    if (fixedOffset > -1 && fixedOffset != streamOffset)
                    {
                        streamOffset = (int)fixedOffset;
                        trailer.setInt(COSName.XREF_STM, streamOffset);
                    }
                    if (streamOffset > 0)
                    {
                        pdfSource.seek(streamOffset);
                        skipSpaces();
                        parseXrefObjStream(prev, false);
                    }
                    else
                    {
                        if(isLenient)
                        {
                            LOG.error("Skipped XRef stream due to a corrupt offset:"+streamOffset);
                        }
                        else
                        {
                            throw new IOException("Skipped XRef stream due to a corrupt offset:"+streamOffset);
                        }
                    }
                }
                prev = trailer.getInt(COSName.PREV);
                if (prev > 0)
                {
                    // check the xref table reference
                    fixedOffset = checkXRefOffset(prev);
                    if (fixedOffset > -1 && fixedOffset != prev)
                    {
                        prev = fixedOffset;
                        trailer.setLong(COSName.PREV, prev);
                    }
                }
            }
            else
            {
                // parse xref stream
                prev = parseXrefObjStream(prev, true);
                if (prev > 0)
                {
                    // check the xref table reference
                    fixedOffset = checkXRefOffset(prev);
                    if (fixedOffset > -1 && fixedOffset != prev)
                    {
                        prev = fixedOffset;
                        COSDictionary trailer = xrefTrailerResolver.getCurrentTrailer();
                        trailer.setLong(COSName.PREV, prev);
                    }
                }
            }
        }
        // ---- build valid xrefs out of the xref chain
        xrefTrailerResolver.setStartxref(startXrefOffset);
        COSDictionary trailer = xrefTrailerResolver.getTrailer();
        document.setTrailer(trailer);
        document.setIsXRefStream(XRefType.STREAM == xrefTrailerResolver.getXrefType());
        // check the offsets of all referenced objects
        checkXrefOffsets();
        // copy xref table
        document.addXRefTable(xrefTrailerResolver.getXrefTable());
        return trailer;
    }

    /**
     * Parses an xref object stream starting with indirect object id.
     *
     * @return value of PREV item in dictionary or <code>-1</code> if no such item exists
     */
    private long parseXrefObjStream(long objByteOffset, boolean isStandalone) throws IOException
    {
        // ---- parse indirect object head
        readObjectNumber();
        readGenerationNumber();
        readExpectedString(OBJ_MARKER, true);

        COSDictionary dict = parseCOSDictionary();
        COSStream xrefStream = parseCOSStream(dict);
        parseXrefStream(xrefStream, (int) objByteOffset, isStandalone);
        xrefStream.close();

        return dict.getLong(COSName.PREV);
    }

    /**
     * Looks for and parses startxref. We first look for last '%%EOF' marker (within last
     * {@link #DEFAULT_TRAIL_BYTECOUNT} bytes (or range set via {@link #setEOFLookupRange(int)}) and go back to find
     * <code>startxref</code>.
     *
     * @return the offset of StartXref
     * @throws IOException If something went wrong.
     */
    protected final long getStartxrefOffset() throws IOException
    {
        byte[] buf;
        long skipBytes;
        // read trailing bytes into buffer
        try
        {
            final int trailByteCount = (fileLen < readTrailBytes) ? (int) fileLen : readTrailBytes;
            buf = new byte[trailByteCount];
            skipBytes = fileLen - trailByteCount;
            pdfSource.seek(skipBytes);
            int off = 0;
            int readBytes;
            while (off < trailByteCount)
            {
                readBytes = pdfSource.read(buf, off, trailByteCount - off);
                // in order to not get stuck in a loop we check readBytes (this should never happen)
                if (readBytes < 1)
                {
                    throw new IOException(
                            "No more bytes to read for trailing buffer, but expected: "
                                    + (trailByteCount - off));
                }
                off += readBytes;
            }
        }
        finally
        {
            pdfSource.seek(0);
        }
        // find last '%%EOF'
        int bufOff = lastIndexOf(EOF_MARKER, buf, buf.length);
        if (bufOff < 0)
        {
            if (isLenient)
            {
                // in lenient mode the '%%EOF' isn't needed
                bufOff = buf.length;
                LOG.debug("Missing end of file marker '" + new String(EOF_MARKER) + "'");
            }
            else
            {
                throw new IOException("Missing end of file marker '" + new String(EOF_MARKER) + "'");
            }
        }
        // find last startxref preceding EOF marker
        bufOff = lastIndexOf(STARTXREF, buf, bufOff);
        long startXRefOffset = skipBytes + bufOff;

        if (bufOff < 0)
        {
            if (isLenient)
            {
                LOG.debug("Can't find offset for startxref");
                return -1;
            }
            else
            {
                throw new IOException("Missing 'startxref' marker.");
            }
        }
        return startXRefOffset;
    }

    /**
     * Searches last appearance of pattern within buffer. Lookup before _lastOff and goes back until 0.
     *
     * @param pattern pattern to search for
     * @param buf buffer to search pattern in
     * @param endOff offset (exclusive) where lookup starts at
     *
     * @return start offset of pattern within buffer or <code>-1</code> if pattern could not be found
     */
    protected int lastIndexOf(final char[] pattern, final byte[] buf, final int endOff)
    {
        final int lastPatternChOff = pattern.length - 1;

        int bufOff = endOff;
        int patOff = lastPatternChOff;
        char lookupCh = pattern[patOff];

        while (--bufOff >= 0)
        {
            if (buf[bufOff] == lookupCh)
            {
                if (--patOff < 0)
                {
                    // whole pattern matched
                    return bufOff;
                }
                // matched current char, advance to preceding one
                lookupCh = pattern[patOff];
            }
            else if (patOff < lastPatternChOff)
            {
                // no char match but already matched some chars; reset
                patOff = lastPatternChOff;
                lookupCh = pattern[patOff];
            }
        }
        return -1;
    }

    /**
     * Return true if parser is lenient. Meaning auto healing capacity of the parser are used.
     *
     * @return true if parser is lenient
     */
    public boolean isLenient()
    {
        return isLenient;
    }

    /**
     * Change the parser leniency flag.
     *
     * This method can only be called before the parsing of the file.
     *
     * @param lenient try to handle malformed PDFs.
     *
     */
    public void setLenient(boolean lenient)
    {
        if (initialParseDone)
        {
            throw new IllegalArgumentException("Cannot change leniency after parsing");
        }
        this.isLenient = lenient;
    }

    /**
     * Creates a unique object id using object number and object generation
     * number. (requires object number &lt; 2^31))
     */
    private long getObjectId(final COSObject obj)
    {
        return obj.getObjectNumber() << 32 | obj.getGenerationNumber();
    }

    /**
     * Adds all from newObjects to toBeParsedList if it is not an COSObject or
     * we didn't add this COSObject already (checked via addedObjects).
     */
    private void addNewToList(final Queue<COSBase> toBeParsedList,
                              final Collection<COSBase> newObjects, final Set<Long> addedObjects)
    {
        for (COSBase newObject : newObjects)
        {
            addNewToList(toBeParsedList, newObject, addedObjects);
        }
    }

    /**
     * Adds newObject to toBeParsedList if it is not an COSObject or we didn't
     * add this COSObject already (checked via addedObjects).
     */
    private void addNewToList(final Queue<COSBase> toBeParsedList, final COSBase newObject,
                              final Set<Long> addedObjects)
    {
        if (newObject instanceof COSObject)
        {
            final long objId = getObjectId((COSObject) newObject);
            if (!addedObjects.add(objId))
            {
                return;
            }
        }
        toBeParsedList.add(newObject);
    }

    /**
     * Will parse every object necessary to load a single page from the pdf document. We try our
     * best to order objects according to offset in file before reading to minimize seek operations.
     *
     * @param dict the COSObject from the parent pages.
     * @param excludeObjects dictionary object reference entries with these names will not be parsed
     *
     * @throws IOException if something went wrong
     */
    protected void parseDictObjects(COSDictionary dict, COSName... excludeObjects) throws IOException
    {
        // ---- create queue for objects waiting for further parsing
        final Queue<COSBase> toBeParsedList = new LinkedList<COSBase>();
        // offset ordered object map
        final TreeMap<Long, List<COSObject>> objToBeParsed = new TreeMap<Long, List<COSObject>>();
        // in case of compressed objects offset points to stmObj
        final Set<Long> parsedObjects = new HashSet<Long>();
        final Set<Long> addedObjects = new HashSet<Long>();

        addExcludedToList(excludeObjects, dict, parsedObjects);
        addNewToList(toBeParsedList, dict.getValues(), addedObjects);

        // ---- go through objects to be parsed
        while (!(toBeParsedList.isEmpty() && objToBeParsed.isEmpty()))
        {
            // -- first get all COSObject from other kind of objects and
            // put them in objToBeParsed; afterwards toBeParsedList is empty
            COSBase baseObj;
            while ((baseObj = toBeParsedList.poll()) != null)
            {
                if (baseObj instanceof COSDictionary)
                {
                    addNewToList(toBeParsedList, ((COSDictionary) baseObj).getValues(), addedObjects);
                }
                else if (baseObj instanceof COSArray)
                {
                    final Iterator<COSBase> arrIter = ((COSArray) baseObj).iterator();
                    while (arrIter.hasNext())
                    {
                        addNewToList(toBeParsedList, arrIter.next(), addedObjects);
                    }
                }
                else if (baseObj instanceof COSObject)
                {
                    COSObject obj = (COSObject) baseObj;
                    long objId = getObjectId(obj);
                    COSObjectKey objKey = new COSObjectKey(obj.getObjectNumber(), obj.getGenerationNumber());

                    if (!parsedObjects.contains(objId))
                    {
                        Long fileOffset = xrefTrailerResolver.getXrefTable().get(objKey);
                        // it is allowed that object references point to null,
                        // thus we have to test
                        if (fileOffset != null && fileOffset != 0)
                        {
                            if (fileOffset > 0)
                            {
                                objToBeParsed.put(fileOffset, Collections.singletonList(obj));
                            }
                            else
                            {
                                // negative offset means we have a compressed
                                // object within object stream;
                                // get offset of object stream
                                fileOffset = xrefTrailerResolver.getXrefTable().get(
                                        new COSObjectKey((int)-fileOffset, 0));
                                if ((fileOffset == null) || (fileOffset <= 0))
                                {
                                    throw new IOException(
                                            "Invalid object stream xref object reference for key '" + objKey + "': "
                                                    + fileOffset);
                                }

                                List<COSObject> stmObjects = objToBeParsed.get(fileOffset);
                                if (stmObjects == null)
                                {
                                    stmObjects = new ArrayList<COSObject>();
                                    objToBeParsed.put(fileOffset, stmObjects);
                                }
                                stmObjects.add(obj);
                            }
                        }
                        else
                        {
                            // NULL object
                            COSObject pdfObject = document.getObjectFromPool(objKey);
                            pdfObject.setObject(COSNull.NULL);
                        }
                    }
                }
            }

            // ---- read first COSObject with smallest offset
            // resulting object will be added to toBeParsedList
            if (objToBeParsed.isEmpty())
            {
                break;
            }

            for (COSObject obj : objToBeParsed.remove(objToBeParsed.firstKey()))
            {
                COSBase parsedObj = parseObjectDynamically(obj, false);

                obj.setObject(parsedObj);
                addNewToList(toBeParsedList, parsedObj, addedObjects);

                parsedObjects.add(getObjectId(obj));
            }
        }
    }

    // add objects not to be parsed to list of already parsed objects
    private void addExcludedToList(COSName[] excludeObjects, COSDictionary dict, final Set<Long> parsedObjects)
    {
        if (excludeObjects != null)
        {
            for (COSName objName : excludeObjects)
            {
                COSBase baseObj = dict.getItem(objName);
                if (baseObj instanceof COSObject)
                {
                    parsedObjects.add(getObjectId((COSObject) baseObj));
                }
            }
        }
    }

    /**
     * This will parse the next object from the stream and add it to the local state. 
     *
     * @param obj object to be parsed (we only take object number and generation number for lookup start offset)
     * @param requireExistingNotCompressedObj if <code>true</code> object to be parsed must not be contained within
     * compressed stream
     * @return the parsed object (which is also added to document object)
     *
     * @throws IOException If an IO error occurs.
     */
    protected final COSBase parseObjectDynamically(COSObject obj,
                                                   boolean requireExistingNotCompressedObj) throws IOException
    {
        return parseObjectDynamically(obj.getObjectNumber(),
                obj.getGenerationNumber(), requireExistingNotCompressedObj);
    }

    /**
     * This will parse the next object from the stream and add it to the local state. 
     * It's reduced to parsing an indirect object.
     *
     * @param objNr object number of object to be parsed
     * @param objGenNr object generation number of object to be parsed
     * @param requireExistingNotCompressedObj if <code>true</code> the object to be parsed must be defined in xref
     * (comment: null objects may be missing from xref) and it must not be a compressed object within object stream
     * (this is used to circumvent being stuck in a loop in a malicious PDF)
     *
     * @return the parsed object (which is also added to document object)
     *
     * @throws IOException If an IO error occurs.
     */
    protected COSBase parseObjectDynamically(long objNr, int objGenNr,
                                             boolean requireExistingNotCompressedObj) throws IOException
    {
        // ---- create object key and get object (container) from pool
        final COSObjectKey objKey = new COSObjectKey(objNr, objGenNr);
        final COSObject pdfObject = document.getObjectFromPool(objKey);

        if (pdfObject.getObject() == null)
        {
            // not previously parsed
            // ---- read offset or object stream object number from xref table
            Long offsetOrObjstmObNr = xrefTrailerResolver.getXrefTable().get(objKey);

            // sanity test to circumvent loops with broken documents
            if (requireExistingNotCompressedObj
                    && ((offsetOrObjstmObNr == null) || (offsetOrObjstmObNr <= 0)))
            {
                throw new IOException("Object must be defined and must not be compressed object: "
                        + objKey.getNumber() + ":" + objKey.getGeneration());
            }

            if (offsetOrObjstmObNr == null)
            {
                // not defined object -> NULL object (Spec. 1.7, chap. 3.2.9)
                pdfObject.setObject(COSNull.NULL);
            }
            else if (offsetOrObjstmObNr > 0)
            {
                // offset of indirect object in file
                parseFileObject(offsetOrObjstmObNr, objKey, objNr, objGenNr, pdfObject);
            }
            else
            {
                // xref value is object nr of object stream containing object to be parsed
                // since our object was not found it means object stream was not parsed so far
                parseObjectStream((int) -offsetOrObjstmObNr);
            }
        }
        return pdfObject.getObject();
    }

    private void parseFileObject(Long offsetOrObjstmObNr, final COSObjectKey objKey, long objNr, int objGenNr, final COSObject pdfObject) throws IOException
    {
        // ---- go to object start
        pdfSource.seek(offsetOrObjstmObNr);

        // ---- we must have an indirect object
        final long readObjNr = readObjectNumber();
        final int readObjGen = readGenerationNumber();
        readExpectedString(OBJ_MARKER, true);

        // ---- consistency check
        if ((readObjNr != objKey.getNumber()) || (readObjGen != objKey.getGeneration()))
        {
            throw new IOException("XREF for " + objKey.getNumber() + ":"
                    + objKey.getGeneration() + " points to wrong object: " + readObjNr
                    + ":" + readObjGen);
        }

        skipSpaces();
        COSBase pb = parseDirObject();
        String endObjectKey = readString();

        if (endObjectKey.equals(STREAM_STRING))
        {
            pdfSource.unread(endObjectKey.getBytes(ISO_8859_1));
            pdfSource.unread(' ');
            if (pb instanceof COSDictionary)
            {
                COSStream stream = parseCOSStream((COSDictionary) pb);

                if (securityHandler != null)
                {
                    securityHandler.decryptStream(stream, objNr, objGenNr);
                }
                pb = stream;
            }
            else
            {
                // this is not legal
                // the combination of a dict and the stream/endstream
                // forms a complete stream object
                throw new IOException("Stream not preceded by dictionary (offset: "
                        + offsetOrObjstmObNr + ").");
            }
            skipSpaces();
            endObjectKey = readLine();

            // we have case with a second 'endstream' before endobj
            if (!endObjectKey.startsWith(ENDOBJ_STRING) && endObjectKey.startsWith(ENDSTREAM_STRING))
            {
                endObjectKey = endObjectKey.substring(9).trim();
                if (endObjectKey.length() == 0)
                {
                    // no other characters in extra endstream line
                    // read next line
                    endObjectKey = readLine();
                }
            }
        }
        else if (securityHandler != null)
        {
            securityHandler.decrypt(pb, objNr, objGenNr);
        }

        pdfObject.setObject(pb);

        if (!endObjectKey.startsWith(ENDOBJ_STRING))
        {
            if (isLenient)
            {
                LOG.warn("Object (" + readObjNr + ":" + readObjGen + ") at offset "
                        + offsetOrObjstmObNr + " does not end with 'endobj' but with '"
                        + endObjectKey + "'");
            }
            else
            {
                throw new IOException("Object (" + readObjNr + ":" + readObjGen
                        + ") at offset " + offsetOrObjstmObNr
                        + " does not end with 'endobj' but with '" + endObjectKey + "'");
            }
        }
    }

    private void parseObjectStream(int objstmObjNr) throws IOException
    {
        final COSBase objstmBaseObj = parseObjectDynamically(objstmObjNr, 0, true);
        if (objstmBaseObj instanceof COSStream)
        {
            // parse object stream
            PDFObjectStreamParser parser = new PDFObjectStreamParser((COSStream) objstmBaseObj, document);
            parser.parse();
            parser.close();

            // get set of object numbers referenced for this object stream
            final Set<Long> refObjNrs = xrefTrailerResolver.getContainedObjectNumbers(objstmObjNr);

            // register all objects which are referenced to be contained in object stream
            for (COSObject next : parser.getObjects())
            {
                COSObjectKey stmObjKey = new COSObjectKey(next);
                if (refObjNrs.contains(stmObjKey.getNumber()))
                {
                    COSObject stmObj = document.getObjectFromPool(stmObjKey);
                    stmObj.setObject(next.getObject());
                }
            }
        }
    }

    private boolean inGetLength = false;

    /**
     * Returns length value referred to or defined in given object. 
     */
    private COSNumber getLength(final COSBase lengthBaseObj) throws IOException
    {
        if (lengthBaseObj == null)
        {
            return null;
        }

        if (inGetLength)
        {
            throw new IOException("Loop while reading length from " + lengthBaseObj);
        }

        COSNumber retVal = null;

        try
        {
            inGetLength = true;
            // maybe length was given directly
            if (lengthBaseObj instanceof COSNumber)
            {
                retVal = (COSNumber) lengthBaseObj;
            }
            // length in referenced object
            else if (lengthBaseObj instanceof COSObject)
            {
                COSObject lengthObj = (COSObject) lengthBaseObj;
                if (lengthObj.getObject() == null)
                {
                    // not read so far, keep current stream position
                    final long curFileOffset = pdfSource.getOffset();
                    parseObjectDynamically(lengthObj, true);
                    // reset current stream position
                    pdfSource.seek(curFileOffset);
                    if (lengthObj.getObject() == null)
                    {
                        throw new IOException("Length object content was not read.");
                    }
                }
                if (!(lengthObj.getObject() instanceof COSNumber))
                {
                    throw new IOException("Wrong type of referenced length object " + lengthObj
                            + ": " + lengthObj.getObject().getClass().getSimpleName());
                }
                retVal = (COSNumber) lengthObj.getObject();
            }
            else
            {
                throw new IOException("Wrong type of length object: "
                        + lengthBaseObj.getClass().getSimpleName());
            }
        }
        finally
        {
            inGetLength = false;
        }
        return retVal;
    }

    private static final int STREAMCOPYBUFLEN = 8192;
    private final byte[] streamCopyBuf = new byte[STREAMCOPYBUFLEN];

    /**
     * This will read a COSStream from the input stream using length attribute within dictionary. If
     * length attribute is a indirect reference it is first resolved to get the stream length. This
     * means we copy stream data without testing for 'endstream' or 'endobj' and thus it is no
     * problem if these keywords occur within stream. We require 'endstream' to be found after
     * stream data is read.
     *
     * @param dic dictionary that goes with this stream.
     *
     * @return parsed pdf stream.
     *
     * @throws IOException if an error occurred reading the stream, like problems with reading
     * length attribute, stream does not end with 'endstream' after data read, stream too short etc.
     */
    protected COSStream parseCOSStream(COSDictionary dic) throws IOException
    {
        final COSStream stream = document.createCOSStream(dic);
        OutputStream out = null;
        try
        {
            // read 'stream'; this was already tested in parseObjectsDynamically()
            readString();

            skipWhiteSpaces();

            /*
             * This needs to be dic.getItem because when we are parsing, the underlying object might still be null.
             */
            COSNumber streamLengthObj = getLength(dic.getItem(COSName.LENGTH));
            if (streamLengthObj == null)
            {
                if (isLenient)
                {
                    LOG.warn("The stream doesn't provide any stream length, using fallback readUntilEnd, at offset "
                            + pdfSource.getOffset());
                }
                else
                {
                    throw new IOException("Missing length for stream.");
                }
            }

            // get output stream to copy data to
            if (streamLengthObj != null && validateStreamLength(streamLengthObj.longValue()))
            {
                out = stream.createFilteredStream(streamLengthObj);
                readValidStream(out, streamLengthObj);
            }
            else
            {
                out = stream.createFilteredStream();
                readUntilEndStream(new EndstreamOutputStream(out));
            }
            String endStream = readString();
            if (endStream.equals("endobj") && isLenient)
            {
                LOG.warn("stream ends with 'endobj' instead of 'endstream' at offset "
                        + pdfSource.getOffset());
                // avoid follow-up warning about missing endobj
                pdfSource.unread(ENDOBJ);
            }
            else if (endStream.length() > 9 && isLenient && endStream.substring(0,9).equals(ENDSTREAM_STRING))
            {
                LOG.warn("stream ends with '" + endStream + "' instead of 'endstream' at offset "
                        + pdfSource.getOffset());
                // unread the "extra" bytes
                pdfSource.unread(endStream.substring(9).getBytes(ISO_8859_1));
            }
            else if (!endStream.equals(ENDSTREAM_STRING))
            {
                throw new IOException(
                        "Error reading stream, expected='endstream' actual='"
                                + endStream + "' at offset " + pdfSource.getOffset());
            }
        }
        finally
        {
            if (out != null)
            {
                out.close();
            }
        }
        return stream;
    }

    private void readValidStream(OutputStream out, COSNumber streamLengthObj) throws IOException
    {
        long remainBytes = streamLengthObj.longValue();
        while (remainBytes > 0)
        {
            final int chunk = (remainBytes > STREAMCOPYBUFLEN) ? STREAMCOPYBUFLEN : (int) remainBytes;
            final int readBytes = pdfSource.read(streamCopyBuf, 0, chunk);
            if (readBytes <= 0)
            {
                // shouldn't happen, the stream length has already been validated
                throw new IOException("read error at offset " + pdfSource.getOffset()
                        + ": expected " + chunk + " bytes, but read() returns " + readBytes);
            }
            out.write(streamCopyBuf, 0, readBytes);
            remainBytes -= readBytes;
        }
    }

    private boolean validateStreamLength(long streamLength) throws IOException
    {
        boolean streamLengthIsValid = true;
        long originOffset = pdfSource.getOffset();
        long expectedEndOfStream = originOffset + streamLength;
        if (expectedEndOfStream > fileLen)
        {
            streamLengthIsValid = false;
            LOG.error("The end of the stream is out of range, using workaround to read the stream, " +
                    "found " + originOffset + " but expected " + expectedEndOfStream);
        }
        else
        {
            pdfSource.seek(expectedEndOfStream);
            skipSpaces();
            if (!isString(ENDSTREAM))
            {
                streamLengthIsValid = false;
                LOG.error("The end of the stream doesn't point to the correct offset, using workaround to read the stream, " +
                        "found " + originOffset + " but expected " + expectedEndOfStream);
            }
            pdfSource.seek(originOffset);
        }
        return streamLengthIsValid;
    }

    /**
     * Check if the cross reference table/stream can be found at the current offset.
     *
     * @param startXRefOffset
     * @return the revised offset
     * @throws IOException
     */
    private long checkXRefOffset(long startXRefOffset) throws IOException
    {
        // repair mode isn't available in non-lenient mode
        if (!isLenient)
        {
            return startXRefOffset;
        }
        pdfSource.seek(startXRefOffset);
        if (pdfSource.peek() == X && isString(XREF_TABLE))
        {
            return startXRefOffset;
        }
        if (startXRefOffset > 0)
        {
            long fixedOffset = checkXRefStreamOffset(startXRefOffset, true);
            if (fixedOffset > -1)
            {
                return fixedOffset;
            }
        }
        // try to find a fixed offset
        return calculateXRefFixedOffset(startXRefOffset, false);
    }

    /**
     * Check if the cross reference stream can be found at the current offset.
     *
     * @param startXRefOffset the expected start offset of the XRef stream
     * @param checkOnly check only but don't repair the offset if set to true
     * @return the revised offset
     * @throws IOException if something went wrong
     */
    private long checkXRefStreamOffset(long startXRefOffset, boolean checkOnly) throws IOException
    {
        // repair mode isn't available in non-lenient mode
        if (!isLenient || startXRefOffset == 0)
        {
            return startXRefOffset;
        }
        // seek to offset-1 
        pdfSource.seek(startXRefOffset-1);
        int nextValue = pdfSource.read();
        // the first character has to be a whitespace, and then a digit
        if (isWhitespace(nextValue) && isDigit())
        {
            try
            {
                // it's a XRef stream
                readObjectNumber();
                readGenerationNumber();
                readExpectedString(OBJ_MARKER, true);
                pdfSource.seek(startXRefOffset);
                return startXRefOffset;
            }
            catch (IOException exception)
            {
                // there wasn't an object of a xref stream
                // try to repair the offset
                pdfSource.seek(startXRefOffset);
            }
        }
        // try to find a fixed offset
        return checkOnly ? -1 : calculateXRefFixedOffset(startXRefOffset, true);
    }

    /**
     * Try to find a fixed offset for the given xref table/stream.
     *
     * @param objectOffset the given offset where to look at
     * @param streamsOnly search for xref streams only
     * @return the fixed offset
     *
     * @throws IOException if something went wrong
     */
    private long calculateXRefFixedOffset(long objectOffset, boolean streamsOnly) throws IOException
    {
        if (objectOffset < 0)
        {
            LOG.error("Invalid object offset " + objectOffset + " when searching for a xref table/stream");
            return 0;
        }
        // start a brute force search for all xref tables and try to find the offset we are looking for
        long newOffset = bfSearchForXRef(objectOffset, streamsOnly);
        if (newOffset > -1)
        {
            LOG.debug("Fixed reference for xref table/stream " + objectOffset + " -> " + newOffset);
            return newOffset;
        }
        LOG.error("Can't find the object axref table/stream at offset " + objectOffset);
        return 0;
    }

    /**
     * Check the XRef table by dereferencing all objects and fixing the offset if necessary.
     *
     * @throws IOException if something went wrong.
     */
    private void checkXrefOffsets() throws IOException
    {
        // repair mode isn't available in non-lenient mode
        if (!isLenient)
        {
            return;
        }
        Map<COSObjectKey, Long> xrefOffset = xrefTrailerResolver.getXrefTable();
        if (xrefOffset != null)
        {
            boolean bruteForceSearch = false;
            for (Entry<COSObjectKey, Long> objectEntry : xrefOffset.entrySet())
            {
                COSObjectKey objectKey = objectEntry.getKey();
                Long objectOffset = objectEntry.getValue();
                // a negative offset number represents a object number itself
                // see type 2 entry in xref stream
                if (objectOffset != null && objectOffset >= 0
                        && !checkObjectKeys(objectKey, objectOffset))
                {
                    LOG.debug("Stop checking xref offsets as at least one couldn't be dereferenced");
                    bruteForceSearch = true;
                    break;
                }
            }
            if (bruteForceSearch)
            {
                bfSearchForObjects();
                if (bfSearchCOSObjectKeyOffsets != null && !bfSearchCOSObjectKeyOffsets.isEmpty())
                {
                    LOG.debug("Replaced read xref table with the results of a brute force search");
                    xrefOffset.putAll(bfSearchCOSObjectKeyOffsets);
                }
            }
        }
    }

    /**
     * Check if the given object can be found at the given offset.
     *
     * @param objectKey the object we are looking for
     * @param offset the offset where to look
     * @return returns true if the given object can be dereferenced at the given offset
     * @throws IOException if something went wrong
     */
    private boolean checkObjectKeys(COSObjectKey objectKey, long offset) throws IOException
    {
        // there can't be any object at the very beginning of a pdf
        if (offset < MINIMUM_SEARCH_OFFSET)
        {
            return false;
        }
        long objectNr = objectKey.getNumber();
        int objectGen = objectKey.getGeneration();
        long originOffset = pdfSource.getOffset();
        pdfSource.seek(offset);
        String objectString = createObjectString(objectNr, objectGen);
        try
        {
            if (isString(objectString.getBytes(ISO_8859_1)))
            {
                // everything is ok, return origin object key
                pdfSource.seek(originOffset);
                return true;
            }
        }
        catch (IOException exception)
        {
            // Swallow the exception, obviously there isn't any valid object number
        }
        finally
        {
            pdfSource.seek(originOffset);
        }
        // no valid object number found
        return false;
    }
    /**
     * Create a string for the given object id.
     *
     * @param objectID the object id
     * @param genID the generation id
     * @return the generated string
     */
    private String createObjectString(long objectID, int genID)
    {
        return Long.toString(objectID) + " " + Integer.toString(genID) + " obj";
    }

    /**
     * Brute force search for every object in the pdf.
     *
     * @throws IOException if something went wrong
     */
    private void bfSearchForObjects() throws IOException
    {
        if (bfSearchCOSObjectKeyOffsets == null)
        {
            bfSearchCOSObjectKeyOffsets = new HashMap<COSObjectKey, Long>();
            long originOffset = pdfSource.getOffset();
            long currentOffset = MINIMUM_SEARCH_OFFSET;
            String objString = " obj";
            char[] string = objString.toCharArray();
            do
            {
                pdfSource.seek(currentOffset);
                if (isString(string))
                {
                    long tempOffset = currentOffset - 1;
                    pdfSource.seek(tempOffset);
                    int genID = pdfSource.peek();
                    // is the next char a digit?
                    if (isDigit(genID))
                    {
                        genID -= 48;
                        tempOffset--;
                        pdfSource.seek(tempOffset);
                        if (isSpace())
                        {
                            while (tempOffset > MINIMUM_SEARCH_OFFSET && isSpace())
                            {
                                pdfSource.seek(--tempOffset);
                            }
                            int length = 0;
                            while (tempOffset > MINIMUM_SEARCH_OFFSET && isDigit())
                            {
                                pdfSource.seek(--tempOffset);
                                length++;
                            }
                            if (length > 0)
                            {
                                pdfSource.read();
                                byte[] objIDBytes = pdfSource.readFully(length);
                                String objIdString = new String(objIDBytes, 0,
                                        objIDBytes.length, ISO_8859_1);
                                Long objectID;
                                try
                                {
                                    objectID = Long.valueOf(objIdString);
                                }
                                catch (NumberFormatException exception)
                                {
                                    objectID = null;
                                }
                                if (objectID != null)
                                {
                                    bfSearchCOSObjectKeyOffsets.put(new COSObjectKey(objectID, genID), tempOffset+1);
                                }
                            }
                        }
                    }
                }
                currentOffset++;
            }
            while (!pdfSource.isEOF());
            // reestablish origin position
            pdfSource.seek(originOffset);
        }
    }

    /**
     * Search for the offset of the given xref table/stream among those found by a brute force search.
     *
     * @param streamsOnly search for xref streams only
     * @return the offset of the xref entry
     * @throws IOException if something went wrong
     */
    private long bfSearchForXRef(long xrefOffset, boolean streamsOnly) throws IOException
    {
        long newOffset = -1;
        long newOffsetTable = -1;
        long newOffsetStream = -1;
        if (!streamsOnly)
        {
            bfSearchForXRefTables();
        }
        bfSearchForXRefStreams();
        if (!streamsOnly && bfSearchXRefTablesOffsets != null)
        {
            // TODO to be optimized, this won't work in every case
            newOffsetTable = searchNearestValue(bfSearchXRefTablesOffsets, xrefOffset);
        }
        if (bfSearchXRefStreamsOffsets != null)
        {
            // TODO to be optimized, this won't work in every case
            newOffsetStream = searchNearestValue(bfSearchXRefStreamsOffsets, xrefOffset);
        }
        // choose the nearest value
        if (newOffsetTable > -1 && newOffsetStream > -1)
        {
            long differenceTable = xrefOffset - newOffsetTable;
            long differenceStream = xrefOffset - newOffsetStream;
            if (Math.abs(differenceTable) > Math.abs(differenceStream))
            {
                newOffset = differenceStream;
                bfSearchXRefStreamsOffsets.remove(newOffsetStream);
            }
            else
            {
                newOffset = differenceTable;
                bfSearchXRefTablesOffsets.remove(newOffsetTable);
            }
        }
        else if (newOffsetTable > -1)
        {
            newOffset = newOffsetTable;
            bfSearchXRefTablesOffsets.remove(newOffsetTable);
        }
        else if (newOffsetStream > -1)
        {
            newOffset = newOffsetStream;
            bfSearchXRefStreamsOffsets.remove(newOffsetStream);
        }
        return newOffset;
    }

    private long searchNearestValue(List<Long> values, long offset)
    {
        long newValue = -1;
        long currentDifference = -1;
        int currentOffsetIndex = -1;
        int numberOfOffsets = values.size();
        // find the nearest value
        for (int i = 0; i < numberOfOffsets; i++)
        {
            long newDifference = offset - values.get(i);
            // find the nearest offset
            if (currentDifference == -1
                    || (Math.abs(currentDifference) > Math.abs(newDifference)))
            {
                currentDifference = newDifference;
                currentOffsetIndex = i;
            }
        }
        if (currentOffsetIndex > -1)
        {
            newValue = values.get(currentOffsetIndex);
        }
        return newValue;
    }
    /**
     * Brute force search for all xref entries (tables).
     *
     * @throws IOException if something went wrong
     */
    private void bfSearchForXRefTables() throws IOException
    {
        if (bfSearchXRefTablesOffsets == null)
        {
            // a pdf may contain more than one xref entry
            bfSearchXRefTablesOffsets = new Vector<Long>();
            long originOffset = pdfSource.getOffset();
            pdfSource.seek(MINIMUM_SEARCH_OFFSET);
            // search for xref tables
            while (!pdfSource.isEOF())
            {
                if (isString(XREF_TABLE))
                {
                    long newOffset = pdfSource.getOffset();
                    pdfSource.seek(newOffset - 1);
                    // ensure that we don't read "startxref" instead of "xref"
                    if (isWhitespace())
                    {
                        bfSearchXRefTablesOffsets.add(newOffset);
                    }
                    pdfSource.seek(newOffset + 4);
                }
                pdfSource.read();
            }
            pdfSource.seek(originOffset);
        }
    }

    /**
     * Brute force search for all /XRef entries (streams).
     *
     * @throws IOException if something went wrong
     */
    private void bfSearchForXRefStreams() throws IOException
    {
        if (bfSearchXRefStreamsOffsets == null)
        {
            // a pdf may contain more than one /XRef entry
            bfSearchXRefStreamsOffsets = new Vector<Long>();
            long originOffset = pdfSource.getOffset();
            pdfSource.seek(MINIMUM_SEARCH_OFFSET);
            // search for XRef streams
            String objString = " obj";
            char[] string = objString.toCharArray();
            while (!pdfSource.isEOF())
            {
                if (isString(XREF_STREAM))
                {
                    // search backwards for the beginning of the stream
                    long newOffset = -1;
                    long xrefOffset = pdfSource.getOffset();
                    boolean objFound = false;
                    for (int i = 1; i < 30 && !objFound; i++)
                    {
                        long currentOffset = xrefOffset - (i * 10);
                        if (currentOffset > 0)
                        {
                            pdfSource.seek(currentOffset);
                            for (int j = 0; j < 10; j++)
                            {
                                if (isString(string))
                                {
                                    long tempOffset = currentOffset - 1;
                                    pdfSource.seek(tempOffset);
                                    int genID = pdfSource.peek();
                                    // is the next char a digit?
                                    if (isDigit(genID))
                                    {
                                        genID -= 48;
                                        tempOffset--;
                                        pdfSource.seek(tempOffset);
                                        if (isSpace())
                                        {
                                            int length = 0;
                                            pdfSource.seek(--tempOffset);
                                            while (tempOffset > MINIMUM_SEARCH_OFFSET && isDigit())
                                            {
                                                pdfSource.seek(--tempOffset);
                                                length++;
                                            }
                                            if (length > 0)
                                            {
                                                pdfSource.read();
                                                newOffset = pdfSource.getOffset();
                                            }
                                        }
                                    }
                                    LOG.debug("Fixed reference for xref stream " + xrefOffset
                                            + " -> " + newOffset);
                                    objFound = true;
                                    break;
                                }
                                else
                                {
                                    currentOffset++;
                                    pdfSource.read();
                                }
                            }
                        }
                    }
                    if (newOffset > -1)
                    {
                        bfSearchXRefStreamsOffsets.add(newOffset);
                    }
                    pdfSource.seek(xrefOffset + 5);
                }
                pdfSource.read();
            }
            pdfSource.seek(originOffset);
        }
    }

    /**
     * Rebuild the trailer dictionary if startxref can't be found.
     *
     * @return the rebuild trailer dictionary
     *
     * @throws IOException if something went wrong
     */
    protected final COSDictionary rebuildTrailer() throws IOException
    {
        COSDictionary trailer = null;
        bfSearchForObjects();
        if (bfSearchCOSObjectKeyOffsets != null)
        {
            xrefTrailerResolver.nextXrefObj(0, XRefType.TABLE);
            for (COSObjectKey objectKey : bfSearchCOSObjectKeyOffsets.keySet())
            {
                xrefTrailerResolver.setXRef(objectKey, bfSearchCOSObjectKeyOffsets.get(objectKey));
            }
            xrefTrailerResolver.setStartxref(0);
            trailer = xrefTrailerResolver.getTrailer();
            getDocument().setTrailer(trailer);
            // search for the different parts of the trailer dictionary 
            for(COSObjectKey key : bfSearchCOSObjectKeyOffsets.keySet())
            {
                Long offset = bfSearchCOSObjectKeyOffsets.get(key);
                pdfSource.seek(offset);
                readObjectNumber();
                readGenerationNumber();
                readExpectedString(OBJ_MARKER, true);
                try
                {
                    COSDictionary dictionary = parseCOSDictionary();
                    if (dictionary != null)
                    {
                        // document catalog
                        if (COSName.CATALOG.equals(dictionary.getCOSName(COSName.TYPE)))
                        {
                            trailer.setItem(COSName.ROOT, document.getObjectFromPool(key));
                        }
                        // info dictionary
                        else if (dictionary.containsKey(COSName.TITLE)
                                || dictionary.containsKey(COSName.AUTHOR)
                                || dictionary.containsKey(COSName.SUBJECT)
                                || dictionary.containsKey(COSName.KEYWORDS)
                                || dictionary.containsKey(COSName.CREATOR)
                                || dictionary.containsKey(COSName.PRODUCER)
                                || dictionary.containsKey(COSName.CREATION_DATE))
                        {
                            trailer.setItem(COSName.INFO, document.getObjectFromPool(key));
                        }
                        // TODO encryption dictionary
                    }
                }
                catch(IOException exception)
                {
                    LOG.debug("Skipped object "+key+", either it's corrupt or not a dictionary");
                }
            }
        }
        return trailer;
    }

    /**
     * This will parse the startxref section from the stream.
     * The startxref value is ignored.
     *
     * @return the startxref value or -1 on parsing error
     * @throws IOException If an IO error occurs.
     */
    private long parseStartXref() throws IOException
    {
        long startXref = -1;
        if (isString(STARTXREF))
        {
            readString();
            skipSpaces();
            // This integer is the byte offset of the first object referenced by the xref or xref stream
            startXref = readLong();
        }
        return startXref;
    }

    /**
     * Checks if the given string can be found at the current offset.
     *
     * @param string the bytes of the string to look for
     * @return true if the bytes are in place, false if not
     * @throws IOException if something went wrong
     */
    private boolean isString(byte[] string) throws IOException
    {
        boolean bytesMatching = false;
        if (pdfSource.peek() == string[0])
        {
            int length = string.length;
            byte[] bytesRead = new byte[length];
            int numberOfBytes = pdfSource.read(bytesRead, 0, length);
            while (numberOfBytes < length)
            {
                int readMore = pdfSource.read(bytesRead, numberOfBytes, length - numberOfBytes);
                if (readMore < 0)
                {
                    break;
                }
                numberOfBytes += readMore;
            }
            if (Arrays.equals(string, bytesRead))
            {
                bytesMatching = true;
            }
            pdfSource.unread(bytesRead, 0, numberOfBytes);
        }
        return bytesMatching;
    }

    /**
     * Checks if the given string can be found at the current offset.
     *
     * @param string the bytes of the string to look for
     * @return true if the bytes are in place, false if not
     * @throws IOException if something went wrong
     */
    private boolean isString(char[] string) throws IOException
    {
        boolean bytesMatching = true;
        long originOffset = pdfSource.getOffset();
        for (char c : string)
        {
            if (pdfSource.read() != c)
            {
                bytesMatching = false;
            }
        }
        pdfSource.seek(originOffset);
        return bytesMatching;
    }

    /**
     * This will parse the trailer from the stream and add it to the state.
     *
     * @return false on parsing error
     * @throws IOException If an IO error occurs.
     */
    private boolean parseTrailer() throws IOException
    {
        if(pdfSource.peek() != 't')
        {
            return false;
        }
        //read "trailer"
        long currentOffset = pdfSource.getOffset();
        String nextLine = readLine();
        if( !nextLine.trim().equals( "trailer" ) )
        {
            // in some cases the EOL is missing and the trailer immediately
            // continues with "<<" or with a blank character
            // even if this does not comply with PDF reference we want to support as many PDFs as possible
            // Acrobat reader can also deal with this.
            if (nextLine.startsWith("trailer"))
            {
                // we can't just unread a portion of the read data as we don't know if the EOL consist of 1 or 2 bytes
                int len = "trailer".length();
                // jump back right after "trailer"
                pdfSource.seek(currentOffset + len);
            }
            else
            {
                return false;
            }
        }

        // in some cases the EOL is missing and the trailer continues with " <<"
        // even if this does not comply with PDF reference we want to support as many PDFs as possible
        // Acrobat reader can also deal with this.
        skipSpaces();

        COSDictionary parsedTrailer = parseCOSDictionary();
        xrefTrailerResolver.setTrailer( parsedTrailer );

        skipSpaces();
        return true;
    }

    /**
     * Parse the header of a pdf.
     *
     * @return true if a PDF header was found
     * @throws IOException if something went wrong
     */
    protected boolean parsePDFHeader() throws IOException
    {
        return parseHeader(PDF_HEADER, PDF_DEFAULT_VERSION);
    }

    /**
     * Parse the header of a fdf.
     *
     * @return true if a FDF header was found
     * @throws IOException if something went wrong
     */
    protected boolean parseFDFHeader() throws IOException
    {
        return parseHeader(FDF_HEADER, FDF_DEFAULT_VERSION);
    }

    // rewritten by Evgeniy Muravitskiy
    private boolean parseHeader(String headerMarker, String defaultVersion) throws IOException
    {
        // read first line
        String header = readLine();
        // some pdf-documents are broken and the pdf-version is in one of the following lines
        if (!header.contains(headerMarker))
        {
            if (header.contains(headerMarker.substring(1))) {
                document.setNonValidHeader(true);
            }
            header = readLine();
            while (!header.contains(headerMarker) && !header.contains(headerMarker.substring(1)))
            {
                // if a line starts with a digit, it has to be the first one with data in it
                if ((header.length() > 0) && (Character.isDigit(header.charAt(0))))
                {
                    break;
                }
                header = readLine();
            }
        } else if (header.charAt(0) != '%') {
            document.setNonValidHeader(true);
        }

        // nothing found
        if (!header.contains(headerMarker))
        {
            pdfSource.seek(0);
            return false;
        }

        //sometimes there is some garbage in the header before the header
        //actually starts, so lets try to find the header first.
        int headerStart = header.indexOf( headerMarker );

        // greater than zero because if it is zero then there is no point of trimming
        if ( headerStart > 0 )
        {
            //trim off any leading characters
            header = header.substring( headerStart, header.length() );
        }

        // This is used if there is garbage after the header on the same line
        if (header.startsWith(headerMarker) && !header.matches(headerMarker + "\\d.\\d"))
        {
            if (header.length() < headerMarker.length() + 3)
            {
                // No version number at all, set to 1.4 as default
                header = headerMarker + defaultVersion;
                LOG.debug("No version found, set to " + defaultVersion + " as default.");
            }
            else
            {
                String headerGarbage = header.substring(headerMarker.length() + 3, header.length()) + "\n";
                header = header.substring(0, headerMarker.length() + 3);
                pdfSource.unread(headerGarbage.getBytes(ISO_8859_1));
            }
        }
        float headerVersion = -1;
        try
        {
            String[] headerParts = header.split("-");
            if (headerParts.length == 2)
            {
                headerVersion = Float.parseFloat(headerParts[1]);
            }
        }
        catch (NumberFormatException exception)
        {
            LOG.debug("Can't parse the header version.", exception);
        }
        if (headerVersion < 0)
        {
            throw new IOException( "Error getting header version: " + header);
        }
        document.setVersion(headerVersion);
        parseComment();
        // rewind
        pdfSource.seek(0);
        return true;
    }

    private void parseComment() throws IOException {
        String comment = readLine();

        if (comment.charAt(0) != '%') {
            document.setNonValidCommentStart(true);
        }

        Integer pos = comment.indexOf('%') > -1 ? comment.indexOf('%') + 1 : 0;
        if (comment.substring(pos).trim().length() < 4) {
            document.setNonValidCommentLength(true);
        }

        Integer repetition = Math.min(4, comment.substring(pos).length());
        for (int i = 0; i < repetition; i++, pos++) {
            if ((int)comment.charAt(pos) < 128) {
                document.setNonValidCommentContent(true);
                break;
            }
        }
    }

    /**
     * This will parse the xref table from the stream and add it to the state
     * The XrefTable contents are ignored.
     * @param startByteOffset the offset to start at
     * @return false on parsing error
     * @throws IOException If an IO error occurs.
     */
    protected boolean parseXrefTable(long startByteOffset) throws IOException
    {
        if(pdfSource.peek() != 'x')
        {
            return false;
        }
        String xref = readString();
        if( !xref.trim().equals( "xref" ) )
        {
            return false;
        }

        // check for trailer after xref
        String str = readString();
        byte[] b = str.getBytes(ISO_8859_1);
        pdfSource.unread(b, 0, b.length);

        // signal start of new XRef
        xrefTrailerResolver.nextXrefObj( startByteOffset, XRefType.TABLE );

        if (str.startsWith("trailer"))
        {
            LOG.warn("skipping empty xref table");
            return false;
        }

        // Xref tables can have multiple sections. Each starts with a starting object id and a count.
        while(true)
        {
            // first obj id
            long currObjID = readObjectNumber();

            // the number of objects in the xref table
            long count = readLong();

            skipSpaces();
            for(int i = 0; i < count; i++)
            {
                if(pdfSource.isEOF() || isEndOfName((char)pdfSource.peek()))
                {
                    break;
                }
                if(pdfSource.peek() == 't')
                {
                    break;
                }
                //Ignore table contents
                String currentLine = readLine();
                String[] splitString = currentLine.split("\\s");
                if (splitString.length < 3)
                {
                    LOG.warn("invalid xref line: " + currentLine);
                    break;
                }
                /* This supports the corrupt table as reported in
                 * PDFBOX-474 (XXXX XXX XX n) */
                if(splitString[splitString.length-1].equals("n"))
                {
                    try
                    {
                        int currOffset = Integer.parseInt(splitString[0]);
                        int currGenID = Integer.parseInt(splitString[1]);
                        COSObjectKey objKey = new COSObjectKey(currObjID, currGenID);
                        xrefTrailerResolver.setXRef(objKey, currOffset);
                    }
                    catch(NumberFormatException e)
                    {
                        throw new IOException(e);
                    }
                }
                else if(!splitString[2].equals("f"))
                {
                    throw new IOException("Corrupt XRefTable Entry - ObjID:" + currObjID);
                }
                currObjID++;
                skipSpaces();
            }
            skipSpaces();
            if (!isDigit())
            {
                break;
            }
        }
        return true;
    }

    /**
     * Fills XRefTrailerResolver with data of given stream.
     * Stream must be of type XRef.
     * @param stream the stream to be read
     * @param objByteOffset the offset to start at
     * @param isStandalone should be set to true if the stream is not part of a hybrid xref table
     * @throws IOException if there is an error parsing the stream
     */
    private void parseXrefStream(COSStream stream, long objByteOffset, boolean isStandalone) throws IOException
    {
        // the cross reference stream of a hybrid xref table will be added to the existing one
        // and we must not override the offset and the trailer
        if ( isStandalone )
        {
            xrefTrailerResolver.nextXrefObj( objByteOffset, XRefType.STREAM );
            xrefTrailerResolver.setTrailer( stream );
        }
        PDFXrefStreamParser parser = new PDFXrefStreamParser( stream, document, xrefTrailerResolver );
        parser.parse();
        parser.close();
    }

    /**
     * This will get the document that was parsed.  parse() must be called before this is called.
     * When you are done with this document you must call close() on it to release
     * resources.
     *
     * @return The document that was parsed.
     *
     * @throws IOException If there is an error getting the document.
     */
    public COSDocument getDocument() throws IOException
    {
        if( document == null )
        {
            throw new IOException( "You must call parse() before calling getDocument()" );
        }
        return document;
    }

    /**
     * Create a temporary file with the input stream. The caller must take care
     * to delete this file at end of the parse method.
     *
     * @param input
     * @return the temporary file
     * @throws IOException If something went wrong.
     */
    File createTmpFile(InputStream input) throws IOException
    {
        FileOutputStream fos = null;
        try
        {
            File tmpFile = File.createTempFile(TMP_FILE_PREFIX, ".pdf");
            fos = new FileOutputStream(tmpFile);
            IOUtils.copy(input, fos);
            return tmpFile;
        }
        finally
        {
            IOUtils.closeQuietly(input);
            IOUtils.closeQuietly(fos);
        }
    }

    /**
     * Parse the values of the trailer dictionary and return the root object.
     *
     * @param trailer The trailer dictionary.
     * @return The parsed root object.
     * @throws IOException If an IO error occurs or if the root object is
     * missing in the trailer dictionary.
     */
    protected COSBase parseTrailerValuesDynamically(COSDictionary trailer) throws IOException
    {
        // PDFBOX-1557 - ensure that all COSObject are loaded in the trailer
        // PDFBOX-1606 - after securityHandler has been instantiated
        for (COSBase trailerEntry : trailer.getValues())
        {
            if (trailerEntry instanceof COSObject)
            {
                COSObject tmpObj = (COSObject) trailerEntry;
                parseObjectDynamically(tmpObj, false);
            }
        }
        // parse catalog or root object
        COSObject root = (COSObject) trailer.getItem(COSName.ROOT);
        if (root == null)
        {
            throw new IOException("Missing root object specification in trailer.");
        }
        return parseObjectDynamically(root, false);
    }

}
package org.verapdf.validation.profile.parser;

import org.verapdf.exceptions.validationprofileparser.MissedHashTagException;
import org.verapdf.exceptions.validationprofileparser.NullProfileException;
import org.verapdf.exceptions.validationprofileparser.WrongProfileEncodingException;

import javax.xml.stream.Location;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Formatter;

/**
 * Operations with validation profile's hash.
 *
 * @author Maksim Bezrukov
 */
public final class ValidationProfileSignatureChecker {
    
    private static final String HASH_NAME = "hash";
    private static final String HASH_OPEN_TAG = "<hash>";
    private static final String HASH_CLOSE_TAG = "</hash>";
    private static final String HASH_EMPTY_TAG = "<hash/>";
    private static final String UTF8 = "utf-8";

    private static final byte[] KEY_ARRAY = {-48, -78, -48, -75, -47, -128, -48, -80, -48, -97, -48, -108, -48, -92};

    private File profile;
    private int startOfHash = -1;
    private int endOfHash = -1;
    private String contentWithoutHash;
    private String currentHashAsString;
    private String realHashAsString;
    private byte[] realHashAsBytes;


    private ValidationProfileSignatureChecker(File file) {
        this.profile = file;
    }

    private void parseProfile() throws IOException, XMLStreamException, WrongProfileEncodingException {
        XMLInputFactory factory = XMLInputFactory.newInstance();
        XMLStreamReader streamReader = factory.createXMLStreamReader(new FileInputStream(profile));

        if (!UTF8.equalsIgnoreCase(streamReader.getEncoding())) {
            streamReader.close();
            throw new WrongProfileEncodingException("The given profile has not utf-8 encoding: " + profile.getCanonicalPath());
        }
        while (streamReader.hasNext()) {
            streamReader.next();
            if (streamReader.isStartElement() && HASH_NAME.equals(streamReader.getLocalName())) {
                Location location = streamReader.getLocation();
                startOfHash = location.getCharacterOffset();
                currentHashAsString = streamReader.getElementText().trim();
            }
            if (streamReader.isEndElement() && HASH_NAME.equals(streamReader.getLocalName())) {
                Location location = streamReader.getLocation();
                endOfHash = location.getCharacterOffset();
                if (endOfHash != startOfHash) {
                    startOfHash -= HASH_OPEN_TAG.length();
                } else {
                    startOfHash -= HASH_EMPTY_TAG.length();
                }
            }
        }
    }

    private byte[] getBytesForHash() throws IOException, MissedHashTagException {

        if (startOfHash < 0 || endOfHash < 0 || startOfHash > endOfHash) {
            throw new MissedHashTagException("Can not find well formed hash element in the validation profile at path: " + profile.getCanonicalPath());
        }
        byte[] bytesOfFile = Files.readAllBytes(profile.toPath());

        String fileString = new String(bytesOfFile, UTF8);
        contentWithoutHash = fileString.substring(0, startOfHash) + fileString.substring(endOfHash);

        return concatenateByteArrays(contentWithoutHash.getBytes(UTF8), KEY_ARRAY);

    }

    private static byte[] concatenateByteArrays(byte[] array1, byte[] array2) {
        byte[] res = Arrays.copyOf(array1, array1.length + array2.length);

        for (int i = 0; i < array2.length; ++i) {
            res[i + array1.length] = array2[i];
        }

        return res;
    }

    private static String byteArrayToHex(byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash) {
            formatter.format("%02x", b);
        }
        formatter.close();
        return formatter.toString();
    }

    private byte[] getSHA1(byte[] source) {
        byte[] res = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            res = md.digest(source);
        } catch (NoSuchAlgorithmException e) {
            // SHA-1 algorithm exists, so do nothing
            /**
             * cfw
             * TODO: Conversely if it doesn't exist (which is the only way this block is hit)
             * something is badly amiss, I'd suggest wrapping and throwing a runtime
             * IllegalStateException if this is ever hit.
             * e.g. throw new IllegalStateException("No SHA1 algorithm detected", e);
             */
        }

        return res;
    }

    /**
     * Generates hash code by using SHA-1 algorithm
     *
     * @return generated hash as hex String
     */
    public String getHashAsString() {
        return this.realHashAsString;
    }

    /**
     * Checks, if the given file's signature correct
     *
     * @return true if the file's signature is correct, and false if the file's signature is incorrect
     */
    public boolean isValidSignature() {
        return this.realHashAsString.equals(this.currentHashAsString);
    }

    /**
     * Generating the file's hash and signing the file
     *
     * @throws IOException - if an I/O error occurs reading from the file's path stream
     */
    public void signFile() throws IOException {
        String resultingContent = contentWithoutHash.substring(0, startOfHash) + HASH_OPEN_TAG +
                realHashAsString + HASH_CLOSE_TAG + contentWithoutHash.substring(startOfHash);

        try (FileOutputStream out = new FileOutputStream(profile)) {
            out.write(resultingContent.getBytes(UTF8));
        }
    }

    /**
     * Generating the file's hash and signing the file
     *
     * @param profile - the file for sign
     * @throws IOException                   - if an I/O error occurs reading from the file's path stream
     * @throws XMLStreamException            - error in parsing profile
     * @throws MissedHashTagException        - occurs when there is no hash element in the given profile
     * @throws WrongProfileEncodingException - occurs when the given profile has not utf8 encoding
     * @throws NullProfileException          - occurs when trying to create signature checker on null profile
     */
    public static ValidationProfileSignatureChecker newInstance(File profile) throws MissedHashTagException, XMLStreamException, IOException, WrongProfileEncodingException, NullProfileException {
        if (profile == null) {
            throw new NullProfileException("Null pointer to the profile is used for creating signature checker.");
        } else {

            ValidationProfileSignatureChecker checker = new ValidationProfileSignatureChecker(profile);
            checker.parseProfile();
            byte[] source = checker.getBytesForHash();
            checker.realHashAsBytes = checker.getSHA1(source);
            checker.realHashAsString = byteArrayToHex(checker.realHashAsBytes);
            return checker;
        }
    }
}

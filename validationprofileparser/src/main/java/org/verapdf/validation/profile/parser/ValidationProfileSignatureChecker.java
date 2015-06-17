package org.verapdf.validation.profile.parser;

import org.verapdf.exceptions.validationprofileparser.MissedHashTagException;
import org.verapdf.exceptions.validationprofileparser.NullProfileException;
import org.verapdf.exceptions.validationprofileparser.WrongProfileEncodingException;

import javax.xml.stream.Location;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.*;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Formatter;

/**
 * Operations with validation profile's hash.
 * Created by bezrukov on 6/8/15.
 *
 * @author Maksim Bezrukov
 * @version 1.0
 */
public class ValidationProfileSignatureChecker {

    private static final byte[] keyArray = {-48,-78,-48,-75,-47,-128,-48,-80,-48,-97,-48,-108,-48,-92};

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

        if (!"utf-8".equals(streamReader.getEncoding().toLowerCase())){
            streamReader.close();
            throw new WrongProfileEncodingException("The given profile has not utf-8 encoding: " + profile.getCanonicalPath());
        } else {

            while (streamReader.hasNext()) {
                streamReader.next();
                if (streamReader.isStartElement() && streamReader.getLocalName().equals("hash")) {
                    Location location = streamReader.getLocation();
                    startOfHash = location.getCharacterOffset();
                    currentHashAsString = streamReader.getElementText().trim();
                }
                if (streamReader.isEndElement() && streamReader.getLocalName().equals("hash")) {
                    Location location = streamReader.getLocation();
                    endOfHash = location.getCharacterOffset();
                    if (endOfHash != startOfHash) {
                        startOfHash -= "<hash>".length();
                    } else {
                        startOfHash -= "<hash/>".length();
                    }
                }
            }
        }
    }

    private byte[] getBytesForHash() throws IOException, MissedHashTagException {

        if (startOfHash < 0 || endOfHash < 0 || startOfHash > endOfHash) {
            throw new MissedHashTagException("Can not find well formed hash element in the validation profile at path: " + profile.getCanonicalPath());
        } else {
            byte[] bytesOfFile = Files.readAllBytes(profile.toPath());

            String fileString = new String(bytesOfFile, "utf8");
            contentWithoutHash = fileString.substring(0, startOfHash) + fileString.substring(endOfHash);

            return concatenateByteArrays(contentWithoutHash.getBytes("utf8"), keyArray);
        }

    }

    private static byte[] concatenateByteArrays(byte[] array1, byte[] array2){
        byte[] res = Arrays.copyOf(array1, array1.length + array2.length);

        for (int i = 0; i < array2.length; ++i){
            res[i + array1.length] = array2[i];
        }

        return res;
    }

    private static String byteArrayToHex(byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash) {
            formatter.format("%02x", b);
        }
        return formatter.toString();
    }

    private byte[] getSHA1(byte[] source) {
        byte[] res = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            res = md.digest(source);
        } catch (NoSuchAlgorithmException e) {
            // SHA-1 algorithm exists, so do nothing
        }

        return res;
    }

    /**
     * Generates hash code by using SHA-1 algorithm
     * @return generated hash as hex String
     */
    public String getHashAsString() {
        return this.realHashAsString;
    }

    /**
     * Checks, if the given file's signature correct
     * @return true if the file's signature is correct, and false if the file's signature is incorrect
     */
    public boolean isValidSignature() {
        return this.realHashAsString.equals(this.currentHashAsString);
    }

    /**
     * Generating the file's hash and signing the file
     * @throws IOException - if an I/O error occurs reading from the file's path stream
     */
    public void signFile() throws IOException {
        String resultingContent = contentWithoutHash.substring(0,startOfHash) + "<hash>" +
                realHashAsString + "</hash>" + contentWithoutHash.substring(startOfHash);

        FileOutputStream out = new FileOutputStream(profile);
        out.write(resultingContent.getBytes("utf8"));
    }

    /**
     * Generating the file's hash and signing the file
     * @param profile - the file for sign
     * @throws IOException - if an I/O error occurs reading from the file's path stream
     * @throws XMLStreamException - error in parsing profile
     * @throws MissedHashTagException - occurs when there is no hash element in the given profile
     * @throws WrongProfileEncodingException - occurs when the given profile has not utf8 encoding
     * @throws NullProfileException - occurs when trying to create signature checker on null profile
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

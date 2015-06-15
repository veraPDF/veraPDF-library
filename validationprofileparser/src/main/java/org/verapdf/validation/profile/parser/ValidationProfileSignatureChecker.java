package org.verapdf.validation.profile.parser;

import org.verapdf.exceptions.validationprofileparser.MissedHashTagException;

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
    private int startOfHash;
    private int endOfHash;
    private String contentWithoutHash;
    private String currentHashAsString;
    private String realHashAsString;
    private byte[] realHashAsBytes;


    public ValidationProfileSignatureChecker(File file) throws IOException, XMLStreamException, MissedHashTagException {
        this.profile = file;
        this.startOfHash = -1;
        this.endOfHash = -1;
        parseProfile();
        byte[] source = getBytesForHash();
        this.realHashAsBytes = getSHA1(source);
        realHashAsString = byteArrayToHex(this.realHashAsBytes);
    }

    private void parseProfile() throws FileNotFoundException, XMLStreamException {
        XMLInputFactory factory = XMLInputFactory.newInstance();
        XMLStreamReader streamReader = factory.createXMLStreamReader(new FileInputStream(profile));

        boolean check = true;
        //<hash>А тут помещен хэш</hash>

        while (streamReader.hasNext()) {
            streamReader.next();
            if (streamReader.isStartElement() && streamReader.getName().toString().equals("hash")) {
                Location location = streamReader.getLocation();
                startOfHash = location.getCharacterOffset();
                currentHashAsString = streamReader.getElementText().trim();
            }
            if (streamReader.isEndElement() && streamReader.getName().toString().equals("hash")) {
                check = false;
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

    private byte[] getSHA1(byte[] source) throws IOException {
        byte[] res = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            res = md.digest(source);
        } catch (NoSuchAlgorithmException e) {
            // SHA-1 algorithm exists, so do nothing
        }

        return res;
    }

    private void setSHA1() throws IOException {
        String resultingContent = contentWithoutHash.substring(0,startOfHash) + "<hash>" +
                realHashAsString + "</hash>" + contentWithoutHash.substring(startOfHash);

        FileOutputStream out = new FileOutputStream(profile);
        out.write(resultingContent.getBytes("utf8"));
    }


    /**
     * Generates hash code by using SHA-1 algorithm
     * @param file - file for which generates hash
     * @return generated hash
     * @throws IOException - if an I/O error occurs reading from the file's path stream
     * @throws XMLStreamException - error in parsing profile
     * @throws MissedHashTagException - occurs when there is no hash element in the given profile
     */
    public static byte[] getHash(File file) throws MissedHashTagException, XMLStreamException, IOException {
        ValidationProfileSignatureChecker checker = new ValidationProfileSignatureChecker(file);
        return checker.realHashAsBytes;
    }

    /**
     * Generates hash code by using SHA-1 algorithm
     * @param file - file for which generates hash
     * @return generated hash as hex String
     * @throws IOException - if an I/O error occurs reading from the file's path stream
     * @throws XMLStreamException - error in parsing profile
     * @throws MissedHashTagException - occurs when there is no hash element in the given profile
     */
    public static String getHashAsString(File file) throws IOException, XMLStreamException, MissedHashTagException {
        ValidationProfileSignatureChecker checker = new ValidationProfileSignatureChecker(file);
        return checker.realHashAsString;
    }

    /**
     * Checks, if the given file's signature correct
     * @param file - the file for validate
     * @return true if the file's signature is correct, and false if the file's signature is incorrect
     * @throws IOException - if an I/O error occurs reading from the file's path stream
     * @throws XMLStreamException - error in parsing profile
     * @throws MissedHashTagException - occurs when there is no hash element in the given profile
     */
    public static boolean isValidSignature(File file) throws IOException, XMLStreamException, MissedHashTagException {
        ValidationProfileSignatureChecker checker = new ValidationProfileSignatureChecker(file);
        return checker.realHashAsString.equals(checker.currentHashAsString);
    }

    /**
     * Generating the file's hash and signing the file
     * @param file - the file for sign
     * @throws IOException - if an I/O error occurs reading from the file's path stream
     * @throws XMLStreamException - error in parsing profile
     * @throws MissedHashTagException - occurs when there is no hash element in the given profile
     */
    public static void signFile(File file) throws IOException, XMLStreamException, MissedHashTagException {
        ValidationProfileSignatureChecker checker = new ValidationProfileSignatureChecker(file);
        checker.setSHA1();
    }
}

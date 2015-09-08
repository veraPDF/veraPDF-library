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
 * Class for handling the hash checking of Validation Profiles.
 *
 * @author Maksim Bezrukov
 */
public final class ValidationProfileSignatureChecker {

	private static final String HASH_NAME = "hash";
	private static final String HASH_OPEN_TAG = "<hash>";
	private static final String HASH_CLOSE_TAG = "</hash>";
	private static final String HASH_EMPTY_TAG = "<hash/>";
	private static final String UTF8 = "utf-8";

	private static final byte[] KEY_ARRAY = {-48, -78, -48, -75, -47, -128,
			-48, -80, -48, -97, -48, -108, -48, -92};

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

	/**
	 * @throws IOException
	 * @throws XMLStreamException
	 * @throws UnsupportedEncodingException if the profile isn't UTF-8 encoded.
	 */
	private void parseProfile() throws IOException, XMLStreamException {
		XMLInputFactory factory = XMLInputFactory.newInstance();
		try (FileInputStream profileStream = new FileInputStream(this.profile)) {
			XMLStreamReader streamReader = factory
					.createXMLStreamReader(profileStream);

			if (!UTF8.equalsIgnoreCase(streamReader.getEncoding())) {
				streamReader.close();
				throw new UnsupportedEncodingException(
						"Only UTF-8 encoded profiles are parsable, please check : "
								+ this.profile.getCanonicalPath());
			}
			while (streamReader.hasNext()) {
				streamReader.next();
				if (streamReader.isStartElement()
						&& HASH_NAME.equals(streamReader.getLocalName())) {
					Location location = streamReader.getLocation();
					this.startOfHash = location.getCharacterOffset();
					this.currentHashAsString = streamReader.getElementText()
							.trim();
				}
				if (streamReader.isEndElement()
						&& HASH_NAME.equals(streamReader.getLocalName())) {
					Location location = streamReader.getLocation();
					this.endOfHash = location.getCharacterOffset();
					if (this.endOfHash != this.startOfHash) {
						this.startOfHash -= HASH_OPEN_TAG.length();
					} else {
						this.startOfHash -= HASH_EMPTY_TAG.length();
					}
				}
			}
		}
	}

	private byte[] getBytesForHash() throws IOException, MissedHashTagException {

		if (this.startOfHash < 0 || this.endOfHash < 0
				|| this.startOfHash > this.endOfHash) {
			throw new MissedHashTagException(
					"Can not find well formed hash element in the validation profile at path: "
							+ this.profile.getCanonicalPath());
		}
		byte[] bytesOfFile = Files.readAllBytes(this.profile.toPath());

		String fileString = new String(bytesOfFile, UTF8);
		this.contentWithoutHash = fileString.substring(0, this.startOfHash)
				+ fileString.substring(this.endOfHash);

		return concatenateByteArrays(this.contentWithoutHash.getBytes(UTF8),
				KEY_ARRAY);

	}

	private static byte[] concatenateByteArrays(byte[] array1, byte[] array2) {
		byte[] res = Arrays.copyOf(array1, array1.length + array2.length);

		for (int i = 0; i < array2.length; ++i) {
			res[i + array1.length] = array2[i];
		}

		return res;
	}

	private static String byteArrayToHex(byte[] hash) {
		try (Formatter formatter = new Formatter()) {
			for (byte b : hash) {
				formatter.format("%02x", Byte.valueOf(b));
			}
			return formatter.toString();
		}
	}

	private static byte[] getSHA1(byte[] source) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-1");
			return md.digest(source);
		} catch (NoSuchAlgorithmException e) {
			// SHA-1 algorithm exists, so do nothing
			/**
			 * cfw Conversely if it doesn't exist (which is the only way this
			 * block is hit) something is badly amiss, I'd suggest wrapping and
			 * throwing a runtime IllegalStateException if this is ever hit.
			 * e.g. throw new
			 * IllegalStateException("No SHA1 algorithm detected", e);
			 */
			throw new IllegalStateException(
					"Required JRE SHA-1 algorithm not found", e);
		}
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
	 * @return true if the file's signature is correct, and false if the file's
	 * signature is incorrect
	 */
	public boolean isValidSignature() {
		return this.realHashAsString.equals(this.currentHashAsString);
	}

	/**
	 * Generating the file's hash and signing the file
	 *
	 * @throws IOException        if an I/O error occurs reading from the file's path stream
	 * @throws XMLStreamException error in parsing profile after signing it
	 */
	public void signFile() throws IOException, XMLStreamException {
		String resultingContent = this.contentWithoutHash.substring(0,
				this.startOfHash)
				+ HASH_OPEN_TAG
				+ this.realHashAsString
				+ HASH_CLOSE_TAG
				+ this.contentWithoutHash.substring(this.startOfHash);

		try (FileOutputStream out = new FileOutputStream(this.profile)) {
			out.write(resultingContent.getBytes(UTF8));
		}
		this.parseProfile();
	}

	/**
	 * Generating the file's hash and signing the file
	 *
	 * @param profile the file for sign
	 * @return a new {@link ValidationProfileSignatureChecker} instance
	 * @throws IOException                  if an I/O error occurs reading from the file's path stream
	 * @throws XMLStreamException           error in parsing profile
	 * @throws MissedHashTagException       occurs when there is no hash element in the given profile
	 * @throws UnsupportedEncodingException occurs when the given profile has not utf8 encoding
	 */
	public static ValidationProfileSignatureChecker newInstance(File profile)
			throws MissedHashTagException, XMLStreamException, IOException {
		if (profile == null) {
			throw new NullPointerException(
					"Null pointer to the profile is used for creating signature checker.");
		}
		ValidationProfileSignatureChecker checker = new ValidationProfileSignatureChecker(
				profile);
		checker.parseProfile();
		byte[] source = checker.getBytesForHash();
		checker.realHashAsBytes = ValidationProfileSignatureChecker
				.getSHA1(source);
		checker.realHashAsString = byteArrayToHex(checker.realHashAsBytes);
		return checker;
	}
}

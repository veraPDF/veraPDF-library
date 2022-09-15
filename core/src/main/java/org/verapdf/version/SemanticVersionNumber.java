/**
 * 
 */
package org.verapdf.version;

/**
 * Simple interface for a <a href="http://semver.org/">semantic version
 * number</a>. Currently ignores pre-release and build metadata 
 * 
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *         <a href="https://github.com/carlwilson">carlwilson AT github</a>
 * @version 0.1 Created 25 May 2017:21:49:36
 */
public interface SemanticVersionNumber extends Comparable<SemanticVersionNumber> {
	/**
	 * @return the version number as a String, that is major.minor.patch
	 */
	public String getVersionString();

	/**
	 * @return the int value of the major version number
	 */
	public int getMajor();

	/**
	 * @return the int value of the minor version number
	 */
	public int getMinor();

	/**
	 * @return the int value of the patch number
	 */
	public int getPatch();
}

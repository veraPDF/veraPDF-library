package org.verapdf.metadata.fixer.utils;

import java.io.OutputStream;

/**
 * Current enum describe validation state according to
 * validity of separated parts. This parts can separated
 * to 4 groups:
 * <ul>
 * <li>Valid - if document is valid according to profile</li>
 * <li>
 * Invalid metadata - if and only if according to specific
 * list invalid only metadata. This list describes rules
 * which provide difference between is metadata invalid
 * or not
 * </li>
 * <li>
 * Invalid structure - if and only if [нарушает] rules
 * other than metadata rules.
 * </li>
 * <li>
 * Invalid document - if [нарушает] as metadata rules
 * as other than metadata rules
 * </li>
 * </ul>
 * According to this status {@link MetadataFixerImpl}
 * run different handling of document.
 *
 * @author Evgeniy Muravitskiy
 * @see MetadataFixerImpl#fixAndSaveDocument(OutputStream, FixerConfig)
 * @see org.verapdf.metadata.fixer.utils.ProcessedObjectsInspector
 */
public enum ValidationStatus {

	VALID(3),

	INVALID_METADATA(2),

	INVALID_STRUCTURE(1),

	INVALID_DOCUMENT(0);

	private final int index;

	ValidationStatus(int index) {
		this.index = index;
	}

	/**
	 * Get validation status from integer value. Value must be from 0 to 3
	 *
	 * @param index number representation of validation status
	 * @return corresponding validation status
	 */
	public static ValidationStatus valueOf(int index) {
		switch (index) {
			case 0:
				return INVALID_DOCUMENT;
			case 1:
				return INVALID_STRUCTURE;
			case 2:
				return INVALID_METADATA;
			case 3:
				return VALID;
			default:
				throw new IllegalArgumentException("No enum constant for index: " + index);
		}
	}

	/**
	 * Return combination of current status and given status according to next table:
	 * <table border="1" style="border: 1px solid">
	 *     <col border="1" style="border: 1px solid"/>
	 *     <col border="1" style="border: 1px solid"/>
	 *     <col border="1" style="border: 1px solid"/>
	 *     <body>
	 *         <tr>
	 *             <th>Current value</th>
	 *             <th>Passed value</th>
	 *             <th>Result value</th>
	 *         </tr>
	 *         <tr border="1" style="border: 1px solid" >
	 *             <td rowspan="5">INVALID_DOCUMENT</td>
	 *             <tr>
	 *                 <td>INVALID_DOCUMENT</td>
	 *                 <td>INVALID_DOCUMENT</td>
	 *             </tr>
	 *             <tr>
	 *                 <td>INVALID_STRUCTURE</td>
	 *                 <td>INVALID_DOCUMENT</td>
	 *             </tr>
	 *             <tr>
	 *                 <td>INVALID_METADATA</td>
	 *                 <td>INVALID_DOCUMENT</td>
	 *             </tr>
	 *             <tr>
	 *                 <td>VALID</td>
	 *                 <td>INVALID_DOCUMENT</td>
	 *             </tr>
	 *         </tr>
	 *         <tr border="1" style="border: 1px solid" >
	 *             <td rowspan="5">INVALID_STRUCTURE</td>
	 *             <tr>
	 *                 <td>INVALID_DOCUMENT</td>
	 *                 <td>INVALID_DOCUMENT</td>
	 *             </tr>
	 *             <tr>
	 *                 <td>INVALID_STRUCTURE</td>
	 *                 <td>INVALID_STRUCTURE</td>
	 *             </tr>
	 *             <tr>
	 *                 <td>INVALID_METADATA</td>
	 *                 <td>INVALID_DOCUMENT</td>
	 *             </tr>
	 *             <tr>
	 *                 <td>VALID</td>
	 *                 <td>INVALID_STRUCTURE</td>
	 *             </tr>
	 *         </tr>
	 *         <tr border="1" style="border: 1px solid" >
	 *             <td rowspan="5">INVALID_METADATA</td>
	 *             <tr>
	 *                 <td>INVALID_DOCUMENT</td>
	 *                 <td>INVALID_DOCUMENT</td>
	 *             </tr>
	 *             <tr>
	 *                 <td>INVALID_STRUCTURE</td>
	 *                 <td>INVALID_DOCUMENT</td>
	 *             </tr>
	 *             <tr>
	 *                 <td>INVALID_METADATA</td>
	 *                 <td>INVALID_METADATA</td>
	 *             </tr>
	 *             <tr>
	 *                 <td>VALID</td>
	 *                 <td>INVALID_METADATA</td>
	 *             </tr>
	 *         </tr>
	 *         <tr border="1" style="border: 1px solid" >
	 *             <td rowspan="5">VALID</td>
	 *             <tr>
	 *                 <td>INVALID_DOCUMENT</td>
	 *                 <td>INVALID_DOCUMENT</td>
	 *             </tr>
	 *             <tr>
	 *                 <td>INVALID_STRUCTURE</td>
	 *                 <td>INVALID_STRUCTURE</td>
	 *             </tr>
	 *             <tr>
	 *                 <td>INVALID_METADATA</td>
	 *                 <td>INVALID_METADATA</td>
	 *             </tr>
	 *             <tr>
	 *                 <td>VALID</td>
	 *                 <td>VALID</td>
	 *             </tr>
	 *         </tr>
	 *     </body>
	 * </table>
	 *
	 * @param status passed status argument
	 * @return result status
	 */
	public ValidationStatus getStatus(ValidationStatus status) {
		int highBit = status.index & this.index & 2;
		int lowBit = status.index & this.index & 1;
		switch (highBit | lowBit) {
			case 0:
				return INVALID_DOCUMENT;
			case 1:
				return INVALID_STRUCTURE;
			case 2:
				return INVALID_METADATA;
			case 3:
				return VALID;
			default:
				throw new IllegalArgumentException("Result of transform is " + (highBit | lowBit));
		}
	}
}

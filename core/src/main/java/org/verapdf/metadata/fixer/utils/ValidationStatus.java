/**
 * This file is part of veraPDF Library core, a module of the veraPDF project.
 * Copyright (c) 2015, veraPDF Consortium <info@verapdf.org>
 * All rights reserved.
 *
 * veraPDF Library core is free software: you can redistribute it and/or modify
 * it under the terms of either:
 *
 * The GNU General public license GPLv3+.
 * You should have received a copy of the GNU General Public License
 * along with veraPDF Library core as the LICENSE.GPL file in the root of the source
 * tree.  If not, see http://www.gnu.org/licenses/ or
 * https://www.gnu.org/licenses/gpl-3.0.en.html.
 *
 * The Mozilla Public License MPLv2+.
 * You should have received a copy of the Mozilla Public License along with
 * veraPDF Library core as the LICENSE.MPL file in the root of the source tree.
 * If a copy of the MPL was not distributed with this file, you can obtain one at
 * http://mozilla.org/MPL/2.0/.
 */
package org.verapdf.metadata.fixer.utils;

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
 * According to this status {@link org.verapdf.pdfa.MetadataFixer}
 * run different handling of document.
 *
 * @author Evgeniy Muravitskiy
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
	 * <p>
	 *
	 * <table border="1" style="border: 1px solid">
	 *     <caption>Table: Validation status</caption>
	 *         <tr>
	 *             <th>Current value</th>
	 *             <th>Passed value</th>
	 *             <th>Result value</th>
	 *         </tr>
	 *         <tr style="border: 1px solid">
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
	 *         <tr style="border: 1px solid">
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
	 *         <tr style="border: 1px solid">
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
	 *         <tr style="border: 1px solid">
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
	 * </table>
	 *
	 * @param status passed status argument
	 *
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

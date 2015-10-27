package org.verapdf.model.impl.pb.cos;

import org.apache.log4j.Logger;
import org.apache.pdfbox.cos.COSArray;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSNumber;
import org.verapdf.model.coslayer.CosBBox;

/**
 * Implementation of bounding box
 *
 * @author Evgeniy Muravitskiy
 */
public class PBCosBBox extends PBCosArray implements CosBBox {

	private static final Logger LOGGER = Logger.getLogger(PBCosBBox.class);

	public static final String COS_BBOX_TYPE = "CosBBox";
	private static final int LEFT_CORNER_POSITION = 0;
	private static final int BOTTOM_CORNER_POSITION = 1;
	private static final int RIGHT_CORNER_POSITION = 2;
	private static final int TOP_CORNER_POSITION = 3;

	/**
	 * Default constructor
	 *
	 * @param array pdfbox COSArray
	 */
	public PBCosBBox(COSArray array) {
		super(array, COS_BBOX_TYPE);
	}

	/**
	 * @return top corner of bounding box
	 */
	@Override
	public Double gettop() {
		return this.getRequiredValue(TOP_CORNER_POSITION);
	}

	/**
	 * @return bottom corner of bounding box
	 */
	@Override
	public Double getbottom() {
		return this.getRequiredValue(BOTTOM_CORNER_POSITION);
	}

	/**
	 * @return left corner of bounding box
	 */
	@Override
	public Double getleft() {
		return this.getRequiredValue(LEFT_CORNER_POSITION);
	}

	/**
	 * @return right corner of bounding box
	 */
	@Override
	public Double getright() {
		return this.getRequiredValue(RIGHT_CORNER_POSITION);
	}

	private Double getRequiredValue(final int position) {
		COSArray array = (COSArray) this.baseObject;
		if (array.size() > position) {
			COSBase base = array.get(position);
			if (base instanceof COSNumber) {
				return Double.valueOf(((COSNumber) base).doubleValue());
			} else {
				LOGGER.warn("In bbox expected number but got "
						+ base.getClass().getSimpleName());
			}
		} else {
			LOGGER.warn("Expected size of bbox array greater than"
					+ position + "but got " + array.size());
		}
		return null;
	}
}

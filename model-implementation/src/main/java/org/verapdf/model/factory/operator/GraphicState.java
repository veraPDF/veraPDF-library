package org.verapdf.model.factory.operator;

import org.apache.pdfbox.pdmodel.graphics.color.PDColorSpace;
import org.apache.pdfbox.pdmodel.graphics.color.PDDeviceGray;
import org.apache.pdfbox.pdmodel.graphics.pattern.PDAbstractPattern;

/**
 * @author Timur Kamalov
 */
public class GraphicState implements Cloneable {

	private PDColorSpace fillColorSpace = PDDeviceGray.INSTANCE;
	private PDColorSpace strokeColorSpace = PDDeviceGray.INSTANCE;
	private PDAbstractPattern pattern;

	public PDColorSpace getFillColorSpace() {
		return fillColorSpace;
	}

	public void setFillColorSpace(PDColorSpace fillColorSpace) {
		this.fillColorSpace = fillColorSpace;
	}

	public PDColorSpace getStrokeColorSpace() {
		return strokeColorSpace;
	}

	public void setStrokeColorSpace(PDColorSpace strokeColorSpace) {
		this.strokeColorSpace = strokeColorSpace;
	}

	public PDAbstractPattern getPattern() {
		return pattern;
	}

	public void setPattern(PDAbstractPattern pattern) {
		this.pattern = pattern;
	}

	/**
	 * This method will copy properties from passed graphic state to current object
	 * @param graphicState graphic state to copy properties from
	 */
	public void copyProperties(GraphicState graphicState) {
		this.fillColorSpace = graphicState.getFillColorSpace();
		this.strokeColorSpace = graphicState.getStrokeColorSpace();
		this.pattern = graphicState.getPattern();
	}

	@Override
	public GraphicState clone() {
		GraphicState graphicState = new GraphicState();
		graphicState.setFillColorSpace(this.getFillColorSpace());
		graphicState.setStrokeColorSpace(this.getStrokeColorSpace());
		graphicState.setPattern(this.getPattern());
		return graphicState;
	}

}

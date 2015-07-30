package org.verapdf.model.factory.operator;

import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.graphics.color.PDColorSpace;
import org.apache.pdfbox.pdmodel.graphics.color.PDDeviceGray;
import org.apache.pdfbox.pdmodel.graphics.state.RenderingMode;

/**
 * @author Timur Kamalov
 */
public class GraphicState implements Cloneable {

	private PDColorSpace fillColorSpace = PDDeviceGray.INSTANCE;
	private PDColorSpace strokeColorSpace = PDDeviceGray.INSTANCE;
	private RenderingMode renderingMode = RenderingMode.FILL;
	private PDFont font;

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

	public RenderingMode getRenderingMode() {
		return renderingMode;
	}

	public void setRenderingMode(RenderingMode renderingMode) {
		this.renderingMode = renderingMode;
	}

	public PDFont getFont() {
		return font;
	}

	public void setFont(PDFont font) {
		this.font = font;
	}

	/**
	 * This method will copy properties from passed graphic state to current object
	 * @param graphicState graphic state to copy properties from
	 */
	public void copyProperties(GraphicState graphicState) {
		this.fillColorSpace = graphicState.getFillColorSpace();
		this.strokeColorSpace = graphicState.getStrokeColorSpace();
		this.renderingMode = graphicState.getRenderingMode();
		this.font = graphicState.getFont();
	}

	@Override
	public GraphicState clone() {
		GraphicState graphicState = new GraphicState();
		graphicState.setFillColorSpace(this.getFillColorSpace());
		graphicState.setStrokeColorSpace(this.getStrokeColorSpace());
		graphicState.setRenderingMode(this.getRenderingMode());
		graphicState.setFont(this.getFont());
		return graphicState;
	}

}

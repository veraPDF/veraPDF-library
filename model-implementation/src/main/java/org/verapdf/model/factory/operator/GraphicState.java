package org.verapdf.model.factory.operator;

import org.apache.log4j.Logger;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.graphics.color.PDColorSpace;
import org.apache.pdfbox.pdmodel.graphics.color.PDDeviceGray;
import org.apache.pdfbox.pdmodel.graphics.pattern.PDAbstractPattern;
import org.apache.pdfbox.pdmodel.graphics.state.PDExtendedGraphicsState;
import org.apache.pdfbox.pdmodel.graphics.state.RenderingMode;

import java.io.IOException;

/**
 * Implementation of graphic state for content stream.
 * Contains follows settings: fill and stroke color spaces,
 * pattern, rendering mode and font
 *
 * @author Timur Kamalov
 */
public class GraphicState implements Cloneable {

    private static final Logger LOGGER = Logger.getLogger(GraphicState.class);

    private PDColorSpace fillColorSpace = PDDeviceGray.INSTANCE;
    private PDColorSpace strokeColorSpace = PDDeviceGray.INSTANCE;
    private PDAbstractPattern pattern = null;
    private RenderingMode renderingMode = RenderingMode.FILL;
    private PDFont font;

	/**
	 * @return fill color space of current state
	 */
    public PDColorSpace getFillColorSpace() {
        return fillColorSpace;
    }

	/**
	 * @param fillColorSpace set fill color space to current state
	 */
    public void setFillColorSpace(PDColorSpace fillColorSpace) {
        this.fillColorSpace = fillColorSpace;
    }

	/**
	 * @return stroke color space of current state
	 */
    public PDColorSpace getStrokeColorSpace() {
        return strokeColorSpace;
    }

	/**
	 * @param strokeColorSpace set stroke color space to current state
	 */
    public void setStrokeColorSpace(PDColorSpace strokeColorSpace) {
        this.strokeColorSpace = strokeColorSpace;
    }

	/**
	 * @return pattern of current state
	 */
    public PDAbstractPattern getPattern() {
        return pattern;
    }

	/**
	 * @param pattern set pattern to current state
	 */
    public void setPattern(PDAbstractPattern pattern) {
        this.pattern = pattern;
    }

	/**
	 * @return rendering mode of current state
	 */
    public RenderingMode getRenderingMode() {
        return renderingMode;
    }

	/**
	 * @param renderingMode set renderint mode to color space
	 */
    public void setRenderingMode(RenderingMode renderingMode) {
        this.renderingMode = renderingMode;
    }

	/**
	 * @return font of current state
	 */
    public PDFont getFont() {
        return font;
    }

	/**
	 * @param font set font to current state
	 */
    public void setFont(PDFont font) {
        this.font = font;
    }

    /**
     * This method will copy properties from passed graphic state to current
     * object
     * 
     * @param graphicState
     *            graphic state to copy properties from
     */
    public void copyProperties(GraphicState graphicState) {
        this.fillColorSpace = graphicState.getFillColorSpace();
        this.strokeColorSpace = graphicState.getStrokeColorSpace();
        this.pattern = graphicState.getPattern();
        this.renderingMode = graphicState.getRenderingMode();
        this.font = graphicState.getFont();
    }

	/**
	 * Set font to current state from extended graphic state
	 *
	 * @param extGState extended graphic state
	 */
    public void copyPropertiesFromExtGState(PDExtendedGraphicsState extGState) {
        if (extGState != null) {
            try {
                if (extGState.getFontSetting() != null) {
                    this.font = extGState.getFontSetting().getFont();
                }
            } catch (IOException e) {
                LOGGER.error(e);
            }
        }
    }

	/**
	 * Create copy of current graphic state.
	 *
	 * @return copy of current graphic state
	 * @throws CloneNotSupportedException
	 */
    @Override
    public GraphicState clone() throws CloneNotSupportedException {
        GraphicState graphicState = (GraphicState) super.clone();
        graphicState.fillColorSpace = this.fillColorSpace;
        graphicState.strokeColorSpace = this.strokeColorSpace;
        graphicState.pattern = this.pattern;
        graphicState.renderingMode = this.renderingMode;
        graphicState.font = this.font;
        return graphicState;
    }

}

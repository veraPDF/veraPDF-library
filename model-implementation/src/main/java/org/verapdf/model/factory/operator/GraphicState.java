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
 * @author Timur Kamalov
 */
public class GraphicState implements Cloneable {

    private static final Logger LOGGER = Logger.getLogger(GraphicState.class);

    private PDColorSpace fillColorSpace = PDDeviceGray.INSTANCE;
    private PDColorSpace strokeColorSpace = PDDeviceGray.INSTANCE;
    private PDAbstractPattern pattern = null;
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

    public PDAbstractPattern getPattern() {
        return pattern;
    }

    public void setPattern(PDAbstractPattern pattern) {
        this.pattern = pattern;
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

    public void copyPropertiesFromExtGState(PDExtendedGraphicsState extGState) {
        try {
            this.font = extGState.getFontSetting().getFont();
        } catch (IOException e) {
            LOGGER.error(e);
        }
    }

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

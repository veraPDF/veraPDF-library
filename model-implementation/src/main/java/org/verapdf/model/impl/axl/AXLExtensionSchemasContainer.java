package org.verapdf.model.impl.axl;

import com.adobe.xmp.impl.VeraPDFXMPNode;
import com.adobe.xmp.options.PropertyOptions;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.tools.xmp.ValidatorsContainer;
import org.verapdf.model.xmplayer.ExtensionSchemasContainer;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Maksim Bezrukov
 */
public class AXLExtensionSchemasContainer extends AXLXMPObject implements ExtensionSchemasContainer {

    public static final String EXTENSION_SCHEMAS_CONTAINER = "ExtensionSchemasContainer";

    public static final String EXTENSION_SCHEMA_DEFINITIONS = "ExtensionSchemaDefinitions";

    protected VeraPDFXMPNode xmpNode;
    protected final ValidatorsContainer containerForPDFA_1;
    protected final ValidatorsContainer containerForPDFA_2_3;

    public AXLExtensionSchemasContainer(VeraPDFXMPNode xmpNode, ValidatorsContainer containerForPDFA_1, ValidatorsContainer containerForPDFA_2_3) {
        super(EXTENSION_SCHEMAS_CONTAINER);
        this.xmpNode = xmpNode;
        this.containerForPDFA_1 = containerForPDFA_1;
        this.containerForPDFA_2_3 = containerForPDFA_2_3;
    }

    /**
     * @param link name of the link
     * @return List of all objects with link name
     */
    @Override
    public List<? extends Object> getLinkedObjects(String link) {
        switch (link) {
            case EXTENSION_SCHEMA_DEFINITIONS:
                return this.getExtensionSchemaDefinitions();
            default:
                return super.getLinkedObjects(link);
        }
    }

    private List<AXLExtensionSchemaDefinition> getExtensionSchemaDefinitions() {
        List<AXLExtensionSchemaDefinition> res = new ArrayList<>();
        if (this.xmpNode != null && this.xmpNode.getOptions().isArray()) {
            for (VeraPDFXMPNode node : this.xmpNode.getChildren()) {
                res.add(new AXLExtensionSchemaDefinition(node, containerForPDFA_1, containerForPDFA_2_3));
            }
        }
        return res;
    }

    @Override
    public Boolean getisValidBag() {
        PropertyOptions options = this.xmpNode.getOptions();
        return (options.isArray() && !(options.isArrayOrdered() || options.isArrayAlternate()));
    }

    @Override
    public String getprefix() {
        return this.xmpNode == null ? null : this.xmpNode.getPrefix();
    }

}

package org.verapdf.model.tools.xmp;

import com.adobe.xmp.impl.VeraPDFXMPNode;

/**
 * @author Maksim Bezrukov
 */
public class SchemasDefinitionCreator {

    /**
     * Creates schemas definition object valid for PDF/A-1. Ignores all invalid fields types for PDF/A-1
     *
     * @param schemas extension schemas container node
     * @return created Schemas Definition object
     * @throws IllegalArgumentException occurs in case of wrong node passed to method
     */
    public static SchemasDefinition createSchemasDefinitionForPDFA_1(VeraPDFXMPNode schemas) {
        return createSchemasDefinition(schemas, true);
    }

    /**
     * Creates schemas definition object valid for PDF/A-2 or for PDF/A-3. Ignores all invalid fields types for PDF/A-2 or for PDF/A-3
     *
     * @param schemas extension schemas container node
     * @return created Schemas Definition object
     * @throws IllegalArgumentException occurs in case of wrong node passed to method
     */
    public static SchemasDefinition createSchemasDefinitionForPDFA_2_3(VeraPDFXMPNode schemas, SchemasDefinition mainSchemasDefinition) {
        return createSchemasDefinition(schemas, false);
    }

    private static SchemasDefinition createSchemasDefinition(VeraPDFXMPNode schemas, boolean isPDFA_1) {
        if (schemas == null) {
            return getEmptySchemasDefinition();
        }
        if (false) {
            throw new IllegalArgumentException("Argument must be schemas property from pdfa extension schema");
        }
        //TODO: implement this with node type check as exception. Differ for pdfa-1 and pdfa-2, 3
        return new SchemasDefinition(null);
    }

    public static SchemasDefinition getEmptySchemasDefinition() {
        //TODO: implement this
        return new SchemasDefinition(null);
    }

    public static SchemasDefinition getPredefinedPDFA_1SchemasDefinition() {
        //TODO: implement this
        return new SchemasDefinition(null);
    }

    public static SchemasDefinition getPredefinedPDFA_2_3SchemasDefinition() {
        //TODO: implement this
        return new SchemasDefinition(null);
    }
}

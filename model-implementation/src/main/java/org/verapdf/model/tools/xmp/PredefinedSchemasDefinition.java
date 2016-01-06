package org.verapdf.model.tools.xmp;

import com.adobe.xmp.impl.VeraPDFXMPNode;

import javax.xml.namespace.QName;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @author Maksim Bezrukov
 */
public class PredefinedSchemasDefinition extends SchemasDefinition {

    private Map<QName, Pattern> closedSimpleChoice = new HashMap<>();
    private Map<QName, Pattern> restrictedSeqText = new HashMap<>();
    private Map<QName, String[][]> closedSeqChoice = new HashMap<>();

    protected PredefinedSchemasDefinition() {
    }

    protected PredefinedSchemasDefinition(ValidatorsContainer validator) {
        super(validator);
    }

    @Override
    protected boolean isDefinedProperty(QName name) {
        return closedSimpleChoice.containsKey(name) ||
                restrictedSeqText.containsKey(name) ||
                closedSeqChoice.containsKey(name) ||
                super.isDefinedProperty(name);
    }

    @Override
    public Boolean isCorrespondsDefinedType(VeraPDFXMPNode node) {
        if (node == null) {
            throw new IllegalArgumentException("Argument node can not be null");
        }

        QName name = new QName(node.getNamespaceURI(), node.getName());
        if (closedSimpleChoice.containsKey(name)) {
            return isCorrespondsClosedSimpleChoice(node, closedSimpleChoice.get(name));
        } else if (restrictedSeqText.containsKey(name)) {
            return isCorrespondsRestrictedSeqText(node, restrictedSeqText.get(name));
        } else if (closedSeqChoice.containsKey(name)) {
            return isCorrespondsClosedSeqChoice(node, closedSeqChoice.get(name));
        } else {
            return super.isCorrespondsDefinedType(node);
        }
    }

    private Boolean isCorrespondsClosedSimpleChoice(VeraPDFXMPNode node, Pattern p) {
        return node.getOptions().isSimple() && p.matcher(node.getValue()).matches();
    }

    private Boolean isCorrespondsRestrictedSeqText(VeraPDFXMPNode node, Pattern p) {
        if (!node.getOptions().isArrayOrdered()) {
            return Boolean.FALSE;
        }
        for (VeraPDFXMPNode child : node.getChildren()) {
            if (!(child.getOptions().isSimple() && p.matcher(child.getValue()).matches())) {
                return Boolean.FALSE;
            }
        }
        return Boolean.TRUE;
    }

    private Boolean isCorrespondsClosedSeqChoice(VeraPDFXMPNode node, String[][] choices) {
        if (!node.getOptions().isArrayOrdered()) {
            return Boolean.FALSE;
        }
        List<VeraPDFXMPNode> children = node.getChildren();
        for (String[] choice : choices) {
            if (isEqualValues(children, choice)) {
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }

    private boolean isEqualValues(List<VeraPDFXMPNode> nodes, String[] values) {
        if (nodes.size() != values.length) {
            return false;
        }
        for (int i = 0; i < values.length; ++i) {
            if (!values[i].equals(nodes.get(i).getValue())) {
                return false;
            }
        }
        return true;
    }

    protected boolean registerSimpleChoiceProperty(String namespaceURI, String propertyName, Pattern pattern) {
        return registerPatternProperty(closedSimpleChoice, namespaceURI, propertyName, pattern);
    }

    protected boolean registerRestrictedTextProperty(String namespaceURI, String propertyName, Pattern pattern) {
        return registerPatternProperty(restrictedSeqText, namespaceURI, propertyName, pattern);
    }

    private boolean registerPatternProperty(Map<QName, Pattern> map, String namespaceURI, String propertyName, Pattern pattern) {
        if (namespaceURI == null) {
            throw new IllegalArgumentException("Argument namespaceURI can not be null");
        }
        if (propertyName == null) {
            throw new IllegalArgumentException("Argument property name can not be null");
        }
        if (pattern == null) {
            throw new IllegalArgumentException("Argument pattern can not be null");
        }

        QName name = new QName(namespaceURI, propertyName);
        if (isDefinedProperty(name)) {
            return false;
        } else {
            map.put(name, pattern);
            return true;
        }
    }

    protected boolean registerSeqChoiceProperty(String namespaceURI, String propertyName, String[][] choices) {
        if (namespaceURI == null) {
            throw new IllegalArgumentException("Argument namespaceURI can not be null");
        }
        if (propertyName == null) {
            throw new IllegalArgumentException("Argument property name can not be null");
        }
        if (choices == null) {
            throw new IllegalArgumentException("Argument choices can not be null");
        }

        QName name = new QName(namespaceURI, propertyName);
        if (isDefinedProperty(name)) {
            return false;
        } else {
            closedSeqChoice.put(name, choices);
            return true;
        }
    }
}

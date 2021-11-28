package net.coleh.autoautolanguageplugin.parse;

import com.intellij.lang.ASTNode;

public class AutoautoPsiUtilImpl {

    public static String getLabel(AutoautoLabeledStatepath element) {
        ASTNode keyNode = element.getNode().findChildByType(AutoautoTypes.STATEPATH_LABEL_ID);
        if (keyNode != null) {
            String label = keyNode.getText();
            if(label.charAt(0) == '#') label = label.substring(1);
            return label.trim();
        } else {
            return null;
        }
    }
}

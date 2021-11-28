package net.coleh.autoautolanguageplugin.annotate;

import com.android.tools.r8.position.TextRange;
import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.lang.annotation.HighlightSeverity;
import com.intellij.psi.PsiElement;

import net.coleh.autoautolanguageplugin.AutoautoUtil;
import net.coleh.autoautolanguageplugin.parse.AutoautoFrontMatter;
import net.coleh.autoautolanguageplugin.parse.AutoautoFrontMatterKeyValue;
import net.coleh.autoautolanguageplugin.parse.AutoautoGotoStatement;
import net.coleh.autoautolanguageplugin.parse.AutoautoUnitValue;
import net.coleh.autoautolanguageplugin.parse.AutoautoVariableReference;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class AutoautoAnnotator implements Annotator {
    public void annotate(@NotNull final PsiElement element, @NotNull AnnotationHolder holder) {
        if(element instanceof AutoautoVariableReference) {
            AutoautoFrontMatterKeyValue frontMatterKeyValue = (AutoautoFrontMatterKeyValue) element;
            boolean hasUnitvalue = includesUnitValueChild(frontMatterKeyValue.getValue());
            if(hasUnitvalue) {
                holder.newAnnotation(HighlightSeverity.ERROR, "Usage of unit values in frontmatter is not supported")
                        .range(frontMatterKeyValue.getValue())
                        .highlightType(ProblemHighlightType.LIKE_UNKNOWN_SYMBOL)
                        .create();
            }
        } else if(element instanceof AutoautoFrontMatter) {
            AutoautoFrontMatter frontmatter = (AutoautoFrontMatter) element;
            List<AutoautoFrontMatterKeyValue> KVs = frontmatter.getFrontMatterKeyValueList();
            ArrayList<String> names = new ArrayList<String>();

            for(int i = KVs.size() - 1; i >= 0; i--) {
                String text = KVs.get(i).getText();
                int colonIndex = text.indexOf(":");
                if(colonIndex == -1) continue;
                String name = text.substring(0, colonIndex).trim();
                if(names.contains(name)) {
                    holder.newAnnotation(HighlightSeverity.WARNING, "Duplicate key in frontmatter-- only last value will be preserved")
                            .range(KVs.get(i))
                            .highlightType(ProblemHighlightType.WARNING)
                            .create();
                } else {
                    names.add(name);
                }
            }
        }
    }
    private boolean includesUnitValueChild(PsiElement parent) {
        for(PsiElement child : parent.getChildren()) {
            if(child instanceof AutoautoUnitValue) return true;
            else return includesUnitValueChild(child);
        }
        return false;
    }
}

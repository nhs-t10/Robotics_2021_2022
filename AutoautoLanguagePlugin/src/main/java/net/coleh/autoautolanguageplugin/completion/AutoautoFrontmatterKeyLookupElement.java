package net.coleh.autoautolanguageplugin.completion;

import com.intellij.codeInsight.completion.InsertionContext;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementPresentation;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;

import org.jetbrains.annotations.NotNull;

public class AutoautoFrontmatterKeyLookupElement extends LookupElement {
    private String description;
    private String name;
    @NotNull
    @Override
    public String getLookupString() {
        return name;
    }

    public AutoautoFrontmatterKeyLookupElement(String name, String description) {
        this.name = name;
        this.description = description;
    }

    @Override
    public void handleInsert(InsertionContext context) {
        PsiFile file = context.getFile();
        PsiElement elem = file.findElementAt(context.getSelectionEndOffset());


    }
    @Override
    public void renderElement(LookupElementPresentation presentation) {
        presentation.setItemText(name);
        presentation.appendTailTextItalic(description, false);
    }

    public boolean isWorthShowingInAutoPopup() {
        return true;
    }
}

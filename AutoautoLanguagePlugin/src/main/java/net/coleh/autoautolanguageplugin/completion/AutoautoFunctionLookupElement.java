package net.coleh.autoautolanguageplugin.completion;

import com.intellij.codeInsight.completion.InsertionContext;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementPresentation;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;

import org.jetbrains.annotations.NotNull;

public class AutoautoFunctionLookupElement extends LookupElement {
    private AutoautoBuiltinFunctionCompletions.AutoautoBuiltinFunctionRecord record;
    private String name;
    @NotNull
    @Override
    public String getLookupString() {
        return name;
    }

    public AutoautoFunctionLookupElement(AutoautoBuiltinFunctionCompletions.AutoautoBuiltinFunctionRecord record) {
        this.name = record.getName();
        this.record = record;
    }

    @Override
    public void handleInsert(InsertionContext context) {
        PsiFile file = context.getFile();
        PsiElement elem = file.findElementAt(context.getSelectionEndOffset());


    }
    @Override
    public void renderElement(LookupElementPresentation presentation) {
        presentation.setItemText(name);

        if(record != null) {
            String args = String.join(", ", record.getArgsAsStrarr());
            presentation.appendTailTextItalic("(" + args + ")", false);
        }
    }

    public boolean isWorthShowingInAutoPopup() {
        return true;
    }
}

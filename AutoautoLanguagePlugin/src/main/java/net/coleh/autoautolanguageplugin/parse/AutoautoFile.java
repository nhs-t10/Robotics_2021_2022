package net.coleh.autoautolanguageplugin.parse;

import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.FileViewProvider;

import net.coleh.autoautolanguageplugin.AutoautoFileType;
import net.coleh.autoautolanguageplugin.AutoautoLanguage;

import org.jetbrains.annotations.NotNull;

public class AutoautoFile extends PsiFileBase {
    public AutoautoFile(@NotNull FileViewProvider viewProvider) {
        super(viewProvider, AutoautoLanguage.INSTANCE);
    }

    @NotNull
    @Override
    public FileType getFileType() {
        return AutoautoFileType.INSTANCE;
    }

    @Override
    public String toString() {
        return "Autoauto File";
    }
}

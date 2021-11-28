package net.coleh.autoautolanguageplugin;

import com.intellij.openapi.fileTypes.LanguageFileType;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.Icon;

public class AutoautoFileType extends LanguageFileType {
    public static final AutoautoFileType INSTANCE = new AutoautoFileType();

    private AutoautoFileType() {
        super(AutoautoLanguage.INSTANCE);
    }

    @NotNull
    public String getName() {
        return "Autoauto File";
    }

    @NotNull
    public String getDescription() {
        return "Autoauto robot movement file";
    }

    @NotNull
    public String getDefaultExtension() {
        return "autoauto";
    }

    @Nullable
    public Icon getIcon() {
        return AutoautoIcons.FILE;
    }

}
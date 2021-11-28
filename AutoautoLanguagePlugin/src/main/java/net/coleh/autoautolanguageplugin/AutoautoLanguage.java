package net.coleh.autoautolanguageplugin;

import com.intellij.lang.Language;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AutoautoLanguage extends Language {
    public static final AutoautoLanguage INSTANCE = new AutoautoLanguage();

    private AutoautoLanguage() {
        super("Autoauto");
    }
}


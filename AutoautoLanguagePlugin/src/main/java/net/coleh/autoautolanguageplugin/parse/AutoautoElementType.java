package net.coleh.autoautolanguageplugin.parse;

import com.intellij.psi.tree.IElementType;

import net.coleh.autoautolanguageplugin.AutoautoLanguage;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class AutoautoElementType extends IElementType {
    public AutoautoElementType(@NotNull @NonNls String debugName) {
        super(debugName, AutoautoLanguage.INSTANCE);
    }
}

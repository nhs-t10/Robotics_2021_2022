package net.coleh.autoautolanguageplugin.parse;

import com.intellij.psi.tree.IElementType;

import net.coleh.autoautolanguageplugin.AutoautoLanguage;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class AutoautoTokenType extends IElementType {
    public AutoautoTokenType(@NotNull @NonNls String debugName) {
        super(debugName, AutoautoLanguage.INSTANCE);
    }

    @Override
    public String toString() {
        return "AutoautoTokenType." + super.toString();
    }
}

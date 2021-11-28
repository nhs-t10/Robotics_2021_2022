package net.coleh.autoautolanguageplugin.parse.lexer;

import com.intellij.lexer.FlexAdapter;
import net.coleh.autoautolanguageplugin.parse.lexer.AutoautoLexer;

import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import java.io.IOException;
import java.io.Reader;

public class AutoautoLexerAdapter extends FlexAdapter {
    public AutoautoLexerAdapter() {
        super(new AutoautoLexer(null));
    }
}

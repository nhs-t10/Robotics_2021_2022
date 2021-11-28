package net.coleh.autoautolanguageplugin.parse.syntaxhighlight;

import com.intellij.lexer.Lexer;
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.editor.HighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;

import net.coleh.autoautolanguageplugin.parse.AutoautoTypes;
import net.coleh.autoautolanguageplugin.parse.lexer.AutoautoLexerAdapter;

import static com.intellij.openapi.editor.colors.TextAttributesKey.createTextAttributesKey;

import org.jetbrains.annotations.NotNull;

public class AutoautoSyntaxHighlighter extends SyntaxHighlighterBase {

    public static final TextAttributesKey OPERATOR =
            createTextAttributesKey("SIMPLE_SEPARATOR", DefaultLanguageHighlighterColors.OPERATION_SIGN);
    public static final TextAttributesKey LABEL =
            createTextAttributesKey("AUTOAUTO_LABEL", DefaultLanguageHighlighterColors.CLASS_NAME);
    public static final TextAttributesKey FUNCTION =
            createTextAttributesKey("AUTOAUTO_FUNCTION", DefaultLanguageHighlighterColors.FUNCTION_CALL);
    public static final TextAttributesKey VALUE =
            createTextAttributesKey("AUTOAUTO_VALUE", DefaultLanguageHighlighterColors.STRING);
    public static final TextAttributesKey COMMA =
            createTextAttributesKey("AUTOAUTO_COMMA", DefaultLanguageHighlighterColors.COMMA);
    public static final TextAttributesKey PARENS =
            createTextAttributesKey("AUTOAUTO_PARENS", DefaultLanguageHighlighterColors.PARENTHESES);
    public static final TextAttributesKey SEMICOLON =
            createTextAttributesKey("AUTOAUTO_SEMICOLON", DefaultLanguageHighlighterColors.COMMA);
    public static final TextAttributesKey COMMENT =
            createTextAttributesKey("AUTOAUTO_COMMENT", DefaultLanguageHighlighterColors.BLOCK_COMMENT);
    public static final TextAttributesKey STATEMENT =
            createTextAttributesKey("AUTOAUTO_STATEMENT", DefaultLanguageHighlighterColors.KEYWORD);
    public static final TextAttributesKey NUMERIC_VALUE =
            createTextAttributesKey("AUTOAUTO_NUMERIC_VALUE", DefaultLanguageHighlighterColors.NUMBER);
    public static final TextAttributesKey STRING =
            createTextAttributesKey("AUTOAUTO_STRING", DefaultLanguageHighlighterColors.STRING);
    public static final TextAttributesKey DOLLAR_SIGN =
            createTextAttributesKey("AUTOAUTO_DOLLAR_SIGN", DefaultLanguageHighlighterColors.BRACES);

    public static final TextAttributesKey BAD_CHARACTER =
            createTextAttributesKey("SIMPLE_BAD_CHARACTER", HighlighterColors.BAD_CHARACTER);


    public static final TextAttributesKey[] OPERATOR_KEYS = new TextAttributesKey[] { OPERATOR };
    public static final TextAttributesKey[] PARENS_KEYS = new TextAttributesKey[] { PARENS };
    public static final TextAttributesKey[] LABEL_KEYS = new TextAttributesKey[] { LABEL };
    public static final TextAttributesKey[] FUNCTION_KEYS = new TextAttributesKey[] { FUNCTION };
    public static final TextAttributesKey[] VALUE_KEYS = new TextAttributesKey[] { VALUE };
    public static final TextAttributesKey[] COMMA_KEYS = new TextAttributesKey[] { COMMA };
    public static final TextAttributesKey[] SEMICOLON_KEYS = new TextAttributesKey[] { SEMICOLON };
    public static final TextAttributesKey[] COMMENT_KEYS = new TextAttributesKey[] { COMMENT };
    public static final TextAttributesKey[] STATEMENT_KEYS = new TextAttributesKey[] { STATEMENT };
    public static final TextAttributesKey[] NUMERIC_VALUE_KEYS = new TextAttributesKey[] { NUMERIC_VALUE };
    public static final TextAttributesKey[] STRING_KEYS = new TextAttributesKey[] { STRING };
    public static final TextAttributesKey[] BAD_CHARACTER_KEYS = new TextAttributesKey[] { BAD_CHARACTER };
    public static final TextAttributesKey[] DOLLAR_SIGN_KEYS = new TextAttributesKey[] { DOLLAR_SIGN };

    public static final TextAttributesKey[] EMPTY_KEYS = new TextAttributesKey[] { };

    @NotNull
    @Override
    public Lexer getHighlightingLexer() {
        return new AutoautoLexerAdapter();
    }


    @NotNull
    @Override
    public final TextAttributesKey[] getTokenHighlights(IElementType tokenType) {

        if (tokenType.equals(AutoautoTypes.VALUE)) {
            return VALUE_KEYS;
        } else if (tokenType.equals(AutoautoTypes.STATEPATH_LABEL_ID)) {
            return LABEL_KEYS;
        } else if (tokenType.equals(AutoautoTypes.COMMA)) {
            return COMMA_KEYS;
        } else if (tokenType.equals(AutoautoTypes.SEMICOLON)) {
            return SEMICOLON_KEYS;
        } else if (tokenType.equals(TokenType.BAD_CHARACTER)) {
            return BAD_CHARACTER_KEYS;
        } else if(tokenType.equals(AutoautoTypes.COMMENT_BEGIN) ||
                tokenType.equals(AutoautoTypes.COMMENT_TEXT) ||
                tokenType.equals(AutoautoTypes.COMMENT_END)) {
            return COMMENT_KEYS;
        } else if(tokenType.equals(AutoautoTypes.FUNCTION_CALL)) {
            return FUNCTION_KEYS;
        } else if(tokenType.equals(AutoautoTypes.NEXT) ||
                tokenType.equals(AutoautoTypes.AFTER) ||
                tokenType.equals(AutoautoTypes.SKIP) ||
                tokenType.equals(AutoautoTypes.IF) ||
                tokenType.equals(AutoautoTypes.WHEN) ||
                tokenType.equals(AutoautoTypes.GOTO) ||
                tokenType.equals(AutoautoTypes.LET) ||
                tokenType.equals(AutoautoTypes.FUNCTION) ||
                tokenType.equals(AutoautoTypes.FUNC)) {
            return STATEMENT_KEYS;
        } else if(tokenType.equals(AutoautoTypes.COLON)) {
            return OPERATOR_KEYS;
        } else if(tokenType.equals(AutoautoTypes.NUMERIC_VALUE) ||
                tokenType.equals(AutoautoTypes.NUMERIC_VALUE_WITH_UNIT)) {
            return NUMERIC_VALUE_KEYS;
        } else if(tokenType.equals(AutoautoTypes.NON_QUOTE_CHARACTER) || tokenType.equals(AutoautoTypes.QUOTE)) {
            return STRING_KEYS;
        } else if(tokenType.equals(AutoautoTypes.CLOSE_PAREN) || tokenType.equals(AutoautoTypes.OPEN_PAREN)) {
            return PARENS_KEYS;
        } else if(tokenType.equals(AutoautoTypes.DOLLAR_SIGN)) {
            return DOLLAR_SIGN_KEYS;
        } else{
            return EMPTY_KEYS;
        }
    }
}

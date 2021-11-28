package net.coleh.autoautolanguageplugin.parse;

import com.intellij.lang.ASTNode;
import com.intellij.lang.ParserDefinition;
import com.intellij.lang.PsiParser;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.project.Project;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IFileElementType;
import com.intellij.psi.tree.TokenSet;

import net.coleh.autoautolanguageplugin.AutoautoLanguage;
import net.coleh.autoautolanguageplugin.parse.lexer.AutoautoLexerAdapter;

import org.jetbrains.annotations.NotNull;
import org.junit.Test;

public class AutoautoParserDefinition implements ParserDefinition {
    public static final IFileElementType FILE = new IFileElementType(AutoautoLanguage.INSTANCE);

    public static final TokenSet WHITE_SPACES = TokenSet.create(TokenType.WHITE_SPACE, AutoautoTypes.WHITESPACE_RANGE);
    public static final TokenSet COMMENTS = TokenSet.create(AutoautoTypes.COMMENT);
    public static final TokenSet STRINGS = TokenSet.create(AutoautoTypes.STRING_LITERAL);

    @NotNull
    @Override
    public Lexer createLexer(Project project) {
        return new AutoautoLexerAdapter();
    }

    @NotNull
    @Override
    public TokenSet getWhitespaceTokens() {
        return WHITE_SPACES;
    }

    @NotNull
    @Override
    public TokenSet getCommentTokens() {
        return COMMENTS;
    }

    @NotNull
    @Override
    public TokenSet getStringLiteralElements() {
        return STRINGS;
    }

    @NotNull
    @Override
    public PsiParser createParser(final Project project) {
        return new AutoautoParser();
    }

    @Override
    public IFileElementType getFileNodeType() {
        return FILE;
    }

    @Override
    public PsiFile createFile(FileViewProvider viewProvider) {
        return new AutoautoFile(viewProvider);
    }

    @Override
    public SpaceRequirements spaceExistenceTypeBetweenTokens(ASTNode left, ASTNode right) {
        return SpaceRequirements.MAY;
    }

    @NotNull
    @Override
    public PsiElement createElement(ASTNode node) {
        return AutoautoTypes.Factory.createElement(node);
    }

    @Test
    public void test() {
    }


}

package net.coleh.autoautolanguageplugin.parse.lexer;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;

import com.intellij.psi.TokenType;
import net.coleh.autoautolanguageplugin.parse.AutoautoTypes;

%%

%{
  public AutoautoLexer() {
    this((java.io.Reader)null);
  }
%}

%public
%class AutoautoLexer
%implements FlexLexer
%function advance
%type IElementType
%unicode

WHITE_SPACE=\s
NUMERIC_VALUE=-?[0-9]*\.?[0-9]+
NUMERIC_VALUE_WITH_UNIT=-?[0-9]*\.?[0-9]+\w+
WHITESPACE_RANGE=\s+
STATEPATH_LABEL_ID=#\w+

%state IN_COMMENT
%state IN_STRING
%state IN_LINE_COMMENT
%%
<YYINITIAL> {
  {WHITE_SPACE}              { return TokenType.WHITE_SPACE; }
  {WHITESPACE_RANGE}         { return AutoautoTypes.WHITESPACE_RANGE; }

  {STATEPATH_LABEL_ID}    { return AutoautoTypes.STATEPATH_LABEL_ID; }
  ":"                    { return AutoautoTypes.COLON; }
  ";"                { return AutoautoTypes.SEMICOLON; }
  ","                    { return AutoautoTypes.COMMA; }
  "after "                    { return AutoautoTypes.AFTER; }
  "when "                    { return AutoautoTypes.WHEN; }
  "func "                    { return AutoautoTypes.FUNC; }
  "function "                    { return AutoautoTypes.FUNCTION; }
  "goto "                     { return AutoautoTypes.GOTO; }
  "if "                       { return AutoautoTypes.IF; }
  "if"                       { return AutoautoTypes.IF; }
  "("               { return AutoautoTypes.OPEN_PAREN; }
  ")"              { return AutoautoTypes.CLOSE_PAREN; }
  "let "                      { return AutoautoTypes.LET; }
  "="                   { return AutoautoTypes.EQUALS; }
  "next"                     { return AutoautoTypes.NEXT; }
  "skip"                     { return AutoautoTypes.SKIP; }
  {NUMERIC_VALUE}            { return AutoautoTypes.NUMERIC_VALUE; }
  {NUMERIC_VALUE_WITH_UNIT}                 {return AutoautoTypes.NUMERIC_VALUE_WITH_UNIT; }
  "\""                    { yybegin(IN_STRING); return AutoautoTypes.QUOTE; }
  "<"                     { return AutoautoTypes.COMPARE_LT; }
  "<="                    { return AutoautoTypes.COMPARE_LTE; }
  "=="                    { return AutoautoTypes.COMPARE_EQ; }
  "!="                    { return AutoautoTypes.COMPARE_NEQ; }
  ">="                    { return AutoautoTypes.COMPARE_GTE; }
  ">"                     { return AutoautoTypes.COMPARE_LT; }
  "true"                    { return AutoautoTypes.TRUE; }
  "false"                    { return AutoautoTypes.FALSE; }
  \w+                     { return AutoautoTypes.IDENTIFIER; }
  "/*"                       { yybegin(IN_COMMENT); return AutoautoTypes.COMMENT_BEGIN;}
  "//"                       { yybegin(IN_LINE_COMMENT); return AutoautoTypes.LINE_COMMENT_BEGIN;}
  "+"                        { return AutoautoTypes.PLUS; }
  "-"                        { return AutoautoTypes.MINUS; }
  "/"                        { return AutoautoTypes.DIVIDE; }
  "*"                        { return AutoautoTypes.MULTIPLY; }
  "%"                        { return AutoautoTypes.MODULUS; }
  "^"                        { return AutoautoTypes.EXPONENTIATE; }
  "$"                        { return AutoautoTypes.DOLLAR_SIGN; }
  "["                        { return AutoautoTypes.OPEN_SQUARE_BRACKET; }
  "]"                        { return AutoautoTypes.CLOSE_SQUARE_BRACKET; }
  "."                        { return AutoautoTypes.DOT; }
  "{"                        { return AutoautoTypes.OPEN_CURLY_BRACKET; }
  "}"                        { return AutoautoTypes.CLOSE_CURLY_BRACKET; }
}

<IN_STRING> {
    "\""                       { yybegin(YYINITIAL); return AutoautoTypes.QUOTE; }
     .      { return AutoautoTypes.NON_QUOTE_CHARACTER; }
}

<IN_COMMENT> {
    "*/"              { yybegin(YYINITIAL); return AutoautoTypes.COMMENT_END;  }
     .              { return AutoautoTypes.COMMENT_TEXT; }
}

<IN_LINE_COMMENT> {
    "\n"              { yybegin(YYINITIAL); return AutoautoTypes.LINE_COMMENT_END;  }
     .              { return AutoautoTypes.COMMENT_TEXT; }
}

[^] { return TokenType.BAD_CHARACTER; }

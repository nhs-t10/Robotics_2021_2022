// This is a generated file. Not intended for manual editing.
package net.coleh.autoautolanguageplugin.parse.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static net.coleh.autoautolanguageplugin.parse.AutoautoTypes.*;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import net.coleh.autoautolanguageplugin.parse.*;

public class AutoautoBaseExpressionImpl extends ASTWrapperPsiElement implements AutoautoBaseExpression {

  public AutoautoBaseExpressionImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull AutoautoVisitor visitor) {
    visitor.visitBaseExpression(this);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof AutoautoVisitor) accept((AutoautoVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public AutoautoArrayLiteral getArrayLiteral() {
    return findChildByClass(AutoautoArrayLiteral.class);
  }

  @Override
  @Nullable
  public AutoautoBooleanLiteral getBooleanLiteral() {
    return findChildByClass(AutoautoBooleanLiteral.class);
  }

  @Override
  @NotNull
  public List<AutoautoCommentOpportunity> getCommentOpportunityList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, AutoautoCommentOpportunity.class);
  }

  @Override
  @Nullable
  public AutoautoFunctionCall getFunctionCall() {
    return findChildByClass(AutoautoFunctionCall.class);
  }

  @Override
  @Nullable
  public AutoautoStringLiteral getStringLiteral() {
    return findChildByClass(AutoautoStringLiteral.class);
  }

  @Override
  @Nullable
  public AutoautoUnitValue getUnitValue() {
    return findChildByClass(AutoautoUnitValue.class);
  }

  @Override
  @Nullable
  public AutoautoValueInParens getValueInParens() {
    return findChildByClass(AutoautoValueInParens.class);
  }

  @Override
  @Nullable
  public AutoautoVariableReference getVariableReference() {
    return findChildByClass(AutoautoVariableReference.class);
  }

}

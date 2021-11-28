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

public class AutoautoFunctionCallImpl extends ASTWrapperPsiElement implements AutoautoFunctionCall {

  public AutoautoFunctionCallImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull AutoautoVisitor visitor) {
    visitor.visitFunctionCall(this);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof AutoautoVisitor) accept((AutoautoVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public AutoautoArgumentList getArgumentList() {
    return findChildByClass(AutoautoArgumentList.class);
  }

  @Override
  @NotNull
  public List<AutoautoCommentOpportunity> getCommentOpportunityList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, AutoautoCommentOpportunity.class);
  }

  @Override
  @Nullable
  public AutoautoDynamicValue getDynamicValue() {
    return findChildByClass(AutoautoDynamicValue.class);
  }

  @Override
  @Nullable
  public AutoautoValueInParens getValueInParens() {
    return findChildByClass(AutoautoValueInParens.class);
  }

}

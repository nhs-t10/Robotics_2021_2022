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

public class AutoautoSingleStatementImpl extends ASTWrapperPsiElement implements AutoautoSingleStatement {

  public AutoautoSingleStatementImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull AutoautoVisitor visitor) {
    visitor.visitSingleStatement(this);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof AutoautoVisitor) accept((AutoautoVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public AutoautoAfterStatement getAfterStatement() {
    return findChildByClass(AutoautoAfterStatement.class);
  }

  @Override
  @NotNull
  public List<AutoautoCommentOpportunity> getCommentOpportunityList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, AutoautoCommentOpportunity.class);
  }

  @Override
  @Nullable
  public AutoautoFuncDefStatement getFuncDefStatement() {
    return findChildByClass(AutoautoFuncDefStatement.class);
  }

  @Override
  @Nullable
  public AutoautoFunctionCallStatement getFunctionCallStatement() {
    return findChildByClass(AutoautoFunctionCallStatement.class);
  }

  @Override
  @Nullable
  public AutoautoGotoStatement getGotoStatement() {
    return findChildByClass(AutoautoGotoStatement.class);
  }

  @Override
  @Nullable
  public AutoautoIfStatement getIfStatement() {
    return findChildByClass(AutoautoIfStatement.class);
  }

  @Override
  @Nullable
  public AutoautoLetStatement getLetStatement() {
    return findChildByClass(AutoautoLetStatement.class);
  }

  @Override
  @Nullable
  public AutoautoNextStatement getNextStatement() {
    return findChildByClass(AutoautoNextStatement.class);
  }

  @Override
  @Nullable
  public AutoautoSkipStatement getSkipStatement() {
    return findChildByClass(AutoautoSkipStatement.class);
  }

}

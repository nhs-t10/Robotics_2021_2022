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

public class AutoautoFrontMatterImpl extends ASTWrapperPsiElement implements AutoautoFrontMatter {

  public AutoautoFrontMatterImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull AutoautoVisitor visitor) {
    visitor.visitFrontMatter(this);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof AutoautoVisitor) accept((AutoautoVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<AutoautoCommentOpportunity> getCommentOpportunityList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, AutoautoCommentOpportunity.class);
  }

  @Override
  @NotNull
  public List<AutoautoFrontMatterKeyValue> getFrontMatterKeyValueList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, AutoautoFrontMatterKeyValue.class);
  }

}

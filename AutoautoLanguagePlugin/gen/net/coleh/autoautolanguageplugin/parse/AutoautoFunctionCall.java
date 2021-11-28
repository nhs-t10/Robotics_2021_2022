// This is a generated file. Not intended for manual editing.
package net.coleh.autoautolanguageplugin.parse;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface AutoautoFunctionCall extends PsiElement {

  @Nullable
  AutoautoArgumentList getArgumentList();

  @NotNull
  List<AutoautoCommentOpportunity> getCommentOpportunityList();

  @Nullable
  AutoautoDynamicValue getDynamicValue();

  @Nullable
  AutoautoValueInParens getValueInParens();

}

// This is a generated file. Not intended for manual editing.
package net.coleh.autoautolanguageplugin.parse;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface AutoautoSingleStatement extends PsiElement {

  @Nullable
  AutoautoAfterStatement getAfterStatement();

  @NotNull
  List<AutoautoCommentOpportunity> getCommentOpportunityList();

  @Nullable
  AutoautoFuncDefStatement getFuncDefStatement();

  @Nullable
  AutoautoFunctionCallStatement getFunctionCallStatement();

  @Nullable
  AutoautoGotoStatement getGotoStatement();

  @Nullable
  AutoautoIfStatement getIfStatement();

  @Nullable
  AutoautoLetStatement getLetStatement();

  @Nullable
  AutoautoNextStatement getNextStatement();

  @Nullable
  AutoautoSkipStatement getSkipStatement();

}

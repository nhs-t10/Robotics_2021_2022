package net.coleh.autoautolanguageplugin;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.util.PsiTreeUtil;

import net.coleh.autoautolanguageplugin.parse.AutoautoFile;
import net.coleh.autoautolanguageplugin.parse.AutoautoLabeledStatepath;
import net.coleh.autoautolanguageplugin.parse.AutoautoStatepath;

import java.util.ArrayList;
import java.util.List;

public class AutoautoUtil {

    /**
     * Searches the entire project for Simple language files with instances of the Simple property with the given key.
     *
     * @param file file to search in
     * @param statepathLabel     to check
     * @return matching properties
     */
    public static AutoautoLabeledStatepath findStatepath(PsiFile file, String statepathLabel) {
        if (file != null) {
            AutoautoLabeledStatepath[] statepaths = PsiTreeUtil.getChildrenOfType(file, AutoautoLabeledStatepath.class);
            if (statepaths != null) {
                for (AutoautoLabeledStatepath path : statepaths) {
                    if (path.getLabel().equals(statepathLabel)) {
                        return path;
                    }
                }
            }
        }
        return null;
    }
}

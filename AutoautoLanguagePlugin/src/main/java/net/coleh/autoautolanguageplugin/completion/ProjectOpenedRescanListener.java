package net.coleh.autoautolanguageplugin.completion;

import com.android.tools.idea.gradle.project.build.GradleBuildState;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManagerListener;

import org.jetbrains.annotations.NotNull;

public class ProjectOpenedRescanListener implements ProjectManagerListener {
    @Override
    public void projectOpened(@NotNull Project project) {
        AutoautoBuiltinFunctionCompletions.loadRecords(project.getBasePath());
    }
}

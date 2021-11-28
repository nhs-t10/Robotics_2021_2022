package net.coleh.autoautolanguageplugin.completion;

import com.android.tools.idea.gradle.project.build.BuildContext;
import com.android.tools.idea.gradle.project.build.BuildStatus;
import com.android.tools.idea.gradle.project.build.GradleBuildListener;
import com.android.tools.idea.gradle.project.build.invoker.GradleBuildInvoker;
import com.intellij.notification.EventLog;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManagerListener;
import com.intellij.openapi.startup.StartupActivity;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GradleBuildFinishedRescanListener implements GradleBuildListener {
    public void buildExecutorCreated(@NotNull GradleBuildInvoker.Request request) {}
    public void buildStarted(@NotNull BuildContext context) {}

    @Override
    public void buildFinished(@NotNull BuildStatus status, @Nullable BuildContext context) {
        if(context != null) AutoautoBuiltinFunctionCompletions.loadRecords(context.getProject().getBasePath());
    }
}

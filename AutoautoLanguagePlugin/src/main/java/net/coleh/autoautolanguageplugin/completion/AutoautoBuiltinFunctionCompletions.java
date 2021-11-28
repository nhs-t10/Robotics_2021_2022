package net.coleh.autoautolanguageplugin.completion;


import com.android.tools.idea.gradle.project.build.BuildContext;
import com.android.tools.idea.gradle.project.build.BuildStatus;
import com.android.tools.idea.gradle.project.build.GradleBuildState;
import com.android.tools.idea.gradle.project.build.GradleBuildListener;
import com.android.tools.idea.gradle.project.build.invoker.GradleBuildInvoker;
import com.intellij.openapi.project.Project;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;



public class AutoautoBuiltinFunctionCompletions {
    public static class AutoautoArg {
        String name;
        String type;

        public String getName() {
            return name;
        }

        public String getType() {
            return type;
        }

        public AutoautoArg(String name, String type) {
            this.name = name;
            this.type = type;
        }

        public String toString() {
            return type + " " + name;
        }
    }
    public static class AutoautoBuiltinFunctionRecord {
        String name;
        AutoautoArg[] args;

        public AutoautoBuiltinFunctionRecord(String name, AutoautoArg[] argNames) {
            this.name = name;
            this.args = argNames;
        }

        public String getName() {
            return name;
        }

        public String[] getArgsAsStrarr() {
            String[] s = new String[args.length];
            for(int i = 0; i < s.length; i++) s[i] = args[i].toString();
            return s;
        }
    }

    public static void loadRecords(String baseDirFolder) {
        ArrayList<AutoautoBuiltinFunctionRecord> recordsList = new ArrayList<>();

        if(baseDirFolder != null) {
            File f = new File(baseDirFolder);
            File robotFunctionsIndex = findRobotFunctionsIndex(f);
            if(robotFunctionsIndex != null) {
                processFileAndAddRecords(robotFunctionsIndex, recordsList);
            }
        }

        recordsList.add(new AutoautoBuiltinFunctionRecord("processed: " + recordsList.size() + " sly; bdf = " + baseDirFolder, new AutoautoArg[0]));

        records = recordsList.toArray(new AutoautoBuiltinFunctionRecord[0]);
    }

    public static void processFileAndAddRecords(File file, ArrayList<AutoautoBuiltinFunctionRecord> recordsList) {
        String fileContent = readFileContent(file);
        if(fileContent != null) {
            String[] lines = fileContent.split("\n");
            for(String line : lines) {
                String trimmed = line.trim();
                if(trimmed.startsWith("* ")) {
                    AutoautoBuiltinFunctionRecord record = parseCommentRecordLine(trimmed);
                    if(record != null) recordsList.add(record);
                }
            }
        }
    }

    private static AutoautoBuiltinFunctionRecord parseCommentRecordLine(String line) {
        line = line.substring(line.indexOf(' ') + 1);

        if (!line.contains("(")) return null;

        String name = line.substring(0, line.indexOf('('));
        AutoautoArg[] args = new AutoautoArg[0];

        String parameSrc = line.substring(line.indexOf('(') + 1, line.indexOf(')'));
        if(parameSrc.length() > 0) {
            String[] params = parameSrc.split(",");
            args = new AutoautoArg[params.length];

            for (int i = 0; i < params.length; i++) {
                String p = params[i];
                args[i] = new AutoautoArg(p.substring(p.indexOf(' ') + 1), p.substring(0, p.indexOf(' ')));
            }
        }

        return new AutoautoBuiltinFunctionRecord(name, args);
    }

    public static String readFileContent(File f) {
        try (FileInputStream s = new FileInputStream(f)) {
            StringBuilder r = new StringBuilder();

            int i;
            while((i = s.read()) != -1) r.append((char) i);

            return r.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static File findRobotFunctionsIndex(File rootDir) {
        return findRobotFunctionsIndex(rootDir, 0);
    }

    public static File findRobotFunctionsIndex(File rootDir, int depth) {
        if(rootDir.isDirectory()) {
            for(File child : rootDir.listFiles()) {
                boolean isBuildArtifactDirectory = depth < 5 && child.getName().equalsIgnoreCase("build");

                if(child.getName().equalsIgnoreCase(".autoauto-function-index.txt")) {
                    return child;
                } else if(child.isDirectory() && !isBuildArtifactDirectory) {
                    File f = findRobotFunctionsIndex(child, depth + 1);
                    if(f != null) return f;
                }
            }
        }
        return null;
    }

    private static boolean listenersAdded;
    public static void addListenersIfNeeded(Project p) {
        if(!listenersAdded) {
            listenersAdded = true;
            GradleBuildState.subscribe(p, new GradleBuildListener() {
                public void buildExecutorCreated(@NotNull GradleBuildInvoker.Request request) { }
                public void buildStarted(@NotNull BuildContext context) { }

                @Override
                public void buildFinished(@NotNull BuildStatus status, @Nullable BuildContext context) {
                    loadRecords(context.getProject().getBasePath());
                }
            });
        }
    }

    public static AutoautoBuiltinFunctionRecord[] getRecords(String basePathDir) {
        if(records == null) loadRecords(basePathDir);
        return records;
    }

    private static AutoautoBuiltinFunctionRecord[] records = null;
}

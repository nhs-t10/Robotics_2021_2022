package org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model;

import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.AutoautoString;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.AutoautoNumericValue;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.AutoautoRuntimeVariableScope;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.AutoautoSystemVariableNames;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.StoredAutoautoVariable;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.errors.AutoautoNameException;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class AutoautoProgram implements AutoautoProgramElement {
    public static String fileName;
    private String initialPathName;
    private String oldPathName;
    public LinkedHashMap<String, Statepath> paths;
    public Statepath currentPath;

    AutoautoRuntimeVariableScope scope;
    Location location;

    private StoredAutoautoVariable currentStatepathVariable;


    public static AutoautoProgram P(Statepath[] s, String initialPathName) {
        LinkedHashMap<String, Statepath> paths = new LinkedHashMap<>();
        for(Statepath p : s) paths.put(p.name, p);

        return new AutoautoProgram(paths, initialPathName);
    }

    public AutoautoProgram(LinkedHashMap<String, Statepath> s, String initialPathName) {
        this.paths = s;
        this.initialPathName = initialPathName;
        this.currentPath = paths.get(initialPathName);
    }
    public void init() {
        scope.systemSet(AutoautoSystemVariableNames.STATEPATH_NAME, new AutoautoString(this.initialPathName));
        scope.systemSet(AutoautoSystemVariableNames.STATE_NUMBER, new AutoautoNumericValue(0));

        this.currentStatepathVariable = scope.getConsistentVariableHandle(AutoautoSystemVariableNames.STATEPATH_NAME);

        for(Statepath p : this.paths.values()) p.init();
    }

    public void loop() {

        String currentStatepathName = this.currentStatepathVariable.value.getString();

        //if steps have changed, init the new one
        if(!currentStatepathName.equals(this.oldPathName)) {
            //reset current state
            scope.systemSet(AutoautoSystemVariableNames.STATE_NUMBER, new AutoautoNumericValue(0));

            this.currentPath = this.paths.get(currentStatepathName);
            if(this.currentPath == null) throw new AutoautoNameException(currentStatepathName + " is not a defined statepath. Make sure you didn't make a typo, Paul Max Braverman!");
            this.currentPath.stepInit();
            this.oldPathName = currentStatepathName;
        }

        this.currentPath.loop();
    }

    public void stepInit() {

        for(Statepath p : this.paths.values()) p.stepInit();
    }

    @NotNull
    public String toString() {
        StringBuilder pathsStr = new StringBuilder();
        for(Map.Entry<String, Statepath> entry : paths.entrySet()) {
            pathsStr.append(entry.getKey()).append(": ").append(entry.getValue().toString()).append("\n.\n");
        }
        return pathsStr.toString();
    }

    @Override
    public AutoautoRuntimeVariableScope getScope() {
        return scope;
    }

    @Override
    public void setScope(AutoautoRuntimeVariableScope scope) {
        this.scope = scope;
        for(Statepath s : paths.values()) s.setScope(scope);
    }

    @Override
    public Location getLocation() {
        return location;
    }

    @Override
    public void setLocation(Location location) {
        this.location = location;
    }

    @Override
    public AutoautoProgram clone() {
        LinkedHashMap<String, Statepath> pathsCloned = new LinkedHashMap<>();
        for(String p : paths.keySet()) {
            pathsCloned.put(p, paths.get(p).clone());
        }
        AutoautoProgram c = new AutoautoProgram(pathsCloned, initialPathName);
        c.setLocation(location);
        return c;
    }
}

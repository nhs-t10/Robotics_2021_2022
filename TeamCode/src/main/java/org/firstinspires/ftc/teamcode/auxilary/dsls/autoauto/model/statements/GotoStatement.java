package org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.statements;

import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.AutoautoProgram;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.Location;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.State;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.Statepath;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.AutoautoString;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.AutoautoRuntimeVariableScope;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.AutoautoSystemVariableNames;

public class GotoStatement extends Statement {
    String gotoPath;

    private AutoautoRuntimeVariableScope scope;
    private Location location;

    public static GotoStatement G(String path) {
        return new GotoStatement(path);
    }

    public GotoStatement(String path) {
        this.gotoPath = path;
    }

    public void loop() {
        scope.systemSet(AutoautoSystemVariableNames.STATEPATH_NAME, new AutoautoString(gotoPath));
    }

    @Override
    public GotoStatement clone() {
        GotoStatement c = new GotoStatement(gotoPath);
        c.setLocation(location);
        return c;
    }

    public String toString() {
        return "goto " + this.gotoPath;
    }

    @Override
    public AutoautoRuntimeVariableScope getScope() {
        return scope;
    }

    @Override
    public void setScope(AutoautoRuntimeVariableScope scope) {
        this.scope = scope;
    }

    @Override
    public Location getLocation() {
        return location;
    }

    @Override
    public void setLocation(Location location) {
        this.location = location;
    }
}

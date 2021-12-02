package org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.statements;

import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.Location;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.AutoautoNumericValue;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.AutoautoRuntimeVariableScope;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.AutoautoSystemVariableNames;
import org.firstinspires.ftc.teamcode.managers.feature.FeatureManager;
import org.jetbrains.annotations.NotNull;

public class SkipStatement extends Statement {
    public int delta;
    private AutoautoRuntimeVariableScope scope;
    private Location location;

    int stateCount = -1;

    public SkipStatement(int delta) {
        this.delta = delta;
    }
    public void loop() {
        int currentState = (int)((AutoautoNumericValue)scope.get(AutoautoSystemVariableNames.STATE_NUMBER)).getFloat();
        int nextState = (currentState + delta) % stateCount;

        scope.systemSet(AutoautoSystemVariableNames.STATE_NUMBER, new AutoautoNumericValue(nextState));
    }

    @Override
    public void init() {
        this.stateCount = (int)((AutoautoNumericValue)scope.get(AutoautoSystemVariableNames.STATE_COUNT_OF_PREFIX + location.statepath)).getFloat();
    }

    @Override
    public SkipStatement clone() {
        SkipStatement c = new SkipStatement(delta);
        c.setLocation(location);
        return c;
    }

    @NotNull
    public String toString() {
        return "skip " + delta;
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

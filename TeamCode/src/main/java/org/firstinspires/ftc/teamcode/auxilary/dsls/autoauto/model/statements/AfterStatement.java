package org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.statements;

import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.Location;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.AutoautoCallableValue;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.primitives.AutoautoPrimitive;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.primitives.AutoautoNumericValue;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.primitives.AutoautoUndefined;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.primitives.AutoautoUnitValue;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.AutoautoRuntimeVariableScope;
import org.firstinspires.ftc.teamcode.auxilary.units.DistanceUnit;
import org.firstinspires.ftc.teamcode.auxilary.units.RotationUnit;
import org.jetbrains.annotations.NotNull;

public class AfterStatement extends Statement {
    //Attributes
    AutoautoUnitValue wait;
    Statement action;

    Location location;
    AutoautoRuntimeVariableScope scope;

    private long stepStartTime = 0;
    private float stepStartTick;

    //Constructors
    public static AfterStatement W (AutoautoUnitValue wait, Statement action) {
        return new AfterStatement(wait, action);
    }

    public AfterStatement(AutoautoUnitValue wait, Statement action) {
        this.wait = wait.convertToNaturalUnit();
        this.action = action;
    }

    AutoautoCallableValue getTicks;
    boolean isDistanceUnit;

    @NotNull
    public String toString() {
        return "after " + this.wait.toString() + " " + this.action.toString();
    }

    @Override
    public void init() {
        action.init();
        wait.init();
    }

    @Override
    public AfterStatement clone() {
        AfterStatement c = new AfterStatement(wait.clone(), action.clone());
        c.setLocation(location);
        return c;
    }

    String[][] unitMethodMapping = new String[][] {
            {DistanceUnit.naturalDistanceUnit.name, "getCentimeters"},
            {RotationUnit.naturalRotationUnit.name, "getThirdAngleOrientation"},
            {"ticks", "getTicks"}
    };

    private boolean unitExistsInMethodMapping(String unit) {
        for(String[] s : unitMethodMapping) {
            if(s[0].equals(unit)) return true;
        }
        return false;
    }
    private String getUnitMethodFromMapping(String unit) {
        for(String[] s : unitMethodMapping) {
            if(s[0].equals(unit)) return s[1];
        }
        throw new IllegalArgumentException("No such unit `" + unit + "` in mapping.");
    }

    @Override
    public void stepInit() {

        if(unitExistsInMethodMapping(wait.originalUnitName) && wait.unitType == AutoautoUnitValue.UnitType.UNKNOWN) wait.unitType = AutoautoUnitValue.UnitType.DISTANCE;

        switch (wait.unitType) {
            case TIME:
                this.stepStartTime = System.currentTimeMillis();
                break;
            case DISTANCE:
            case ROTATION:
                getTicks = (AutoautoCallableValue) scope.get(getUnitMethodFromMapping(wait.unit.name));
                this.stepStartTick = ((AutoautoNumericValue)getTicks.call(new AutoautoUndefined(), new AutoautoPrimitive[0])).getFloat();
                break;
        }
    }

    public void loop() {
        switch (wait.unitType) {
            case TIME:
                if (System.currentTimeMillis() >= stepStartTime + wait.value) action.loop();
                break;
            case DISTANCE:
            case ROTATION:
                double targetDifference = wait.value;
                float referPoint = stepStartTick;
                float currentPosition = ((AutoautoNumericValue)getTicks.call(new AutoautoUndefined(), new AutoautoPrimitive[0])).getFloat();

                if(Math.abs(currentPosition - referPoint) >= Math.abs(targetDifference)) action.loop();
                break;
        }
    }

    @Override
    public AutoautoRuntimeVariableScope getScope() {
        return scope;
    }

    @Override
    public void setScope(AutoautoRuntimeVariableScope scope) {
        this.scope = scope;
        action.setScope(scope);
        wait.setScope(scope);
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

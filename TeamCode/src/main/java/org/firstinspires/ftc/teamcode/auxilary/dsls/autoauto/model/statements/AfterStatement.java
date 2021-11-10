package org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.statements;

import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.Location;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.AutoautoCallableValue;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.AutoautoPrimitive;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.AutoautoNumericValue;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.AutoautoUnitValue;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.AutoautoRuntimeVariableScope;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.errors.AutoautoNameException;
import org.firstinspires.ftc.teamcode.auxilary.units.DistanceUnits;
import org.firstinspires.ftc.teamcode.auxilary.units.RotationUnits;
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
        this.wait = wait;
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
            {"D", DistanceUnits.naturalDistanceUnit.name, "getCentimeters"},
            {"D", RotationUnits.naturalRotationUnit.name, "getThirdAngleOrientation"},
            {"D", "ticks", "getTicks"}
    };

    private boolean unitExistsInMethodMapping(String unit) {
        for(String[] s : unitMethodMapping) {
            if(s[1].equals(unit)) return true;
        }
        return false;
    }
    private String getUnitMethodFromMapping(String unit) {
        for(String[] s : unitMethodMapping) {
            if(s[1].equals(unit)) return s[2];
        }
        throw new IllegalArgumentException("No such unit `" + unit + "` in mapping.");
    }
    private boolean checkUnitIsDistance(String unit) {
        for(String[] s : unitMethodMapping) {
            if(s[1].equals(unit)){
                return s[0].equals("D");
            }
        }
        return false;
    }

    @Override
    public void stepInit() {

        if(unitExistsInMethodMapping(wait.unit)) wait.unitType = AutoautoUnitValue.UnitType.DISTANCE;

        switch (wait.unitType) {
            case TIME:
                this.stepStartTime = System.currentTimeMillis();
                break;
            case DISTANCE:
            case ROTATION:
                getTicks = (AutoautoCallableValue) scope.get(getUnitMethodFromMapping(wait.unit));
                this.stepStartTick = ((AutoautoNumericValue)getTicks.call(new AutoautoPrimitive[0])).getFloat();
                break;
        }
    }

    public void loop() {
        switch (wait.unitType) {
            case TIME:
                if (System.currentTimeMillis() >= stepStartTime + wait.baseAmount) action.loop();
                break;
            case DISTANCE:
            case ROTATION:
                float targetDifference = wait.baseAmount;
                float referPoint = stepStartTick;
                float currentPosition = ((AutoautoNumericValue)getTicks.call(new AutoautoPrimitive[0])).getFloat();

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

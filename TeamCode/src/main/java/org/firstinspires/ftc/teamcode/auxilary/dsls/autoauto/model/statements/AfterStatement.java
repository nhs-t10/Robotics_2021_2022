package org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.statements;

import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.Location;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.AutoautoCallableValue;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.AutoautoPrimitive;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.AutoautoNumericValue;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.AutoautoUnitValue;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.AutoautoRuntimeVariableScope;
import org.jetbrains.annotations.NotNull;

public class AfterStatement extends Statement {
    AutoautoUnitValue wait;
    Statement action;

    Location location;
    AutoautoRuntimeVariableScope scope;

    private long stepStartTime = 0;
    private float stepStartTick;

    public static AfterStatement W (AutoautoUnitValue wait, Statement action) {
        return new AfterStatement(wait, action);
    }

    public AfterStatement(AutoautoUnitValue wait, Statement action) {
        this.wait = wait;
        this.action = action;
    }

    AutoautoCallableValue getTicks;
    boolean waitUnits;

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

    @Override
    public void stepInit() {
        this.stepStartTime = System.currentTimeMillis();
        waitUnits = wait.unit.equals("ticks") || wait.unit.equals("hticks") || wait.unit.equals("vticks") || wait.unit.equals("degs");
        if(waitUnits) {
            getTicks = (AutoautoCallableValue) scope.get(
                    wait.unit.equals("ticks") ? "getTicks" :
                            wait.unit.equals("hticks") ? "getHorizontalTicks" :
                                    wait.unit.equals("vticks") ? "getVerticalTicks" :
                                            wait.unit.equals("degs") ? "getThirdAngleOrientation" : "ERROR BAD BAD UNIT");
            this.stepStartTick = ((AutoautoNumericValue)getTicks.call(new AutoautoPrimitive[0])).getFloat();
        }
    }

    public void loop() {
        if(waitUnits) {
            float tarTicks = wait.baseAmount;
            float ticksReferPoint = stepStartTick;


            float cTicks = ((AutoautoNumericValue)getTicks.call(new AutoautoPrimitive[0])).getFloat();

            
            if(Math.abs(cTicks - ticksReferPoint) >= Math.abs(tarTicks)) action.loop();
        } else {
            if (System.currentTimeMillis() >= stepStartTime + wait.baseAmount) action.loop();
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

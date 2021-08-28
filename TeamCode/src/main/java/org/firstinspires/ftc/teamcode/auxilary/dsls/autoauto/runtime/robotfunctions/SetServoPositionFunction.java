package org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.robotfunctions;

import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.AutoautoProgram;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.AutoautoPrimitive;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.AutoautoUndefined;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.AutoautoNumericValue;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.NativeRobotFunction;
import org.firstinspires.ftc.teamcode.managers.FeatureManager;

public class SetServoPositionFunction extends NativeRobotFunction {
    public String name = "setServoPosition";
    public int argCount = 2;
    public Class<?> declaringClass = org.firstinspires.ftc.teamcode.managers.ManipulationManager.class;

    private org.firstinspires.ftc.teamcode.managers.ManipulationManager manager;

    public SetServoPositionFunction(FeatureManager manager) {
        this.manager = (org.firstinspires.ftc.teamcode.managers.ManipulationManager)manager;
    }

    public AutoautoPrimitive call(AutoautoPrimitive[] args) {
        if(args.length < 2) FeatureManager.logger.log(args.length + " is not a valid argument count for " + name + "()" + AutoautoProgram.formatStack(this.getLocation()));
        if(args[0] == null) FeatureManager.logger.log( "args[0] is null for " + name + "()" + AutoautoProgram.formatStack(this.getLocation()));
        if(args[1] == null) FeatureManager.logger.log( "args[1] is null for " + name + "()" + AutoautoProgram.formatStack(this.getLocation()));

        manager.setServoPosition((int)((AutoautoNumericValue)args[0]).getFloat(), ((AutoautoNumericValue)args[1]).getFloat());
        return new AutoautoUndefined();
    }
}
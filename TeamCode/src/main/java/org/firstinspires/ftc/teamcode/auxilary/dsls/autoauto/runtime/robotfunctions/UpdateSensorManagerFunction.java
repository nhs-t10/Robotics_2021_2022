package org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.robotfunctions;

import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.*;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.NativeRobotFunction;
import org.firstinspires.ftc.teamcode.managers.FeatureManager;

public class UpdateSensorManagerFunction extends NativeRobotFunction {
    private org.firstinspires.ftc.teamcode.managers.sensor.SensorManager manager;

    public UpdateSensorManagerFunction(FeatureManager manager) {
        this.manager = (org.firstinspires.ftc.teamcode.managers.sensor.SensorManager)manager;
    }

    @Override
    public AutoautoPrimitive call(AutoautoPrimitive[] args) {
        if(args.length == 0) {manager.update(); return new AutoautoUndefined();}if(args.length == 1) {if(args[0] instanceof AutoautoNumericValue) {manager.update((int)((AutoautoNumericValue)args[0]).getFloat()); return new AutoautoUndefined();}}throw new org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.errors.AutoautoNoNativeMethodOverloadException("No updateSensorManager with 1 args");
    }
}
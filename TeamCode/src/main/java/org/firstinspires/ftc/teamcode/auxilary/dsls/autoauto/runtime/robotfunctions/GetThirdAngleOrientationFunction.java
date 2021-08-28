package org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.robotfunctions;

import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.AutoautoPrimitive;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.AutoautoNumericValue;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.NativeRobotFunction;
import org.firstinspires.ftc.teamcode.managers.FeatureManager;
import org.firstinspires.ftc.teamcode.managers.imu.ImuManager;

public class GetThirdAngleOrientationFunction extends NativeRobotFunction {
    public String name = "getThirdAngleOrientation";
    public int argCount = 0;
    public Class<?> declaringClass = ImuManager.class;


    private ImuManager manager;

    public GetThirdAngleOrientationFunction(FeatureManager manager) {
        this.manager = (ImuManager)manager;
    }

    public AutoautoPrimitive call(AutoautoPrimitive[] args) {
        return new AutoautoNumericValue( (float)manager.getOrientation().thirdAngle );
    }

}

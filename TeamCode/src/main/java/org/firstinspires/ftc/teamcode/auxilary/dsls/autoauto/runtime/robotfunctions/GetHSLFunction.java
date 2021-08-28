package org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.robotfunctions;

import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.AutoautoTable;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.AutoautoPrimitive;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.AutoautoValue;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.AutoautoNumericValue;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.NativeRobotFunction;
import org.firstinspires.ftc.teamcode.managers.FeatureManager;
import org.firstinspires.ftc.teamcode.managers.sensor.SensorManager;

public class GetHSLFunction extends NativeRobotFunction {
    public String name = "getHSL";
    public int argCount = 1;
    public Class<?> declaringClass = SensorManager.class;

    private SensorManager manager;

    public GetHSLFunction(FeatureManager manager) {
        this.manager = (SensorManager)manager;
    }

    public AutoautoPrimitive call(AutoautoPrimitive[] args) {
        float[] v = manager.getHSL((int)((AutoautoNumericValue)args[0]).getFloat());

        AutoautoValue[] res = new AutoautoValue[v.length];

        for(int i = 0; i < v.length; i++) {
            res[i] = new AutoautoNumericValue(v[i]);
        }

        return new AutoautoTable(res);
    }
}
package org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime;

import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.AutoautoNumericValue;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.errors.ManagerSetupException;
import org.firstinspires.ftc.teamcode.managers.feature.FeatureManager;
import org.firstinspires.ftc.teamcode.managers.manipulation.ManipulationManager;
import org.firstinspires.ftc.teamcode.managers.sensor.SensorManager;

public class ManagerDeviceScanner {
    public static void scan(AutoautoRuntimeVariableScope scope, FeatureManager... managers) throws ManagerSetupException {
        ManipulationManager manip = null;
        SensorManager sense = null;
        for(FeatureManager f : managers) {
            if(f instanceof ManipulationManager) manip = (ManipulationManager)f;
            if(f instanceof SensorManager) sense = (SensorManager)f;
        }

        if(manip == null) throw new ManagerSetupException("No manipulation manager");
        if(sense == null) throw new ManagerSetupException("No sensor manager");

        for(int i = manip.servoNames.length - 1; i >= 0; i--) scope.put(manip.servoNames[i], new AutoautoNumericValue(i));
        for(int i = manip.crservoNames.length - 1; i >= 0; i--) scope.put(manip.crservoNames[i], new AutoautoNumericValue(i));
        for(int i = manip.motorNames.length - 1; i >= 0; i--) scope.put(manip.motorNames[i], new AutoautoNumericValue(i));

        for(int i = sense.sensorNames.length - 1; i >= 0; i--) scope.put(sense.sensorNames[i], new AutoautoNumericValue(i));
    }
}

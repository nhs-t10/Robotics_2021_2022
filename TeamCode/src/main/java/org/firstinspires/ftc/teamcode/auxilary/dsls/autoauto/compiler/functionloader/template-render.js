module.exports = function(callMethodSource, definingClass, classname) {
    return `package org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.robotfunctions;

import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.*;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.NativeRobotFunction;
import org.firstinspires.ftc.teamcode.managers.FeatureManager;

public class ${classname} extends NativeRobotFunction {
    private ${definingClass} manager;

    public ${classname}(FeatureManager manager) {
        this.manager = (${definingClass})manager;
    }

    @Override
    public AutoautoPrimitive call(AutoautoPrimitive[] args) {
        ${callMethodSource}
    }
}`;
}
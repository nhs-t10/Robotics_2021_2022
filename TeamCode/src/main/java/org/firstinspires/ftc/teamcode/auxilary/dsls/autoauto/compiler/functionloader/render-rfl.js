module.exports = function(loadFunctionsSrc, margs) {
    return `package org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime;

    import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.robotfunctions.*;
    import org.firstinspires.ftc.teamcode.managers.imu.ImuManager;
    import org.firstinspires.ftc.teamcode.managers.manipulation.ManipulationManager;
    import org.firstinspires.ftc.teamcode.managers.movement.MovementManager;
    import org.firstinspires.ftc.teamcode.managers.sensor.SensorManager;
    
    import java.util.ArrayList;
    
    public class RobotFunctionLoader {
    
        public static void loadFunctions(${margs}, AutoautoRuntimeVariableScope scope) {
${indent(3, remExcessiveWhitespace(loadFunctionsSrc))}
        }
    }
    `;
}

function remExcessiveWhitespace(src) {
    return src.replace(/\n\n+/g, "\n\n");
}

function indent(lvl, src) {
    return src.split("\n").map(x=>"    ".repeat(lvl)+x).join("\n");
}
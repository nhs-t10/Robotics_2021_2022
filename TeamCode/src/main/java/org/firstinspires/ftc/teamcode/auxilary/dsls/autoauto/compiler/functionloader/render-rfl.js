module.exports = function(loadFunctionsSrc, margs) {
    return `package org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime;

    import org.firstinspires.ftc.teamcode.managers.FeatureManager;
    import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.robotfunctions.*;
    import java.util.ArrayList;
    
    public class RobotFunctionLoader {
    
        public static void loadFunctions(AutoautoRuntimeVariableScope scope, FeatureManager... managers) {
${indent(3,margs.map(x=>x[0] + " " + x[1] + " = null;").join("\n"))}
            for(FeatureManager f : managers) {
${indent(4,margs.map(x=>"if(f instanceof " + x[0] + ") " + x[1] + " = (" + x[0] + ")f;").join("\n"))}
            }
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
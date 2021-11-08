package org.firstinspires.ftc.teamcode.managers.feature.robotconfiguration;

import org.firstinspires.ftc.teamcode.managers.feature.FeatureManager;

public class OmniCalcComponents {
    public WheelCoefficients hor, ver, rot;

    public OmniCalcComponents(WheelCoefficients h, WheelCoefficients v, WheelCoefficients r) {
        //assemble into an array for directive-checking
        WheelCoefficients[] coefs = new WheelCoefficients[] {h, v, r};

        //fail-safe: if non-directive'd coefs are given, then it'll use HVR to enter them by order.
        this.hor = coefs[0];
        this.ver = coefs[1];
        this.rot = coefs[2];

        for(WheelCoefficients c : coefs) {
            if(c.directive == WheelCoefficients.WheelDirective.OMNI_HOR) hor = c;
            if(c.directive == WheelCoefficients.WheelDirective.OMNI_VER) ver = c;
            if(c.directive == WheelCoefficients.WheelDirective.OMNI_ROT) rot = c;
        }
    }
}

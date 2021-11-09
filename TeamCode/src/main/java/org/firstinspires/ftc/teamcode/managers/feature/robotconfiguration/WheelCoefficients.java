package org.firstinspires.ftc.teamcode.managers.feature.robotconfiguration;


import org.firstinspires.ftc.teamcode.managers.feature.FeatureManager;

public class WheelCoefficients {
    public enum WheelDirective { OMNI_HOR, OMNI_VER, OMNI_ROT, MOTORS };

    public static WheelCoefficients W(float fl, float fr, float bl, float br) {
        return new WheelCoefficients(fl, fr, bl, br);
    }
    public static WheelCoefficients horizontal(float fl, float fr, float bl, float br) {
        return new WheelCoefficients(fl, fr, bl, br, WheelDirective.OMNI_HOR);
    }
    public static WheelCoefficients rotational(float fl, float fr, float bl, float br) {
        return new WheelCoefficients(fl, fr, bl, br, WheelDirective.OMNI_ROT);
    }
    public static WheelCoefficients vertical(float fl, float fr, float bl, float br) {
        return new WheelCoefficients(fl, fr, bl, br, WheelDirective.OMNI_VER);
    }

    public float fl, fr, bl, br;
    public WheelDirective directive;

    public WheelCoefficients(float fl, float fr, float bl, float br, WheelDirective directive) {
        this.fl = fl;
        this.fr = fr;
        this.bl = bl;
        this.br = br;
        this.directive = directive;
    }

    public WheelCoefficients(float fl, float fr, float bl, float br) {
        this(fl, fr, bl,br, WheelDirective.MOTORS);
    }
}
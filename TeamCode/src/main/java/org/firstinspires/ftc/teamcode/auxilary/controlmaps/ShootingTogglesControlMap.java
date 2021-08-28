package org.firstinspires.ftc.teamcode.auxilary.controlmaps;

public class ShootingTogglesControlMap extends ControlMap {
    public static String drive = "vector3(leftStickY, leftStickX, rightStickX)";
    public static String fullIntake = "toggleBetween(leftTrigger, 0, 1)";
    public static String lt = "leftTrigger";
    public static String wobbleGraber = "toggleBetween(rightBumper, 0, 0.65)";
    public static String wobbleGraberNegative = "toggleBetween(leftBumper, 0, 0.65)";
}

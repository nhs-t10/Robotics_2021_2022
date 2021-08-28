package org.firstinspires.ftc.teamcode.auxilary.controlmaps;

public class OneControllerContolMap extends ControlMap {
    public static String drive = "vector3(deadzone(leftStickY,0.1), deadzone(leftStickX,0.05), rightStickX)";
    public static String intake = "ternary(circle, -1, ternary(square, 1, ternary(leftTrigger, -1, 0)))";
    public static String drum = "ternary(dpadUp, -1, ternary(dpadDown, 1, ternary(leftTrigger, -1, 0)))";
    public static String shooterArm = "ternary(greaterThan(cross, 0.1), 0, 0.57)";
    public static String flywheel = "scale(ternary(triangle, -0.3, ternary(greaterThan(rightTrigger, 0.3), 1, 0)), 1)";
    public static String wobbleGrabRight = "ternary(rightBumper, 1, 0)";
    public static String wobbleGrabLeft = "ternary(rightBumper, 1, 0)";
    public static String wobbleArmRight = "ternary(leftBumper, 0, 0.2164)";
    public static String wobbleArmLeft = "ternary(leftBumper, 0, 0.2164)";

}

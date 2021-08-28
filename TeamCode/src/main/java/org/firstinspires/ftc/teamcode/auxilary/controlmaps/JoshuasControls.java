package org.firstinspires.ftc.teamcode.auxilary.controlmaps;

public class JoshuasControls extends ControlMap {
    public static String drive = "vector3(deadzone(leftStickY,0.15), deadzone(leftStickX,0.15), rightStickX)";
    public static String shootingServo = "press(x)";
    public static String flywheels = "vector2(hold(rightTrigger), hold(rightTrigger))";
    public static String wobbleGoalPosition = "toggleBetween(leftBumper, 1, 0.5)";
    public static String wobbleGoalClaw = "toggleBetween(rightBumper, 0, 1)";
    public static String drum = "ternary(or(dpadUp, dpadDown), ternary(dpadUp, 1, -1), wasPressed(getVariable(intakeMovement)) )";
    public static String intake = "setVariable(intakeMovement, toggle(leftTrigger))";
}

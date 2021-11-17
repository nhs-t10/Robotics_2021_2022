package org.firstinspires.ftc.teamcode.managers.telemetry.fallible;

public enum
FailureType {
    /**
     * Turns off motors, servos, etc. Denies further control.
     */
    POWER_FAILURE,

    /**
     * Denies further control. Does NOT turn off motors or sensors. Be careful with this one.
     */
    CONTROL_FAILURE,

    /**
     * For each method call, there is a 50% chance of silent failure. In getter calls, a failure means returning 0, 0.0, false, or a default-constructed object.
     */
    INTERMITTENT_FAILURES,

    /**
     * Sensors keep reporting their last value before failing. Motors, servos, etc. behave normally, but their encoders fail in the aforementioned manner.
     */
    SENSOR_FAILURE,

    /**
     * Sensors report random, nonsensical values. Motors, servos, etc. behave normally, but their encoders fail in the aforementioned manner.
     */
    SENSOR_FLAILURE,

    /**
     * Motor directions reverse.
     */
    DIRECTION_FAILURE,

    /**
     * All motor encoder values are multiplied by 28.
     */
    CONFIG_FAILURE,

    /**
     * Normal behavior.
     */
    NOT_FAILING,

    /**
     * Cause motor power to multiply by -1 once every 3-8s.
     */
    JERKING
}

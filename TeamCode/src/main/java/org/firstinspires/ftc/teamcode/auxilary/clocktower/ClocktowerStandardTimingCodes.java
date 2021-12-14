package org.firstinspires.ftc.teamcode.auxilary.clocktower;

import org.firstinspires.ftc.teamcode.auxilary.problems.ProblemTypeCodes;

import java.lang.reflect.Field;

public class ClocktowerStandardTimingCodes {

    public static final int MAIN_OPMODE_LOOP = 0;
    public static final int MOTOR_INTEGRATION_THREAD = 1;
    public static final int IMU_INTEGRATION_THREAD = 2;
    public static final int SENSOR_COMBINATION_THREAD = 3;
    public static final int MACRO_RUNNER_THREAD = 4;
    public static final int INPUT_NODE_UPDATER_THREAD = 5;
    public static final int TELEMETRY_STREAM_SENDER_THREAD = 6;
    public static final int CV_UPDATE_LOOP = 5;

    public static int size() {
        return getCodes().length;
    }
    public static String[] getCodes() {
        Field[] fields = ProblemTypeCodes.class.getDeclaredFields();
        String[] names = new String[fields.length];

        for(int i = 0; i < fields.length; i++) {
            names[i] = fields[i].getName();
        }

        return names;
    }
}

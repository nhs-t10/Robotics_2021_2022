package org.firstinspires.ftc.teamcode.auxilary.clocktower;

/**
 * A centralized place to report loop times. Reporting a loop in Clocktower means that it automatically gets Telemetry'd.
 */
public class Clocktower {
    public static long[] starts = new long[ClocktowerStandardTimingCodes.size()];
    public static long[] durations = new long[starts.length];

    public void time(int code) {
        if(code < 0) throw new IllegalArgumentException("A Clocktower loop code MUST be 0 or greater. Try using the ClocktowerStandardTimingCodes class.");
        if(code >= starts.length) throw new IllegalArgumentException("A Clocktower loop code MUST be within the range of the `starts` array. Try using the ClocktowerStandardTimingCodes class; adding a `public static final int` field will automatically re-size the array.");

        long now = System.nanoTime();
        if(starts[code] != 0) durations[code] = now - starts[code];
        starts[code] = now;
    }
}

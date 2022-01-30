package org.firstinspires.ftc.teamcode.auxilary;

import android.os.Build;

import java.time.Clock;
import java.time.ZoneId;

import androidx.annotation.RequiresApi;

/**
 * A class that lets us play around with the time. This is <i>very</i> fast, and lets us get precision over System.currentTimeMillis, where it's possible.
 */
public class RobotTime {
    static Clock clock = Clock.systemUTC();

    /**
     * The same as System.currentTimeMillis, but with greater precision if the device allows it
     * @see System#currentTimeMillis()
     */
    public static long currentTimeMillis() {
        return clock.millis();
    }

    /**
     * The same as System.nanoTime, but with greater precision if the device allows it
     * @see System#nanoTime()
     */
    public static long nanoTime() {
        return clock.instant().getNano();
    }
}

package org.firstinspires.ftc.teamcode.managers;

import com.qualcomm.robotcore.eventloop.opmode.OpModeManager;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.io.PrintStream;
import java.util.Arrays;

public class FeatureManager {

    public final static float SPEED = 0.6f;
    public final static int TICK_PER_ROT = 1680;

    public final static float P = 0.03f;
    public final static double ENCODER_CPR = 1680;
    public final static double GEAR_RATIO = 1;
    public final static double WHEEL_DIAMETER = 4;
    public final static double SLIP = 0.7;
    public final static double CIRCUMFERENCE = Math.PI * WHEEL_DIAMETER;
    public final static float EXPONENTIAL_SCALAR = 3f;

    public final static int INPUT_DOUBLECLICK_TIME = 400;

    public static final Logger logger = new Logger();

    public static boolean debug = false;

    public static boolean isOpModeRunning = false;

    public static OpModeManager opModeManager;

    public static void setOpModeManager(OpModeManager o) {
        opModeManager = o;
    }

    public static void setIsOpModeRunning(boolean b) {
        isOpModeRunning = b;
    }

    public static class Logger {
        private Telemetry.Log backend;
        private PrintStream fallbackBackend;
        private boolean usesFallback;
        public void setBackend(Telemetry.Log log) {
            backend = log;
            usesFallback = false;
        }

        public void setBackend(PrintStream log) {
            fallbackBackend = log;
            usesFallback = true;
        }

        public Logger() {this.fallbackBackend = System.out; this.usesFallback = true;}

        public Logger(Telemetry.Log log) { this.backend = log;}

        public void log(String l) {
            if(usesFallback) fallbackBackend.println(l);
            else backend.add(l);
        }
        public void log(String n, Object l) {
            if(!usesFallback) backend.add(n, l);
        }

        public void log(Object l) {log(l.toString());}
        public void log(double l) {log("" + l);}
        public void log(boolean l) {log("" + l);}
        public void log(int l) {log("" + l);}
        public void log(float l) {log("" + l);}
        public void log(long l) {log("" + l);}
        public void log(char l) {log("" + l);}
        public void log(Object[] l) {log(Arrays.toString(l));}
        public void log(double[] l) { log(Arrays.toString(l));}
        public void log(boolean[] l) { log(Arrays.toString(l));}
        public void log(int[] l) { log(Arrays.toString(l));}
        public void log(float[] l) { log(Arrays.toString(l));}
        public void log(long[] l) { log(Arrays.toString(l));}
        public void log(char[] l) { log(Arrays.toString(l));}

        public void add(Object l) {log(l.toString());}
        public void add(double l) {log("" + l);}
        public void add(boolean l) {log("" + l);}
        public void add(int l) {log("" + l);}
        public void add(float l) {log("" + l);}
        public void add(long l) {log("" + l);}
        public void add(char l) {log("" + l);}
        public void add(Object[] l) {log(Arrays.toString(l));}
        public void add(double[] l) { log(Arrays.toString(l));}
        public void add(boolean[] l) { log(Arrays.toString(l));}
        public void add(int[] l) { log(Arrays.toString(l));}
        public void add(float[] l) { log(Arrays.toString(l));}
        public void add(long[] l) { log(Arrays.toString(l));}
        public void add(char[] l) { log(Arrays.toString(l));}

        public void println(Object l) {log(l.toString());}
        public void println(double l) {log("" + l);}
        public void println(boolean l) {log("" + l);}
        public void println(int l) {log("" + l);}
        public void println(float l) {log("" + l);}
        public void println(long l) {log("" + l);}
        public void println(char l) {log("" + l);}
        public void println(Object[] l) {log(Arrays.toString(l));}
        public void println(double[] l) { log(Arrays.toString(l));}
        public void println(boolean[] l) { log(Arrays.toString(l));}
        public void println(int[] l) { log(Arrays.toString(l));}
        public void println(float[] l) { log(Arrays.toString(l));}
        public void println(long[] l) { log(Arrays.toString(l));}
        public void println(char[] l) { log(Arrays.toString(l));}


        public void println(String l) {
            log(l);
        }
        public void add(String l) {
            log(l);
        }

        public void logToPhone(String l) {
            if(usesFallback) fallbackBackend.println(l);
            else backend.add(l);
        }
    }
}

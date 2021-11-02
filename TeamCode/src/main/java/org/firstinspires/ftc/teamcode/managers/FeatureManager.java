package org.firstinspires.ftc.teamcode.managers;

import com.qualcomm.robotcore.eventloop.opmode.OpModeManager;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.auxilary.FileSaver;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;

public class FeatureManager {
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

    public static final RobotConfiguration bigBoyConfiguration = new RobotConfiguration(0.5f,0.5f,0.5f,0.5f
            ,
            0.03f, 1680, 1, 8.9, 0.7, 3f);

    public static final RobotConfiguration littleBoyConfiguration = new RobotConfiguration(-1,1,-1,1,
            0.03f, 1680, 1, 4, 0.7, 3f);

    public static final RobotConfiguration defaultConfiguration = littleBoyConfiguration;


    private static RobotConfiguration cachedConfiguration;

    public static RobotConfiguration getRobotConfiguration() {
        //if it's been cached, don't bother re-loading everything. Just return the cache.
        if(cachedConfiguration != null) return cachedConfiguration;

        ArrayList<String> lines = (new FileSaver(RobotConfiguration.fileName)).readLines();

        //if the file doesn't exist, return the default. This doesn't adjust the cache, so if there's a later edit, it'll be loaded.
        if(lines.size() == 0) return defaultConfiguration;

        String fileContent = lines.get(0);

        switch (fileContent) {
            case RobotConfiguration.bigBoyFileContent:
                cachedConfiguration = bigBoyConfiguration;
                break;
            case RobotConfiguration.littleBoyFileContent:
                cachedConfiguration = littleBoyConfiguration;
                break;
            default:
                cachedConfiguration = defaultConfiguration;
                break;
        }
        return cachedConfiguration;
    }

    public static class RobotConfiguration {
        public final static String fileName = "configuration";
        public final static String bigBoyFileContent = "bigBoy";
        public final static String littleBoyFileContent = "littleBoy";

        public float flCoef;
        public float frCoef;
        public float blCoef;
        public float brCoef;

        /**
         * The `p` coefficient of a PID controller. This should not be used, since we want to be able to use different coefficients in different situations.
         */
        @Deprecated
        public float pidPCoefficient;
        /**
         * How many "ticks" quantify a rotation of the motor.
         */
        public double encoderTicksPerRotation;
        /**
         * The gear ratio of the main drive motors.
         */
        public double gearRatio;
        /**
         * The diameter of the main drive wheels, in centimeters
         */
        public double wheelDiameterCm;
        /**
         * A coefficient indicating how much sliding we can expect of the wheels. 1 is perfect traction; 0 is no traction at all.
         */
        public double slip;
        /**
         * The circumference of the main drive wheels, in centimeters
         */
        public double wheelCircumference;
        public float exponentialScalar;

        public RobotConfiguration(float flCoef, float frCoef, float blCoef, float brCoef, float pidPCoefficient, double encoderTicksPerRotation, double gearRatio, double wheelDiameterCm, double slip, float exponentialScalar) {
            this.flCoef = flCoef;
            this.frCoef = frCoef;
            this.blCoef = blCoef;
            this.brCoef = brCoef;
            this.encoderTicksPerRotation = encoderTicksPerRotation;
            this.gearRatio = gearRatio;
            this.wheelDiameterCm = wheelDiameterCm;
            this.slip = slip;
            this.wheelCircumference = Math.PI * wheelDiameterCm;
            this.exponentialScalar = exponentialScalar;
        }
    }
}

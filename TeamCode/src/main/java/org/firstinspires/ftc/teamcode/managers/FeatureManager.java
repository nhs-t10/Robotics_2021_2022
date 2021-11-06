package org.firstinspires.ftc.teamcode.managers;

import com.qualcomm.robotcore.eventloop.opmode.OpModeManager;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.auxilary.FileSaver;
import org.firstinspires.ftc.teamcode.managers.FeatureManager.RobotConfiguration.OmniCalcComponents;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;

import static org.firstinspires.ftc.teamcode.managers.FeatureManager.RobotConfiguration.WheelCoefficients.W;

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

    public static final RobotConfiguration bigBoyConfiguration = new RobotConfiguration(
            W(0.75f,0.75f,0.75f,-0.75f),
            new OmniCalcComponents(
                W(-1f,1f,1f,-1f), //Horizontal omniCalc components
                W(-1f,-1f,-1f,-1f), //Vertical omniCalc components
                W(-1f, 1f, -1f, 1f) //Rotational omniCalc components
            ),
            0.03f, 1680, 1, 8.9, 0.7, 3f);

    public static final RobotConfiguration littleBoyConfiguration = new RobotConfiguration(
            W(1, 1, -1, -1),
            new OmniCalcComponents(
                    W(-1f,1f,1f,-1f), //Horizontal omniCalc components
                    W(-1f,-1f,-1f,-1f), //Vertical omniCalc components
                    W(-1f, 1f, -1f, 1f) //Rotational omniCalc components
            ),
            0.03f, 1680, 1, 4, 0.7, 3f);

    public static final RobotConfiguration defaultConfiguration = littleBoyConfiguration;


    private static RobotConfiguration cachedConfiguration;

    public static String getRobotName() {
        ArrayList<String> lines = (new FileSaver(RobotConfiguration.fileName)).readLines();

        //if the file doesn't exist, return the default. This doesn't adjust the cache, so if there's a later edit, it'll be loaded.
        if(lines.size() == 0) return "nonexistentBoy";

        String fileContent = lines.get(0);

        switch (fileContent) {
            case RobotConfiguration.bigBoyFileContent:
                return "bigBoy";
            case RobotConfiguration.littleBoyFileContent:
                return "smallBoy";
            default:
                return "mysteryBoy";
        }
    }

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

        public WheelCoefficients motorCoefficients;

        public OmniCalcComponents omniComponents;

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

        public static class OmniCalcComponents {
            public WheelCoefficients hor, ver, rot;

            public OmniCalcComponents(WheelCoefficients hor, WheelCoefficients ver, WheelCoefficients rot) {
                this.hor = hor;
                this.ver = ver;
                this.rot = rot;
            }
        }

        public static class WheelCoefficients {
            public static WheelCoefficients W(float fl, float fr, float bl, float br) {
                return new WheelCoefficients(fl, fr, bl, br);
            }
            public float fl, fr, bl, br;

            public WheelCoefficients(float fl, float fr, float bl, float br) {
                this.fl = fl;
                this.fr = fr;
                this.bl = bl;
                this.br = br;
            }
        }

        public RobotConfiguration(WheelCoefficients motorCoefficients, OmniCalcComponents omniComponents, float pidPCoefficient, double encoderTicksPerRotation, double gearRatio, double wheelDiameterCm, double slip, float exponentialScalar) {
            this.motorCoefficients = motorCoefficients;
            this.omniComponents = omniComponents;
            this.encoderTicksPerRotation = encoderTicksPerRotation;
            this.gearRatio = gearRatio;
            this.wheelDiameterCm = wheelDiameterCm;
            this.slip = slip;
            this.wheelCircumference = Math.PI * wheelDiameterCm;
            this.exponentialScalar = exponentialScalar;
        }
    }
}

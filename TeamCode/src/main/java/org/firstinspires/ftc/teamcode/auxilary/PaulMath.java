package org.firstinspires.ftc.teamcode.auxilary;

import static java.lang.Math.PI;

import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.managers.feature.FeatureManager;
import org.firstinspires.ftc.teamcode.managers.feature.robotconfiguration.RobotConfiguration;

import java.util.ArrayList;

public abstract class PaulMath extends FeatureManager {

    public static float highestValue(float[] array) {
        int arrayLength = array.length;
        float highest = -1000000;
        for (int i = 0; i < arrayLength; i++) {
            if (array[i] > highest) {
                highest = array[i];
            }
        }
        return highest;
    }

    public static float[] rgbToHsl(float r, float g, float b) {
        r /= 255; g /= 255; b /= 255;

        float max = Math.max(r, Math.max(g, b));
        float min = Math.min(r, Math.min(g, b));
        float h, s, l = (max + min) / 2;

        if (max == min) {
            h = s = 0; // achromatic
        } else {
            float d = max - min;
            s = l > 0.5 ? d / (2 - max - min) : d / (max + min);

            if(max == r) h = (g - b) / d + (g < b ? 6 : 0);
            else if(max == g) h = (b - r) / d + 2;
            else h = (r - g) / d + 4;

            h /= 6;
        }

        return new float[] { h, s, l };
    }

    public static float[] normalizeArray(float[] array) {
        int arrayLength = array.length;
        float highest = highestValue(array);
        for (int i = 0; i < arrayLength; i++) {
            array[i] = array[i] / highest;
        }
        return array;
    }

    public static float[] cartesianToPolar(float x, float y) {
        double radius = Math.sqrt((x*x) + (y*y));
        double angle = Math.atan2(y, x);

        return new float[] {(float)radius, (float)(angle * 180 / PI)};
    }

    public static float[] polarToCartesian(float angle, float magnitude) {

        double y = Math.sin(angle) * magnitude;
        double x = Math.cos(angle) * magnitude;

        return new float[] {(float)x, (float)y };
    }

    public static float[] omniCalc(float verticalPower, float horizontalPower, float rotationalPower) {
        float v = Range.clip(verticalPower, -1, 1);
        float h = Range.clip(horizontalPower, -1, 1);
        float r = Range.clip(rotationalPower, -1, 1);

        RobotConfiguration configuration = FeatureManager.getRobotConfiguration();

        // Motor powers of fl, fr, br, bl
        float[] vertical = {configuration.omniComponents.ver.fl * v,
                            configuration.omniComponents.ver.fr * v,
                            configuration.omniComponents.ver.br * v,
                            configuration.omniComponents.ver.bl * v};

        float[] horizontal = {configuration.omniComponents.hor.fl * h,
                            configuration.omniComponents.hor.fr * h,
                            configuration.omniComponents.hor.br * h,
                            configuration.omniComponents.hor.bl * h};

        float[] rotational = {configuration.omniComponents.rot.fl * r,
                            configuration.omniComponents.rot.fr * r,
                            configuration.omniComponents.rot.br * r,
                            configuration.omniComponents.rot.bl * r};

        float[] sum = new float[4];
        for (int i = 0; i < 4; i++) {
            sum[i] = vertical[i] + horizontal[i] + rotational[i];
        }
        //This makes sure that no value is greater than 1 by dividing all of them by the maximum
        float highest = highestValue(sum);
        if (highest > 1) {
            sum = normalizeArray(sum);
            return sum;
        }
        return sum;
    }

    public static float roundToPoint(float input, float place) {
        return Math.round(input / place) * place;
    }

    /**
     * Converts a number of ticks to centimeters
     * @param distance Number of ticks
     * @return Centimeters covered by the robot in the given number of ticks
     */
    public static int encoderDistanceCm(double distance) {
        RobotConfiguration config = FeatureManager.getRobotConfiguration();
        double rotations = distance / config.encoderTicksPerRotation;
        return (int) ((config.wheelCircumference * rotations * config.gearRatio) / config.slip);
    }

    public static float delta(float one, float two) {
        return Math.abs(one - two);
    }

    public static String camelToSnake(String camel) {
        ArrayList<String> words = new ArrayList<String>();

        String currentWord = "";
        for (char letter : camel.toCharArray()) {

            if (Character.isUpperCase(letter)) {
                //if multiple uppercase in a row, don't break words
                if (!currentWord.toUpperCase().equals(currentWord)) {
                    words.add(currentWord);
                    currentWord = "";
                }
            }

            currentWord += letter;


        }
        //add the final word if it's relevant
        if (!currentWord.equals("")) words.add(currentWord);

        //join into snake
        StringBuilder snakey = new StringBuilder();
        for (int i = 0; i < words.size(); i++) {
            snakey.append(words.get(i));
            //if it's not the last word, add the underscore
            if (i + 1 < words.size()) snakey.append("_");
        }

        return snakey.toString().toUpperCase();
    }


    /**
     * Counts proportional error for PID control.
     *
     * @param currentValue  the value we are currently at
     * @param expectedValue the value we want
     * @return amount to change in order to get to the target value quickly and smoothly
     */
    public static float proportionalPID(float currentValue, float expectedValue) {
        return proportionalPID(currentValue, expectedValue, 0.007f);
    }

    public static float proportionalPID(float currentValue, float expectedValue, float Kp) {
        return proportionalPID(currentValue, expectedValue, Kp, 0.2f, 0.5f);
    }

    /**
     * Counts proportional error for PID control.
     *
     * @param currentValue  the value we are currently at
     * @param expectedValue the value we want
     * @param Kp proportional coefficient. 0.007 by default
     * @param clipMin Minimum absolute value that the output can be. 0.2 by default.
     * @param clipMax Maximum absolute value that the output can be. 0.5 by default.
     * @return a constant times the difference between the paramaters
     */
    public static float proportionalPID(float currentValue, float expectedValue, float Kp, float clipMin, float clipMax) {
        float currentBased360 = (currentValue + 360) % 360;
        float targetBased360 = (expectedValue + 360) % 360;

        float difference = (targetBased360 - currentBased360);

        if(difference > 180) difference += -360;
        else if(difference < -180) difference += 360;

        if (Math.abs(difference) > 1) {
            //abs val then multiply by sign of difference; easier than two ifs for positive clip and negative clip
            return (Range.clip((Kp * Math.abs(difference)), clipMin, clipMax)) * (difference<=0?-1:1);
        } else {
            return 0;
        }
    }

    /**
     * Counts proportional error for PID control.
     *
     * @param currentValue  the value we are currently at
     * @param expectedValue the value we want
     * @param Kp proportional coefficient. 0.007 by default
     * @return a constant times the difference between the paramaters
     */
    public static float generalProportionalPID(float currentValue, float expectedValue, float Kp) {
        float difference = (expectedValue - currentValue);

        //abs val then multiply by sign of difference; easier than two ifs for positive clip and negative clip
        return Kp * difference;
    }

    /**
     * Escape a string, so as to put it in a JSON object or Java source string. The inverse of unescapeString()
     * @param value The string to escape
     * @return Escaped string
     */
    public static String escapeString(String value) {
        return value
                .replace("\\", "\\\\")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\"", "\\\"")
                .replace("\t", "\\t");
    }

    /**
     * Unescape a string like it was in a JSON object. The inverse of escapeString().
     * @param value The escaped string
     * @return Unescaped string
     */
    public static String unescapeString(String value) {
        StringBuilder result = new StringBuilder();
        char[] chars = value.toCharArray();

        boolean escaped = false;
        for (char c : chars) {
            if (escaped) {
                escaped = false;
                if (c == 'n') result.append('\n');
                else if (c == 'r') result.append('\r');
                else if (c == 't') result.append('\t');
                else result.append(c);
            } else if (c == '\\') {
                escaped = true;
            } else {
                result.append(c);
            }
        }
        return result.toString();

    }

    public static String join(String s, String[] a) {
        StringBuilder r = new StringBuilder();
        for(int i = 0; i < a.length; i++) {
            if(i!=0) r.append(s);
            r.append(a[i]);
        }
        return r.toString();
    }

    public static String deJSONify(String s) {
        if(s.length() > 2 && s.startsWith("\"") && s.endsWith("\"")) {
            return unescapeString(s.substring(1, s.length() - 1));
        } else {
            return s;
        }
    }

    public static String JSONify(String s) {
        return '"' + escapeString(s) + '"';
    }
    public static String JSONify(Object s) {
        if(s == null) return "null";
        else if(s instanceof Number || s instanceof Boolean) return s.toString();
        else return JSONify(s.toString());
    }

    public static boolean isJSONable(Object s) {
        return s == null || s instanceof Number || s instanceof Boolean || s instanceof String || s instanceof Character;
    }

    public static double rotsToTicks(double rotations){
        return rotations*28;
    }
    public static double metersToRots(double meters){
        return meters*0.1*PI;
    }
    public static double metersToTicks(double meters){
        return meters*2.8*PI;
    }
}

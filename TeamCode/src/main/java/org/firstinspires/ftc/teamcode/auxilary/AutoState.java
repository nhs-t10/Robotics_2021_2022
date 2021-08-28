package org.firstinspires.ftc.teamcode.auxilary;

import com.qualcomm.robotcore.hardware.AccelerationSensor;
import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.configuration.typecontainers.MotorConfigurationType;

public class AutoState {
    public static final float CONTINUE_TO =0f;
    public static final float DRIVE_OMNI = 1f;
    public static final float MANIP_SERVO = 2f;
    public static final float MANIP_MOTOR = 3f;

    public ContinueCondition condition;
    public StateAction stateAction;

    public AutoState(ContinueCondition condition, StateAction action) {
        this.condition = condition;
        this.stateAction = action;
    }

    public float[][] step() {
        float[] action = stateAction.value;
        float type = stateAction.typeInt();

        //don't change action disposition when continuing; that way, actions can be continuous.
        float actDisposition = type;
        if(condition.shouldContinue()) {
            type = 0f;
        }

        return new float[][] {
                new float[] {type, (float)condition.moveTo, actDisposition},
                action
        };

    }

    public static enum ActionType { DRIVE, MANIP_SERVO, MANIP_MOTOR };

    public static class StateAction {


        public float[] value;
        public ActionType type;
        public int typeInt() {
            return type.ordinal() + 1;
        }

        public StateAction(ActionType _type, float[] _value) {
            this.type = _type;
            this.value = _value;
        }
    }

    public static enum ContinueType { TIME, SENSOR, INSTANT }

    public static class ContinueCondition {

        public ContinueCondition(ContinueType type, int moveTo) {
            this.type = type;
            this.moveTo = moveTo;
        }

        public ContinueCondition(ContinueType type) {
            this.type = type;
        }

        public ContinueCondition(ContinueType type, long timeMs, int moveTo) {
            this.type = type;
            this.moveTo = moveTo;
            this.timeMs = timeMs;
        }

        public int moveTo;
        public ContinueType type;
        public String sensorProp;
        public Sensor sensor;
        public long timeMs;
        public float target;

        public LogicalComparison comparison;

        public long startMs = 0;

        public boolean shouldContinue() {
            if(startMs == 0){ startMs = System.currentTimeMillis();}

            switch(type) {
                case TIME:
                    return System.currentTimeMillis() - startMs > timeMs;
                case SENSOR:
                    //TODO: Implement sensors!
                    return comparison.res((float) sensor.getValue(sensorProp), target);
                case INSTANT:
                    return true;
                default:
                    return false;
            }
        }

        public static class LogicalComparison {
            private static enum Type {LT, LTE, EQ, GTE, GT};
            private Type t;

            public LogicalComparison(String t) {
                this.t = Type.valueOf(t);
            }

            public boolean res(float a, float b) {
                switch(t) {
                    case LT: return a < b;
                    case LTE: return a <= b;
                    case EQ: return a == b;
                    case GTE: return a >= b;
                    case GT: return a > b;
                }
                return false;
            }
        }

        private static class Sensor {
            private HardwareDevice sensor;

            public Sensor(HardwareDevice s) {
                this.sensor = s;
            }

            public double getValue(String prop) {
                if(sensor instanceof AccelerationSensor) {
                    switch(prop) {
                        case "accelX": return ((AccelerationSensor) sensor).getAcceleration().xAccel;
                        case "yAccel": return ((AccelerationSensor) sensor).getAcceleration().yAccel;
                        case "zAccel": return ((AccelerationSensor) sensor).getAcceleration().zAccel;
                    }
                }
                return 0;
            }
        }
    }
}

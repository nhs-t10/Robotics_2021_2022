package org.firstinspires.ftc.teamcode.auxilary;

public class StateMachine {
    public AutoState[] states;
    public int currentState;

    public StateMachine(AutoState[] _states) {
        this.states = _states;
        this.currentState = 0;
    }

    public StateMachine(AutoState[] _states, int initialState) {
        this.states = _states;
        this.currentState = initialState;
    }

    public StateMachineExportAction getNextFrameMovement() {
        AutoState state = states[currentState];
        float[][] fullResult = state.step();
        float[] meta = fullResult[0];
        float[] resultData = fullResult[1];

        if(meta[0] == AutoState.CONTINUE_TO) currentState = (int)meta[1];

        return new StateMachineExportAction(fullResult);
    }

    public enum StateMachineExportActionType { DRIVE_OMNI, MANIP_SERVO, MANIP_MOTOR }

    public static class StateMachineExportAction {
        public StateMachineExportActionType type;
        public float[] action;

        public StateMachineExportAction(float[][] unshapedResult) {
            float t = unshapedResult[0][2];
            if(t == AutoState.DRIVE_OMNI) type = StateMachineExportActionType.DRIVE_OMNI;
            if(t == AutoState.MANIP_SERVO) type = StateMachineExportActionType.MANIP_SERVO;
            if(t == AutoState.MANIP_MOTOR) type = StateMachineExportActionType.MANIP_MOTOR;

            action = unshapedResult[1];
        }
    }
}
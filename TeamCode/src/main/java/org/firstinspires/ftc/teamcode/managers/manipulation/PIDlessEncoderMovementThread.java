package org.firstinspires.ftc.teamcode.managers.manipulation;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.auxilary.clocktower.Clocktower;
import org.firstinspires.ftc.teamcode.auxilary.clocktower.ClocktowerCodes;
import org.firstinspires.ftc.teamcode.managers.feature.FeatureManager;

public class PIDlessEncoderMovementThread extends Thread {
    DcMotor[] motors;
    private int[] targets;
    private boolean[] shouldMove;
    private double[] powerCoefs;

    private double slowRange;

    private boolean running;

    public PIDlessEncoderMovementThread(DcMotor[] motors) {
        this.motors = motors;
        this.targets = new int[motors.length];
        this.shouldMove = new boolean[motors.length];
        this.powerCoefs = new double[motors.length];

        running = true;

        this.slowRange = FeatureManager.getRobotConfiguration().encoderTicksPerRotation;
    }

    @Override
    public void run() {
        while(FeatureManager.isOpModeRunning) {
            for(int i = 0; i < motors.length; i++) {
                if(shouldMove[i]) {
                    int currentPos = motors[i].getCurrentPosition();
                    int delta = targets[i] - currentPos;

                    double power = Math.signum(delta);

                    double coef = powerCoefs[i];
                    if(Math.abs(delta) < slowRange) coef *= (Math.abs(delta) / slowRange);

                    motors[i].setPower(power * coef);

                    if(Math.abs(delta) < ManipulationManager.ENCODER_TICK_VALUE_TOLERANCE) shouldMove[i] = false;
                }
            }
            Clocktower.time(ClocktowerCodes.MOTOR_ENCODER_THREAD);
        }
    }

    public boolean isMoving(int i) {
        return shouldMove[i];
    }

    public void move(int i, int target, double power) {
        shouldMove[i] = true;
        powerCoefs[i] = power;
        targets[i] = target;
    }

    public void cancelMovement(int index) {
        shouldMove[index] = false;
    }
}

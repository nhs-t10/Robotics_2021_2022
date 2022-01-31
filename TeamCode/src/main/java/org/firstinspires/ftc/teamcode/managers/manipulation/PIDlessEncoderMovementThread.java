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
    //checks
    private float[] direction;
    private float[] lastDeltas;


    private double slowRange;

    private boolean running;

    public PIDlessEncoderMovementThread(DcMotor[] motors) {
        this.motors = motors;
        this.targets = new int[motors.length];
        this.shouldMove = new boolean[motors.length];
        this.powerCoefs = new double[motors.length];
        
        this.lastDeltas = new float[motors.length];

        this.direction = new float[motors.length];
        for (int i = 0; i < motors.length; i++) direction[i] = 1;

        running = true;

        this.slowRange = FeatureManager.getRobotConfiguration().encoderTicksPerRotation;
    }

    @Override
    public void run() {
        try {
            while(FeatureManager.isOpModeRunning) {
                for(int i = 0; i < motors.length; i++) {
                    if(shouldMove[i]) {
                        if(motors[i].getMode() != DcMotor.RunMode.RUN_WITHOUT_ENCODER) motors[i].setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

                        int currentPos = motors[i].getCurrentPosition();
                        int delta = targets[i] - currentPos;

                        if(lastDeltas[i] != 0) {
                            float deltaChange = delta - lastDeltas[i];
                            if ((delta>0 && deltaChange>0) || (delta<0 && deltaChange<0)) direction[i] = -1;
                        }

                        double power = direction[i] * delta * powerCoefs[i] * 0.01;

                        motors[i].setPower(power);

                        if(Math.abs(delta) < ManipulationManager.ENCODER_TICK_VALUE_TOLERANCE) {
                            shouldMove[i] = false;
                            motors[i].setPower(0.0);
                        }

                        lastDeltas[i] = delta;
                    }
                }
                Clocktower.time(ClocktowerCodes.MOTOR_ENCODER_THREAD);
                Thread.yield();
            }
        } catch(Throwable t) {
            FeatureManager.logger.log("Silent error in 'PIDlessEncoderMovementThread'");
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

    public int getTarget(int i) {
        return targets[i];
    }
    public void cancelMovement(int index) {
        shouldMove[index] = false;
    }
}

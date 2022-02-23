package org.firstinspires.ftc.teamcode.managers.manipulation;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.auxilary.clocktower.Clocktower;
import org.firstinspires.ftc.teamcode.auxilary.clocktower.ClocktowerCodes;
import org.firstinspires.ftc.teamcode.auxilary.pid.AutotuningPIDController;
import org.firstinspires.ftc.teamcode.managers.feature.FeatureManager;

public class PIDlessEncoderMovementThread extends Thread {
    DcMotor[] motors;
    private int[] targets;
    private boolean[] shouldMove;
    private double[] powerCoefs;
    //checks
    private float[] direction;
    private float[] lastDeltas;
    private boolean[] zeroed;

    AutotuningPIDController[] controllers;

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

        this.zeroed = new boolean[motors.length];

        this.controllers = new AutotuningPIDController[motors.length];

        running = true;

        this.slowRange = FeatureManager.getRobotConfiguration().encoderTicksPerRotation;
    }

    @Override
    public void run() {
        try {
            while(FeatureManager.isOpModeRunning) {
                for(int i = 0; i < motors.length; i++) {
                    if(shouldMove[i]) {
                        if(this.controllers[i] == null) this.controllers[i] = new AutotuningPIDController(ManipulationManager.ENCODER_TICK_VALUE_TOLERANCE);

                        if(motors[i].getMode() != DcMotor.RunMode.RUN_WITHOUT_ENCODER) motors[i].setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

                        int currentPos = motors[i].getCurrentPosition();
                        int delta = targets[i] - currentPos;

                        controllers[i].setTarget(targets[i]);

                        if(lastDeltas[i] != 0) {
                            float deltaChange = delta - lastDeltas[i];
                            if ((delta>0 && deltaChange>0) || (delta<0 && deltaChange<0)) direction[i] = -1;
                        }

                        double power = powerCoefs[i] * controllers[i].getControl(currentPos);

                        motors[i].setPower(Range.clip(power, -0.5,0.5));

                        if(controllers[i].isStable(currentPos)) {
                            shouldMove[i] = false;
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

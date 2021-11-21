package org.firstinspires.ftc.teamcode.managers.manipulation;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.managers.feature.FeatureManager;

public class MotorEncodedMovementThread extends Thread {
    public DcMotor motor;
    public int position;
    public double power;

    public boolean running = true;
    private long deltaEclipsedTime = 0;

    private final static int TICK_TOLERANCE = 2;
    private final static int MS_TO_CONFIRM_COMPLETION = FeatureManager.DOUBLE_CLICK_TIME_MS;

    public MotorEncodedMovementThread(DcMotor motor, int position, double power) {
        this.motor = motor;
        this.position = position;
        this.power = power;
    }

    public void run() {
        while (FeatureManager.isOpModeRunning && running) {
            motor.setTargetPosition(position);
            motor.setPower(power);
            motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            int currentPos = motor.getCurrentPosition();
            int tickDelta = Math.abs(currentPos - position);

            //make sure that the current position is within the tolerance for at least MS_TO_CONFIRM_COMPLETION milliseconds.
            if(tickDelta <= TICK_TOLERANCE && deltaEclipsedTime == 0) deltaEclipsedTime = System.currentTimeMillis();
            if(tickDelta > TICK_TOLERANCE) deltaEclipsedTime = 0;

            long timeDelta = System.currentTimeMillis() - deltaEclipsedTime;

            //once the required time is fulfilled, break the loop
            if(tickDelta <= TICK_TOLERANCE && timeDelta > MS_TO_CONFIRM_COMPLETION) {
                break;
            }
        }
        this.running = false;
    }

    public void cancelMovement() {
        this.running = false;
    }

    public boolean equalTarget(int position, double power) {
        return this.position == position && this.power == power;
    }
}

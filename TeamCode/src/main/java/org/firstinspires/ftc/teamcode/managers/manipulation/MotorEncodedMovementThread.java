package org.firstinspires.ftc.teamcode.managers.manipulation;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.DcMotorControllerEx;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;
import org.firstinspires.ftc.teamcode.managers.feature.FeatureManager;
import org.firstinspires.ftc.teamcode.managers.telemetry.fallible.FailureType;
import org.firstinspires.ftc.teamcode.managers.telemetry.fallible.hardwaredevices.FallibleHardwareDevice;

public class MotorEncodedMovementThread extends Thread {
    public DcMotor motor;
    public int position;
    public double power;

    public boolean running = true;
    private long deltaEclipsedTime = 0;

    public final static int TICK_TOLERANCE = 2;
    public final static int MS_TO_CONFIRM_COMPLETION = FeatureManager.DOUBLE_CLICK_TIME_MS;

    public MotorEncodedMovementThread(DcMotor motor, int position, double power) {
        this.motor = motor;
        this.position = position;
        this.power = power;
    }

    public void run() {
        motor.setTargetPosition(position);
        motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motor.setPower(power);

        while (FeatureManager.isOpModeRunning && running) {

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

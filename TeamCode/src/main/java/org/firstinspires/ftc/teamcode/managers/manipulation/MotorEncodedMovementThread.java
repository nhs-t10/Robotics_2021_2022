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

    private final static int TICK_TOLERANCE = 2;
    private final static int MS_TO_CONFIRM_COMPLETION = FeatureManager.DOUBLE_CLICK_TIME_MS;

    public MotorEncodedMovementThread(DcMotor motor, int position, double power) {
        this.motor = motor;
        this.position = position;
        this.power = power;
    }

    public void run() {
        //integration variables for safety-tracker
        double oldTicksPerNs = 0, oldTickDelta = 0;
        long oldTime;

        oldTime = System.nanoTime();

        while (FeatureManager.isOpModeRunning && running) {
            long time = System.nanoTime();

            motor.setTargetPosition(position);
            motor.setPower(power);
            motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            int currentPos = motor.getCurrentPosition();
            int tickDelta = Math.abs(currentPos - position);


            double ticksPerNs = (tickDelta - oldTickDelta) / (time - oldTime);
            double ticksPerNsPerNs = (ticksPerNs - oldTicksPerNs) / (time - oldTime);

            // If both acceleration and velocity are low (but not zero)AND we haven't reached the target, then we have a safety problem
            // Stop the motor instantly. Don't wait for synchronization. For further measure, disable it completely and report an emergency stop.
            if(deltaEclipsedTime == 0 && ticksPerNsPerNs != 0 && Math.abs(ticksPerNsPerNs) < 0.01 && ticksPerNs != 0 && Math.abs(ticksPerNs) < 0.01) {
                motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                motor.setPower(0);
                running = false;
                FeatureManager.reportEmergencyStop(motor.getConnectionInfo());
            }

            oldTime = time;
            oldTicksPerNs = ticksPerNs;
            oldTickDelta = tickDelta;

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

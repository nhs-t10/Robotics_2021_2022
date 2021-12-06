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

    public DcMotorEx safetyController;
    public boolean hasSafety;

    public boolean running = true;
    private long deltaEclipsedTime = 0;

    private final static int TICK_TOLERANCE = 2;
    private final static int MS_TO_CONFIRM_COMPLETION = FeatureManager.DOUBLE_CLICK_TIME_MS;

    public MotorEncodedMovementThread(DcMotor motor, int position, double power) {
        this.motor = motor;
        this.position = position;
        this.power = power;

        this.hasSafety = motor instanceof DcMotorEx;
        if(hasSafety) {
            this.safetyController = (DcMotorEx) motor;
        }
    }

    public void run() {
        double oldAmperage = 0, oldAmpsPerSecond = 0, oldDegreesPerSecond = 0, amperage = 0;
        long oldTime;

        if(hasSafety) {
            oldAmperage = safetyController.getCurrent(CurrentUnit.MILLIAMPS);
        }
        oldTime = System.nanoTime();

        while (FeatureManager.isOpModeRunning && running) {
            long time = System.nanoTime();

            motor.setTargetPosition(position);
            motor.setPower(power);
            motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            int currentPos = motor.getCurrentPosition();
            int tickDelta = Math.abs(currentPos - position);

            //if a safety controller was found, use it. Check whether there's an amperage surge and a corresponding speed dip; if so, cancel movement.
            if(hasSafety) {
                amperage = safetyController.getCurrent(CurrentUnit.MILLIAMPS);
                double ampsPerSecond = (amperage - oldAmperage) / (time - oldTime);
                double ampsPerSecondPerSecond = (ampsPerSecond - oldAmpsPerSecond) / (time - oldTime);

                double degreesPerSecond = safetyController.getVelocity(AngleUnit.DEGREES);
                double degreesPerSecondPerSecond = (degreesPerSecond - oldDegreesPerSecond) / (time - oldTime);

                //if there's a sudden decrease in velocity AND a sudden increase in energy, that's a signal that something is going very wrong.
                // Stop the motor instantly. Don't wait for synchronization. For further measure, disable it completely and report an emergency stop.
                if(ampsPerSecondPerSecond > 0.01 && degreesPerSecondPerSecond < -0.01) {
                    motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                    motor.setPower(0);

                    if(motor instanceof FallibleHardwareDevice) ((FallibleHardwareDevice) motor).setFailureType(FailureType.POWER_FAILURE);

                    safetyController.setMotorDisable();

                    running = false;

                    FeatureManager.reportEmergencyStop(motor.getConnectionInfo());
                }

                oldDegreesPerSecond = degreesPerSecond;
                oldAmpsPerSecond = ampsPerSecond;
                oldTime = time;
                oldAmperage = amperage;
            }

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

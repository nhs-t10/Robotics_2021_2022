package org.firstinspires.ftc.teamcode.managers.localization;

import org.firstinspires.ftc.teamcode.auxilary.PaulMath;
import org.firstinspires.ftc.teamcode.managers.feature.FeatureManager;
import org.firstinspires.ftc.teamcode.managers.imu.ImuManager;
import org.firstinspires.ftc.teamcode.managers.movement.MovementManager;

public class LocalizationThreadWithPowerEstimation extends Thread {


    private final ImuManager imu;
    private final MovementManager driver;
    private final LocalizationManager parent;

    public LocalizationThreadWithPowerEstimation(MovementManager driver, ImuManager imu, LocalizationManager parent) {
        this.driver = driver;
        this.imu = imu;
        this.parent = parent;
    }

    @Override
    public void run() {
        while(FeatureManager.isOpModeRunning) {
            float[] deltas = estimatePositionDeltas();
            float fl = deltas[0], fr = deltas[1], bl = deltas[2], br = deltas[3];

            float[] omni = PaulMath.omniCalcInverse(fl, fr, bl, br, true);
            float v = omni[0], h = omni[1];

            parent.posY += v;
            parent.posX += h;

            Thread.yield();
        }
    }
    private float oldMeters;

    private float[] estimatePositionDeltas() {
        float sensedMotorMeters = driver.getMeters();
        float sensedMotorDelta = sensedMotorMeters - oldMeters;
        oldMeters = sensedMotorMeters;

        float sensedMotorPower = (float) driver.getGeneralMeasurementMotor().getPower();

        if(sensedMotorPower == 0) return new float[4];

        return new float[] {
                (float) ((driver.frontLeft.getPower() /  sensedMotorPower) * sensedMotorDelta),
                (float) (driver.frontRight.getPower() /  sensedMotorPower) * sensedMotorDelta,
                (float) (driver.backLeft.getPower() /  sensedMotorPower) * sensedMotorDelta,
                (float) (driver.backRight.getPower() /  sensedMotorPower) * sensedMotorDelta,
        };
    }
}

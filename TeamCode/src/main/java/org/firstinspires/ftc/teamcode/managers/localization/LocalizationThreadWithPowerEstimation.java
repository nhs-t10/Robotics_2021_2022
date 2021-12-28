package org.firstinspires.ftc.teamcode.managers.localization;

import org.firstinspires.ftc.teamcode.managers.feature.FeatureManager;
import org.firstinspires.ftc.teamcode.managers.imu.ImuManager;
import org.firstinspires.ftc.teamcode.managers.movement.MovementManager;

public class LocalizationThreadWithTrig extends Thread {


    private final ImuManager imu;
    private final MovementManager driver;
    private final LocalizationManager parent;

    public LocalizationThreadWithTrig(MovementManager driver, ImuManager imu, LocalizationManager parent) {
        this.driver = driver;
        this.imu = imu;
        this.parent = parent;
    }

    @Override
    public void run() {
        double lastM = driver.getMeters();
        while(FeatureManager.isOpModeRunning) {
            double imuRadians = (Math.PI/180) * imu.getThirdAngleOrientation();
            double distance = driver.getMeters();
            double deltaDistance = distance - lastM;
            lastM = distance;

            parent.posX += Math.cos(imuRadians) * deltaDistance;
            parent.posY += Math.sin(imuRadians) * deltaDistance;
        }
    }
}

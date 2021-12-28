package org.firstinspires.ftc.teamcode.managers.localization;

import org.firstinspires.ftc.teamcode.managers.feature.FeatureManager;
import org.firstinspires.ftc.teamcode.managers.imu.ImuManager;
import org.firstinspires.ftc.teamcode.managers.movement.MovementManager;

public class LocalizationManager extends FeatureManager {
    private final ImuManager imu;
    private final MovementManager driver;

    public double posX;
    public double posY;

    public LocalizationManager(MovementManager driver, ImuManager imu) {
        this.driver = driver;
        this.imu = imu;
        (new LocalizationThreadWithTrig(driver, imu, this)).start();
    }
}

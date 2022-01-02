package org.firstinspires.ftc.teamcode.managers.feature;

import com.qualcomm.robotcore.eventloop.opmode.OpModeManager;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.auxilary.EncodedMotor;
import org.firstinspires.ftc.teamcode.auxilary.FileSaver;
import org.firstinspires.ftc.teamcode.managers.feature.robotconfiguration.OmniCalcComponents;
import org.firstinspires.ftc.teamcode.managers.feature.robotconfiguration.RobotConfiguration;
import org.firstinspires.ftc.teamcode.managers.feature.robotconfiguration.WheelCoefficients;

import java.util.ArrayList;

import static org.firstinspires.ftc.teamcode.managers.feature.robotconfiguration.WheelCoefficients.W;
import static org.firstinspires.ftc.teamcode.managers.feature.robotconfiguration.WheelCoefficients.horizontal;
import static org.firstinspires.ftc.teamcode.managers.feature.robotconfiguration.WheelCoefficients.rotational;
import static org.firstinspires.ftc.teamcode.managers.feature.robotconfiguration.WheelCoefficients.vertical;

public class FeatureManager {
    public static final Logger logger = new Logger();

    public static final int DOUBLE_CLICK_TIME_MS = 300;

    public static DcMotor DELETE_ME_LATER_IM_NOT_GOOD_PRACTICE_DONT_BELIEVE_CHLOE_IF_THEY_TELL_YOU_NOT_TO_IM_NOT_OLD_CODE_I_WAS_CREATED_ON_DECEMBER_27_2021 = null;

    public static boolean isOpModeRunning = false;

    public static void setIsOpModeRunning(boolean b) {
        setIsOpModeRunning(b, false);
    }
    public static void setIsOpModeRunning(boolean b, boolean shouldLoadAutoConfig) {
        isOpModeRunning = b;

        if(b) {
            if (shouldLoadAutoConfig) reconfigureForAuto();
            else reconfigureForTeleop();
        }
    }

    public static final RobotConfiguration bigBoyConfiguration = new RobotConfiguration(
            W(1,1,1,-1),
            new OmniCalcComponents(
                vertical      (-1f,-1f,-1f,-1f),
                horizontal    (1f, -1f, 1f, -1f),
                rotational    (1f,-1f,-1f,1f)
            ),
            0.03f, 1680, 1, 8.9, 1, 3f);

    public static void reconfigureForTeleop() {
        FeatureManager.logger.log("I am teleop");
        bigBoyConfiguration.motorCoefficients = new WheelCoefficients(1,1,1,-1);
    }
    public static void reconfigureForAuto() {
        FeatureManager.logger.log("I am auto");
        bigBoyConfiguration.motorCoefficients = new WheelCoefficients(1,1,-1,1);
    }
    public static final RobotConfiguration littleBoyConfiguration = new RobotConfiguration(
            W(1, -1, 1, -1),
            new OmniCalcComponents(
                vertical      (-1f,-1f,-1f,-1f),
                horizontal    (-1f,1f,1f,-1f),
                rotational    (-1f, 1f, -1f, 1f)
            ),
            0.03f, 1680, 1, 4, 0.7, 3f);

    public static final RobotConfiguration defaultConfiguration = bigBoyConfiguration;


    private static RobotConfiguration cachedConfiguration;

    public static String getRobotName() {
        ArrayList<String> lines = (new FileSaver(RobotConfiguration.fileName)).readLines();

        //if the file doesn't exist, return the default. This doesn't adjust the cache, so if there's a later edit, it'll be loaded.
        if(lines.size() == 0) return "nonexistentBoy";

        String fileContent = lines.get(0);

        switch (fileContent) {
            case RobotConfiguration.bigBoyFileContent:
                return "bigBoy";
            case RobotConfiguration.littleBoyFileContent:
                return "smallBoy";
            default:
                return "mysteryBoy";
        }
    }

    public static RobotConfiguration getRobotConfiguration() {
        //if it's been cached, don't bother re-loading everything. Just return the cache.
        if(cachedConfiguration != null) return cachedConfiguration;

        ArrayList<String> lines = (new FileSaver(RobotConfiguration.fileName)).readLines();

        //if the file doesn't exist, return the default. This doesn't adjust the cache, so if there's a later edit, it'll be loaded.
        if(lines.size() == 0) return cachedConfiguration = defaultConfiguration;

        String fileContent = lines.get(0);

        switch (fileContent) {
            case RobotConfiguration.bigBoyFileContent:
                return cachedConfiguration = bigBoyConfiguration;
            case RobotConfiguration.littleBoyFileContent:
                return cachedConfiguration = littleBoyConfiguration;
            default:
                return cachedConfiguration = defaultConfiguration;
        }
    }

    /**
     * Show a warning about an emergency stop to the user. This does <u>not</u> perform any stopping by itself.
     * @param description an unstructured description of what caused the stop. This can be anything, including `null`.
     */
    public static void reportEmergencyStop(String description) {
        FeatureManager.logger.log("EMERGENCY STOP: " + description);
    }
}

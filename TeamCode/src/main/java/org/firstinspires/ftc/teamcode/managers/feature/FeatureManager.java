package org.firstinspires.ftc.teamcode.managers.feature;

import com.qualcomm.robotcore.eventloop.opmode.OpModeManager;

import org.firstinspires.ftc.teamcode.auxilary.FileSaver;
import org.firstinspires.ftc.teamcode.managers.feature.robotconfiguration.OmniCalcComponents;
import org.firstinspires.ftc.teamcode.managers.feature.robotconfiguration.RobotConfiguration;

import java.util.ArrayList;

import static org.firstinspires.ftc.teamcode.managers.feature.robotconfiguration.WheelCoefficients.W;
import static org.firstinspires.ftc.teamcode.managers.feature.robotconfiguration.WheelCoefficients.horizontal;
import static org.firstinspires.ftc.teamcode.managers.feature.robotconfiguration.WheelCoefficients.rotational;
import static org.firstinspires.ftc.teamcode.managers.feature.robotconfiguration.WheelCoefficients.vertical;

public class FeatureManager {
    public static final Logger logger = new Logger();

    public static final int DOUBLE_CLICK_TIME_MS = 300;

    public static boolean isOpModeRunning = false;

    public static void setIsOpModeRunning(boolean b) {
        isOpModeRunning = b;
    }

    public static final RobotConfiguration bigBoyConfiguration = new RobotConfiguration(
            W(1,1,1,-1),
            new OmniCalcComponents(
                vertical      (-1f,-1f,-1f,-1f),
                horizontal    (1f, -1f, 1f, -1f),
                rotational    (1f,-1f,-1f,1f)
            ),
            0.03f, 1680, 1, 8.9, 0.7, 3f);

    public static final RobotConfiguration littleBoyConfiguration = new RobotConfiguration(
            W(1, -1, 1, -1),
            new OmniCalcComponents(
                vertical      (1f,1f,1f,1f),
                horizontal    (-1f,1f,1f,-1f),
                rotational    (-1f, 1f, -1f, 1f)
            ),
            0.03f, 1680, 1, 4, 0.7, 3f);

    public static final RobotConfiguration defaultConfiguration = littleBoyConfiguration;


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
}

package org.firstinspires.ftc.teamcode.managers.macro;

import org.firstinspires.ftc.teamcode.managers.FeatureManager;

public abstract class Macro {
    public abstract void start(FeatureManager... managers);
    public abstract void stop();
    public abstract void loop();
}

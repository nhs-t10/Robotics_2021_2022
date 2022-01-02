package org.firstinspires.ftc.teamcode.managers.macro;

import org.firstinspires.ftc.teamcode.auxilary.clocktower.Clocktower;
import org.firstinspires.ftc.teamcode.auxilary.clocktower.ClocktowerCodes;
import org.firstinspires.ftc.teamcode.managers.feature.FeatureManager;

public class MacroRunnerThread extends Thread {
    Macro macro;
    boolean running = true;

    public MacroRunnerThread(Macro m) {
        macro = m;
    }

    @Override
    public void run() {
        while(running) {
            Clocktower.time(ClocktowerCodes.MACRO_RUNNER_THREAD);
            macro.loop();
            Thread.yield();
        }
    }

    public void stopMacro() {
        running = false;
        macro.stop();
    }
}

package org.firstinspires.ftc.teamcode.managers.macro;

public class MacroRunnerThread extends Thread {
    Macro macro;
    boolean running = true;

    public MacroRunnerThread(Macro m) {
        macro = m;
    }

    @Override
    public void run() {
        while(running) {
            macro.loop();
        }
    }

    public void stopMacro() {
        running = false;
        macro.stop();
    }
}

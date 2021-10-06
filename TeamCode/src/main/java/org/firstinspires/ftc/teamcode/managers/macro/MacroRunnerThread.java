package org.firstinspires.ftc.teamcode.managers.macro;

public class MacroRunnerThread implements Runnable {
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

    public void stop() {
        running = false;
        macro.stop();
    }
}

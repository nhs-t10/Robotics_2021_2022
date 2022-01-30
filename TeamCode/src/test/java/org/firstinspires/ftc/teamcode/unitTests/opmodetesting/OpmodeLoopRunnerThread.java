package org.firstinspires.ftc.teamcode.unitTests.opmodetesting;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.auxilary.RobotTime;
import org.firstinspires.ftc.teamcode.unitTests.teleop.longterm.ExampleTeleopCarouselHangingTest;

public class OpmodeLoopRunnerThread extends Thread {
    OpmodeHangingWatchdogThread watchdog;
    public OpMode opmode;
    long startTime;

    protected boolean running;

    public OpmodeLoopRunnerThread(OpMode mode) {
        this.running = true;

        //watch self
        (watchdog = new OpmodeHangingWatchdogThread(this)).start();

        this.opmode = mode;
        startTime = RobotTime.currentTimeMillis();
    }

    @Override
    public void run() {

        //init!
        watchdog.promiseNotDead();

        watchdog.setState("init");
        opmode.init();
        watchdog.promiseNotDead();

        watchdog.setState("init_loop");
        for(int i = 20; i >= 0; i--) {
            opmode.init_loop();
            watchdog.promiseNotDead();
        }


        watchdog.setState("start");
        opmode.start();
        watchdog.promiseNotDead();


        while (running) {
            opmode.time = (RobotTime.currentTimeMillis() - startTime)/1000.0;
            watchdog.setState("loop (iteration started " + opmode.time + "s in)");

            opmode.loop();
            watchdog.promiseNotDead();
        }
        watchdog.setState("stop");
        opmode.stop();
        watchdog.promiseNotDead();
    }

    public void testOver() {
        running = false;
        Thread.yield();
        if(watchdog.running) watchdog.standDown();
    }
}

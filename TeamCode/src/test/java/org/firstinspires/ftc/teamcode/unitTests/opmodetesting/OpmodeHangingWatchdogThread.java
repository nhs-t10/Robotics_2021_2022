package org.firstinspires.ftc.teamcode.unitTests.opmodetesting;

import org.firstinspires.ftc.teamcode.auxilary.RobotTime;
import org.firstinspires.ftc.teamcode.managers.feature.FeatureManager;
import org.firstinspires.ftc.teamcode.unitTests.teleop.longterm.ExampleTeleopCarouselHangingTest;

import static org.junit.Assert.fail;

/**
 * Makes sure that an OpMode doesn't go into an infinite loop
 */
public class OpmodeHangingWatchdogThread extends Thread {
    private final OpmodeLoopRunnerThread watched;

    private final static long MS_BETWEEN_CALL_ALLOWED = 5000;

    protected boolean running;

    private long killTime;
    private String state;
    private boolean success;
    private boolean cleanupDone;

    public OpmodeHangingWatchdogThread(OpmodeLoopRunnerThread t) {
        this.watched = t;
        this.running = true;
        this.killTime = RobotTime.currentTimeMillis() + MS_BETWEEN_CALL_ALLOWED;
        this.state = "";
    }
    @Override
    public void run() {
        while(RobotTime.currentTimeMillis() < killTime) Thread.yield();

        if(!watched.isAlive()) {
            success = true;
        } else {
            fail("blocked in " + state + "!");
            for (StackTraceElement s : watched.getStackTrace()) FeatureManager.logger.log(s);
            success = false;
        }
        this.running = false;
        watched.interrupt();
        watched.testOver();

        this.cleanupDone = true;
    }

    public void standDown() {
        this.running = false;
    }

    //gives the thread `MS_BETWEEN_CALL_ALLOWED` milliseconds to call `promiseNotDead` again
    public void promiseNotDead() {
        if(running) this.killTime = RobotTime.currentTimeMillis() + MS_BETWEEN_CALL_ALLOWED;
    }

    public void setState(String state) {
        this.state = state;
    }

    public boolean blockUntilDone() {
        while(!this.cleanupDone) Thread.yield();
        return success;
    }
}

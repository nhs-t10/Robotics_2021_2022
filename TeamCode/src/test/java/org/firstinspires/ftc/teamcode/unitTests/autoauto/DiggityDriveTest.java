package org.firstinspires.ftc.teamcode.unitTests.autoauto;

import static org.firstinspires.ftc.teamcode.unitTests.TestTypeManager.testRunTypeIs;
import static org.junit.Assert.assertTrue;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.__compiledautoauto.teamcode.opmodes.auto.DiggityDrive__autoauto;
import org.firstinspires.ftc.teamcode.managers.feature.FeatureManager;
import org.firstinspires.ftc.teamcode.unitTests.dummy.DummyGamepad;
import org.firstinspires.ftc.teamcode.unitTests.dummy.DummyHardwareMap;
import org.firstinspires.ftc.teamcode.unitTests.dummy.DummyTelemetry;
import org.junit.Test;

public class DiggityDriveTest {
    @Test
    public void runTest() {
        if(!testRunTypeIs("long")) return;

        DiggityDrive__autoauto opmode = new DiggityDrive__autoauto();
        opmode.telemetry = new DummyTelemetry();
        opmode.gamepad1 = new DummyGamepad();
        opmode.gamepad2 = new DummyGamepad();
        opmode.hardwareMap = new DummyHardwareMap();

        LoopRunnerThread runningOpmode = new LoopRunnerThread(opmode);
        WatchdogThread watchdogThread = runningOpmode.watchdog;
        runningOpmode.start();

        TestInputterThread testInputterThread = new TestInputterThread(opmode, runningOpmode);
        testInputterThread.start();

        assertTrue(watchdogThread.blockUntilDone());
    }
    private static final long HUMAN_REACTION_TIME = 265;

    private static class TestInputterThread extends Thread {
        private final OpMode opmode;
        private final LoopRunnerThread tested;
        private boolean running = true;

        public TestInputterThread(OpMode opmode, LoopRunnerThread tested) {
            this.opmode = opmode;
            this.tested = tested;
        }

        public void inputTests(OpMode opmode) throws InterruptedException {
            sleep(60_000);
            seemsGood();
        }

        private void seemsGood() {
            running = false;
            tested.testOver();
        }

        public void run() {
            try {
                inputTests(opmode);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


    }

    private static class LoopRunnerThread extends Thread {
        WatchdogThread watchdog;
        public OpMode opmode;
        long startTime;
        private boolean running = true;

        public LoopRunnerThread(OpMode mode) {
            //watch self
            (watchdog = new WatchdogThread(this)).start();

            this.opmode = mode;
            startTime = System.currentTimeMillis();
        }

        @Override
        public void run() {

            //init!
            watchdog.promiseNotDead(30_000);

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


            for(int i = 0; running; i++) {
                opmode.time = (System.currentTimeMillis() - startTime)/1000.0;
                watchdog.setState("loop (iteration " + i + " (started " + opmode.time + "s in))");

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
    private static class WatchdogThread extends Thread {
        private final LoopRunnerThread watched;

        private final static long MS_BETWEEN_CALL_ALLOWED = HUMAN_REACTION_TIME;

        private long killTime;
        private boolean running;
        private String state;
        private boolean success;
        private boolean cleanupDone;

        public WatchdogThread(LoopRunnerThread t) {
            this.watched = t;
            this.running = true;
            this.killTime = System.currentTimeMillis() + MS_BETWEEN_CALL_ALLOWED;
            this.state = "";
        }
        @Override
        public void run() {
            while(System.currentTimeMillis() < killTime) Thread.yield();

            if(!watched.isAlive()) {
                FeatureManager.logger.log("Success!");
                success = true;
            } else {
                FeatureManager.logger.log("blocked in " + state + "!");
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
            promiseNotDead(MS_BETWEEN_CALL_ALLOWED);
        }
        public void promiseNotDead(long ms) {
            if(running) this.killTime = System.currentTimeMillis() + ms;
        }

        public void setState(String state) {
            this.state = state;
        }

        public boolean blockUntilDone() {
            while(!this.cleanupDone) Thread.yield();
            return success;
        }
    }
}

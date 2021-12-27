package org.firstinspires.ftc.teamcode.unitTests.dummy;

import com.qualcomm.robotcore.hardware.DcMotor;

import java.lang.ref.WeakReference;

public class DummyMotorMovementThread extends Thread {
    //use a WeakReference so this thread won't keep its motor from being garbage collected
    private final WeakReference<DummyDcMotor> motorRef;
    public volatile boolean running;

    private double ticksPerRot;

    public DummyMotorMovementThread(DummyDcMotor m) {
        this.running = true;
        this.motorRef = new WeakReference<>(m);
        this.ticksPerRot = m.ticksPerRot;
    }
    @Override
    public void run() {
        while(running) {
            try {
                //get the motor from the weak reference. If the result is `null`, it's been garbaged and this thread should stop.
                DummyDcMotor motor = motorRef.get();
                if(motor == null) {
                    running = false;
                    break;
                }

                double currentPosition = motor.currentPosition, target = motor.target;

                if (motor.runMode == DcMotor.RunMode.RUN_TO_POSITION) {
                    //if it's RUN_TO_POSITION, run the motor to the target
                    double delta = target - currentPosition;
                    motor.currentPosition += Math.min(delta, ticksPerRot) * 0.1;
                } else {
                    //if it's not RUN_TO_POSITION, just increment by the power.
                    motor.currentPosition += motor.power * ticksPerRot * 0.1;
                }
                sleep(10);
            } catch(InterruptedException ignored) {}
        }
    }
}

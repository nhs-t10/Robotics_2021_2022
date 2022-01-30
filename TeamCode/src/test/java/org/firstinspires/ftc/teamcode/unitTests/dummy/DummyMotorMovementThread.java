package org.firstinspires.ftc.teamcode.unitTests.dummy;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.auxilary.RobotTime;

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
        long lastIterationTime = RobotTime.currentTimeMillis();
        while(running) {
            long thisIterationTime = RobotTime.currentTimeMillis();
            long iterDelta = thisIterationTime - lastIterationTime;

            //get the motor from the weak reference. If the result is `null`, it's been garbaged and this thread should stop.
            DummyDcMotor motor = motorRef.get();
            if(motor == null) {
                running = false;
                break;
            }

            double currentPosition = motor.currentPosition, target = motor.target;

            //just taking iterDelta would mean 1000rpm, so we divide by 100 for 10rpm.
            double movementTimeCoef = iterDelta / 100.0;

            if (motor.runMode == DcMotor.RunMode.RUN_TO_POSITION) {
                //if it's RUN_TO_POSITION, run the motor to the target
                double delta = target - currentPosition;
                motor.currentPosition += Math.min(delta, ticksPerRot) * movementTimeCoef;
            } else {
                //if it's not RUN_TO_POSITION, just increment by the power (or decrement if it's reverse).
                double movement = -1 * motor.power * ticksPerRot * movementTimeCoef;
                if (motor.direction == DcMotorSimple.Direction.REVERSE)
                    motor.currentPosition -= movement;
                else motor.currentPosition += movement;
            }
            lastIterationTime = thisIterationTime;
            Thread.yield();
        }
    }
}

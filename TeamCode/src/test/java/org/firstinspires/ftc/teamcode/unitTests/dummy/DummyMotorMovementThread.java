package org.firstinspires.ftc.teamcode.unitTests.dummy;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.auxilary.PaulMath;
import org.firstinspires.ftc.teamcode.auxilary.RobotTime;

import java.lang.ref.WeakReference;

public class DummyMotorMovementThread extends Thread {
    //use a WeakReference so this thread won't keep its motor from being garbage collected
    private final WeakReference<DummyDcMotor> motorRef;
    public volatile boolean running;

    private double lastVelocityRotPerms;

    public final double MAX_ROTS_PER_MS = 0.01;
    public final double DRAG_COEF = 0.5;

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
            double deltaTime = thisIterationTime - lastIterationTime;

            //get the motor from the weak reference. If the result is `null`, it's been garbaged and this thread should stop.
            DummyDcMotor motor = motorRef.get();
            if(motor == null) {
                running = false;
                break;
            }

            //just taking iterDelta would mean 1000rpm, so we divide by 100 for 10rpm.
            double movementTimeCoef = deltaTime / 100.0;

            if (motor.runMode == DcMotor.RunMode.RUN_TO_POSITION) {
                moveMotorTowardsTarget(motor, deltaTime);

            } else {
                moveMotorAccordingToPower(motor, deltaTime);
            }
            lastIterationTime = thisIterationTime;
            Thread.yield();
        }
    }

    private void moveMotorAccordingToPower(DummyDcMotor motor, double elapsedTimeMs) {
        double acceleration = motor.power * 5;
        if(motor.direction.equals(DcMotorSimple.Direction.REVERSE)) acceleration *= -1;

        double velocity = lastVelocityRotPerms * DRAG_COEF + acceleration * elapsedTimeMs;
        velocity = PaulMath.clamp(velocity, -MAX_ROTS_PER_MS, MAX_ROTS_PER_MS);

        motor.currentPosition += (velocity * elapsedTimeMs) * ticksPerRot;

        lastVelocityRotPerms = velocity;
    }

    private void moveMotorTowardsTarget(DummyDcMotor motor, double elapsedTimeMs) {
        double currentPosition = motor.currentPosition, target = motor.target;

        double delta = target - currentPosition;
        motor.currentPosition += Math.min(delta, ticksPerRot) * (elapsedTimeMs / 100.0);
    }
}

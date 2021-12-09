package org.firstinspires.ftc.teamcode.managers.nate;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.auxilary.PaulMath;
import org.firstinspires.ftc.teamcode.managers.feature.FeatureManager;
import org.firstinspires.ftc.teamcode.managers.manipulation.ManipulationManager;
import org.firstinspires.ftc.teamcode.managers.sensor.SensorManager;

public class NateManager extends FeatureManager {
    private boolean input;
    private boolean found;
    private int position;
    private int currentPosition;
    private double currentPos;
    private boolean clawState = false;
    ManipulationManager hands;

    private double resetOffset;

    public NateManager(ManipulationManager hands){

        this.hands = hands;
    }

    //TODO: test and remove untested warning.
    /**
     * Make the claw open or close
     *
     * <h2>Warning: This method has not been tested in the wild. Use it, but don't rely fully until it's been tested.</h2>
     *
     * @param open If {@code true}, open the claw; otherwise, close it.
     */
    public void setClawOpen(boolean open) {
        //by setting the `clawState` variable to the opposite and then calling toggleClawOpen(), we can cut down on code reuse.
        //toggleClawOpen() will reverse it back to the value the user asked for.
        hands.setServoPosition("nateClaw", open ? 1 : 0);
    }

    /**
     * Toggle whether the robot's claw is open.
     * If the claw is closed, it'll become open. If it's open, it'll become closed.
     */
    public void toggleClaw(){
        clawState = !clawState;
        setClawOpen(clawState);
    }

    //TODO: test and remove untested warning.
    /**
     * Get the current claw status
     * <h2>Warning: This method has not been tested in the wild. Use it, but don't rely fully until it's been tested.</h2>
     *
     * @return {@code true} if the claw is open, {@code false} if the claw is closed.
     */
    public boolean getClawStatus() {
        return clawState;
    }

    /**
     * Stop any asynchronous movement of the claw lift immediately
     * @see #positionOne()
     * @see #positionTwo()
     * @see #positionThree()
     * @see #positionHome()
     */
    public void emergencyStop(){
//        currentPos = hands.getMotorPosition("ClawMotor");
//        currentPosition = (int) Math.round(currentPos);
//        hands.encodeMoveToPosition("ClawMotor", currentPosition, 0.75);
        hands.cancelEncodedMovement("ClawMotor");
    }

    /**
     * Checks that the claw lift <u>isn't</u> moving asynchronously.
     *
     * @see #positionOne()
     * @see #positionTwo()
     * @see #positionThree()
     * @see #positionHome()
     *
     * @return {@code false} if the claw lift has any asynchronous movement; {@code true} otherwise
     */
    public boolean liftMovementFinished() {
        return !hands.hasEncodedMovement("ClawMotor");
    }

    /**
     * <p>Move the claw's lift to position 1, the highest position.</p>
     *
     * <p>Note that this is an <u>asynchronous</u> method, meaning that the method will complete before the action does.
     * Something like {@code positionOne(); toggleClawOpen()} will open the claw <u>right away</u>. It does <u>not</u> wait for the lift to move.
     * </p>
     * <p>
     * If you want autonomous to have synchronous-ish behaviour, use the {@link #liftMovementFinished clawMovementFinished()} method, like so:
     * <pre><code> positionOne(), when(clawMovementFinished()) next; </code></pre>
     * </p>
     */
    public void positionOne(){
        if (!found){
            position = -1490;
        }
        else {
            position = -3470;
        }


        hands.encodeMoveToPosition("ClawMotor", position, 1);
    }

    /**
     * <p>Move the claw's lift to position 2, the middle position.</p>
     *
     * <p>Note that this is an <u>asynchronous</u> method, meaning that the method will complete before the action does.
     * Something like {@code positionTwo(); toggleClawOpen()} will open the claw <u>right away</u>. It does <u>not</u> wait for the lift to move.
     * </p>
     * <p>
     * If you want autonomous to have synchronous-ish behaviour, use the {@link #liftMovementFinished clawMovementFinished()} method, like so:
     * <pre><code> positionTwo(), when(clawMovementFinished()) next; </code></pre>
     * </p>
     */
    public void positionTwo(){
        if(!found) {
            position = -3024;
        } else{
            position = -5295;
        }

        hands.encodeMoveToPosition("ClawMotor", position, 1);
    }

    /**
     * <p>Move the claw's lift to position 3, the lowest position.</p>
     *
     * <p>Note that this is an <u>asynchronous</u> method, meaning that the method will complete before the action does.
     * Something like {@code positionThree(); toggleClawOpen()} will open the claw <u>right away</u>. It does <u>not</u> wait for the lift to move.
     * </p>
     * <p>
     * If you want autonomous to have synchronous-ish behaviour, use the {@link #liftMovementFinished clawMovementFinished()} method, like so:
     * <pre><code> positionThree(), when(clawMovementFinished()) next; </code></pre>
     * </p>
     */
    public void positionThree(){
        if(!found) {
            position = -4679;
        } else {
            position = -6893;
        }

        hands.encodeMoveToPosition("ClawMotor", position, 1);
    }

    /**
     * <p>Move the claw's lift back to the home position inside the chassis in order to intake objects.</p>
     *
     * <p>Note that this is an <u>asynchronous</u> method, meaning that the method will complete before the action does.
     * Something like {@code positionHome(); toggleClawOpen()} will open the claw <u>right away</u>. It does <u>not</u> wait for the lift to move.
     * </p>
     * <p>
     * If you want autonomous to have synchronous-ish behaviour, use the {@link #liftMovementFinished clawMovementFinished()} method, like so:
     * <pre><code> positionHome(), when(clawMovementFinished()) next; </code></pre>
     * </p>
     */
    public void positionHome(){
        if(!found){
            position = 2537;
        } else {
            position = 570;
        }

        hands.encodeMoveToPosition("ClawMotor", position, 1);
    }

    /**
     * <p>Move the claw back to the {@link #positionHome() home position} by scanning the sensors on the lift.</p>
     *
     * <p>This is an asynchronous method, meaning that the method will complete before the action does. Unfortunately, the {@link #liftMovementFinished()}
     * method won't work here. Try using time instead :(</p>
     *
     * <h2>TODO: <i><u>THIS DOES NOT WORK.</u></i> We should make it work.</h2>
     * <p>To do that, we need a sensor connected in the code.</p>
     */

    public void homing(){
        //`input` will always be false for now, but we need to replace it with `sensor.isPressed()` or whichever the correct method is
        if (found) {
            hands.encodeMoveToPosition("ClawMotor", 0, 0.25);
        } else if (input) {
            found = true;
            hands.setMotorPower("ClawMotor",0);
            hands.setMotorMode("ClawMotor", DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        } else {
            hands.setMotorPower("ClawMotor", -0.75);
        }
    }
}

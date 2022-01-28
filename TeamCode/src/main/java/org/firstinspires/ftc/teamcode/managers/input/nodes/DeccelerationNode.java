package org.firstinspires.ftc.teamcode.managers.input.nodes;

import androidx.annotation.NonNull;

import org.firstinspires.ftc.teamcode.auxilary.PaulMath;
import org.firstinspires.ftc.teamcode.managers.input.InputManager;
import org.firstinspires.ftc.teamcode.managers.input.InputManagerNodeResult;

public class DeccelerationNode extends InputManagerInputNode{

    private final InputManagerNodeResult result = new InputManagerNodeResult();

    private InputManagerInputNode control;
    private InputManagerInputNode startingSpeed;
    private InputManagerInputNode endingSpeed;
    private InputManagerInputNode movementTime;

    private boolean wasPressed;
    private long decelerationStartTime;

    public DeccelerationNode(InputManagerInputNode control, InputManagerInputNode startingSpeed, InputManagerInputNode endingSpeed, InputManagerInputNode movementTime) {
        this.control = control;
        this.startingSpeed = startingSpeed;
        this.endingSpeed = endingSpeed;
        this.movementTime = movementTime;
    }

    @Override
    public void init(InputManager boss) {
        control.init(boss);
        startingSpeed.init(boss);
        endingSpeed.init(boss);
        movementTime.init(boss);
    }

    @Override
    public void update() {
        control.update();
        startingSpeed.update();
        endingSpeed.update();
        movementTime.update();

        float totalMovementTime = movementTime.getResult().getFloat();

        boolean isPressed = control.getResult().getBool();

        if(isPressed == false && wasPressed == true) {
            decelerationStartTime = System.currentTimeMillis();
        }

        float starting = startingSpeed.getResult().getFloat();
        float ending = endingSpeed.getResult().getFloat();

        float resultNumber = starting;

        if(!isPressed && System.currentTimeMillis() > decelerationStartTime && decelerationStartTime != 0) {
            long timeSinceStart = System.currentTimeMillis() - decelerationStartTime;
            float percentageCompleted = Math.min(1, timeSinceStart / totalMovementTime);

            resultNumber = starting - percentageCompleted * (starting - ending);
        }

        wasPressed = isPressed;

        result.setFloat(resultNumber);
    }

    @NonNull
    @Override
    public InputManagerNodeResult getResult() {
        return result;
    }

    @Override
    public int complexity() {
        return control.complexity() + startingSpeed.complexity() + endingSpeed.complexity() + movementTime.complexity() + 1;
    }

    @Override
    public String[] getKeysUsed() {
        return PaulMath.concatArrays(control.getKeysUsed(), startingSpeed.getKeysUsed(), endingSpeed.getKeysUsed(), movementTime.getKeysUsed());
    }

    @Override
    public boolean usesKey(String s) {
        return control.usesKey(s) || startingSpeed.usesKey(s) || endingSpeed.usesKey(s) || movementTime.usesKey(s);
    }
}

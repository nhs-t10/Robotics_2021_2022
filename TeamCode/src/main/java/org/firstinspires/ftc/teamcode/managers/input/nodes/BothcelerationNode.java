package org.firstinspires.ftc.teamcode.managers.input.nodes;

import androidx.annotation.NonNull;

import org.firstinspires.ftc.teamcode.auxilary.PaulMath;
import org.firstinspires.ftc.teamcode.managers.input.InputManager;
import org.firstinspires.ftc.teamcode.managers.input.InputManagerNodeResult;

public class BothcelerationNode extends InputManagerInputNode{

    private final InputManagerNodeResult result = new InputManagerNodeResult();

    private InputManagerInputNode control;
    private InputManagerInputNode defaultSpeed;
    private InputManagerInputNode alternativeSpeed;
    private InputManagerInputNode movementTime;

    private boolean wasPressed;
    private long interpolationStartTime;
    private float interpolationStartValue;
    private float interpolationEndValue;

    public BothcelerationNode(InputManagerInputNode control, InputManagerInputNode defaultSpeed, InputManagerInputNode alternativeSpeed, InputManagerInputNode movementTime) {
        this.control = control;
        this.defaultSpeed = defaultSpeed;
        this.alternativeSpeed = alternativeSpeed;
        this.movementTime = movementTime;
    }

    @Override
    public void init(InputManager boss) {
        control.init(boss);
        defaultSpeed.init(boss);
        alternativeSpeed.init(boss);
        movementTime.init(boss);
    }

    @Override
    public void update() {
        control.update();
        defaultSpeed.update();
        alternativeSpeed.update();
        movementTime.update();

        float totalMovementTime = movementTime.getResult().getFloat();

        boolean isPressed = control.getResult().getBool();

        if(interpolationStartTime == 0) result.setFloat(defaultSpeed.getResult().getFloat());

        if(isPressed != wasPressed) {
            interpolationStartTime = System.currentTimeMillis();
            interpolationStartValue = result.getFloat();
            if(isPressed) {
                interpolationEndValue = alternativeSpeed.getResult().getFloat();
            } else {
                interpolationEndValue = defaultSpeed.getResult().getFloat();
            }
        }

        long timeSinceStart = System.currentTimeMillis() - interpolationStartTime;
        float percentageCompleted = Math.max(0,Math.min(1, timeSinceStart / totalMovementTime));

        float resultNumber = interpolationStartValue + percentageCompleted * (interpolationEndValue - interpolationStartValue);

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
        return control.complexity() + defaultSpeed.complexity() + alternativeSpeed.complexity() + movementTime.complexity() + 1;
    }

    @Override
    public String[] getKeysUsed() {
        return PaulMath.concatArrays(control.getKeysUsed(), defaultSpeed.getKeysUsed(), alternativeSpeed.getKeysUsed(), movementTime.getKeysUsed());
    }

    @Override
    public boolean usesKey(String s) {
        return control.usesKey(s) || defaultSpeed.usesKey(s) || alternativeSpeed.usesKey(s) || movementTime.usesKey(s);
    }
}

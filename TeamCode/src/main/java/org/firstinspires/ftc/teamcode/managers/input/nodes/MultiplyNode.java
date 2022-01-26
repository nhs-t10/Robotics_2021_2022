package org.firstinspires.ftc.teamcode.managers.input.nodes;

import androidx.annotation.NonNull;

import org.firstinspires.ftc.teamcode.auxilary.PaulMath;
import org.firstinspires.ftc.teamcode.managers.input.InputManager;
import org.firstinspires.ftc.teamcode.managers.input.InputManagerNodeResult;

public class MultiplyNode extends InputManagerInputNode {
    private final InputManagerInputNode input;
    private InputManagerInputNode multiplier;
    private final InputManagerNodeResult result = new InputManagerNodeResult();

    /**
     * Multiplies one number by another.
     * @param input The input listed in the MultiplyNode
     * @param multiplier The number that the input will be multiplied by (it can be a second input, if you want)
     * @see MinusNode#MinusNode(InputManagerInputNode, InputManagerInputNode) MinusNode
     */
    public MultiplyNode(InputManagerInputNode input, InputManagerInputNode multiplier) {
        this.input = input;
        this.multiplier = multiplier;
    }

    public MultiplyNode(InputManagerInputNode input, float multiplyBy) {
        this.multiplier = new StaticValueNode(multiplyBy);
        this.input = input;
    }
    public MultiplyNode(float multiplyBy, InputManagerInputNode input) {
        this.multiplier = new StaticValueNode(multiplyBy);
        this.input = input;
    }

    @Override
    public void init(InputManager boss) {
        input.init(boss);
        multiplier.init(boss);
    }

    public void update() {
        input.update();
        multiplier.update();
    }

    @NonNull
    @Override
    public InputManagerNodeResult getResult() {
        float muliplier = multiplier.getResult().getFloat();
        float value = input.getResult().getFloat();
        value = value * muliplier;
        result.setFloat(value);
        return result;
    }

    @Override
    public int complexity() {
        return input.complexity() + multiplier.complexity() + 1;
    }

    @Override
    public String[] getKeysUsed() {
        return PaulMath.concatArrays(input.getKeysUsed(), multiplier.getKeysUsed());
    }

    @Override
    public boolean usesKey(String s) {
        if (input.usesKey(s) || multiplier.usesKey(s)){
            return true;
        }
        return false;
    }
}

package org.firstinspires.ftc.teamcode.managers.input.nodes;

import org.firstinspires.ftc.teamcode.auxilary.PaulMath;
import org.firstinspires.ftc.teamcode.managers.input.InputManager;
import org.firstinspires.ftc.teamcode.managers.input.InputManagerNodeResult;

public class BothNode extends InputManagerInputNode {
    private final InputManagerInputNode input1;
    private InputManager boss;

    private InputManagerInputNode input2;

    public BothNode(InputManagerInputNode input1, InputManagerInputNode input2) {
        this.input1 = input1;
        this.input2 = input2;
    }

    @Override
    public void init(InputManager boss) {
        this.boss = boss;
        input1.init(boss);
        input2.init(boss);
    }

    @Override
    public void update() {
        input1.update();
        input2.update();
    }

    @Override
    public InputManagerNodeResult getResult() {
        boolean input1Check = input1.getResult().getBool();
        boolean input2Check = input2.getResult().getBool();
        return new InputManagerNodeResult(input1Check && input2Check ? 1f : 0f);
    }

    @Override
    public int complexity() {
        return input1.complexity() + input2.complexity() + 1;
    }

    @Override
    public String[] getKeysUsed() {
        return PaulMath.concatArrays(input1.getKeysUsed(), input2.getKeysUsed());
    }

    @Override
    public boolean usesKey(String s) {
        return input1.usesKey(s) || input2.usesKey(s);
    }
}

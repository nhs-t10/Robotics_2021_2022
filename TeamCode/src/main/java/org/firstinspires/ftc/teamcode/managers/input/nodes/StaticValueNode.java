package org.firstinspires.ftc.teamcode.managers.input.nodes;

import org.firstinspires.ftc.teamcode.managers.input.InputManager;
import org.firstinspires.ftc.teamcode.managers.input.InputManagerNodeResult;

public class StaticValueNode extends InputManagerInputNode {
    private InputManager boss;

    private float v;

    public StaticValueNode(float f) {
        this.v = f;
    }

    @Override
    public void init(InputManager boss) {
        this.boss = boss;
    }

    @Override
    public void update() {
    }

    @Override
    public InputManagerNodeResult getResult() {
        return new InputManagerNodeResult(v);
    }
}

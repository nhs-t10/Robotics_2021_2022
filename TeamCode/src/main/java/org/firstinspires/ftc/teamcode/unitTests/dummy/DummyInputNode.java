package org.firstinspires.ftc.teamcode.unitTests.dummy;

import org.firstinspires.ftc.teamcode.managers.input.InputManager;
import org.firstinspires.ftc.teamcode.managers.input.InputManagerNodeResult;
import org.firstinspires.ftc.teamcode.managers.input.nodes.InputManagerInputNode;

public class DummyInputNode extends InputManagerInputNode {
    private float value;

    public DummyInputNode() {
        this(0);
    }
    public DummyInputNode(float v) {
        this.value = v;
    }

    @Override
    public void init(InputManager boss) {

    }

    @Override
    public void update() {

    }

    @Override
    public final InputManagerNodeResult getResult() {
        return new InputManagerNodeResult(value);
    }

    public void set(float v) {
        this.value = v;
    }
}

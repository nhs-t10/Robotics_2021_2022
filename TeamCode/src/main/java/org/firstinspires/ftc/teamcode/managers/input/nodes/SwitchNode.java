package org.firstinspires.ftc.teamcode.managers.input.nodes;

import org.firstinspires.ftc.teamcode.managers.input.InputManager;
import org.firstinspires.ftc.teamcode.managers.input.InputManagerNodeResult;

public class SwitchNode extends InputManagerInputNode {
    private final InputManagerInputNode node;
    private final InputManagerInputNode nodeIfOff;
    private final InputManagerInputNode nodeIfOn;

    private InputManager boss;

    private boolean wasPressed;
    private boolean toggledOn;
    private boolean on;

    public SwitchNode(InputManagerInputNode onOff, InputManagerInputNode ifOn, InputManagerInputNode ifOff) {
        this.node = onOff;
        this.nodeIfOn = ifOn;
        this.nodeIfOff = ifOff;
    }

    @Override
    public void init(InputManager boss) {
        this.boss = boss;
    }

    @Override
    public void update() {
        node.update();
        nodeIfOn.update();
        nodeIfOff.update();

        on = node.getResult().getBool();
    }

    @Override
    public InputManagerNodeResult getResult() {
        if(on) return nodeIfOn.getResult();
        else return nodeIfOff.getResult();
    }
}

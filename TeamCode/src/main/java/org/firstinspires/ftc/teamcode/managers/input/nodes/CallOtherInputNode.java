package org.firstinspires.ftc.teamcode.managers.input.nodes;

import org.firstinspires.ftc.teamcode.managers.input.InputManager;
import org.firstinspires.ftc.teamcode.managers.input.InputManagerNodeResult;

public class CallOtherInputNode extends InputManagerInputNode {
    private final String target;
    private InputManager boss;

    public CallOtherInputNode(String name) {
        this.target = name;
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
        InputManagerInputNode targetNode = boss.getInputNode(target);

        if(targetNode == null) return new InputManagerNodeResult(0f);
        else return targetNode.getResult();
    }
}

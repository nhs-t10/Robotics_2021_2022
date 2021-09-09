package org.firstinspires.ftc.teamcode.managers.input.nodes;

import org.firstinspires.ftc.teamcode.managers.input.InputManager;
import org.firstinspires.ftc.teamcode.managers.input.InputManagerNodeResult;

public class ButtonNode extends InputManagerInputNode {
    private final String key;
    private InputManager boss;

    public ButtonNode(String key) {
        this.key = key;
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
        return new InputManagerNodeResult(boss.getKey(key));
    }
}

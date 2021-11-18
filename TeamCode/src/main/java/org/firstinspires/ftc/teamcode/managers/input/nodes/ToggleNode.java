package org.firstinspires.ftc.teamcode.managers.input.nodes;

import org.firstinspires.ftc.teamcode.managers.input.InputManager;
import org.firstinspires.ftc.teamcode.managers.input.InputManagerNodeResult;

public class ToggleNode extends InputManagerInputNode {
    private final InputManagerInputNode node;
    private InputManager boss;

    private boolean wasPressed;
    private boolean toggledOn;

    public ToggleNode(InputManagerInputNode node) {
        this.node = node;
    }

    @Override
    public void init(InputManager boss) {
        this.boss = boss;
    }

    @Override
    public void update() {
        node.update();
        boolean isPressed = node.getResult().getBool();
        if(isPressed && !wasPressed) {
            toggledOn = !toggledOn;
        }
        wasPressed = isPressed;
    }

    @Override
    public InputManagerNodeResult getResult() {
        return new InputManagerNodeResult(toggledOn ? 1f : 0f);
    }

    @Override
    public int complexity() {
        return node.complexity() + 1;
    }

    @Override
    public String[] getKeysUsed() {
        return node.getKeysUsed();
    }

    @Override
    public boolean usesKey(String s) {
        return node.usesKey(s);
    }
}

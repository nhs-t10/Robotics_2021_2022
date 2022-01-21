package org.firstinspires.ftc.teamcode.managers.input.nodes;

import org.firstinspires.ftc.teamcode.managers.input.InputManager;
import org.firstinspires.ftc.teamcode.managers.input.InputManagerNodeResult;

public class ToggleNode extends InputManagerInputNode {
    private final InputManagerInputNode node;

    private boolean wasPressed;
    private boolean toggledOn;
    private final InputManagerNodeResult result = new InputManagerNodeResult();

    public ToggleNode(InputManagerInputNode node) {
        this.node = node;
    }

    @Override
    public void init(InputManager boss) {
        node.init(boss);
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
        result.setBool(toggledOn);
        return result;
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

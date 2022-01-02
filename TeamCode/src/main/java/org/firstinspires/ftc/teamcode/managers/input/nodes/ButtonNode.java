package org.firstinspires.ftc.teamcode.managers.input.nodes;

import org.firstinspires.ftc.teamcode.managers.input.InputManager;
import org.firstinspires.ftc.teamcode.managers.input.InputManagerNodeResult;
import org.firstinspires.ftc.teamcode.managers.input.buttonhandles.ButtonHandle;

public class ButtonNode extends InputManagerInputNode {
    private ButtonHandle keyHandle;
    private final String key;
    private InputManager boss;
    private final InputManagerNodeResult result = new InputManagerNodeResult();

    public ButtonNode(String key) {
        this.key = key;
    }

    @Override
    public void init(InputManager boss) {
        this.keyHandle = boss.getButtonHandle(key);
    }

    @Override
    public void update() {

    }

    @Override
    public InputManagerNodeResult getResult() {
        this.result.setFloat(keyHandle.get());
        return result;
    }

    @Override
    public int complexity() {
        return 0;
    }

    @Override
    public String[] getKeysUsed() {
        return new String[] {key};
    }

    @Override
    public boolean usesKey(String s) {
        return key.equals(s);
    }
}

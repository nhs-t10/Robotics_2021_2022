package org.firstinspires.ftc.teamcode.managers.input.nodes;

import org.firstinspires.ftc.teamcode.managers.input.InputManager;
import org.firstinspires.ftc.teamcode.managers.input.InputManagerNodeResult;

public class JoystickNode extends InputManagerInputNode {
    private final String key;
    private InputManager boss;
    private final InputManagerNodeResult result = new InputManagerNodeResult();

    public JoystickNode(String key) {
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
        this.result.setFloat(boss.getKey(key));
        return result;
    }

    @Override
    public int complexity() {
        return 0;
    }

    @Override
    public String[] getKeysUsed() {
        return new String[] { key };
    }

    @Override
    public boolean usesKey(String s) {
        return key.equals(s);
    }
}

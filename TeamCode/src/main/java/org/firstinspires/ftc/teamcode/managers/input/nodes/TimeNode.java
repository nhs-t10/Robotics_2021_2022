package org.firstinspires.ftc.teamcode.managers.input.nodes;

import org.firstinspires.ftc.teamcode.managers.input.InputManager;
import org.firstinspires.ftc.teamcode.managers.input.InputManagerNodeResult;

import androidx.annotation.NonNull;

public class TimeNode extends InputManagerInputNode {
    private final InputManagerInputNode node;
    private InputManager boss;

    private float time;
    private boolean turnedOn;
    private long timeTurnedOn;
    private final InputManagerNodeResult result = new InputManagerNodeResult();

    public TimeNode(InputManagerInputNode node, float numberOfMillis) {
        this.time = (numberOfMillis);
        this.node = node;
    }
    public TimeNode(float numberOfMillis, InputManagerInputNode node) {
        this.time = numberOfMillis;
        this.node = node;
    }

    @Override
    public void init(InputManager boss) {
        this.boss = boss;
        node.init(boss);
    }

    @Override
    public void update() {
        node.update();

        if(!turnedOn) {
            this.turnedOn = node.getResult().getBool();
            this.timeTurnedOn = System.currentTimeMillis();
        } else {
            if(System.currentTimeMillis() - this.time > this.timeTurnedOn) {
                this.turnedOn = false;
                this.timeTurnedOn = 0;
            }
        }
        result.setBool(turnedOn);
    }

    @NonNull
    @Override
    public InputManagerNodeResult getResult() {
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

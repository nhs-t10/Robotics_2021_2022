package org.firstinspires.ftc.teamcode.managers.input.nodes;

import org.firstinspires.ftc.teamcode.auxilary.units.TimeUnit;
import org.firstinspires.ftc.teamcode.managers.feature.FeatureManager;
import org.firstinspires.ftc.teamcode.managers.input.InputManager;
import org.firstinspires.ftc.teamcode.managers.input.InputManagerNodeResult;

public class DoubleClickNode extends InputManagerInputNode {
    private final InputManagerInputNode node;
    private InputManager boss;

    private boolean lastTimeClicked;

    boolean firstClicked = false;
    private long timeClickedFirst = 0;
    private boolean currentlyDblClicked;

    private final long clickLimitOffsetNs = (long) TimeUnit.convertBetween(TimeUnit.MS, TimeUnit.NS, FeatureManager.DOUBLE_CLICK_TIME_MS);

    private final InputManagerNodeResult result = new InputManagerNodeResult();


    public DoubleClickNode(InputManagerInputNode node) {
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
        boolean clickedCheck = node.getResult().getBool();

        //only register a doubleclick on rising edge
        boolean isClickedRisingEdge = clickedCheck && !lastTimeClicked;

        long now = System.nanoTime();
        long lastValidClickTime = now - clickLimitOffsetNs;

        //if the first click was before the last valid time, reset the time-keepy-tracky variables
        if (timeClickedFirst <= lastValidClickTime) {
            firstClicked = false;
            timeClickedFirst = 0;
        }

        if(isClickedRisingEdge) {
            if(firstClicked) {
                currentlyDblClicked = true;
            } else {
                timeClickedFirst = System.nanoTime();
                firstClicked = true;
            }
        } else {
            currentlyDblClicked = false;
        }

        lastTimeClicked = clickedCheck;
    }

    @Override
    public InputManagerNodeResult getResult() {
        result.setBool(currentlyDblClicked);
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

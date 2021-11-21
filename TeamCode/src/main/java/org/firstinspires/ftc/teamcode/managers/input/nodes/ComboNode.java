package org.firstinspires.ftc.teamcode.managers.input.nodes;

import org.firstinspires.ftc.teamcode.auxilary.PaulMath;
import org.firstinspires.ftc.teamcode.managers.input.InputManager;
import org.firstinspires.ftc.teamcode.managers.input.InputManagerNodeResult;

public class ComboNode extends InputManagerInputNode {
    InputManagerInputNode[] conditionals;

    private final InputManagerNodeResult result = new InputManagerNodeResult();
    private long[] risingEdgeTime;
    private boolean comboMatch;
    private InputManagerNodeResult lastChildResult;

    public ComboNode(InputManagerInputNode... conditionals) {
        this.conditionals = conditionals;

        risingEdgeTime = new long[conditionals.length];
    }
    @Override
    public void init(InputManager boss) {
        for(InputManagerInputNode i : conditionals) i.init(boss);
    }

    @Override
    public void update() {
        InputManagerNodeResult lastResult = null;
        boolean comboMatch = true;

        for(int i = 0; i < conditionals.length; i++) {
            InputManagerInputNode node = conditionals[i];
            node.update();

            InputManagerNodeResult r = node.getResult();
            boolean rBool = r != null && r.getBool();

            if(!rBool) risingEdgeTime[i] = -1;
            else if(risingEdgeTime[i] == -1) risingEdgeTime[i] = System.nanoTime();


            if(rBool) lastResult = r;

            //check if the list of times is sorted
            if(risingEdgeTime[i] == -1 || (i > 0 && risingEdgeTime[i] < risingEdgeTime[i - 1])) {
                comboMatch = false;
            }
        }
        this.comboMatch = comboMatch;
        this.lastChildResult = lastResult;
    }

    @Override
    public InputManagerNodeResult getResult() {
        if(comboMatch && lastChildResult != null) {
            this.result.copyValuesFrom(lastChildResult);
        } else {
            this.result.setBool(false);
        }

        return result;
    }

    @Override
    public int complexity() {
        int r = 0;
        for(InputManagerInputNode n : conditionals) r += n.complexity();
        return r + 1;
    }

    @Override
    public String[] getKeysUsed() {
        String[][] keylists = new String[conditionals.length][];
        for(int i = 0; i < conditionals.length; i++) {
            keylists[i] = conditionals[i].getKeysUsed();
        }
        return PaulMath.concatArrays(keylists);
    }

    @Override
    public boolean usesKey(String s) {
        for(InputManagerInputNode n : conditionals) {
            if(n.usesKey(s)) return true;
        }
        return false;
    }
}

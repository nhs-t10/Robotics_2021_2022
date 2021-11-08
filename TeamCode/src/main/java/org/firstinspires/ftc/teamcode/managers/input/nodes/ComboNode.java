package org.firstinspires.ftc.teamcode.managers.input.nodes;

import org.firstinspires.ftc.teamcode.managers.input.InputManager;
import org.firstinspires.ftc.teamcode.managers.input.InputManagerNodeResult;

public class ComboNode extends InputManagerInputNode {
    InputManagerInputNode[] conditionals;

    private InputManagerNodeResult result  = new InputManagerNodeResult(0f);
    private long[] risingEdgeTime;

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
        if(comboMatch && lastResult != null) {
            this.result = lastResult;
        } else {
            this.result = new InputManagerNodeResult(0f);
        }
    }

    @Override
    public InputManagerNodeResult getResult() {
        return result;
    }
}

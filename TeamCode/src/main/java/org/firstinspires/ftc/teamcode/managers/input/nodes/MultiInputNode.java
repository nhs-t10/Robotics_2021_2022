package org.firstinspires.ftc.teamcode.managers.input.nodes;

import org.firstinspires.ftc.teamcode.managers.input.InputManager;
import org.firstinspires.ftc.teamcode.managers.input.InputManagerNodeResult;

public class MultiInputNode extends InputManagerInputNode {
    private final InputManagerInputNode[] childs;

    public MultiInputNode(InputManagerInputNode... childs) {
        this.childs = childs;
    }

    @Override
    public void init(InputManager boss) {
        for(InputManagerInputNode node : childs) node.init(boss);
    }

    public void update() {
        for(InputManagerInputNode n : childs) n.update();
    }

    @Override
    public InputManagerNodeResult getResult() {
        InputManagerNodeResult[] vals = new InputManagerNodeResult[childs.length];

        for(int i = 0; i < vals.length; i++) vals[i] = childs[i].getResult();

        return new InputManagerNodeResult(vals);
    }
}

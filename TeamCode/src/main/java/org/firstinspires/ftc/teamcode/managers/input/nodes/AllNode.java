package org.firstinspires.ftc.teamcode.managers.input.nodes;

import org.firstinspires.ftc.teamcode.auxilary.PaulMath;
import org.firstinspires.ftc.teamcode.managers.input.InputManager;
import org.firstinspires.ftc.teamcode.managers.input.InputManagerNodeResult;

public class AllNode extends InputManagerInputNode {
    private final InputManagerInputNode[] inputs;
    private InputManager boss;

    private final InputManagerNodeResult result = new InputManagerNodeResult();

    public AllNode(InputManagerInputNode... inputs) {
        this.inputs = inputs;
    }

    @Override
    public void init(InputManager boss) {
        this.boss = boss;
        for(InputManagerInputNode node : inputs) node.init(boss);
    }

    @Override
    public void update() {
        for(InputManagerInputNode n :inputs) n.update();
    }

    @Override
    public InputManagerNodeResult getResult() {
        boolean isTrue = true;
        for(InputManagerInputNode b : inputs) {
            if(b.getResult().getBool() == false){
                isTrue = false;
                break;
            }
        }
        result.setBool(isTrue);
        return result;
    }

    @Override
    public int complexity() {
        int r = 0;
        for(InputManagerInputNode n : inputs) r += n.complexity();
        return r + 1;
    }

    @Override
    public String[] getKeysUsed() {
        String[][] keylists = new String[inputs.length][];
        for(int i = 0; i < inputs.length; i++) {
            keylists[i] = inputs[i].getKeysUsed();
        }
        return PaulMath.concatArrays(keylists);
    }

    @Override
    public boolean usesKey(String s) {
        for(InputManagerInputNode n : inputs) {
            if(n.usesKey(s)) return true;
        }
        return false;
    }
}

package org.firstinspires.ftc.teamcode.managers.input.nodes;

import org.firstinspires.ftc.teamcode.managers.input.InputManager;
import org.firstinspires.ftc.teamcode.managers.input.InputManagerNodeResult;
import org.firstinspires.ftc.teamcode.managers.input.InputOverlapResolutionMethod;

import java.util.ArrayList;

public abstract class InputManagerInputNode {
    public InputManagerInputNode[] overlappingNodes = new InputManagerInputNode[0];

    public abstract void init(InputManager boss);
    public abstract void update();
    public abstract InputManagerNodeResult getResult();

    public abstract int complexity();
    public abstract String[] getKeysUsed();
    public abstract boolean usesKey(String s);

    public void updateOverlaps(InputOverlapResolutionMethod newMethod, InputManagerInputNode[] allRootNodes) {
        if(newMethod == InputOverlapResolutionMethod.BOTH) {
            //nothing
            this.overlappingNodes = new InputManagerInputNode[0];
        } else {
            String[] keysUsed = getKeysUsed();
            ArrayList<InputManagerInputNode> overlappingNodeList = new ArrayList<>();
            for(InputManagerInputNode comparedNode : allRootNodes) {
                for(String key : keysUsed) {
                    if(comparedNode.usesKey(key)) {
                        if(newMethod == InputOverlapResolutionMethod.PREFER_LEAST_COMPLEX &&comparedNode.complexity() < complexity()) overlappingNodeList.add(comparedNode);
                        else if(newMethod == InputOverlapResolutionMethod.PREFER_MOST_COMPLEX && comparedNode.complexity() > complexity()) overlappingNodeList.add(comparedNode);
                        break;
                    }
                }
            }
            this.overlappingNodes = overlappingNodeList.toArray(new InputManagerInputNode[0]);
        }
    }
    public boolean isOverlapped() {
        for(InputManagerInputNode n : overlappingNodes) {
            if(n.getResult().getBool()) return true;
        }
        return false;
    }
    public InputManagerNodeResult getOverlappedResult() {
        if(isOverlapped()) return new InputManagerNodeResult(false);
        else return getResult();
    }
}

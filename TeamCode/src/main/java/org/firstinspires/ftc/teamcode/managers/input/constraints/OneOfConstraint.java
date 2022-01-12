package org.firstinspires.ftc.teamcode.managers.input.constraints;

import org.firstinspires.ftc.teamcode.managers.input.InputManager;
import org.firstinspires.ftc.teamcode.managers.input.InputManagerNodeResult;
import org.firstinspires.ftc.teamcode.managers.input.nodes.InputManagerInputNode;

public class OneOfConstraint extends InputManagerNodeConstraint {

    private final String[] constrainedNames;
    private String isReplacing;

    public OneOfConstraint(String... constrainedNames) {
        this.constrainedNames = constrainedNames;
    }

    @Override
    public void init(InputManager boss) {

    }

    @Override
    public InputManagerNodeResult getConstrainedResultForNode(InputManagerInputNode node) {
        return null;
    }

    @Override
    public String[] getConstrainedNames() {
        return constrainedNames;
    }
}

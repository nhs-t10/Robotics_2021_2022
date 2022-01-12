package org.firstinspires.ftc.teamcode.managers.input.constraints;

import org.firstinspires.ftc.teamcode.managers.input.InputManager;
import org.firstinspires.ftc.teamcode.managers.input.InputManagerNodeResult;
import org.firstinspires.ftc.teamcode.managers.input.nodes.InputManagerInputNode;

public abstract class InputManagerNodeConstraint {
    public abstract String[] getConstrainedNames();

    public abstract void init(InputManager inputManager);
    public abstract InputManagerNodeResult getConstrainedResultForNode(InputManagerInputNode node);
}

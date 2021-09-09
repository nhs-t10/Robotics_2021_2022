package org.firstinspires.ftc.teamcode.managers.input.nodes;

import org.firstinspires.ftc.teamcode.managers.input.InputManager;
import org.firstinspires.ftc.teamcode.managers.input.InputManagerNodeResult;

public abstract class InputManagerInputNode {
    public abstract void init(InputManager boss);
    public abstract void update();
    public abstract InputManagerNodeResult getResult();
}

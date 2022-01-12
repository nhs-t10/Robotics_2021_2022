package org.firstinspires.ftc.teamcode.managers.input;

import org.firstinspires.ftc.teamcode.auxilary.clocktower.Clocktower;
import org.firstinspires.ftc.teamcode.auxilary.clocktower.ClocktowerCodes;
import org.firstinspires.ftc.teamcode.managers.feature.FeatureManager;
import org.firstinspires.ftc.teamcode.managers.input.nodes.InputManagerInputNode;
import org.firstinspires.ftc.teamcode.managers.input.constraints.InputManagerNodeConstraint;

import java.util.ArrayList;
import java.util.List;

public class InputUpdateThread extends Thread {

    public interface ActionRunnerFunction {
        void run(InputManagerNodeResult results);
    }

    private final InputManagerInputNode node;
    private final ActionRunnerFunction action;
    private InputManagerNodeConstraint constraint;

    public InputUpdateThread(InputManagerInputNode node) {
        this.node = node;
        this.action = null;
    }
    public InputUpdateThread(InputManagerInputNode node, ActionRunnerFunction f) {
        this.node = node;
        this.action = f;
    }

    public void run() {
        //check if action is null only once-- this'll save CPU cycles for later
        if(action == null) {
            while(FeatureManager.isOpModeRunning) {
                node.update();
                Clocktower.time(ClocktowerCodes.INPUT_NODE_UPDATER_THREAD);
                yield();
            }
        } else {
            while(FeatureManager.isOpModeRunning) {
                node.update();

                if(this.constraint == null) action.run(node.getResult());
                else action.run(constraint.getConstrainedResultForNode(node));

                Clocktower.time(ClocktowerCodes.INPUT_NODE_UPDATER_THREAD);
                yield();
            }
        }
    }

    public void addConstraint(InputManagerNodeConstraint constraint) {
        this.constraint = constraint;
    }
}

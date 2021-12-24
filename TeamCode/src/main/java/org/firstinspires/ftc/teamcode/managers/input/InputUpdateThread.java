package org.firstinspires.ftc.teamcode.managers.input;

import org.firstinspires.ftc.teamcode.auxilary.clocktower.Clocktower;
import org.firstinspires.ftc.teamcode.auxilary.clocktower.ClocktowerCodes;
import org.firstinspires.ftc.teamcode.managers.feature.FeatureManager;
import org.firstinspires.ftc.teamcode.managers.input.nodes.InputManagerInputNode;

import java.util.ArrayList;

public class InputUpdateThread extends Thread {
    private final ArrayList<InputManagerInputNode> nodes;
    public InputUpdateThread() {
        nodes = new ArrayList<>();
    }
    public void addNode(InputManagerInputNode node) {
        nodes.add(node);
    }
    @Override
    public void run() {
        while(FeatureManager.isOpModeRunning) {
            synchronized (this) {
            for (InputManagerInputNode node : nodes) {
                    node.update();
                }
            }
            Clocktower.time(ClocktowerCodes.INPUT_NODE_UPDATER_THREAD);
        }
    }
}

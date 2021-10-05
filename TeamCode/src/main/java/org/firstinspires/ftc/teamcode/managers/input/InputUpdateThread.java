package org.firstinspires.ftc.teamcode.managers.input;

import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.managers.FeatureManager;
import org.firstinspires.ftc.teamcode.managers.input.nodes.InputManagerInputNode;

import java.util.ArrayList;

public class InputUpdateThread extends Thread {
    private ArrayList<InputManagerInputNode> nodes;
    public InputUpdateThread() {
        nodes = new ArrayList<InputManagerInputNode>();
    }
    public void addNode(InputManagerInputNode node) {
        nodes.add(node);
    }
    @Override
    public void run() {
        while(FeatureManager.isOpModeRunning) {
            for (InputManagerInputNode node : nodes) node.update();
        }
    }
}

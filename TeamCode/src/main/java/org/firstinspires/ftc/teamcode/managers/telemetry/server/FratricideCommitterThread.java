package org.firstinspires.ftc.teamcode.managers.telemetry.server;

import org.firstinspires.ftc.teamcode.managers.feature.FeatureManager;

import java.io.IOException;
import java.net.ServerSocket;

public class FratricideCommitterThread implements Runnable {
    private final ServerSocket sibling;
    public FratricideCommitterThread(ServerSocket sib) {
        sibling = sib;
    }
    @Override
    public void run() {
        try {
            while (FeatureManager.isOpModeRunning) { }
            sibling.close();
        } catch(IOException ignored) {}
    }
}

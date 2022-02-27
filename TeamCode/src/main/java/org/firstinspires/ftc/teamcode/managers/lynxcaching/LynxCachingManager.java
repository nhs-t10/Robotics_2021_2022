package org.firstinspires.ftc.teamcode.managers.lynxcaching;

import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import java.util.List;

/**
 * "Side-loads" onto the FTC SDK's hardware-board modules, making sure that our concurrent performance isn't wrecked by FTC making everything synchronous :/
 */
public class LynxCachingManager {
    LynxModule[] hubs;
    public LynxCachingManager(OpMode opmode, long msBetweenCacheInvalidation) {
        List<LynxModule> allControlAndExpansionHubs = opmode.hardwareMap.getAll(LynxModule.class);

        //make it an array for better performance under concurrency.
        //we won't ever have a hub added/removed in the middle of our code running!
        this.hubs = allControlAndExpansionHubs.toArray(new LynxModule[0]);

        LynxCacheInvalidationThread cThread = new LynxCacheInvalidationThread(msBetweenCacheInvalidation, this.hubs);
        cThread.start();
    }
}

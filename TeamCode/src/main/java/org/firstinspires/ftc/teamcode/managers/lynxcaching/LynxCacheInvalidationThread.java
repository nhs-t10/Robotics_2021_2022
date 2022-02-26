package org.firstinspires.ftc.teamcode.managers.lynxcaching;

import com.qualcomm.hardware.lynx.LynxModule;

import org.firstinspires.ftc.teamcode.managers.feature.FeatureManager;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class LynxCacheInvalidationThread extends Thread {
    LynxModule[] hubs;
    long wait;

    public LynxCacheInvalidationThread(long msBetweenCacheInvalidation, LynxModule[] hubs) {
        this.wait = msBetweenCacheInvalidation;
        this.hubs = hubs;
    }

    @Override
    public void run() {
        setAllHubsToManualCacheMode();

        try {
            while (FeatureManager.isOpModeRunning) {
                for(LynxModule m : hubs) m.clearBulkCache();
                Thread.sleep(wait);
            }
        } catch (InterruptedException i) {
            this.interrupt();
        }

        setHubsBackToNoCaching();
    }

    private void setAllHubsToManualCacheMode() {
        for(LynxModule m : hubs) {
            m.setBulkCachingMode(LynxModule.BulkCachingMode.MANUAL);
        }
    }

    private void setHubsBackToNoCaching() {
        for(LynxModule m : hubs) {
            m.setBulkCachingMode(LynxModule.BulkCachingMode.OFF);
        }
    }
}

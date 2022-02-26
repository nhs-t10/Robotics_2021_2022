package org.firstinspires.ftc.teamcode.managers.lynxcaching.cachedHardware;

import androidx.annotation.Nullable;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.managers.lynxcaching.LynxCacheInvalidationThread;

public class CachedHardwareMap extends HardwareMap {
    private final HardwareMap parent;
    LynxCacheInvalidationThread cachingThread;

    public CachedHardwareMap(HardwareMap parent, LynxCacheInvalidationThread cachingThread) {
        super(parent.appContext);
        this.parent = parent;
        this.cachingThread = cachingThread;
    }


    //Overriding tryGet() will change the behavior of get() too, since get() uses tryGet()
    @Nullable
    @Override
    public <T> T tryGet(Class<? extends T> classOrInterface, String deviceName) {
        if(classOrInterface == DcMotor.class) {
            return (T) lookupAndCacheMotor(deviceName);
        } else {
            return parent.tryGet(classOrInterface, deviceName);
        }
    }

    private CachedDcMotor lookupAndCacheMotor(String deviceName) {
        DcMotor m = parent.tryGet(DcMotor.class, deviceName);
        if(m == null) return null;

        CachedDcMotor cdm = new CachedDcMotor(m);
        cachingThread.devices.put(deviceName, cdm);

        return cdm;
    }
}

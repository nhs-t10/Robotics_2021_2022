package org.firstinspires.ftc.teamcode.managers.telemetry.server;

import org.firstinspires.ftc.teamcode.auxilary.PaulMath;
import org.firstinspires.ftc.teamcode.managers.telemetry.TelemetryManager;
import org.firstinspires.ftc.teamcode.managers.telemetry.fallible.FallibleHardwareMap;
import org.firstinspires.ftc.teamcode.managers.telemetry.fallible.hardwaredevices.FallibleHardwareDevice;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class FallableHardwareMapHttpFormatty {
    public static String summarizeHardwareDevices(FallibleHardwareMap hardwareMap) {

        HashMap<String, FallibleHardwareDevice> devices = hardwareMap.deviceHashMap;
        String[] objectLines = new String[devices.size()];
        int i = 0;
        for(Map.Entry<String, FallibleHardwareDevice> d : devices.entrySet()) {
            objectLines[i] = PaulMath.JSONify(d.getKey()) + ":" + "{" +
                    "\"type\":"+ PaulMath.JSONify(d.getValue().getClass().getName()) +
                    "\"capabilities\"" + PaulMath.JSONify(d.getValue().getCapabilities()) +
                    "}";
            i++;
        }
        return "{" + Arrays.toString(objectLines) + "}";
    }
    public static String summarizeHardwareDevices(TelemetryManager dataSource) {
        if(dataSource.fallibleHardwareMap == null) return HttpStatusCodeReplies.I_m_a_teapot("This opmode has no fallible hardware map. You can add one with `| BITMASKS.FALLIBLE_HARDWARE` in the TelemetryManager constructor.");
        return summarizeHardwareDevices(dataSource.fallibleHardwareMap);
    }
}

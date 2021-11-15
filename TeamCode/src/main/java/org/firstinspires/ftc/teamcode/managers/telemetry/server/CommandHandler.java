package org.firstinspires.ftc.teamcode.managers.telemetry.server;

import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.AutoautoNumericValue;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.AutoautoPrimitive;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.AutoautoString;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.AutoautoSystemVariableNames;
import org.firstinspires.ftc.teamcode.managers.feature.FeatureManager;
import org.firstinspires.ftc.teamcode.managers.telemetry.TelemetryManager;
import org.firstinspires.ftc.teamcode.managers.telemetry.fallible.FailureType;

import java.util.Arrays;
import java.util.HashMap;

public class CommandHandler {
    public static String handle(String[] args, TelemetryManager dataSource, HashMap<String, StreamHandler> streamRegistry) {
        if(args.length == 0 || args[0] == null) return HttpStatusCodeReplies.Bad_Request;
        if(args.length == 1) return HttpStatusCodeReplies.Unauthorized;

        String command = args[0].trim();
        String streamID = args[1];
        StreamHandler stream = streamRegistry.get(streamID);

        if(stream == null) return HttpStatusCodeReplies.Forbidden;

        switch(command) {
            case ControlCodes.DONT_SEND_YOUR_AUTOAUTO_PROGRAMS_ENTIRE_LIFE_STORY_IT_GETS_BORING:
                stream.programJsonSendingFlag = 0;
                return HttpStatusCodeReplies.No_Content;
            case ControlCodes.ACTUALLY_AUNT_AUTOAUTO_I_DO_WANT_TO_HEAR_YOUR_LIFE_STORY:
                stream.programJsonSendingFlag = 1;
                return HttpStatusCodeReplies.No_Content;
            case ControlCodes.COULD_I_GET_LIKE_A_QUICK_SUMMARY_OF_THE_AUTOAUTO_PROGRAMS_LIFE_PLEASE:
                stream.programJsonSendingFlag = 2;
                return HttpStatusCodeReplies.No_Content;
            case ControlCodes.PLEASE_SKIP_TO_THIS_AUTOAUTO_STATE:
                return moveToAutoautoState(args, dataSource);
            case ControlCodes.THERES_THIS_OPMODE_FIELD_COULD_YOU_SET_IT_PLEASE_TY:
                return setOpmodeField(args, dataSource);
            case ControlCodes.SET_PERSEC_STREAMS:
                return setPersec(args, dataSource, stream);
            case ControlCodes.FAIL_ON_PURPOSE:
                return failOnPurpose(args, dataSource, stream);
            default:
                return HttpStatusCodeReplies.Bad_Request;
        }
    }
    private static String failOnPurpose(String[] args, TelemetryManager dataSource, StreamHandler stream) {
        if(args.length < 4) return HttpStatusCodeReplies.Bad_Request("Not enough arguments; must be command,streamid,devicename,failtype");
        if(dataSource.fallibleHardwareMap == null) return HttpStatusCodeReplies.I_m_a_teapot("This opmode has no fallible hardware map. You can add one with `| BITMASKS.FALLIBLE_HARDWARE` in the TelemetryManager constructor.");

        String deviceName = args[2], failTypeStr = args[3].toUpperCase().trim();
        FailureType failureType = FailureType.NOT_FAILING;
        try {
            failureType = FailureType.valueOf(failTypeStr);
        } catch(IllegalArgumentException e) {
            FailureType[] failtypes = FailureType.values();
            StringBuilder failTypeNames = new StringBuilder();
            for(int i = 0; i < failtypes.length; i++) failTypeNames.append(" ").append(failtypes[i].name());

            return HttpStatusCodeReplies.Bad_Request("`" + failTypeStr + "` is not a valid failure type. Use one of" + failTypeNames);
        }
        if(!dataSource.fallibleHardwareMap.deviceHashMap.containsKey(deviceName)) {
            return HttpStatusCodeReplies.Not_Found("No such hardware device `" + deviceName + "`");
        }

        dataSource.fallibleHardwareMap.deviceHashMap.get(deviceName).setFailureType(failureType);

        return HttpStatusCodeReplies.OK;
    }
    private static String setPersec(String[] args, TelemetryManager dataSource, StreamHandler stream) {
        if(args.length < 3) return HttpStatusCodeReplies.Bad_Request("Not enough arguments; must be command,streamid,persec.");
        try {
            stream.sendPerSecond = Float.parseFloat(args[2]);
            return HttpStatusCodeReplies.No_Content;
        } catch(NumberFormatException ignored) {
            return HttpStatusCodeReplies.Bad_Request;
        }
    }
    private static String setOpmodeField(String[] args, TelemetryManager dataSource) {
        if(dataSource.opmodeFieldMonitor == null) return HttpStatusCodeReplies.Bad_Gateway;
        if(args.length < 3) return HttpStatusCodeReplies.Bad_Request;
        if(!dataSource.opmodeFieldMonitor.hasKey(args[1])) return HttpStatusCodeReplies.Not_Found;

        dataSource.opmodeFieldMonitor.parseAndSet(args[0], args[2]);

        return HttpStatusCodeReplies.No_Content;
    }
    private static String moveToAutoautoState(String[] args, TelemetryManager dataSource) {
        FeatureManager.logger.log("debug: argslength " + args.length);
        //requires at least 2 extra args
        if(args.length < 3) return HttpStatusCodeReplies.Bad_Request;
        //and the 2nd one has to be a number
        int state = -1;
        try { state = Integer.parseInt(args[2].trim()); } catch (NumberFormatException ignored) {
            return HttpStatusCodeReplies.Bad_Request;
        }

        String statepathName = args[1].trim();

        FeatureManager.logger.log("debug: statepath " + statepathName + "; state " + state);

        //check that the requested statepath exists
        String lengthOfStatepathVariableName = AutoautoSystemVariableNames.STATE_COUNT_OF_PREFIX + statepathName;
        AutoautoPrimitive lengthOfStatepath = dataSource.autoauto.globalScope.get(lengthOfStatepathVariableName);
        if(lengthOfStatepath == null) return HttpStatusCodeReplies.Not_Found;

        //if it's not null, but also not a numeric value, that's the server's fault
        if(!(lengthOfStatepath instanceof AutoautoNumericValue)) return HttpStatusCodeReplies.Bad_Gateway;

        //ensure that the client isn't asking for a nonexistent state (state >= stateArrayLength)
        if((int)((AutoautoNumericValue)lengthOfStatepath).getFloat() <= state) return HttpStatusCodeReplies.Not_Found;

        //yay the checks are done and we can do the things!
        dataSource.autoauto.globalScope.systemSet(AutoautoSystemVariableNames.STATEPATH_NAME, new AutoautoString(statepathName));
        dataSource.autoauto.globalScope.systemSet(AutoautoSystemVariableNames.STATE_NUMBER, new AutoautoNumericValue(state));

        return HttpStatusCodeReplies.No_Content;
    }
}

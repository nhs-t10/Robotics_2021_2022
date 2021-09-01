package org.firstinspires.ftc.teamcode.managers.telemetry.server;

import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.AutoautoNumericValue;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.AutoautoPrimitive;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.AutoautoString;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.AutoautoSystemVariableNames;
import org.firstinspires.ftc.teamcode.managers.telemetry.TelemetryManager;

public class CommandHandler {
    public static String handle(String[] args, TelemetryManager dataSource) {
        if(args.length == 0) return HttpStatusCodeReplies.Bad_Request;

        switch(args[0].trim()) {
            case ControlCodes.DONT_SEND_YOUR_AUTOAUTO_PROGRAMS_ENTIRE_LIFE_STORY_IT_GETS_BORING:
                dataSource.autoauto.setProgramJsonSendingFlag(0);
                return HttpStatusCodeReplies.No_Content;
            case ControlCodes.ACTUALLY_AUNT_AUTOAUTO_I_DO_WANT_TO_HEAR_YOUR_LIFE_STORY:
                dataSource.autoauto.setProgramJsonSendingFlag(1);
                return HttpStatusCodeReplies.No_Content;
            case ControlCodes.COULD_I_GET_LIKE_A_QUICK_SUMMARY_OF_THE_AUTOAUTO_PROGRAMS_LIFE_PLEASE:
                dataSource.autoauto.setProgramJsonSendingFlag(2);
                return HttpStatusCodeReplies.No_Content;
            case ControlCodes.PLEASE_SKIP_TO_THIS_AUTOAUTO_STATE:
                return moveToAutoautoState(args, dataSource);
            default:
                return HttpStatusCodeReplies.Bad_Request;
        }
    }
    private static String moveToAutoautoState(String[] args, TelemetryManager dataSource) {
        //requires at least 2 extra args
        if(args.length < 3) return HttpStatusCodeReplies.Bad_Request;
        //and the 2nd one has to be a number
        int state = -1;
        try { state = Integer.parseInt(args[2].trim()); } catch (NumberFormatException ignored) {
            return HttpStatusCodeReplies.Bad_Request;
        }

        String statepathName = args[1].trim();

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

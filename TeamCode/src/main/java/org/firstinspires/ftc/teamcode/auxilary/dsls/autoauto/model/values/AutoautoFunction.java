package org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values;

import org.firstinspires.ftc.teamcode.auxilary.PaulMath;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.Location;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.State;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.AutoautoRuntimeVariableScope;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.AutoautoSystemVariableNames;

public class AutoautoFunction extends AutoautoPrimitive implements AutoautoCallableValue {
    private State body;
    private AutoautoRuntimeVariableScope scope;
    private Location location;

    public AutoautoFunction(State body) {
        this.body = body;
    }

    @Override
    public String getString() {
        return "<anonymous function>() {" + body.toString() + "}";
    }

    @Override
    public AutoautoFunction clone() {
        AutoautoFunction c = new AutoautoFunction(body.clone());
        return c;
    }

    @Override
    public AutoautoRuntimeVariableScope getScope() {
        return scope;
    }

    @Override
    public void setScope(AutoautoRuntimeVariableScope scope) {
        this.scope = scope;
    }

    @Override
    public Location getLocation() {
        return null;
    }

    @Override
    public void setLocation(Location location) {
        this.location = location;
    }

    @Override
    public AutoautoPrimitive call(AutoautoPrimitive[] args) {
        AutoautoRuntimeVariableScope callScope = new AutoautoRuntimeVariableScope(scope.getRoot());
        AutoautoNumericValue actualStateNumber = (AutoautoNumericValue) scope.get(AutoautoSystemVariableNames.STATE_NUMBER);
        AutoautoString actualStatepath = (AutoautoString) scope.get(AutoautoSystemVariableNames.STATEPATH_NAME);

        callScope.systemSet(AutoautoSystemVariableNames.STATE_NUMBER, new AutoautoNumericValue(body.location.stateNumber));
        callScope.systemSet(AutoautoSystemVariableNames.STATEPATH_NAME, new AutoautoString(body.location.statepath));

        AutoautoTable arglist = new AutoautoTable(args);
        callScope.systemSet(AutoautoSystemVariableNames.FUNCTION_ARGUMENTS_NAME, arglist.clone());

        State callState = body.clone();
        callState.setScope(callScope);

        callState.init();
        callState.stepInit();
        callState.loop();

        callScope.systemSet(AutoautoSystemVariableNames.STATE_NUMBER, actualStateNumber);
        callScope.systemSet(AutoautoSystemVariableNames.STATEPATH_NAME, actualStatepath);

        callScope.systemRemove(AutoautoSystemVariableNames.RETURNED_VALUE);
        callScope.systemRemove(AutoautoSystemVariableNames.FUNCTION_ARGUMENTS_NAME);

        return callState.getReturnValue();
    }

    @Override
    public String getJSONString() {
        return PaulMath.JSONify("[function Function]");
    }
}

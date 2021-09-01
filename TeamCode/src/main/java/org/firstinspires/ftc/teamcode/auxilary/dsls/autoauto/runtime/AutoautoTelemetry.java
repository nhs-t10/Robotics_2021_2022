package org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime;

import org.firstinspires.ftc.teamcode.auxilary.PaulMath;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.AutoautoPrimitive;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.AutoautoString;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.AutoautoUndefined;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.VariableReference;

public class AutoautoTelemetry {
    String programJson;
    String programOutlineJson;
    VariableReference[] variables;
    public AutoautoRuntimeVariableScope globalScope;

    private int sendProgramJson = 2;

    public void setGlobalScope(AutoautoRuntimeVariableScope globalScope) {
        this.globalScope = globalScope;
    }

    public final String getProgramJson() {
        return programJson;
    }

    public final VariableReference[] getVariables() {
        return variables;
    }

    public final String getVariableJson() {

        if(variables == null) return "null";

        StringBuilder r = new StringBuilder("[");
        for(int i = 0; i < variables.length; i++) {
            r.append('"').append(variables[i].getName()).append('"');
            if(i != variables.length - 1) {
                r.append(",");
            }
        }
        r.append("]");
        return r.toString();
    }

    public void setProgramJsonSendingFlag(int t) {
        this.sendProgramJson = t;
    }

    public final void setProgramJson(String json) {
        this.programJson = json;
    }
    public final void setProgramOutlineJson(String json) {
        this.programOutlineJson = json;
    }

    public final void setVariables(VariableReference[] variables) {
        this.variables = variables;
    }

    public String getVariableValueJson() {
        if(variables == null) return "null";


        StringBuilder r = new StringBuilder("{");
        for(int i = 0; i < variables.length; i++) {
            String name = variables[i].getName();
            r.append(PaulMath.JSONify(name));
            AutoautoPrimitive val = globalScope.get(name);
            if(val == null || val instanceof AutoautoUndefined) r.append("null");
            else r.append(PaulMath.JSONify(val.getString()));


            if(i != variables.length - 1) {
                r.append(",");
            }
        }
        r.append("}");
        return r.toString();
    }
}

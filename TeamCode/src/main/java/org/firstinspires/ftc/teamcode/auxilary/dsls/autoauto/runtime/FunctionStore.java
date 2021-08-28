package org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime;

import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.FunctionCall;

import java.util.HashMap;

public class FunctionStore {
    public HashMap<String, NativeRobotFunction> variables;

    public FunctionStore() {
        this.variables = new HashMap<String, NativeRobotFunction>();
    }

    public void put(String n, int a, NativeRobotFunction f) {
        this.variables.put(n + "(" + a + ")", f);
    }
    public void put(FunctionCall c, NativeRobotFunction f) {
        String s = c.name + "(" + c.args.length + ")";
        this.variables.put(s, f);
    }

    public NativeRobotFunction get(String name, int argLength) {
        String s = name + "(" + argLength + ")";

        if(this.variables.containsKey(s)) return this.variables.get(s);
        else return null;
    }

    public NativeRobotFunction get(FunctionCall c) {
        String s = c.name + "(" + c.args.length + ")";

        if(this.variables.containsKey(s)) return this.variables.get(s);
        else return null;
    }
}

package org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.statements;

import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.Location;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.State;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.AutoautoBooleanValue;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.AutoautoValue;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.AutoautoRuntimeVariableScope;

public class IfStatement extends Statement {
    AutoautoValue check;
    State subject;
    private AutoautoRuntimeVariableScope scope;
    private Location location;

    public static IfStatement I (AutoautoValue v, Statement s) {
        return new IfStatement(v, s);
    }
    public static IfStatement I (AutoautoValue v, State s) {
        return new IfStatement(v, s);
    }

    public IfStatement(AutoautoValue v, State s) {
        check = v;
        subject = s;
    }

    public IfStatement(AutoautoValue v, Statement s) {
        this(v, new State(new Statement[] {s}));
    }

    public void init() {
        check.init();
        subject.init();
    }
    public void stepInit() {
        subject.stepInit();
    }

    @Override
    public IfStatement clone() {
        IfStatement c = new IfStatement(check.clone(), subject.clone());
        c.setLocation(location);
        return c;
    }

    public void loop() {
        check.loop();
        if(AutoautoBooleanValue.isTruthy(check.getResolvedValue())) {
            subject.loop();
        }
    }
    public String toString() {
        return "if (" + check.toString() + ") " + subject.toString();
    }

    @Override
    public AutoautoRuntimeVariableScope getScope() {
        return scope;
    }

    @Override
    public void setScope(AutoautoRuntimeVariableScope scope) {
        this.scope = scope;
        check.setScope(scope);
        subject.setScope(scope);
    }

    @Override
    public Location getLocation() {
        return location;
    }

    @Override
    public void setLocation(Location location) {
        this.location = location;
        if(this.subject.location == null) this.subject.setLocation(location.clone());
    }
}

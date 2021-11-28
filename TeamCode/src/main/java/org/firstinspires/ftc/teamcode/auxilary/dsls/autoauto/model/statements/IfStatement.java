package org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.statements;

import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.Location;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.State;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.BooleanOperator;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.AutoautoBooleanValue;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.AutoautoValue;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.AutoautoRuntimeVariableScope;

public class IfStatement extends Statement {
    BooleanOperator conditional;
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
        conditional = new BooleanOperator(v, new AutoautoBooleanValue(false), "!=");
        subject = s;
    }

    public IfStatement(AutoautoValue v, Statement s) {
        this(v, new State(new Statement[] {s}));
    }

    public void init() {
        conditional.init();
        subject.init();
    }
    public void stepInit() {
        subject.stepInit();
    }

    @Override
    public IfStatement clone() {
        IfStatement c = new IfStatement(conditional.clone(), subject.clone());
        c.setLocation(location);
        return c;
    }

    public void loop() {
        conditional.loop();
        if(conditional.getBoolean() == true) {
            subject.loop();
        }
    }
    public String toString() {
        return "if (" + conditional.toString() + ") " + subject.toString();
    }

    @Override
    public AutoautoRuntimeVariableScope getScope() {
        return scope;
    }

    @Override
    public void setScope(AutoautoRuntimeVariableScope scope) {
        this.scope = scope;
        conditional.setScope(scope);
        subject.setScope(scope);
    }

    @Override
    public Location getLocation() {
        return location;
    }

    @Override
    public void setLocation(Location location) {
        this.location = location;
    }
}

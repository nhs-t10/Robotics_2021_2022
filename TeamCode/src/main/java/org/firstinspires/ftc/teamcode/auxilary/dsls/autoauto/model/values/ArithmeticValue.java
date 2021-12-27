package org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values;

import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.AutoautoProgram;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.Location;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.operators.AutoautoOperator;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.errors.AutoautoNameException;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.AutoautoRuntimeVariableScope;
import org.jetbrains.annotations.NotNull;

public class ArithmeticValue extends AutoautoValue {
    AutoautoOperator operator;
    AutoautoValue right;
    AutoautoValue left;

    AutoautoPrimitive returnValue;

    private AutoautoRuntimeVariableScope scope;
    private Location location;

    public static ArithmeticValue O(AutoautoValue left, String operator, AutoautoValue right) {
        return new ArithmeticValue(left, operator, right);
    }

    public ArithmeticValue(AutoautoValue left, String operator, AutoautoValue right) {
        this.left = left;
        this.operator = AutoautoOperator.get(operator);
        this.right = right;
    }
    @Override
    public void init() {
        left.init();
        right.init();
    }

    public void loop() {
        left.loop();
        right.loop();

        AutoautoPrimitive leftRes = left.getResolvedValue();
        AutoautoPrimitive rightRes = right.getResolvedValue();

        this.returnValue = operator.eval(leftRes, rightRes, true);
    }

    @NotNull
    @Override
    public String getString() {
        return returnValue.getString();
    }

    @Override
    public ArithmeticValue clone() {
        ArithmeticValue c = new ArithmeticValue(left.clone(), operator.getOperatorStr(), right.clone());
        c.setLocation(location);
        return c;
    }

    @NotNull
    public String toString() {
        return left + " " + operator + " " + right;
    }

    @NotNull
    @Override
    public AutoautoPrimitive getResolvedValue() {
        return this.returnValue;
    }

    @Override
    public AutoautoRuntimeVariableScope getScope() {
        return scope;
    }

    @Override
    public void setScope(AutoautoRuntimeVariableScope scope) {
        this.scope = scope;
        left.setScope(scope);
        operator.setScope(scope);
        right.setScope(scope);
    }

    @Override
    public Location getLocation() {
        return location;
    }

    @Override
    public void setLocation(Location location) {
        this.location = location;
        operator.setLocation(location);
    }
}

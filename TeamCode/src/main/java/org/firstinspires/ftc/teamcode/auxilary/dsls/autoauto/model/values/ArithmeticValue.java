package org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values;

import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.Location;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.operators.HasAutoautoDivideOperator;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.operators.HasAutoautoEqualsOperator;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.operators.HasAutoautoExpOperator;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.operators.HasAutoautoGreaterThanOperator;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.operators.HasAutoautoGrequalsOperator;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.operators.HasAutoautoLequalsOperator;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.operators.HasAutoautoLessThanOperator;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.operators.HasAutoautoMinusOperator;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.operators.HasAutoautoModuloOperator;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.operators.HasAutoautoNequalsOperator;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.operators.HasAutoautoOperatorInterface;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.operators.HasAutoautoPlusOperator;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.operators.HasAutoautoTimesOperator;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.primitives.AutoautoBooleanValue;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.primitives.AutoautoNumericValue;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.primitives.AutoautoPrimitive;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.primitives.AutoautoString;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.primitives.AutoautoTable;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.primitives.AutoautoUndefined;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.AutoautoRuntimeVariableScope;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.AutoautoSystemVariableNames;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.errors.AutoautoArgumentException;
import org.jetbrains.annotations.NotNull;

public class ArithmeticValue extends AutoautoValue {
    private final Class<? extends HasAutoautoOperatorInterface> operatorClass;
    String operator;
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
        this.operator = operator;
        this.operatorClass = getOperatorClass(operator);
        this.right = right;
    }

    private Class<? extends HasAutoautoOperatorInterface> getOperatorClass(String operator) {
        switch(operator) {
            case "+": return HasAutoautoPlusOperator.class;
            case "-": return HasAutoautoMinusOperator.class;
            case "*": return HasAutoautoTimesOperator.class;
            case "%": return HasAutoautoModuloOperator.class;
            case "/": return HasAutoautoDivideOperator.class;
            case "^":
            case "**": return HasAutoautoExpOperator.class;
            case "<": return HasAutoautoLessThanOperator.class;
            case "<=": return HasAutoautoLequalsOperator.class;
            case "==": return HasAutoautoEqualsOperator.class;
            case "!=": return HasAutoautoNequalsOperator.class;
            case ">=": return HasAutoautoGrequalsOperator.class;
            case ">": return HasAutoautoGreaterThanOperator.class;
        }
        throw new AutoautoArgumentException("No operator " + operator);
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

        leftRes = replaceNanWithUndefined(leftRes);
        rightRes = replaceNanWithUndefined(rightRes);

        //Choose the widest datatype; if there's a tie, choose left, since we read left-to-right.
        boolean chooseLeft = leftRes.dataWidth() >= rightRes.dataWidth();
        AutoautoPrimitive chosen = chooseLeft ? leftRes : rightRes;

        //let people define their own overloads!
        if(chosen instanceof AutoautoTable &&
                chosen.hasProperty(new AutoautoString(AutoautoSystemVariableNames.OPERATOR_OVERLOADING_PREFIX + this.operator))) {
            AutoautoPrimitive func = ((AutoautoTable) chosen).getProperty(AutoautoSystemVariableNames.OPERATOR_OVERLOADING_PREFIX + this.operator);

            AutoautoPrimitive nonChosen = chooseLeft ? rightRes : leftRes;

            if(func instanceof AutoautoCallableValue) {
                this.returnValue = ((AutoautoCallableValue) func).call(chosen,
                        new AutoautoPrimitive[]{
                                nonChosen, new AutoautoBooleanValue(!chooseLeft)
                        });
            } else {
                this.returnValue = func;
            }
        } else if(chooseLeft && operatorClass.isInstance(rightRes)) {
            this.returnValue = dispatchOperator(operator, leftRes, rightRes, false);
        } else if(operatorClass.isInstance(leftRes)) {
            this.returnValue = dispatchOperator(operator, rightRes, leftRes, true);
        } //if neither one offers this operator, try casting it to a string? and then redo with that?
        else if(operatorClass.isAssignableFrom(AutoautoString.class)) {
            this.returnValue = dispatchOperator(operator, rightRes.castToString(), leftRes.castToString(), !chooseLeft);
        } //if it's *still* nothing, maybe number could do it?
        else if(operatorClass.isAssignableFrom(AutoautoNumericValue.class)) {
            this.returnValue = dispatchOperator(operator, rightRes.castToNumber(), leftRes.castToNumber(), !chooseLeft);
        } //give up and do `undefined`
        else {
            this.returnValue = new AutoautoUndefined();
        }
    }

    private AutoautoPrimitive replaceNanWithUndefined(AutoautoPrimitive value) {
        if(value instanceof AutoautoNumericValue) {
            if(Float.isNaN(((AutoautoNumericValue) value).value)) return new AutoautoUndefined();
        }
        //if we haven't returned undefined,
        return value;
    }

    private AutoautoPrimitive dispatchOperator(String operator,
                                  AutoautoPrimitive evaluateOn, AutoautoPrimitive other, boolean otherIsLeft) {
        switch(operator) {
            case "+": return ((HasAutoautoPlusOperator)evaluateOn).opPlus(other, otherIsLeft);
            case "-": return ((HasAutoautoMinusOperator)evaluateOn).opMinus(other, otherIsLeft);
            case "*": return ((HasAutoautoTimesOperator)evaluateOn).opTimes(other, otherIsLeft);
            case "%": return ((HasAutoautoModuloOperator)evaluateOn).opModulo(other, otherIsLeft);
            case "/": return ((HasAutoautoDivideOperator)evaluateOn).opDivide(other, otherIsLeft);
            case "^":
            case "**": return ((HasAutoautoExpOperator)evaluateOn).opExp(other, otherIsLeft);
            case "<": return ((HasAutoautoLessThanOperator)evaluateOn).opLessThan(other, otherIsLeft);
            case "<=": return ((HasAutoautoLequalsOperator)evaluateOn).opLequals(other, otherIsLeft);
            case "==": return ((HasAutoautoEqualsOperator)evaluateOn).opEquals(other, otherIsLeft);
            case "!=": return ((HasAutoautoNequalsOperator)evaluateOn).opNequals(other, otherIsLeft);
            case ">=": return ((HasAutoautoGrequalsOperator)evaluateOn).opGrequals(other, otherIsLeft);
            case ">": return ((HasAutoautoGreaterThanOperator)evaluateOn).opGreaterThan(other, otherIsLeft);
        }
        throw new AutoautoArgumentException("No operator " + operator);
    }

    @NotNull
    @Override
    public String getString() {
        return returnValue.getString();
    }

    @Override
    public ArithmeticValue clone() {
        ArithmeticValue c = new ArithmeticValue(left.clone(), operator, right.clone());
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
        right.setScope(scope);
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

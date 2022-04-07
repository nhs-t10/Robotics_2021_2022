package org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.bytecode;

import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.programtypes.BytecodeEvaluationProgram;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.AutoautoCallableValue;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.primitives.AutoautoNumericValue;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.primitives.AutoautoPrimitive;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.primitives.AutoautoUnitValue;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.AutoautoRuntimeVariableScope;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.AutoautoSystemVariableNames;

import java.util.Stack;

public class unit_currentv_Bytecode extends AutoautoBytecode {

    private boolean inited;
    private AutoautoCallableValue getCmFunction;
    private AutoautoCallableValue getDegsFunction;

    @Override
    public void invoke(BytecodeEvaluationProgram bytecodeEvaluationProgram, AutoautoRuntimeVariableScope scope, Stack<AutoautoPrimitive> stack, Stack<Integer> callStack) {

        if(!inited) init(scope);

        AutoautoPrimitive p = stack.pop();
        if(p instanceof AutoautoUnitValue) {
            AutoautoUnitValue uv = (AutoautoUnitValue)p;
            switch(uv.unit) {
                case "ms": stack.push(new AutoautoNumericValue(System.currentTimeMillis()));
                case "cm": stack.push(getCmFunction.call(uv, AutoautoPrimitive.EMPTY_ARRAY));
                case "degs": stack.push(getDegsFunction.call(uv, AutoautoPrimitive.EMPTY_ARRAY));
                default: stack.push(new AutoautoNumericValue(0));
            }
        } else {
            stack.push(new AutoautoNumericValue(0));
        }
    }

    private void init(AutoautoRuntimeVariableScope scope) {
        getCmFunction = (AutoautoCallableValue) scope.get(AutoautoSystemVariableNames.GET_CENTIMETERS_FUNCTION_NAME);
        getDegsFunction = (AutoautoCallableValue) scope.get(AutoautoSystemVariableNames.GET_DEGREES_FUNCTION_NAME);

        inited = true;
    }
}

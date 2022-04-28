package org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.bytecode;

import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.operators.AutoautoOperation;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.operators.HasAutoautoLequalsOperator;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.operators.HasAutoautoOperatorInterface;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.programtypes.BytecodeEvaluationProgram;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.primitives.AutoautoPrimitive;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.AutoautoRuntimeVariableScope;

import java.util.Stack;

public class cmp_lte_Bytecode extends AutoautoBytecode {
    private static final Class<? extends HasAutoautoOperatorInterface> operatorClass = HasAutoautoLequalsOperator.class;

    @Override
    public void invoke(BytecodeEvaluationProgram bytecodeEvaluationProgram, AutoautoRuntimeVariableScope scope, Stack<AutoautoPrimitive> stack, Stack<Integer> callStack) {

        AutoautoPrimitive right = stack.pop();
        AutoautoPrimitive left = stack.pop();

        stack.push(AutoautoOperation.invokeOperation(left, right, operatorClass, "<="));
    }
}

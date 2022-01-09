const jvmBytecodeInstructions = require("./jvm-bytecode-instructions");

var aaInstructions = require("./aa-instructions");


var makeConstantPool = require("./jvm/constant-pool");

var constantPool;
var finalizationFunctions = [];
var thisClass;

/**
 * 
 * @param {bcodeline[]} bytecode 
 */
module.exports = function(bytecode, tClass) {
    constantPool = makeConstantPool();
    finalizationFunctions = [];
    thisClass = tClass;

    if(bytecode[bytecode.length - 1][0] != aaInstructions.NOOP) bytecode.push([aaInstructions.NOOP]);

    var jvmBytecode = bytecode.map(x=>aaBytecodeToJvmBytecode(x));

    finalizationFunctions.forEach(x=>x(jvmBytecode));

    return {
        bytecode: jvmBytecode,
        constantPool: constantPool
    }
}

var PROPERTY_BEARING_OBJECT_CLASSNAME = "org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.AutoautoPropertyBearingObject";
var AUTOAUTO_PRIMITIVE_CLASSNAME = "org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.AutoautoPrimitive";
var CALLABLE_VALUE_CLASSNAME = "org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.AutoautoCallableValue";
var AUTOAUTO_OPERATOR_CLASSNAME = "org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.operators.AutoautoOperator";



/**
 * 
 * @param {bcodeline} bytecode 
 */
function aaBytecodeToJvmBytecode(bytecode) {
    switch(bytecode[0]) {
        case aaInstructions.PUSH_CONST: 
            var primClass = (typeof bytecode[1] === "string") ? "org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.AutoautoString"
                : "org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.AutoautoNumericValue";
            var argClass = (typeof bytecode[1] === "string") ? "java.lang.String" : "float";

            var t = indexBytes(constantPool.addClass(primClass));
            var method = indexBytes(constantPool.addMethod(primClass, "<init>", "void", [argClass]));

            var arg = indexBytes(constantPool.addConstant(bytecode[1]));

            return [ jvmBytecodeInstructions.new(t[0], t[1]),
                    jvmBytecodeInstructions.dup(),
                    jvmBytecodeInstructions.ldc_w(arg[0], arg[1]),
                    jvmBytecodeInstructions.invokespecial(method[0], method[1]),
                    jvmBytecodeInstructions.pop()
                 ]
        case aaInstructions.DUP:
            return jvmBytecodeInstructions.dup();
        case aaInstructions.POP:
            return jvmBytecodeInstructions.pop();
        case aaInstructions.NOOP:
            return jvmBytecodeInstructions.nop();
        case aaInstructions.PUSH_EMPTY_TABLE_REF:
            var primClass = "org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.AutoautoTable";

            var t = indexBytes(constantPool.addClass(primClass));
            var method = indexBytes(constantPool.addMethod(primClass, "<init>", "void", []));

            return [ jvmBytecodeInstructions.new(t[0], t[1]),
                jvmBytecodeInstructions.dup(),
                jvmBytecodeInstructions.invokespecial(method[0], method[1]),
                jvmBytecodeInstructions.pop() //pop the void
            ]
        case aaInstructions.GET_VAR_STATIC:
            var method = indexBytes(constantPool.addMethod("org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.AutoautoRuntimeVariableScope",
                    "get", AUTOAUTO_PRIMITIVE_CLASSNAME, [AUTOAUTO_PRIMITIVE_CLASSNAME]));
            return makeVarscopeGetter().concat([
                aaBytecodeToJvmBytecode([aaInstructions.PUSH_CONST, bytecode[1]]),
                jvmBytecodeInstructions.invokevirtual(method[0], method[1])
            ])
        case aaInstructions.SET_VAR_STATIC:
            var method = indexBytes(constantPool.addMethod("org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.AutoautoRuntimeVariableScope",
                    "put", "void", [AUTOAUTO_PRIMITIVE_CLASSNAME, AUTOAUTO_PRIMITIVE_CLASSNAME]));
            return makeVarscopeGetter().concat([
                aaBytecodeToJvmBytecode([aaInstructions.PUSH_CONST, bytecode[1]]),
                jvmBytecodeInstructions.invokevirtual(method[0], method[1]),
                jvmBytecodeInstructions.pop() //pop the void
            ])
        case aaInstructions.GET_PROP:
            var method = indexBytes(constantPool.addMethod(PROPERTY_BEARING_OBJECT_CLASSNAME,
                    "getProperty", AUTOAUTO_PRIMITIVE_CLASSNAME, [AUTOAUTO_PRIMITIVE_CLASSNAME]));
            return [
                jvmBytecodeInstructions.invokeinterface(method[0], method[1], 1, 0)
            ]
        case aaInstructions.SET_PROP:
            var method = indexBytes(constantPool.addMethod(PROPERTY_BEARING_OBJECT_CLASSNAME,
                    "setProperty", "void", [AUTOAUTO_PRIMITIVE_CLASSNAME, AUTOAUTO_PRIMITIVE_CLASSNAME]));
            return [
                jvmBytecodeInstructions.invokeinterface(method[0], method[1], 2, 0),
                jvmBytecodeInstructions.pop() //pop the void
            ]
        case aaInstructions.CALL_FUNC:
            var argCount = bytecode[1];
            var componentClass = indexBytes(constantPool.addClass(AUTOAUTO_PRIMITIVE_CLASSNAME));
            var method = indexBytes(constantPool.addMethod(CALLABLE_VALUE_CLASSNAME,
                "call", AUTOAUTO_PRIMITIVE_CLASSNAME, [AUTOAUTO_PRIMITIVE_CLASSNAME + "[]"]));

                //make new array reference!
            var bcRes = [
                jvmBytecodeInstructions.bipush(argCount),
                jvmBytecodeInstructions.anewarray(componentClass[0], componentClass[1])
            ];
            //put each argument into the array. This is fiddly because the actual value is under the array reference.
            //stack before: value, array
            //stack after: array
            for(var i = argCount - 1; i >= 0; i--) {
                bcRes.push(jvmBytecodeInstructions.dup_x1()); //array, value, array
                bcRes.push(jvmBytecodeInstructions.swap()); //array, array, value
                bcRes.push(jvmBytecodeInstructions.bipush(i)); //array, array, value, index
                bcRes.push(jvmBytecodeInstructions.swap()); //array, array, index, value
                bcRes.push(jvmBytecodeInstructions.aastore()) //array
            }
            bcRes.push(jvmBytecodeInstructions.invokeinterface(method[0], method[1], 1, 0));

            return bcRes;
        case aaInstructions.UPDATE_LOOP_START_STATE_STATIC:
            var field = indexBytes(constantPool.addField(thisClass, "s", "int"));
            var v = bytecode[1];
            return [
                jvmBytecodeInstructions.aload_0(), //push `this`
                jvmBytecodeInstructions.sipush((v >>> 8) & 0xff, v & 0xff), //push value
                jvmBytecodeInstructions.putfield(field[0], field[1])
            ];
        case aaInstructions.ENDLOOP:
            var eL = [
                jvmBytecodeInstructions.goto(null, null)
            ];
            finalizationFunctions.push(updateGotoInstruction(Infinity, eL[0], eL));
            return eL;
        case aaInstructions.ADD:
            var evalMethod = indexBytes(constantPool.addMethod(AUTOAUTO_OPERATOR_CLASSNAME,
                "eval", AUTOAUTO_OPERATOR_CLASSNAME, [AUTOAUTO_PRIMITIVE_CLASSNAME, AUTOAUTO_PRIMITIVE_CLASSNAME, "boolean"]));

            return makeOperatorGetter("+").concat([
                jvmBytecodeInstructions.dup_x2(),
                jvmBytecodeInstructions.pop(),
                jvmBytecodeInstructions.invokevirtual(evalMethod[0], evalMethod[1])
            ]);
        case aaInstructions.MULTIPLY:
            var evalMethod = indexBytes(constantPool.addMethod(AUTOAUTO_OPERATOR_CLASSNAME,
                "eval", AUTOAUTO_OPERATOR_CLASSNAME, [AUTOAUTO_PRIMITIVE_CLASSNAME, AUTOAUTO_PRIMITIVE_CLASSNAME, "boolean"]));

            return makeOperatorGetter("*").concat([
                jvmBytecodeInstructions.dup_x2(),
                jvmBytecodeInstructions.pop(),
                jvmBytecodeInstructions.invokevirtual(evalMethod[0], evalMethod[1])
            ]);
        case aaInstructions.GREATER_THAN_EQUALS:
            var evalMethod = indexBytes(constantPool.addMethod(AUTOAUTO_OPERATOR_CLASSNAME,
                "eval", AUTOAUTO_OPERATOR_CLASSNAME, [AUTOAUTO_PRIMITIVE_CLASSNAME, AUTOAUTO_PRIMITIVE_CLASSNAME, "boolean"]));

            return makeOperatorGetter(">=").concat([
                jvmBytecodeInstructions.dup_x2(),
                jvmBytecodeInstructions.pop(),
                jvmBytecodeInstructions.invokevirtual(evalMethod[0], evalMethod[1])
            ]);
        case aaInstructions.SUBTRACT:
            var evalMethod = indexBytes(constantPool.addMethod(AUTOAUTO_OPERATOR_CLASSNAME,
                "eval", AUTOAUTO_OPERATOR_CLASSNAME, [AUTOAUTO_PRIMITIVE_CLASSNAME, AUTOAUTO_PRIMITIVE_CLASSNAME, "boolean"]));

            return makeOperatorGetter("-").concat([
                jvmBytecodeInstructions.dup_x2(),
                jvmBytecodeInstructions.pop(),
                jvmBytecodeInstructions.invokevirtual(evalMethod[0], evalMethod[1])
            ]);
        case aaInstructions.EQUAL_TO:
            var evalMethod = indexBytes(constantPool.addMethod(AUTOAUTO_OPERATOR_CLASSNAME,
                "eval", AUTOAUTO_OPERATOR_CLASSNAME, [AUTOAUTO_PRIMITIVE_CLASSNAME, AUTOAUTO_PRIMITIVE_CLASSNAME, "boolean"]));

            return makeOperatorGetter("==").concat([
                jvmBytecodeInstructions.dup_x2(),
                jvmBytecodeInstructions.pop(),
                jvmBytecodeInstructions.invokevirtual(evalMethod[0], evalMethod[1])
            ]);
        case aaInstructions.IF_NONZERO_JUMP_STATIC:
            var isTruthyMethod = indexBytes(constantPool.addMethod("org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.AutoautoBooleanValue",
                "isTruthy", "boolean", [AUTOAUTO_PRIMITIVE_CLASSNAME]));

            var eL = [
                jvmBytecodeInstructions.invokestatic(isTruthyMethod[0], isTruthyMethod[1]),
                jvmBytecodeInstructions.ifne(null, null)
            ];
            finalizationFunctions.push(updateGotoInstruction(bytecode[1], eL[1], eL));
            return eL;
        default: 
            throw "can't convert " + Object.entries(aaInstructions).find(x=>x[1]==bytecode[0])[0] + " to Java!";
    }
}

function updateGotoInstruction(tIndex, jvmGotoInstr, jvmInstructionParent) {
    return function(jvmBytecode) {
        var selfIndex = jvmBytecode.indexOf(jvmInstructionParent || jvmGotoInstr);
        var selfByteIndex = jvmBytecode.slice(0, selfIndex).flat(Infinity).length;

        var targetIndex = Math.max(0, Math.min(tIndex, jvmBytecode.length - 1));
        var targetByteIndex = jvmBytecode.slice(0, targetIndex).flat(Infinity).length;

        var byteOffset = targetByteIndex - selfByteIndex;

        if(byteOffset >= Math.pow(2, 15)) throw "bad offset-- get someone to implement goto_w";

        var buf = new ArrayBuffer(2);
        var view = new DataView(buf);
        view.setInt16(0, byteOffset, false);
        

        jvmGotoInstr[1] = view.getUint8(0);
        jvmGotoInstr[2] = view.getUint8(1);
    }
}

function makeOperatorGetter(op) {

    var opStr = indexBytes(constantPool.addConstant(op));
    var method = indexBytes(constantPool.addMethod(AUTOAUTO_OPERATOR_CLASSNAME,
        "get", AUTOAUTO_OPERATOR_CLASSNAME, ["java.lang.String"]));


    return [
        jvmBytecodeInstructions.ldc_w(opStr[0], opStr[1]),
        jvmBytecodeInstructions.invokestatic(method[0], method[1])
    ];
}

function makeVarscopeGetter() {
    return [
        jvmBytecodeInstructions.aload_1()
    ]
}

function indexBytes (i) {
    i.toString();
    return [(i >>> 8) & 0b1111_1111, i & 0b1111_1111];
}

/**
 * @typedef {number[]|string[]} bcodeline
 */
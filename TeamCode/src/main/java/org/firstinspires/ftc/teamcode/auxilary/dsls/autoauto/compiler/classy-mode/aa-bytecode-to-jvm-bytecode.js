const jvmBytecodeInstructions = require("./jvm-bytecode-instructions");

var aaInstructions = require("./aa-instructions");


var constantPool = [];
var finalizationFunctions = [];
var thisClass;

/**
 * 
 * @param {bcodeline[]} bytecode 
 */
module.exports = function(bytecode, tClass) {
    constantPool = [];
    finalizationFunctions = [];
    thisClass = tClass;

    if(bytecode[bytecode.length - 1][0] != aaInstructions.NOOP) bytecode.push([aaInstructions.NOOP]);

    var jvmBytecode = bytecode.map(x=>aaBytecodeToJvmBytecode(x));
    console.log(jvmBytecode);

    finalizationFunctions.forEach(x=>x(jvmBytecode));

    return {
        bytecode: jvmBytecode,
        constantPool: constantPool
    }
}

var PROPERTY_BEARING_OBJECT_CLASSNAME = "org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.AutoautoPropertyBearingObject";
var AUTOAUTO_PRIMITIVE_CLASSNAME = "org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.AutoautoPrimitive";
var CALLABLE_VALUE_CLASSNAME = "org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.AutoautoCallableValue";

var structures = {
    CONSTANT_Utf8: 1,
    CONSTANT_Float: 4,
    CONSTANT_Class: 7,
    CONSTANT_String: 8,
    CONSTANT_Fieldref: 9,
    CONSTANT_Methodref: 10,
    CONSTANT_NameAndType: 12,
};

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

            var t = indexBytes(addClass(primClass));
            var method = indexBytes(addMethod(primClass, "<init>", "void", [argClass]));

            var arg = indexBytes(addConstant(bytecode[1]));

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

            var t = indexBytes(addClass(primClass));
            var method = indexBytes(addMethod(primClass, "<init>", "void", []));

            return [ jvmBytecodeInstructions.new(t[0], t[1]),
                jvmBytecodeInstructions.dup(),
                jvmBytecodeInstructions.invokespecial(method[0], method[1]),
                jvmBytecodeInstructions.pop() //pop the void
            ]
        case aaInstructions.GET_VAR_STATIC:
            var method = indexBytes(addMethod("org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.AutoautoRuntimeVariableScope",
                    "get", AUTOAUTO_PRIMITIVE_CLASSNAME, [AUTOAUTO_PRIMITIVE_CLASSNAME]));
            return makeVarscopeGetter().concat([
                aaBytecodeToJvmBytecode([aaInstructions.PUSH_CONST, bytecode[1]]),
                jvmBytecodeInstructions.invokevirtual(method[0], method[1])
            ])
        case aaInstructions.SET_VAR_STATIC:
            var method = indexBytes(addMethod("org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.AutoautoRuntimeVariableScope",
                    "put", "void", [AUTOAUTO_PRIMITIVE_CLASSNAME, AUTOAUTO_PRIMITIVE_CLASSNAME]));
            return makeVarscopeGetter().concat([
                aaBytecodeToJvmBytecode([aaInstructions.PUSH_CONST, bytecode[1]]),
                jvmBytecodeInstructions.invokevirtual(method[0], method[1]),
                jvmBytecodeInstructions.pop() //pop the void
            ])
        case aaInstructions.GET_PROP:
            var method = indexBytes(addMethod(PROPERTY_BEARING_OBJECT_CLASSNAME,
                    "getProperty", AUTOAUTO_PRIMITIVE_CLASSNAME, [AUTOAUTO_PRIMITIVE_CLASSNAME]));
            return [
                jvmBytecodeInstructions.invokeinterface(method[0], method[1], 1, 0)
            ]
        case aaInstructions.SET_PROP:
            var method = indexBytes(addMethod(PROPERTY_BEARING_OBJECT_CLASSNAME,
                    "setProperty", "void", [AUTOAUTO_PRIMITIVE_CLASSNAME, AUTOAUTO_PRIMITIVE_CLASSNAME]));
            return [
                jvmBytecodeInstructions.invokeinterface(method[0], method[1], 2, 0),
                jvmBytecodeInstructions.pop() //pop the void
            ]
        case aaInstructions.CALL_FUNC:
            var argCount = bytecode[1];
            var componentClass = indexBytes(addClass(AUTOAUTO_PRIMITIVE_CLASSNAME));
            var method = indexBytes(addMethod(CALLABLE_VALUE_CLASSNAME,
                "call", AUTOAUTO_PRIMITIVE_CLASSNAME, [AUTOAUTO_PRIMITIVE_CLASSNAME + "[]"]));

                //make new array reference!
            var bcRes = [
                jvmBytecodeInstructions.bipush(argCount),
                jvmBytecodeInstructions.anewarray(componentClass[0], componentClass[1])
            ];
            //put each argument into the array. This is fiddly because the actual value is under the array reference.
            //stack before: value, array
            //stack after: array
            for(var i = argCount - 1; i >= 0; i++) {
                bcRes.push(jvmBytecodeInstructions.dup_x1()); //array, value, array
                bcRes.push(jvmBytecodeInstructions.swap()); //array, array, value
                bcRes.push(jvmBytecodeInstructions.bipush(i)); //array, array, value, index
                bcRes.push(jvmBytecodeInstructions.swap()); //array, array, index, value
                bcRes.push(jvmBytecodeInstructions.aastore()) //array
            }
            bcRes.push(jvmBytecodeInstructions.invokeinterface(method[0], method[1], 1, 0));

            return bcRes;
        case aaInstructions.UPDATE_LOOP_START_STATE_STATIC:
            var field = indexBytes(addField(thisClass, "s", "int"));
            var v = bytecode[1];
            return [
                jvmBytecodeInstructions.aload_0, //push `this`
                jvmBytecodeInstructions.sipush((v >>> 8) & 0xff, v & 0xff), //push value
                jvmBytecodeInstructions.putfield(field[0], field[1])
            ];
        case aaInstructions.ENDLOOP:
            var eL = [
                jvmBytecodeInstructions.goto(null, null)
            ];
            finalizationFunctions.push(function(jvmBytecode) {
                var selfIndex = jvmBytecode.indexOf(eL);
                var selfByteIndex = jvmBytecode.slice(0, selfIndex).flat().length;
                var byteOffset = (jvmBytecode.flat().length - 1) - selfByteIndex;

                if(byteOffset >= Math.pow(2, 15)) throw "bad offset-- get someone to implement goto_w";

                var buf = new ArrayBuffer(2);
                (new Int16Array(buf))[0] = byteOffset;
                var bytes = (new Uint16Array(buf))[0];

                eL[0][1] = (bytes >>> 8) & 0xff;
                eL[0][2] = bytes & 0xff;
            });
            return eL;
        default: 
            throw "can't convert " + Object.entries(aaInstructions).find(x=>x[1]==bytecode[0])[0] + " to Java!";
    }
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

function addClass(classname) {
    var internalName = getInternalName(classname);
    
    var nameIndex = addUTF8(internalName);

    var c = constantPool.findIndex(x=>x.tag == structures.CONSTANT_Class && x.name_index == nameIndex);
    
    if(c > -1) return c;

    constantPool.push({tag: structures.CONSTANT_Class, name_index: nameIndex });
    return constantPool.length - 1;
}

function getInternalName(classname) {
    var arrDepth = 0;
    while(classname.endsWith("[]")) {
        arrDepth++;
        classname = classname.slice(0, -2);
    }
    var javaPrimitives = {
        "byte": "B",
        "char": "C",
        "double": "D",
        "float": "F",
        "double": "D",
        "int": "I",
        "long": "J",
        "short": "S",
        "boolean": "Z",
        "void": "V"
    };
    return "[".repeat(arrDepth) + (javaPrimitives[classname] || "L" + classname.replace(/\./g, "/") + ";");
}

function addField(classname, fieldname, fieldtype) {
    var classIndex = addClass(classname);
    var nameAndTypeIndex = addNameType(fieldname, fieldtype);

    var c = constantPool.findIndex(x=>x.tag == structures.CONSTANT_Fieldref && x.class_index == classIndex && x.name_and_type_index == nameAndTypeIndex);
    if(c > -1) return c;

    var fieldref = {
        tag: structures.CONSTANT_Fieldref,
        class_index: classIndex,
        name_and_type_index: nameAndTypeIndex
    }
    constantPool.push(fieldref);
    return constantPool.length - 1;
}

function addMethod(classname, methodname, returntype, argtypes) {
    var classIndex = addClass(classname);
    var nameAndTypeIndex = addNameType(methodname, returntype, argtypes);

    var c = constantPool.findIndex(x=>x.tag == structures.CONSTANT_Methodref && x.class_index == classIndex && x.name_and_type_index == nameAndTypeIndex);
    if(c > -1) return c;

    var methodref = {
        tag: structures.CONSTANT_Methodref,
        class_index: classIndex,
        name_and_type_index: nameAndTypeIndex
    }
    constantPool.push(methodref);
    return constantPool.length - 1;
}

function addNameType(name, type, args) {
    var nameIndex = addUTF8(name);
    var descriptorIndex = makeDescriptor(type, args);

    var c = constantPool.findIndex(x=>x.tag == structures.CONSTANT_NameAndType && x.name_index == nameIndex && x.descriptor_index == descriptorIndex);
    if(c > -1) return c;

    constantPool.push({
        tag: structures.CONSTANT_NameAndType,
        name_index: nameIndex,
        descriptor_index: descriptorIndex
    });
    return constantPool.length - 1;
}

function makeDescriptor(type, args) {
    if(args) {
        return "(" + args.map(x=>getInternalName(x)).join("") + ")" + getInternalName(type);
    } else {
        return getInternalName(type);
    }
}

/**
 * 
 * @param {string} s 
 */
function addUTF8(s) {
    var bytes = Buffer.from(s, "utf-8");
    
    var c = constantPool.findIndex(x=>x.tag == structures.CONSTANT_Utf8 && x.str == s);

    if(c > -1) return c;

    constantPool.push({ tag: structures.CONSTANT_Utf8, str: s, length: bytes.length, bytes: bytes });
    return constantPool.length - 1;
}

function addConstant(v) {
    if(typeof v === "string") {
        var sIndex = addUTF8(v);
        var c = constantPool.findIndex(x=>x.tag == structures.CONSTANT_String && x.string_index == sIndex);
        if(c > -1) return c;
        
        constantPool.push({tag: structures.CONSTANT_String, string_index: sIndex});
        return constantPool.length - 1;
    } else if(typeof v === "number") {
        var fBytes = floatBytes(v);
        var c = constantPool.findIndex(x=>x.tag == structures.CONSTANT_Float && x.num == v);
        if(c > -1) return c;

        constantPool.push({tag: structures.CONSTANT_Float, num: v, bytes: fBytes});
        return constantPool.length - 1;
    } else {
        throw "huh?? " + v;
    }
}

function floatBytes(v) {
    var buf = new ArrayBuffer(4);
    (new Float32Array(buf))[0] = v;
    var bytes = (new Uint32Array(buf))[0];

    return [
        (bytes >>> 24) & 0b1111_1111,
        (bytes >>> 16) & 0b1111_1111,
        (bytes >>> 8) & 0b1111_1111,
        bytes  & 0b1111_1111
    ];
}

/**
 * @typedef {number[]|string[]} bcodeline
 */
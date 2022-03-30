const fs = require("fs");
const bytecodeTools = require("../bytecode-tools");
const bc = require("../bc");
const bytecodeSpec = require("../bytecode-spec");
const typeSystemCreator = require("./type-system-creator");

require("../..").registerTransmutation({
    id: "type-inference",
    requires: ["single-static"],
    type: "transmutation",
    run: function (context) {
        var bytecode = context.inputs["single-static"];
        var typeSystem = typeSystemCreator();
        
        Object.values(bytecode).forEach(x=> processBytecodeBlock(x, typeSystem));
    }
});

function processBytecodeBlock(block, typeSystem) {
    addAllLocalVariablesToTypeSystem(block.code, typeSystem);
    addAllLocalVariablesToTypeSystem(block.jumps, typeSystem);
}

function addAllLocalVariablesToTypeSystem(codeArray, typeSystem) {
    for(var i = 0; i < codeArray.length; i++) {
        addLocalTypes(codeArray[i], typeSystem);
    }
} 
function addLocalTypes(bcInstruction, typeSystem) {
    addAllLocalVariablesToTypeSystem(bcInstruction.args, typeSystem);
    
    bcInstruction.__typekey = calcType(bcInstruction, typeSystem.makeAnonymousTypeName(), typeSystem);
}

function calcType(instruction, currentTypeKey, typeSystem) {
    if(instruction.hasOwnProperty("__value")) return getAutoautoTypeOfPrimitive(instruction.__value);
    
    switch(instruction.code) {
        case bytecodeSpec.cmp_lt.code:
        case bytecodeSpec.cmp_lte.code:
        case bytecodeSpec.cmp_eq.code:
        case bytecodeSpec.cmp_neq.code:
        case bytecodeSpec.cmp_gte.code:
        case bytecodeSpec.cmp_gt.code:
            return "boolean";
            
        case bytecodeSpec.construct_table.code:
            return assembleObjectType(instruction, currentTypeKey, typeSystem);
        case bytecodeSpec.construct_relation.code:
            return assembleRelationType(instruction, currentTypeKey, typeSystem);
        case bytecodeSpec.dup.code:
        case bytecodeSpec.swap.code:
            throw new Error("Bad use of dup or swap!");
        
        //things that don't return anything
        case bytecodeSpec.pop.code:
        case bytecodeSpec.jmp_i.code:
        case bytecodeSpec.jmp_i_cond.code:
        case bytecodeSpec.jmp_l.code:
        case bytecodeSpec.jmp_l_cond.code:
        case bytecodeSpec.yieldto.code:
        
        case bytecodeSpec.ret.code:
        case bytecodeSpec.pass.code:
            return "undefined";
        case bytecodeSpec.setvar.code:
            return setVariableType(instruction, currentTypeKey, typeSystem);
            
        case bytecodeSpec.unit_currentv.code:
            return "number";
        case bytecodeSpec.abs_dif.code:
            return "number";
        case bytecodeSpec.getvar.code:
            return getVariableType(instruction, currentTypeKey, typeSystem); 
        
        case bytecodeSpec.callfunction.code:
            return getFunctionReturnType(instruction, currentTypeKey, typeSystem);
            
        case bytecodeSpec.getprop.code: 
            return getGetpropReturnType(instruction, currentTypeKey, typeSystem);
            
        case bytecodeSpec.setprop.code:
            return recordSetpropType(instruction, currentTypeKey, typeSystem);
            
        case bytecodeSpec.add.code:
            return getBinaryOperatorType(instruction, currentTypeKey, typeSystem, "+");
        case bytecodeSpec.mul.code:
            return getBinaryOperatorType(instruction, currentTypeKey, typeSystem, "*");
        case bytecodeSpec.div.code:
            return getBinaryOperatorType(instruction, currentTypeKey, typeSystem, "/");
        case bytecodeSpec.exp.code:
            return getBinaryOperatorType(instruction, currentTypeKey, typeSystem, "**");
        case bytecodeSpec.mod.code:
            return getBinaryOperatorType(instruction, currentTypeKey, typeSystem, "%");
        case bytecodeSpec.subtr.code:
            return getBinaryOperatorType(instruction, currentTypeKey, typeSystem, "-");
        
        case bytecodeSpec.makefunction.code:
            return recordFunctionType(instruction, currentTypeKey, typeSystem);
            
        default: 
            //console.error(instruction);
            console.error("untyped bytecode! " + bc[instruction.code].mnemom);
    }
}

function recordFunctionType(instruction, currentTypeKey, typeSystem) {
    var t = {
        type: "function",
        args: [],
        argnames: [],
        varargs: "undefined",
        return: "*"
    };
    
    var lbl = instruction.args[0].__value;
    
    for(var i = 2; i < instruction.args.length; i += 2) {
        t.argnames.push(instruction.args[i - 1].__value + "");
        t.args.push(instruction.args[i].__typekey + "");
    }
    
    typeSystem.upsertType(currentTypeKey, t);
    
    return currentTypeKey;
}

function getBinaryOperatorType(instruction, key, typeSystem, op) {
    typeSystem.upsertType(key, {
        type: "binary_operator",
        op: op,
        left: instruction.args[0].__typekey,
        right: instruction.args[1].__typekey
    });
    return key;
}

function recordSetpropType(setpropInstr, key, typeSystem) {
    
}

function getGetpropReturnType(getpropInstr, key, typeSystem) {
    var getFromTypeKey = getpropInstr.args[0].__typekey;
    var getFromType = typeSystem.getType(getFromTypeKey);
    
    if (!getFromType.properties) getFromType.properties = {};
    
    var resultTypeKey = "";
    
    var key = getpropInstr.args[1];
    if(key.hasOwnProperty("__value") && getFromType.properties[key.__value]) {
        resultTypeKey = typeSystem.getKeyOf(getFromType.properties[key.__value]);
    } else {
        resultTypeKey = typeSystem.getKeyOf(getFromType.some);
    }
    
    if(!resultTypeKey) resultTypeKey = "*";
    
    return resultTypeKey;
}

function getFunctionReturnType(callfunctionInstr, key, typeSystem) {
    var functionTypeName = callfunctionInstr.args[0].__typekey;
    
    typeSystem.upsertType(key, {
        type: "apply",
        operand: functionTypeName
    });
    
    return key;
}

function getVariableType(getvarInstruction, key, typeSystem) {
    var vname = getvarInstruction.args[0].__value;
    return "var " + vname;
}
function setVariableType(setvarInstruction, key, typeSystem) {
    
    var vname = setvarInstruction.args[0].__value;
    
    var vtype = setvarInstruction.args[1].__typekey;
    
    Object.assign(
        typeSystem.getType("var " + vname),
        { type: "alias", typeTo: typeSystem.__t[vtype] }
    );
    
    return "undefined";
}

function getAutoautoTypeOfPrimitive(v) {
    if(v && typeof v[0] == "number" && typeof v[1] == "string") return "unit";
    else return typeof v;
}

function assembleRelationType(crInstr, currentTypeKey, typeSystem) {
    var t = {
        type: "object",
        properties: {
        }
    };
    
    var k = crInstr.args[0].__value;
    var vT = crInstr.args[1].__typekey;
    t.properties[k] = vT;
    
    typeSystem.upsertType(currentTypeKey, t);
    return currentTypeKey;
}

function assembleObjectType(constructTableInstr, currentTypeKey, typeSystem) {
    
    var valueUnion = { type: "union", types: [] };
    var t = {
        type: "object",
        some: valueUnion,
        properties: {}
    };
    for(var i = 0; i < constructTableInstr.args.length; i += 2) {
        var key = constructTableInstr.args[i].__value;
        if(key === undefined) continue;
        
        var valueType = constructTableInstr.args[i].__typekey;
        valueUnion.types.push(valueType);
        t.properties[key] = valueType;
    }
    
    typeSystem.upsertType(currentTypeKey, t);
    
    return currentTypeKey;
}

/**
 * @typedef {Bytecode} typedBytecode
 * @property __typekey
 */
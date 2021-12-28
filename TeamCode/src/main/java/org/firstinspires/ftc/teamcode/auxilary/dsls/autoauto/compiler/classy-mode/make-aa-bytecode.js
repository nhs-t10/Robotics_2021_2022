var instructions = {
    NOOP: 0,
    PUSH_CONST: 1,
    JUMP: 2, //pop a number off the stack & jump to that INSTRUCTION.
    ENDLOOP: 3,
    POP: 4,
    DUP: 5,
    GET_PROP: 6,
    SET_PROP: 7,
    GET_VAR: 8,
    SET_VAR: 9,
    CALL_FUNC: 10,
    INSTANCEOF: 11,

    ADD: 12,
    SUBTRACT: 13,
    MULTIPLY: 14,
    DIVIDE: 15,
    MODULO: 16,
    
    LESS_THAN: 17,
    GREATER_THAN: 18,
    EQUAL_TO: 19,
    NOT_EQUAL_TO: 20,

    IF_NONZERO_JUMP: 21,

    PUSH_TIME_MS: 22,

    JUMP_STATICVALUE: 23, //jump to given instruction. Has one extra value-- a number, the absolute index of an INSTRUCTION.
    GET_VAR_STATIC: 24,
    SET_VAR_STATIC: 25,

    IF_NONZERO_JUMP_STATIC: 26
};

module.exports = function(statements) {
    var bytecodeStatements = statements.map(x=>statementToBytecodeObject(x));
}

function statementToBytecodeObject(statement) {
    return {
        statepath: statement.statepath,
        state: statement.state,
        bytecodeInstructions: astToBytecode(statement.statement, statement)
    }
}


var astToBytecodeTypes = {
    "GotoStatement": function(statement, context) {
        var bytecode = [], finalizationFunctions = [];

        if(statement.path.type == "Identifier") {
            var jumpInstr = [instructions.JUMP_STATICVALUE, null];

            bytecode.push(jumpInstr);

            finalizationFunctions.push(function(pathStartIndexes, pathStateSizes) {
                jumpInstr[1] = pathStartIndexes[statement.path.value];
            });
            bytecode.push([instructions.ENDLOOP]);
        } else {
            throw "Dynamic `goto` is not supported in classy mode!";
        }
        return [bytecode, finalizationFunctions];
    },
    "ValueStatement": function(statement, context) {
        var bytecode = [], finalizationFunctions = [];

        return [bytecode, finalizationFunctions];
    },
    "NextStatement": function(statement, context) {
        var bytecode = [], finalizationFunctions = [];

        var jumpInstr = [instructions.JUMP_STATICVALUE, null];

        bytecode.push(jumpInstr);

        finalizationFunctions.push(function(pathStartIndexes, pathStateStartIndexesFromPath) {
            var path = context.statepath;
            var stateIndex = (context.state + 1) % pathStateSizes[path].length;

            jumpInstr[1] = pathStartIndexes[path] + pathStateStartIndexesFromPath[path][stateIndex];
        });
        bytecode.push([instructions.ENDLOOP]);

        return [bytecode, finalizationFunctions];
    },
    "AfterStatement": function(statement, context) {
        var bytecode = [], finalizationFunctions = [];

        var initVariableName = "init?@" + context.statementIndex + "@" + context.state + "@" + context.statepath;

        var ifNoInitThenInit = [
            [instructions.GET_VAR_STATIC, initVariableName],
            [instructions.IF_NONZERO_JUMP_STATIC, null],
            astToBytecode(statement.unitValue, context),
            [instructions.SET_VAR_STATIC, initVariableName]
        ]

        var pushLengthCode = astToBytecode(statement.unitValue, context);
        bytecode.push(pushLengthCode);


        return [bytecode, finalizationFunctions];
    }
}

function astToBytecode(statement, context) {
    var bytecode = [];
    var finalizationFunctions = [];

    if(astToBytecodeTypes[statement.type] === undefined) {
        console.log(statement);
        throw "no ast-bytecode converter for " + statement.type;
    }
    
    var b = astToBytecodeTypes[statement.type](statement, context);

    bytecode = bytecode.concat(b[0]);
    finalizationFunctions = finalizationFunctions.concat(b[1]);
}
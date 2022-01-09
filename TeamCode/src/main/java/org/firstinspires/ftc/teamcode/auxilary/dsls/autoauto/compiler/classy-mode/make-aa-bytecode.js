var units = require("./units");
var makeUnitValueGetterBytecode = units.makeUnitValueGetterBytecode;

var instructions = require("./aa-instructions");

module.exports = function(statements) {
    var bytecodeStatements = statements.map(x=>statementToBytecodeObject(x));
    
    var taggedBytecodeInstructions = bytecodeStatements.map(x=>x.bytecodeInstructions[0].map(y=>({
        statepath: x.statepath,
        state: x.state,
        bc: y
    }))).flat();

    //insert ENDLOOP after each state
    var stateNamesForInstructions = taggedBytecodeInstructions.map(x=>x.statepath + " " + x.state);
    //use Array.from(new Set(...)) to make sure it only processes each state once.
    var stateNamesSet = Array.from(new Set(stateNamesForInstructions));
    stateNamesSet.forEach(x=> {
        var i = stateNamesForInstructions.lastIndexOf(x);

        var newInstr = {
            statepath: taggedBytecodeInstructions[i].statepath,
            state: taggedBytecodeInstructions[i].state,
            bc: [instructions.ENDLOOP]
        };

        if(i < stateNamesForInstructions.length) {
            stateNamesForInstructions.splice(i + 1, 0, x);
            taggedBytecodeInstructions.splice(i + 1, 0, newInstr);
        } else {
            stateNamesForInstructions.push(x);
            taggedBytecodeInstructions.push(newInstr);
        }
    });

    var finalizationFunctions = bytecodeStatements.map(x=>x.bytecodeInstructions[1]).flat();
    finalizationFunctions.forEach(x=>x(taggedBytecodeInstructions, stateNamesSet));

    return taggedBytecodeInstructions.map(x=>x.bc);
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
            var jumpInstr = [instructions.UPDATE_LOOP_START_STATE_STATIC, null];

            bytecode.push(jumpInstr);

            finalizationFunctions.push(function(bytecode, states) {
                jumpInstr[1] = states.findIndex(x=>x.startsWith(statement.path.value + " "));
            });
            bytecode.push([instructions.ENDLOOP]);
        } else {
            throw "Dynamic `goto` is not supported in classy mode!";
        }
        return [bytecode, finalizationFunctions];
    },
    "ValueStatement": function(statement, context) {
        return astToBytecode(statement.call, context);
    },
    "UnitValue": function(statement, context) {
        var bytecode = [], finalizationFunctions = [];

        bytecode.push([instructions.PUSH_CONST, units.getRawAmount(statement.value.v, statement)]);

        return [bytecode, finalizationFunctions];
    },
    "": function(statement, context) {
        var bytecode = [], finalizationFunctions = [];

        

        return [bytecode, finalizationFunctions];
    },
    "BooleanLiteral": function(statement, context) {
        return [[[instructions.PUSH_CONST, +statement.value]], []];
    },
    "OperatorExpression": function(statement, context) {
        var bytecode = [], finalizationFunctions = [];

        var left = astToBytecode(statement.left, context);
        bytecode = bytecode.concat(left[0]);
        finalizationFunctions = finalizationFunctions.concat(left[1]);

        var right = astToBytecode(statement.right, context);
        bytecode = bytecode.concat(right[0]);
        finalizationFunctions = finalizationFunctions.concat(right[1]);

        var instr = {
            "+": instructions.ADD,
            "-": instructions.SUBTRACT,
            "*": instructions.MULTIPLY,
            "/": instructions.DIVIDE,
            "%": instructions.MODULO
        };
        if(!instr[statement.operator]) throw "bad operator " + statement.operator;
        
        bytecode.push([instr[statement.operator]]);

        return [bytecode, finalizationFunctions];
    },
    "ArrayLiteral": function(statement, context) {
        var bytecode = [], finalizationFunctions = [];

        bytecode.push([instructions.PUSH_EMPTY_TABLE_REF]);

        statement.elems.args.forEach((x,i)=>{
            bytecode.push([instructions.DUP]);

            if(x.type == "TitledArgument") {
                if(x.name.type == "VariableReference") x.name = { type: "StringLiteral", str: x.name.variable.value };

                var tbc = astToBytecode(x.name);
                bytecode = bytecode.concat(tbc[0]);
                finalizationFunctions = finalizationFunctions.concat(tbc[1]);

                x = x.value;
            } else {
                bytecode.push([instructions.PUSH_CONST, i]);
            }

            var arg = astToBytecode(x);
            bytecode = bytecode.concat(arg[0]);
            finalizationFunctions = finalizationFunctions.concat(arg[1]);

            bytecode.push([instructions.SET_PROP]);

        });

        return [bytecode, finalizationFunctions];
    },
    "StringLiteral": function(statement, context) {
        return [[[instructions.PUSH_CONST, statement.str]], []];
    },
    "TailedValue": function(statement, context) {
        var bytecode = [], finalizationFunctions = [];

        var head = astToBytecode(statement.head, context);
        bytecode = bytecode.concat(head[0]);
        finalizationFunctions = finalizationFunctions.concat(head[1]);

        if(statement.tail.type == "Identifier") statement.tail = { type: "StringLiteral", str: statement.tail.value };

        var tail = astToBytecode(statement.tail, context);
        bytecode = bytecode.concat(tail[0]);
        finalizationFunctions = finalizationFunctions.concat(tail[1]);

        
        bytecode.push([instructions.GET_PROP]);

        return [bytecode, finalizationFunctions];
    },
    "LetStatement": function(statement, context) {
        var bytecode = [], finalizationFunctions = [];

        if(statement.variable.type == "VariableReference") {
            var val = astToBytecode(statement.value, context);

            bytecode = bytecode.concat(val[0]);
            finalizationFunctions = finalizationFunctions.concat(val[1]);

            bytecode.push([instructions.SET_VAR_STATIC, statement.variable.variable.value]);
        } else if(statement.variable.type == "TailedValue") {
            
            var h = astToBytecode(statement.variable.head, context);
            bytecode = bytecode.concat(h[0]);
            finalizationFunctions = finalizationFunctions.concat(h[1]);

            var t = astToBytecode(statement.variable.tail, context);
            bytecode = bytecode.concat(t[0]);
            finalizationFunctions = finalizationFunctions.concat(t[1]);

            var val = astToBytecode(statement.value, context);
            bytecode = bytecode.concat(val[0]);
            finalizationFunctions = finalizationFunctions.concat(val[1]);

            bytecode.push([instructions.SET_PROP]);
        } else throw "bad set type";

        return [bytecode, finalizationFunctions];
    },
    "IfStatement": function(statement, context) {
        var bytecode = [], finalizationFunctions = [];

        var op = astToBytecode(statement.conditional, context);

        bytecode = bytecode.concat(op[0]);
        finalizationFunctions = finalizationFunctions.concat(op[1]);

        var jumpInstr = [instructions.IF_NONZERO_JUMP_STATIC, null];
        bytecode.push(jumpInstr);

        var stmt = astToBytecode(statement.statement, context);

        bytecode = bytecode.concat(stmt[0]);
        finalizationFunctions = finalizationFunctions.concat(stmt[1]);

        finalizationFunctions.push(function(finishedBytecodes) {
            jumpInstr[1] = finishedBytecodes.findIndex(x=>x.bc == jumpInstr) + stmt[0].length;
        });

        return [bytecode, finalizationFunctions];
    },
    "ComparisonOperator": function(statement, context) {
        var bytecode = [], finalizationFunctions = [];

        var left = astToBytecode(statement.left, context);
        bytecode = bytecode.concat(left[0]);
        finalizationFunctions = finalizationFunctions.concat(left[1]);

        var right = astToBytecode(statement.right, context);
        bytecode = bytecode.concat(right[0]);
        finalizationFunctions = finalizationFunctions.concat(right[1]);

        var instr = {
            "<": instructions.LESS_THAN,
            "<=": instructions.LESS_THAN_EQUALS,
            "==": instructions.EQUAL_TO,
            "!=": instructions.NOT_EQUAL_TO,
            ">": instructions.GREATER_THAN,
            ">=": instructions.GREATER_THAN_EQUALS,
        }
        if(!instr[statement.operator]) throw "bad operator " + statement.operator;
        
        bytecode.push([instr[statement.operator]]);

        return [bytecode, finalizationFunctions];
    },
    "VariableReference": function(statement, context) {
        return [[[instructions.GET_VAR_STATIC, statement.variable.value]], []];
    },
    "NumericValue": function(statement, context) {
        return [[[instructions.PUSH_CONST, statement.v]], []];
    },
    "FunctionCall": function(statement, context) {
        var bytecode = [], finalizationFunctions = [];

        statement.args.args.forEach(x=>{
            var b = astToBytecode(x, context);
            bytecode = bytecode.concat(b[0]);
            finalizationFunctions = finalizationFunctions.concat(b[1]);
        });

        var func = astToBytecode(statement.func, context);
        bytecode = bytecode.concat(func[0]);
        finalizationFunctions = finalizationFunctions.concat(func[1]);

        bytecode.push([instructions.CALL_FUNC, statement.args.args.length]);

        return [bytecode, finalizationFunctions];
    },
    "NextStatement": function(statement, context) {
        var bytecode = [], finalizationFunctions = [];

        var jumpInstr = [instructions.UPDATE_LOOP_START_STATE_STATIC, null];

        bytecode.push(jumpInstr);

        finalizationFunctions.push(function(finishedBytecode, states) {
            var path = context.statepath;
            var stateIndex = context.state + 1;

            var idx = states.findIndex(x=>x == path + " " + stateIndex);
            //default to going to the first state
            if(idx == -1) idx = states.findIndex(x=>x.startsWith(path + " "));

            jumpInstr[1] = idx;
        });
        bytecode.push([instructions.ENDLOOP]);

        return [bytecode, finalizationFunctions];
    },
    "AfterStatement": function(statement, context) {
        var bytecode = [], finalizationFunctions = [];

        var initVariableName = "init?@" + context.statementIndex + "@" + context.state + "@" + context.statepath;
        var didInitVariableName = "didInit?@" + context.statementIndex + "@" + context.state + "@" + context.statepath;

        var ifNoInitThenInit = [
            [instructions.GET_VAR_STATIC, didInitVariableName],
            [instructions.IF_NONZERO_JUMP_STATIC, null]
        ].concat(makeUnitValueGetterBytecode(statement.unitValue, instructions))
        .concat([
            [instructions.SET_VAR_STATIC, initVariableName],
            [instructions.PUSH_CONST, 1],
            [instructions.SET_VAR_STATIC, didInitVariableName]
        ]);

        bytecode = bytecode.concat(ifNoInitThenInit);

        var pushLengthCode = makeUnitValueGetterBytecode(statement.unitValue, instructions);
        bytecode = bytecode.concat(pushLengthCode);

        var unitValue = astToBytecode(statement.unitValue, context);
        finalizationFunctions = finalizationFunctions.concat(unitValue[1]);

        var checkCode = [[instructions.GET_VAR_STATIC, initVariableName],
            [instructions.SUBTRACT], //delta = final - initial
            [instructions.ABSOLUTE_VALUE] //Math.abs(delta)
        ].concat(unitValue[0])
        .concat([
            [instructions.GREATER_THAN_EQUALS], //Math.abs(delta) >= targetDelta
            [instructions.IF_NONZERO_JUMP_STATIC, null],
            [instructions.JUMP_STATICVALUE, null]
        ]);

        var statement = astToBytecode(statement.statement, context);
        bytecode = bytecode.concat(statement[0]);
        finalizationFunctions = finalizationFunctions.concat(statement[1]);

        finalizationFunctions.push(function(bytecode) {
            var index = bytecode.findIndex(x=>x.bc == ifNoInitThenInit[0]);
            
            ifNoInitThenInit[1][1] = index + ifNoInitThenInit.length;

            checkCode[5][1] = index + ifNoInitThenInit.length + pushLengthCode.length + checkCode.length;
            checkCode[6][1] = index + ifNoInitThenInit.length + pushLengthCode.length + checkCode.length + statement[0].length;
        });

        return [bytecode, finalizationFunctions];
    }
}

function astToBytecode(statement, context) {
    var bytecode = [];
    var finalizationFunctions = [];

    if(astToBytecodeTypes[statement.type] === undefined) {
        console.warn(statement);
        throw "no ast-bytecode converter for " + statement.type;
    }
    
    return astToBytecodeTypes[statement.type](statement, context);
}
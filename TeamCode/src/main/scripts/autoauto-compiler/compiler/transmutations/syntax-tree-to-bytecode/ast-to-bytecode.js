var bytecodeSpec = require("./bytecode-spec");
const constantPool = require("./constant-pool");

module.exports = treeBlockToBytecode;

//this gets a "tree block", which is equivalent to a state.
function treeBlockToBytecode(block, constantPool) {
    
    var bBlock = {
        subblocks: [],
        entryLabel: block.label + "/stmt/" + 0,
        label: block.label,
        stateCountInPath: block.stateCountInPath
    }

    bBlock.subblocks = block.treeStatements.map((x, i) => ({
        bcode: null,
        label: block.label + "/stmt/" + i
    }));

    block.treeStatements.forEach((x, i) => {
        var nextLabel = block.label + "/stmt/" + (i + 1);
        if(i + 1 == block.treeStatements.length) nextLabel = afterStateDoneJumpTo || block.label;
        
        var bcode = astToBytecode(x, bBlock, constantPool, nextLabel);
        if(i + 1 == block.treeStatements.length) {
            bcode.push(emitBytecodeWithLocation(bytecodeSpec.yield, x));
        }
        bBlock.subblocks[i].bcode = bcode;
    });
    
    bBlock.subblocks.push({
        label: bBlock.label,
        bcode: jumpToLabel(bBlock.entryLabel, constantPool)
    });

    return bBlock;
}

/**
 * @typedef {object} Bytecode
 * @property {number} code
 * @property {*?} __value
 * @property {object} location
 */

/**
 * @typedef {object} Block
 * @property {string} label
 * @property {Bytecode[]} code
 * @property {Bytecode[]} jumps
 */

/**
 * 
 * @param {*} ast 
 * @param {string} label
 * @param {ConstantPool} constantPool 
 * @param {string} afterThisJumpToLabel 
 * @returns {Block[]}
 */
function astToBytecodeBlocks(ast, label, constantPool, afterThisJumpToLabel, stateCountInPath) {
    
    if(!constantPool) constantPool.f;
    
    switch (ast.type) {
        case "SkipStatement":
            return skipStatementToBytecode(ast, label, constantPool, stateCountInPath);
        case "NextStatement":
            return nextStatementToBytecode(ast, label, constantPool, stateCountInPath);
        case "LetPropertyStatement":
            return letPropertyToBytecode(ast, label, constantPool, afterThisJumpToLabel);
        case "FunctionDefStatement":
            return functionDefToBytecode(ast, label, constantPool, afterThisJumpToLabel);    
        case "Block":
            return codeBlockToBytecode(ast, label, constantPool, afterThisJumpToLabel, stateCountInPath);
        case "PassStatement":
            return passStatementToBytecode(ast, label, constantPool, afterThisJumpToLabel);
        case "IfStatement":
            return ifStatementToBytecode(ast, label, constantPool, afterThisJumpToLabel, stateCountInPath);
        case "LetStatement":
            return letStatementToBytecode(ast, label, constantPool, afterThisJumpToLabel);
        case "GotoStatement":
            return gotoStatementToBytecode(ast, label, constantPool);
        case "NextStatement":
            return nextStatementToBytecode(ast, label, constantPool, stateCountInPath);
        case "AfterStatement":
            return afterStatementToBytecode(ast, block, constantPool, afterThisJumpToLabel, stateCountInPath);
        case "ValueStatement":
            return valueStatementToBytecode(ast, label, constantPool, afterThisJumpToLabel);
        default:
            console.error(ast);
            console.error("Can't convert " + ast.type + " to bytecode!");
            HTMLFormControlsCollection.fgssdfg.af.basdfdsa
    }
}

/**
 * @typedef {object} valueComputationBytecode
 * @property {Block[]} dependentBlocks
 * @property {Bytecode[]} computation
 */

/**
 * 
 * @param {*} valueAst 
 * @param {*} constantPool 
 * @returns {valueComputationBytecode}
 */
function valueToBytecodeBlocks(valueAst, constantPool) {
    var simpleBytecodeArray = valueToSimpleBytecodeArray(valueAst, constantPool);
    
    //if it was able to make that into a simple bytecode array, great!
    if(simpleBytecodeArray !== undefined) {
        return {
            dependentBlocks: [],
            computation: simpleBytecodeArray
        };
    }

    //otherwise, try the more-complex ones.
    switch(valueAst.type) {
        case "FunctionLiteral": 
            return functionLiteralToBytecode(valueAst, constantPool)
        case "OperatorExpression":
        case "ComparisonOperator":
            return operationToBytecode(ast, constantPool);
        case "TitledArgument":
            return valueToBytecode(ast.name, constantPool)
            .concat(valueToBytecode(ast.value, constantPool))
            .concat(emitBytecodeWithLocation(bytecodeSpec.construct_relation, ast));
        case "ArgumentList":
            return ast.args.map(x => valueToBytecode(x, constantPool))
                .concat(emitConstantWithLocation(ast.args.length, constantPool, ast));
    }
}

/**
 * 
 * @param {Ast} valueAst 
 * @returns {Bytecode[]}
 */
function valueToSimpleBytecodeArray(valueAst, constantPool) {
    switch(valueAst.type) {
        case "FunctionCall":
            return valueToBytecode(ast.func, constantPool)
                .concat(valueToBytecode(ast.args, constantPool))
                .concat(emitBytecodeWithLocation(bytecodeSpec.callfunction, ast));
        case "Identifier":
            return [emitConstantWithLocation(ast.value, constantPool, ast)];
        case "VariableReference":
            return valueToBytecode(ast.variable, constantPool)
                .concat(emitBytecodeWithLocation(bytecodeSpec.getvar, ast));
        case "StringLiteral":
            return [emitConstantWithLocation(ast.str, constantPool, ast)];
        case "NumericValue":
            return [emitConstantWithLocation(ast.v, constantPool, ast)];
        case "ArrayLiteral":
            return valueToBytecode(ast.elems, constantPool)
                .concat(emitBytecodeWithLocation(bytecodeSpec.construct_table, ast));
        case "TailedValue":
            return valueToBytecode(ast.head, constantPool)
                .concat(valueToBytecode(ast.tail, constantPool))
                .concat(emitBytecodeWithLocation(bytecodeSpec.getprop, ast));
        case "DynamicValue":
            return valueToBytecode(ast.value, constantPool);
        case "BooleanLiteral":
            return [emitConstantWithLocation(ast.value, constantPool, ast)];
        case "UnitValue": 
            return [emitConstantWithLocation(unitwrap(ast), constantPool, ast)];
    };
}

function unitwrap(ast) {
    if(ast.value.type != "NumericValue") throw "Value isn't a number";
    if(ast.unit.type != "Identifier") throw "Unit isn't an identifier";
    return [ast.value.v, ast.unit.value];
}

/**
 * 
 * @param {*} lbl 
 * @param {*} pool 
 * @returns {object[]}
 */
function jumpToLabel(lbl, pool) {
    return [
        emitConstantWithLocation(lbl, pool, {}),
        bytecodeSpec.jmp_l
    ];
}

/**
 * 
 * @param {string} comp 
 * @returns {Bytecode}
 */
function getOperationBytecodeInstruction(comp) {
    switch(comp) {
        case "<": return bytecodeSpec.cmp_lt;
        case "<=": return bytecodeSpec.cmp_lte;
        case ">": return bytecodeSpec.cmp_gt;
        case ">=": return bytecodeSpec.cmp_gte;
        case "==": return bytecodeSpec.cmp_eq;
        case "!=": return bytecodeSpec.cmp_neq;
        
        case "+": return bytecodeSpec.add;
        case "-": return bytecodeSpec.subtr;
        case "*": return bytecodeSpec.mul;
        case "/": return bytecodeSpec.div;
        case "%": return bytecodeSpec.mod;
        case "^":
        case "**": return bytecodeSpec.exp;
    }
    throw "Unrecognized comparison " + comp;
}

function passStatementToBytecode(ast, label, constantPool, nextLabel) {
    return [{
        label: label,
        code: emitBytecodeWithLocation(bytecodeSpec.pass, ast),
        jumps: jumpToLabel(afterThisJumpToLabel, constantPool)
    }];
}

function valueStatementToBytecode(ast, label, constantPool, nextLabel) {
    var val = valueToBytecodeBlocks(ast.call, constantPool);

    return [{
        label: label,
        //pop the value once it's calculated to ensure a clean stack :)
        code: [].concat(
            val.computation, 
            emitBytecodeWithLocation(bytecodeSpec.pop, ast)
        ),
        jumps: jumpToLabel(nextLabel, constantPool)
    }].concat(val.dependentBlocks);
}

function gotoStatementToBytecode(ast, label, constantPool) {
    var pathName = valueToBytecodeBlocks(ast.path, constantPool);

    return [{
        label: label,
        code: [],
        jumps: [emitBytecodeWithLocation("s/", constantPool, ast)]
        .concat(
            pathName.computation,
            emitConstantWithLocation("/0", constantPool, ast),
            emitBytecodesWithLocation([bytecodeSpec.add, bytecodeSpec.add, bytecodeSpec.jmp_l], ast)
        )
    }].concat(pathName.dependentBlocks)
}

/**
 * 
 * @param {*} ast 
 * @param {*} block 
 * @param {*} constantPool 
 * @param {*} nextLabel 
 * @returns {Block[]}
 */
function skipStatementToBytecode(ast, label, constantPool, stateCountInPath) {
    
    if(ast.skip.type != "NumericValue") throw { text: "non-numeric skip! You may only use `skip <n>`, where n is a number.", location: ast.location };
    
    var targetLabelName = calculateSkipToLabel(label, ast.skip.v, stateCountInPath, ast.location);

    return [{
        label: label,
        code: [],
        jumps: jumpToLabel(targetLabelName, constantPool)
    }];
}

function nextStatementToBytecode(ast, label, constantPool, stateCountInPath) {
    var toLabel = calculateSkipToLabel(label, 1, stateCountInPath, ast.location);
    return [{
        label: label,
        code: [],
        jumps: jumpToLabel(toLabel, constantPool)
    }];
}

function calculateSkipToLabel(currentLabel, offsetNum, stateCountInPath, locationForError) {
    var stateBlockParams = currentLabel.split("/");
    var thisStateIndex = +stateBlockParams[2];

    var prefix = "s/" + stateBlockParams[1] + "/";
    
    var skipToIndex = thisStateIndex + offsetNum;

    if(isNaN(skipToIndex) || isNaN(stateCountInPath) || stateCountInPath == 0) {
        throw {
            text: "something went wrong converting a `next` or `skip` statement to bytecode. Make sure that you don't have one inside a function!",
            location: locationForError
        }
    }

    var skipToIndexNorm = skipToIndex % block.stateCountInPath;
    if(skipToIndexNorm < 0) skipToIndexNorm += block.stateCountInPath;
    
    return prefix + skipToIndexNorm; 
}

function letStatementToBytecode(ast, label, constantPool, nextLabel) {
    var variableName = valueToBytecodeBlocks(ast.variable, constantPool);
    var value = valueToBytecodeBlocks(ast.value, constantPool);

    return [{
        label: label,
        code: [].concat(
            variableName.computation,
            value.computation,
            emitBytecodeWithLocation(bytecodeSpec.setvar, ast)
        ),
        jumps: jumpToLabel(nextLabel, constantPool)
    }].concat(variableName.dependentBlocks, value.dependentBlocks);
}

/**
 * 
 * @returns {Block[]}
 */
function letPropertyToBytecode(ast, label, constantPool, nextLabel) {
    if(ast.variable.type != "TailedValue") throw "Attempt to inappropriately `let` an unsettable value";

    var headBytecode = valueToBytecodeBlocks(ast.variable.head, block, constantPool);
    var tailBytecode = valueToBytecodeBlocks(ast.variable.tail, constantPool);
    var setValueBytecode = valueToBytecodeBlocks(ast.value, constantPool);

    return [{
        label: label,
        code: [].concat(
            headBytecode.computation,
            tailBytecode.computation,
            setValueBytecode.computation,
            emitBytecodesWithLocation([bytecodeSpec.setprop], ast)
        ),
        jumps: jumpToLabel(nextLabel, constantPool)

    }].concat(headBytecode.dependentBlocks, tailBytecode.dependentBlocks, setValueBytecode.dependentBlocks);

}

/**
 * 
 * @param {*} ast 
 * @param {*} constantPool 
 * @returns {valueComputationBytecode}
 */
function functionLiteralToBytecode(ast, constantPool) {
    var endBlockLabel = constantPool.subblockLabel("func_end");
    var endBlock = {
        label: endBlockLabel,
        code: [emitBytecodeWithLocation(bytecodeSpec.ret, ast)],
        jumps: []
    };

    var functionBodyLabel = constantPool.subblockLabel("func_body");
    var functionBody = astToBytecodeBlocks(ast.body, functionBodyLabel, constantPool, endBlockLabel, NaN, NaN);
    
    var entryBlockLabel = constantPool.subblockLabel("func_enter");
    var entryBlock = {
        label: entryBlockLabel,
        code: [],
        jumps: jumpToLabel(functionBodyLabel, constantPool)
    };

    var argsCode = valueToBytecodeBlocks(ast.args, constantPool);

    var functionConstructionCode = [].concat(
        [emitConstantWithLocation(entryBlockLabel, constantPool, ast)],
        argsCode.computation,
        emitBytecodeWithLocation(bytecodeSpec.makefunction, ast)
    );
    
    return {
        computation: functionConstructionCode,
        dependentBlocks: [entryBlock, endBlock].concat(argsCode.dependentBlocks, functionBody)
    };
}

function functionDefToBytecode(ast, label, constantPool, nextLabel) {
    var functionLiteral = functionLiteralToBytecode(ast, block, constantPool);
    
    return [{
        label: label,
        code: [].concat(
            valueToSimpleBytecodeArray(ast.name, constantPool),
            functionLiteral.computation,
            emitBytecodesWithLocation([bytecodeSpec.setvar], ast)
        ),
        jumps: jumpToLabel(nextLabel, constantPool)
    }].concat(functionLiteral.dependentBlocks);
}

function codeBlockToBytecode(ast, label, constantPool, nextLabel, stateCountInPath) {
    
    var bcBlocks = [];

    var statements = ast.state.statement;
    var statementLabels = statements.map((x,i)=>constantPool.subblockLabel(label, "blockStatement" + i));
    
    for(var i = 0; i < statements.length; i++) {
        var lbl = statementLabels[i];
        var nextStmtLbl = statementLabels[i + 1] || nextLabel;

        bcBlocks = bcBlocks.concat(astToBytecodeBlocks(statements[i], lbl, constantPool, nextStmtLbl, stateCountInPath));
    }
    
    return bcBlocks;
}

/**
 * 
 * @param {*} ast 
 * @param {*} constantPool 
 * @returns {valueComputationBytecode}
 */
function operationToBytecode(ast, constantPool) {
    var op = getOperationBytecodeInstruction(ast.operator);

    var left = valueToBytecodeBlocks(ast.left, constantPool);
    var right = valueToBytecodeBlocks(ast.right, constantPool);
    
    return {
        computation: [].concat(left.computation, right.computation, emitBytecodeWithLocation(op, ast)),
        dependentBlocks: [].concat(left.dependentBlocks, right.dependentBlocks)
    };
}

function ifStatementToBytecode(ast, label, constantPool, nextLabel, stateCountInPath) {
    /**
     * IfStatements look like this in bytecode:
     * 
     * ifStatement:
     *     if(condition) jump ifStatementIfTrue
     *     jump ifStatmentIfFalse
     * ifStatementIfTrue:
     *     //...body...
     *     jump ifStatmentEnd
     * ifStatementIfFalse
     *     //...body...
     *     jump ifStatementEnd
     * ifStatementEnd
     *     jump NEXT_LABEL
     */
    
    var labels = {
        ifStatementIfTrue: constantPool.subblockLabel(label, "if_true"),
        ifStatementIfFalse: constantPool.subblockLabel(label, "if_false"),
        ifStatmentEnd: constantPool.subblockLabel(label, "if_end")
    };
    
    var ifTrueCode = astToBytecodeBlocks(ast.statement, labels.ifStatementIfTrue, constantPool, labels.ifStatmentEnd, stateCountInPath);
    var ifFalseCode = astToBytecodeBlocks(ast.elseClause, labels.ifStatementIfFalse, constantPool, labels.ifStatmentEnd, stateCountInPath);
    
    var ifStmtEndingBlock = {
        label: labels.ifStatmentEnd,
        code: [],
        jumps: jumpToLabel(nextLabel, constantPool)
    };

    var conditional = valueToBytecodeBlocks(ast.conditional, constantPool);

    var ifStmtStartingBlock = {
        label: label,
        code: [],
        jumps: [[].concat(
            conditional.computation,
            emitConstantWithLocation(labels.ifStatementIfTrue, constantPool, ast),
            [emitBytecodeWithLocation(bytecodeSpec.jmp_l_cond, ast)],
            jumpToLabel(labels.ifStatementIfFalse, constantPool)
        )]
    };
    
    return [ifStmtEndingBlock, ifStmtStartingBlock].concat(ifTrueCode, ifFalseCode, conditional.dependentBlocks);
}

function afterStatementToBytecode(ast, label, constantPool, nextLabel, stateCountInPath) {

    /*
        AfterStatements look like this in bytecode:
        
        {+state entry label}:
            setvar TMP_1 true
        afterStatement (main):
            if (getvar TMP_1) jump afterStatementInit
            jump afterStatementCheckingBody
        afterStatementInit:
            setvar TMP_1 false
            setvar TMP_2 getUnitValueCurrent(unitvalue)
            jump afterStatementCheckingBody
        afterStatementCheckingBody:
            if(abs_difference(getUnitValueCurrent(unitvalue), TMP_2) >= unitvalue) jump afterStatmentIfFinished
            jump afterStatementDone
        afterStatementIfFinished:
            //...body...
            jump afterStatementDone
        afterStatementDone:
            jump NEXT_LABEL
    */
    var tmp1name = emitConstantWithLocation(constantPool.tempVar(), constantPool, ast);
    var tmp2name = emitConstantWithLocation(constantPool.tempVar(), constantPool, ast);
    
    var const_false = emitConstantWithLocation(false, constantPool, ast);
    var const_true = emitConstantWithLocation(true, constantPool, ast);

    var unitvalue = valueToBytecodeBlocks(ast.unitValue, constantPool);
    
    var labels = {
        afterStatementInit: constantPool.subblockLabel(block.label, "after_init"),
        afterStatementCheckingBody: constantPool.subblockLabel(block.label, "after_checking"),
        afterStatementIfFinished: constantPool.subblockLabel(block.label, "after_if_finished_body"),
        afterStatementDone: constantPool.subblockLabel(block.label, "after_done")
    };

    for(labelname in labels) {
        labels[labelname] = emitConstantWithLocation(labels[labelname], constantPool, ast);
    }

    var stateEntryBlock = makeStateinitBlock(emitBytecodesWithLocation([tmp1name, const_true, bytecodeSpec.setvar]), constantPool);
    
    var afterstmtMain = {
        label: label,
        code: [],
        jumps: emitBytecodesWithLocation(
            [tmp1name,
            bytecodeSpec.getvar,
            labels.afterStatementInit,
            bytecodeSpec.jmp_l_cond,

            labels.afterStatementCheckingBody,
            bytecodeSpec.jmp_l]
        , ast)
    };

    var afterstmtInit = {
        label: labels.afterStatementInit.__value,
        code: emitBytecodesWithLocation([
            tmp1name, const_false, bytecodeSpec.setvar,
            tmp2name, unitvalue.computation, bytecodeSpec.unit_currentv, bytecodeSpec.setvar
        ], ast),
        jumps: [labels.afterStatementCheckingBody, bytecodeSpec.jmp_l]
    };

    var afterstmtCheckingBody = {
        label: labels.afterStatementCheckingBody.__value,
        jumps: emitBytecodesWithLocation([
            //compute difference between current value and original.
            unitvalue.computation, bytecodeSpec.unit_currentv, 
            tmp2name, bytecodeSpec.getvar,
            bytecodeSpec.abs_dif, 
            //if it's bigger than the specified value...
            bytecodeSpec.cmp_gte, unitvalue.computation,
            //jump to the 'if finished' body.
            labels.afterStatementIfFinished, bytecodeSpec.jmp_l_cond,

            //otherwise, jump to the 'done' body
            labels.afterStatementDone, bytecodeSpec.jmp_l
        ], ast)
    };
    
    var afterstmtIfFinishedBody = astToBytecodeBlocks(ast.statement, labels.afterStatementIfFinished, constantPool, labels.afterStatementDone, stateCountInPath);

    var afterstmtDone = {
        label: labels.afterStatementDone.__value,
        code: [],
        jumps: jumpToLabel(nextLabel)
    };

    return [].concat(
        unitvalue.dependentBlocks,
        [
            stateEntryBlock,
            afterstmtMain,
            afterstmtInit,
            afterstmtCheckingBody,
            afterstmtIfFinishedBody,
            afterstmtDone
        ]
    );
}

function makeStateinitBlock(bytecode, pool) {
    var label = "+state entry label " + pool.tempVar();

    return {
        label: label,
        code: bytecode,
        jumps: []
    };
}

/**
 * 
 * @param {*} cons 
 * @param {*} pool 
 * @param {*} ast 
 * @returns {object}
 */
function emitConstantWithLocation(cons, pool, ast) {
    var code = pool.getCodeFor(cons);
    return emitBytecodeWithLocation({code: code, __value: cons}, ast);
}

/**
 * 
 * @param {*} code 
 * @param {*} ast 
 * @returns {object}
 */
function emitBytecodeWithLocation(code, ast) {
    var r = {};
    if (typeof code === "number") r.code = code;
    else Object.assign(r, code);

    r.location = ast.location;
    
    return r;
}

/**
 * 
 * @param {*} codes 
 * @param {*} ast 
 * @returns {object[]}
 */
function emitBytecodesWithLocation(codes, ast) {
    return codes.map(x => emitBytecodeWithLocation(x, ast));
}
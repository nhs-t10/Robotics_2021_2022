var bytecodeSpec = require("./bytecode-spec");

module.exports = treeBlockToBytecode;

function treeBlockToBytecode(block, constantPool, afterStateDoneJumpTo) {
    
    var bBlock = {
        subblocks: [],
        entryLabel: block.label + "_stmt_" + 0,
        label: block.label,
    }

    bBlock.subblocks = block.treeStatements.map((x, i) => ({
        bcode: null,
        label: block.label + "_stmt_" + i
    }));

    block.treeStatements.forEach((x, i) => {
        var nextLabel = (i + 1 < block.treeStatements.length) ? (block.label + "_stmt_" + (i + 1)) : (afterStateDoneJumpTo || block.label);
        var bcode = astToBytecode(x, bBlock, constantPool, nextLabel);
        if(i + 1 == block.treeStatements.length) {
            bcode.push(emitBytecodeWithLocation(bytecodeSpec.yield, x));
        }
        bBlock.subblocks[i].bcode = bcode.flat();
    });
    
    bBlock.subblocks.push({
        label: bBlock.label,
        bcode: jumpToLabel(bBlock.entryLabel, constantPool)
    });

    return bBlock;
}

function astToBytecode(ast, block, constantPool, afterThisJumpToLabel) {
    
    if(!constantPool) constantPool.f;
    
    switch (ast.type) {
        case "FunctionLiteral":
            return functionLiteralToBytecode(ast, block, constantPool);

        case "TitledArgument":
            return astToBytecode(ast.name, block, constantPool)
            .concat(astToBytecode(ast.value, block, constantPool))
            .concat(emitBytecodeWithLocation(bytecodeSpec.construct_relation, ast));

        case "SkipStatement":
            return skipStatementToBytecode(ast, block, constantPool, afterThisJumpToLabel);

        case "LetPropertyStatement":
            return letPropertyToBytecode(ast, block, constantPool, afterThisJumpToLabel);

        case "ArrayLiteral":
            return astToBytecode(ast.elems, block, constantPool)
                .concat(emitBytecodeWithLocation(bytecodeSpec.construct_table, ast));
        case "TailedValue":
            return astToBytecode(ast.head, block, constantPool)
                .concat(astToBytecode(ast.tail, block, constantPool))
                .concat(emitBytecodeWithLocation(bytecodeSpec.getprop, ast));
        case "DynamicValue":
            return astToBytecode(ast.value, block, constantPool);  
        case "FunctionDefStatement":
            return functionDefToBytecode(ast, block, constantPool, afterThisJumpToLabel);
        
        case "BooleanLiteral":
            return [emitConstantWithLocation(ast.value, constantPool, ast)];
            
        case "Block":
            return blockToBytecode(ast, block, constantPool, afterThisJumpToLabel);
        
        case "OperatorExpression":
        case "ComparisonOperator":
            return operationToBytecode(ast, block, constantPool);
            
        case "PassStatement":
            return emitBytecodesWithLocation([
                bytecodeSpec.pass,
                jumpToLabel(afterThisJumpToLabel, constantPool)
            ], ast);
        
        case "IfStatement":
            return ifStatementToBytecode(ast, block, constantPool, afterThisJumpToLabel);
            
        case "NumericValue":
            return [emitConstantWithLocation(ast.v, constantPool, ast)];
        case "LetStatement":
            return astToBytecode(ast.variable, block, constantPool)
                .concat(
                    astToBytecode(ast.value, block, constantPool),
                    emitBytecodeWithLocation(bytecodeSpec.setvar, ast),
                    jumpToLabel(afterThisJumpToLabel, constantPool)
                );
                
        case "GotoStatement":
            return astToBytecode(ast.path, block, constantPool)
                .concat(emitBytecodesWithLocation([
                    emitConstantWithLocation("_0", constantPool, ast),
                    bytecodeSpec.add,
                    bytecodeSpec.jmp_l    
                ], ast)
                );
        case "NextStatement":
            var match = afterThisJumpToLabel.split("_");
            var thisIndex = +match[2];
            if(isNaN(thisIndex)) {
                console.error(afterThisJumpToLabel);
                throw "Non-numeric third index";
            }
            
            match[2] = thisIndex + 1;
            return jumpToLabel(match.slice(0,3).join("_"), constantPool);
        
        case "UnitValue": 
            return [emitConstantWithLocation(unitwrap(ast), constantPool, ast)]
        case "AfterStatement":
            return afterStatementToBytecode(ast, block, constantPool, afterThisJumpToLabel)
                .concat(jumpToLabel(afterThisJumpToLabel, constantPool));

        case "Identifier":
            return [emitConstantWithLocation(ast.value, constantPool, ast)];
        case "VariableReference":
            return astToBytecode(ast.variable, block, constantPool)
                .concat(emitBytecodeWithLocation(bytecodeSpec.getvar, ast));
        case "StringLiteral":
            return [emitConstantWithLocation(ast.str, constantPool, ast)];
        case "ArgumentList":
            return ast.args.map(x => astToBytecode(x, block, constantPool))
                .concat(emitConstantWithLocation(ast.args.length, constantPool, ast));
        case "FunctionCall":
            return astToBytecode(ast.args, block, constantPool)
                .concat(astToBytecode(ast.func, block, constantPool));
        case "ValueStatement":
            return astToBytecode(ast.call, block, constantPool)
                .concat(emitBytecodeWithLocation(bytecodeSpec.pop, ast))
                .concat(jumpToLabel(afterThisJumpToLabel, constantPool))
        default:
            console.error(ast);
            console.error("Can't convert " + ast.type + " to bytecode!");
            HTMLFormControlsCollection.addStateEntryLabel.af
    }
}

function unitwrap(ast) {
    if(ast.value.type != "NumericValue") throw "Value isn't a number";
    if(ast.unit.type != "Identifier") throw "Unit isn't an identifier";
    return [ast.value.v, ast.unit.value];
}

function jumpToLabel(lbl, pool) {
    return [
        {code: pool.getCodeFor(lbl)},
        bytecodeSpec.jmp_l
    ];
}

function getOperationBytecode(comp) {
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

function skipStatementToBytecode(ast, block, constantPool, nextLabel) {
    var stateBlockParams = block.label.split("_");
    var thisStateIndex = +stateBlockParams[2];
    if(isNaN(thisStateIndex)) throw "something went wrong in skipStatementToBytecode";

    var prefix = stateBlockParams[0] + "_" + stateBlockParams[1] + "_";

    return emitBytecodesWithLocation([
        constantPool.getCodeFor(prefix),
        constantPool.getCodeFor(thisStateIndex),
        astToBytecode(ast.skip, block, constantPool),
        bytecodeSpec.add, //add skip to current state
        bytecodeSpec.add, //add that resulting number to the prefix, to form the state
        bytecodeSpec.jmp_l, //jump to that label
    ], ast)
}

function letPropertyToBytecode(ast, block, constantPool, nextLabel) {
    if(ast.variable.type != "TailedValue") throw "Attempt to inappropriately `let` an unsettable value";

    return astToBytecode(ast.variable.head, block, constantPool)
        .concat(astToBytecode(ast.variable.tail, block, constantPool))
        .concat(astToBytecode(ast.value, block, constantPool))
        .concat(emitBytecodesWithLocation([
            bytecodeSpec.setprop,
            jumpToLabel(nextLabel, constantPool)
        ], ast));
}

function functionLiteralToBytecode(ast, block, constantPool) {
    var endBlockLabel = constantPool.subblockLabel(block.label, "func_end");
    block.subblocks.push({
        label: endBlockLabel,
        bcode: [emitBytecodeWithLocation(bytecodeSpec.ret, ast)]
    });
    
    var bodyBlockLabel = constantPool.subblockLabel(block.label, "func_enter");
    block.subblocks.push({
        label: bodyBlockLabel,
        bcode: blockToBytecode(ast.body, {
            label: constantPool.subblockLabel(block.label, "func_body"),
            subblocks: block.subblocks
        }, constantPool, endBlockLabel)
    });
    
    return astToBytecode(ast.args, block, constantPool)
    .concat(emitBytecodesWithLocation([
        emitConstantWithLocation(bodyBlockLabel, constantPool, ast),
        bytecodeSpec.makefunction
    ], ast));
}

function functionDefToBytecode(ast, block, constantPool, nextLabel) {
    
    return functionLiteralToBytecode(ast, block, constantPool)
    .concat(astToBytecode(ast.name, block, constantPool))
    .concat(emitBytecodesWithLocation([
        bytecodeSpec.swap,
        bytecodeSpec.setvar,
        jumpToLabel(nextLabel, constantPool)
    ], ast));
}

function blockToBytecode(ast, block, constantPool, nextLabel) {
    var pseudoblock = {
        treeStatements: ast.state.statement,
        label: constantPool.subblockLabel(block.label, "childscope"),
    }
    
    var bytecoded = treeBlockToBytecode(pseudoblock, constantPool, nextLabel);
    
    bytecoded.subblocks.forEach(x=>block.subblocks.push(x));
    
    return [
        jumpToLabel(bytecoded.subblocks[0].label, constantPool)
    ]
}

function operationToBytecode(ast, block, constantPool) {
    var op = getOperationBytecode(ast.operator);
    
    return astToBytecode(ast.left, block, constantPool)
        .concat(astToBytecode(ast.right, block, constantPool))
        .concat(emitBytecodeWithLocation(op, ast));
}

function ifStatementToBytecode(ast, block, constantPool, nextLabel) {
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
        ifStatementIfTrue: constantPool.subblockLabel(block.label, "if_true"),
        ifStatementIfFalse: constantPool.subblockLabel(block.label, "if_false"),
        ifStatmentEnd: constantPool.subblockLabel(block.label, "if_end")
    };
    
    block.subblocks.push({
        label: labels.ifStatementIfTrue,
        bcode: astToBytecode(ast.statement, block, constantPool, labels.ifStatmentEnd)
    });
    
    block.subblocks.push({
        label: labels.ifStatementIfFalse,
        bcode: astToBytecode(ast.elseClause, block, constantPool, labels.ifStatmentEnd)
    });
    
    block.subblocks.push({
        label: labels.ifStatmentEnd,
        bcode: [jumpToLabel(nextLabel, constantPool)]
    });
    
    return astToBytecode(ast.conditional, block, constantPool).concat(emitBytecodesWithLocation([
        constantPool.getCodeFor(labels.ifStatementIfTrue),
        bytecodeSpec.jmp_l_cond,
        jumpToLabel(labels.ifStatementIfFalse, constantPool)
    ], ast));
}

function afterStatementToBytecode(ast, block, constantPool, nextLabel) {

    /*
        AfterStatemnts look like this in bytecode:
        
        {+state entry label}:
            setvar TMP_1 true
        afterStatement:
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
    var const_unitvalue = astToBytecode(ast.unitValue, block, constantPool, nextLabel);
    
    var labels = {
        afterStatementInit: constantPool.subblockLabel(block.label, "after_init"),
        afterStatementCheckingBody: constantPool.subblockLabel(block.label, "after_checking"),
        afterStatementIfFinished: constantPool.subblockLabel(block.label, "after_if_finished_body"),
        afterStatementDone: constantPool.subblockLabel(block.label, "after_done")
    };
    
    
    block.subblocks.push({
        label: labels.afterStatementInit,
        bcode: emitBytecodesWithLocation([
            tmp1name,
            const_false,
            bytecodeSpec.setvar,
            
            tmp2name,
            const_unitvalue, bytecodeSpec.unit_currentv,
            bytecodeSpec.setvar,
            
            jumpToLabel(labels.afterStatementCheckingBody, constantPool)            
        ], ast)
    });
    
    block.subblocks.push({
        label: labels.afterStatementIfFinished,
        bcode: astToBytecode(ast.statement, block, constantPool, labels.afterStatementDone)
    });
    
    block.subblocks.push({
        label: labels.afterStatementCheckingBody,
        bcode: emitBytecodesWithLocation([
                const_unitvalue, bytecodeSpec.unit_currentv,
                tmp2name, bytecodeSpec.getvar,
                bytecodeSpec.abs_dif,
                const_unitvalue,
                bytecodeSpec.cmp_gte,
                constantPool.getCodeFor(labels.afterStatementIfFinished), bytecodeSpec.jmp_l_cond,
                jumpToLabel(labels.afterStatementDone, constantPool)                
            ], ast)
    });
    
    block.subblocks.push({
        label: labels.afterStatementDone,
        bcode: jumpToLabel(nextLabel, constantPool)
    });
    
    addStateEntryLabel(block, emitBytecodesWithLocation([
        tmp1name,
        const_true,
        bytecodeSpec.setvar
    ], ast), constantPool);

    return emitBytecodesWithLocation([
        tmp1name, bytecodeSpec.getvar,
        constantPool.getCodeFor(labels.afterStatementInit), bytecodeSpec.jmp_l_cond,
        
        jumpToLabel(labels.afterStatementCheckingBody, constantPool)
    ], ast);
}

function addStateEntryLabel(stateBlock, bytecode, pool) {
    var label = pool.subblockLabel(stateBlock.entryLabel, "entry_init");
    stateBlock.subblocks.push({
        label: label,
        bcode: bytecode.concat(jumpToLabel(stateBlock.entryLabel, pool))
    });
    stateBlock.entryLabel = label;
    return label;
}

function emitConstantWithLocation(cons, pool, ast) {
    var code = pool.getCodeFor(cons);
    return emitBytecodeWithLocation(code, ast);
}

function emitBytecodeWithLocation(code, ast) {
    var r = {};
    if (typeof code === "number") r.code = code;
    else r.code = code.code;

    r.location = ast.location;
    
    return r;
}

function emitBytecodesWithLocation(codes, ast) {
    codes = codes.flat(Infinity);
    return codes.map(x => emitBytecodeWithLocation(x, ast));
}
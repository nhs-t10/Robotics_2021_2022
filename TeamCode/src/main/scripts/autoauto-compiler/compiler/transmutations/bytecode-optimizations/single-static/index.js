const bc = require("../bc");
const bytecodeSpec = require("../bytecode-spec");

require("../..").registerTransmutation({
    id: "single-static",
    requires: ["combine-basic-blocks"],
    type: "transmutation",
    run: function(context) {
        var bb = context.inputs["combine-basic-blocks"];
        var bytecode = bb.bytecode;
        var cgraph = bb.cgraph;
        
        var globalVarnameCounters = {};
        
        Object.entries(bytecode).forEach(x=>ssaBlock(x[0], x[1], cgraph, cgraph, globalVarnameCounters));
    }
});

function ssaBlock(blockName, block, cgraph, invertedCgraph, globalVarnameCounters) {
    var varsWithPhiNodeForThisBlock = {};
    
    for(var i = 0; i < block.length; i++) {
        var instr = block[i];
        if(isVariableAddressingInstr(instr)) {
            var varname = findVarnameFromInstr(block, i);
            console.log(varname);
        }
    }
}

function findVarnameFromInstr(block, index) {
    var code = block[index].code;
    if(code == bytecodeSpec.getvar.code) return block[index - 1].__value;
    else return block[stepBackwardsOneStackValue(block, index)].__value;
}

function isVariableAddressingInstr(instr) {
    return instr.code == bytecodeSpec.setvar.code || instr.code == bytecodeSpec.getvar.code;
}

function stepBackwardsOneStackValue(block, index) {
    var l = 0;
    for(var i = index - 1; i >= 0; i--) {
        l += computeArity(block, i);
        if(l == 0) return i;
    }
}

function computeArity(block, index) {
    var instr = block[index];
    var arityRecord = bc[instr.code];
    
    //if there's no record, it's a constant
    if(!arityRecord) return 1;
    
    if(typeof arityRecord.push === "number" && typeof arityRecord.pop === "number") return arityRecord.push - arityRecord.pop;
    
    //variable-arity!
    var pshAr = calcArityInPlace(block, index, arityRecord.push);
    var ppAr = calcArityInPlace(block, index, arityRecord.pop);
    
    return pshAr - ppAr;
}

function calcArityInPlace(block, index, pArity) {
    if(typeof pArity === "number") return pArity;
    
    var t = 0;
    
    for(var i = 0; i < pArity.length; i++) {
        if(typeof pArity[i] === "number") t += pArity[i];
        else t += block[index - i - 1].__value;
    }
    
    return t;
} 
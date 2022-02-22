var codeIndexedSpec = require("../bc");

require("../..").registerTransmutation({
    id: "build-bcreqs",
    requires: ["syntax-tree-to-bytecode"],
    type: "information",
    run: function(context) {
        var bytecode = (
            context.inputs["bc-condense-constants"] ||
            context.inputs["syntax-tree-to-bytecode"].blocks);
        
        var keys = Object.keys(bytecode);
        keys.forEach(k=>bytecode[k] = makeCausallyLinkedCodesHierarchal(bytecode[k]));
        
        context.output = bytecode;
        context.status = "pass";
    }
});

function makeCausallyLinkedCodesHierarchal(block) {       
    block.forEach(x=>x.deps=[]);
    
    var sta = [{deps: [], consm: Infinity}];
    
    for(var i = block.length - 1; i >= 0; i--) {        
        var instr = block[i];
        var opcode = instr.code;
        var opcodeFamily = instr.code >>> 24;
        
        var arity;
        
        if(opcodeFamily != 0) {
            arity = 1;
        } else if(codeIndexedSpec[opcode]) {
            var pushInContext = resolveArity(codeIndexedSpec[opcode].push, block, i) 
            var popInContext =  resolveArity(codeIndexedSpec[opcode].pop, block, i);
            
            arity = popInContext - pushInContext;
        }
        
        sta[sta.length - 1].deps.splice(0,0,instr);
        sta[sta.length - 1].consm -= arity;
        
        if(arity > 0) sta.push({ deps: instr.deps, consm: arity });
        else if(sta[sta.length - 1].consm == 0) sta.pop();
        else if(sta[sta.length - 1].consm < 0) throw "stack underflow!";
    }
    
    return sta[0].deps;
}

function resolveArity(ar, block, index) {
    if(typeof ar === "number") return ar;
    
    var tArity = 0;
    
    for(var j = 0; j < ar.length; j++) {
        if(typeof ar[j] === "number") tArity += ar[j];
        else tArity += findOpcodeLiteralValue(block[index - j - 1]);
    }
    return tArity;
}

function findOpcodeLiteralValue(instr) {
    var c = instr.code;
    var f = c >>> 24;
    var k = c & 0xFFFFFF;
    
    if(f == 0x0E) return k;
    else throw "malformed bytecode; couldn't resolve!";
}
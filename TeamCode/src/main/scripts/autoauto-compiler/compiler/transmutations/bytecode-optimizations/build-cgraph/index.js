var fs = require("fs");
const bytecodeSpec = require("../bytecode-spec");

require("../..").registerTransmutation({
    id: "build-cgraph",
    requires: ["bc-condense-constants"],
    type: "information",
    run: function(context) {
        var heirarchalBytecode = context.inputs["bc-condense-constants"];
        
        context.output = buildGraphFrom(heirarchalBytecode);
        context.status = "pass";
        
        fs.writeFileSync(__dirname + "/cgraph", JSON.stringify(context.output, null, 2));
    }
});

function buildGraphFrom(bcode) {
    var keys = Object.keys(bcode);
    
    var stateKeys = keys.filter(x=>/^s\/[^/]+\/0$/.test(x));
    
    var r = {};
    for(var i = 0; i < keys.length; i++) {
        r[keys[i]] = findBlockTargets(bcode[keys[i]], stateKeys);
    }
    
    return r;
}

function findBlockTargets(block, allBlockNames) {
    
    var jumpLabelCodes = [bytecodeSpec.jmp_l.code, bytecodeSpec.jmp_l_cond.code, bytecodeSpec.yieldto.code];
    
    
    var res = [];
    
    for(var i = 0; i < block.jumps.length; i++) {
        if(!jumpLabelCodes.includes(block.jumps[i].code)) {
            throw `Malformed bytecode: non-jump root (${block.jumps[i].code}) in jumps section.`;
        }

        var jumpArgs = block.jumps[i].args;

        var jmpTrgt = jumpArgs[jumpArgs.length - 1];
        if(!jmpTrgt.__value) res = res.concat(allBlockNames);
        else res.push(jmpTrgt.__value);
    }
    
    return Array.from(new Set(res));
}
var fs = require("fs");
const bytecodeSpec = require("../bytecode-spec");

require("../..").registerTransmutation({
    id: "build-cgraph",
    requires: ["bc-condense-constants"],
    type: "information",
    run: function(context) {
        var heirarchalBytecode = context.inputs["bc-condense-constants"];
        
        context.output = buildGraphFrom(heirarchalBytecode);
        
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
    
    var jumpLabelCodes = [bytecodeSpec.jmp_l.code, bytecodeSpec.jmp_l_cond.code];
    
    var jumps = block
        .map((x,i)=>jumpLabelCodes.includes(x.code)?i:-1)
        .filter(x=>x!=-1);
    
    var res = [];
    
    for(var i = 0; i < jumps.length; i++) {        
        var jmpTrgt = block[jumps[i] - 1];
        if(!jmpTrgt || !jmpTrgt.__value) res = res.concat(allBlockNames);
        else res.push(jmpTrgt.__value);
    }
    
    return Array.from(new Set(res));
}

function getPossibleJumpTargets(heirCode, bkeys) {
    if(heirCode.__value) return ["" + heirCode.__value];
    
    var rgx = new RegExp("^" + computePossibleStrValuesRegex(heirCode) + "$");
    
    return bkeys.filter(x=>rgx.test(x));
}

function computePossibleStrValuesRegex(heirCode) {
    if(heirCode.__value) {
        return regexcape("" + heirCode.value);
    } else if(heirCode.code == bytecodeSpec.add.code) {
        var left = heirCode.dep[0];
        var right = heirCode.dep[1];
        return computePossibleStrValuesRegex(left) + computePossibleStrValuesRegex(right);
    } else {
        return ".*";
    }
}

function regexcape(str) {
    return str.replace(/[.*+?^${}()|[\]\\]/g, '\\$&');
}
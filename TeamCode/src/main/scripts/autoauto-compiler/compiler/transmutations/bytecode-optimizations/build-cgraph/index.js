const bytecodeSpec = require("../bytecode-spec");

require("../..").registerTransmutation({
    id: "build-cgraph",
    requires: ["bc-condense-constants", "build-bcreqs"],
    type: "information",
    run: function(context) {
        var heirarchalBytecode = context.inputs["build-bcreqs"];
        
        context.output = buildGraphFrom(heirarchalBytecode);
    }
});

function buildGraphFrom(bcode) {
    var keys = Object.keys(bcode);
    
    var r = {};
    for(var i = 0; i < keys.length; i++) {
        r[keys[i]] = findBlockTargets(bcode[keys[i]], keys);
    }
    
    return r;
}

function findBlockTargets(block, allBlockNames) {
    
    var jumpLabelCodes = [bytecodeSpec.jmp_l.code, bytecodeSpec.jmp_l_cond.code];
    
    var jumps = block.filter(x=>jumpLabelCodes.includes(x.code));
    
    var res = [];
    
    for(var i = 0; i < jumps.length; i++) {
        var jmpInstr = jumps[i];
        
        if(jmpInstr.deps.length != 1) throw "Unconditional jump depends on more than one item!";
            
        var targets = getPossibleJumpTargets(jmpInstr.deps[jmpInstr.deps.length - 1], allBlockNames);
            
        targets.forEach(x=> {            
            var r = { to: x };
            //if it's conditional...
            if(jmpInstr.code == jumpLabelCodes[1]) r.conditional = jmpInstr.dep[0];
            res.push(r);
        });
    }
    return res;
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
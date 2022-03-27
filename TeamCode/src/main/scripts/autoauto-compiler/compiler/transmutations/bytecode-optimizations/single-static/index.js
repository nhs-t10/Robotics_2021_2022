const fs = require("fs");
const bytecodeTools = require("../bytecode-tools");
const bc = require("../bc");
const bytecodeSpec = require("../bytecode-spec");

require("../..").registerTransmutation({
    id: "single-static",
    requires: ["bc-basic-dead-code-elimination"],
    type: "transmutation",
    run: function(context) {
        var bb = context.inputs["bc-basic-dead-code-elimination"];
        var bytecode = bb.bytecode;
        var cgraph = bb.cgraph;

        var invertedCgraph = bb.invertedCGraph;

        fs.writeFileSync(__dirname + "/icgraph", JSON.stringify(invertedCgraph, null, 2));
        
        var globalVarnameCounters = { blocks: {}, vars: {} };
        
        var rootBlocks = getBlocksWithoutParents(bytecode, invertedCgraph);
        
        console.log(rootBlocks);
        
        rootBlocks.forEach(x=>ssaBlock(x, bytecode, cgraph, invertedCgraph, globalVarnameCounters));
        rootBlocks.forEach(x => copyLastSetToChildrenRecursiveNetwork(x.label, cgraph, globalVarnameCounters));
        
        Object.values(bytecode).forEach(x=>insertPhiNodes(x, invertedCgraph, globalVarnameCounters));

        context.output = bytecode;
        context.status = "pass";

        fs.writeFileSync(__dirname + "/bc", bytecodeTools.formatBc(bytecode));
    }
});

function getBlocksWithoutParents(bytecode, invertedCgraph) {
    return Object.values(bytecode).filter(x => invertedCgraph[x.label].length == 0);
}

function insertPhiNodes(block, invertedCgraph, globalVarnameCounters) {
    
    var varsGotten = globalVarnameCounters.blocks[block.label].firstreads;
    for(var k in varsGotten) {
        var parentSets = getAllParentSetsOfVariable(k, invertedCgraph[block.label], globalVarnameCounters);
        
        if(parentSets.length == 0) parentSets = k + "@0";
        else if(parentSets.length == 1) parentSets = parentSets[0];
        else parentSets = {__phi: parentSets};
        
        varsGotten[k].forEach(x=>x.__value = parentSets);
    }
    
}

function ssaBlock(block, bytecode, cgraph, invertedCgraph, globalVarnameCounters) {
    initVariableCounter(block, globalVarnameCounters);
    
    if(hasBeenSsadAlready(block, globalVarnameCounters)) return;
    markAsSsad(block, globalVarnameCounters);

    ssaBytecodeArray(block.code, block.label, cgraph, invertedCgraph, globalVarnameCounters);
    ssaBytecodeArray(block.jumps, block.label, cgraph, invertedCgraph, globalVarnameCounters);
    
    ensureChildrenAreSsad(block, bytecode, cgraph, invertedCgraph, globalVarnameCounters);
}


function ssaBytecodeArray(bcArr, blockLabel, cgraph, invertedCgraph, globalVarnameCounters) {
    bcArr.forEach(x=>{
        ssaBytecodeInstruction(x, blockLabel, cgraph, invertedCgraph, globalVarnameCounters);
    });
}

function ssaBytecodeInstruction(instr, blockLabel, cgraph, invertedCgraph, globalVarnameCounters) {
    ssaBytecodeArray(instr.args, blockLabel, cgraph, invertedCgraph, globalVarnameCounters);

    if(isVariableAddressingInstr(instr)) {
        var varInstr = findVarnameInstructionFromInstr(instr);

        if (isVariableSettingInstr(instr)) incrementSingleStaticVariable(blockLabel, varInstr.__value, globalVarnameCounters);

        varInstr.__value = getSingleStaticVariableName(blockLabel, varInstr.__value, varInstr, globalVarnameCounters);
    }
}

function incrementSingleStaticVariable(blockLabel, plainVariableName, globalVarnameCounters) {
    var variableRecord = getVariableSSARecord(blockLabel, plainVariableName, globalVarnameCounters);

    globalVarnameCounters.vars[plainVariableName]++;
    variableRecord.blockScopeCounter++;
    variableRecord.varname = plainVariableName + "@" + globalVarnameCounters.vars[plainVariableName];
    
    setLastSetNameForLaterPhi(blockLabel, plainVariableName, variableRecord.varname, globalVarnameCounters);
}

function getSingleStaticVariableName(blockLabel, plainVariableName, instruction, globalVarnameCounters) {
    var variableRecord = getVariableSSARecord(blockLabel, plainVariableName, globalVarnameCounters);
    
    if (variableRecord.blockScopeCounter == 0) {
        setFirstReadInstructionForLaterPhi(blockLabel, plainVariableName, instruction, globalVarnameCounters);
    }

    return variableRecord.varname;
}

function copyLastSetToChildrenRecursiveNetwork(blockLabel, cgraph, globalVarnameCounters) {
    //recursion protection
    if (globalVarnameCounters.blocks[blockLabel].hasPhiCopied) return;
    globalVarnameCounters.blocks[blockLabel].hasPhiCopied = true;
    
    copyLastSetInfoToChildren(blockLabel, cgraph, globalVarnameCounters);
    
    var children = cgraph[blockLabel];
    children.forEach(x=>{
        copyLastSetToChildrenRecursiveNetwork(x, cgraph, globalVarnameCounters);
    });
}

function copyLastSetInfoToChildren(blockLabel, cgraph, globalVarnameCounters) {
    if (!globalVarnameCounters.blocks[blockLabel].lastsets) {
        globalVarnameCounters.blocks[blockLabel].lastsets = {};
    }
    
    var thisLastsets = globalVarnameCounters.blocks[blockLabel].lastsets;
    
    var children = cgraph[blockLabel];
    
    for (var k in thisLastsets) {
        for(var i = 0; i < children.length; i++) {
            if (!globalVarnameCounters.blocks[children[i]].lastsets) globalVarnameCounters.blocks[children[i]].lastsets = {};
            
            var childLastsets = globalVarnameCounters.blocks[children[i]].lastsets;
            if (!childLastsets[k]) childLastsets[k] = [];
            uniquelyPush(childLastsets[k], thisLastsets[k]);
        }
    }
}

function getAllParentSetsOfVariable(variable, parentLabels, globalVarnameCounters) {
    var sets = [];
    
    for(var i = 0; i < parentLabels.length; i++) {
        uniquelyPush(sets, globalVarnameCounters.blocks[parentLabels[i]].lastsets[variable] || []);        
    }
    return sets;
}

function setLastSetNameForLaterPhi(blockLabel, plainVariableName, vName, globalVarnameCounters) {
    if (!globalVarnameCounters.blocks[blockLabel].lastsets) {
        globalVarnameCounters.blocks[blockLabel].lastsets = {};
    }
    
    globalVarnameCounters.blocks[blockLabel].lastsets[plainVariableName] = [vName];
}

function setFirstReadInstructionForLaterPhi(blockLabel, plainVariableName, instruction, globalVarnameCounters) {
    if (!globalVarnameCounters.blocks[blockLabel].firstreads) {
        globalVarnameCounters.blocks[blockLabel].firstreads = {};
    }
    if (!globalVarnameCounters.blocks[blockLabel].firstreads[plainVariableName]) {
        globalVarnameCounters.blocks[blockLabel].firstreads[plainVariableName] = [];
    }
    
    globalVarnameCounters.blocks[blockLabel].firstreads[plainVariableName].push(instruction);
}

function getVariableSSARecord(blockLabel, plainVariableName, globalVarnameCounters) {
    var vc = globalVarnameCounters.blocks[blockLabel].vars;
    
    if (!globalVarnameCounters.vars[plainVariableName]) globalVarnameCounters.vars[plainVariableName] = 0;

    if(!vc[plainVariableName]) vc[plainVariableName] = {
        plain: plainVariableName,
        blockScopeCounter: 0,
        varname: plainVariableName + "@0"
    };
    
    return vc[plainVariableName];
}

function initVariableCounter(block, globalVarnameCounters) {
    if(!globalVarnameCounters.blocks[block.label]) {
        globalVarnameCounters.blocks[block.label] = {
            ssa: false,
            vars: {}
        };
    }
}

function hasBeenSsadAlready(block, globalVarnameCounters) {
    return globalVarnameCounters.blocks[block.label].ssa;
}

function markAsSsad(block, globalVarnameCounters) {
    globalVarnameCounters.blocks[block.label].ssa = true;
}

function ensureChildrenAreSsad(block, bytecode, cgraph, invertedCgraph, globalVarnameCounters) {
    var children = cgraph[block.label] || [];

    children.forEach(x=>ssaBlock(bytecode[x], bytecode, cgraph, invertedCgraph, globalVarnameCounters));
}

function findVarnameInstructionFromInstr(instr) {
    var code = instr.code;
    if(code == bytecodeSpec.setvar.code || code == bytecodeSpec.getvar.code) {
        return instr.args[0];
    }
    else {
        throw {
            text: "Malformed bytecode: unable to derive variable name from variable-addressing bytecode!",
            location: instr.location
        };
    }
}

function isVariableAddressingInstr(instr) {
    return instr.code == bytecodeSpec.setvar.code || instr.code == bytecodeSpec.getvar.code;
}

function isVariableSettingInstr(instr) {
    return instr.code == bytecodeSpec.setvar.code;
}


function uniqueValues(arr) {
    return Array.from(new Set(arr));
}

function uniquelyPush(arr, valuesToPush) {
    for(var i = 0; i < valuesToPush.length; i++) {
        if(!arr.includes(valuesToPush[i])) arr.push(valuesToPush[i]);
    }
}
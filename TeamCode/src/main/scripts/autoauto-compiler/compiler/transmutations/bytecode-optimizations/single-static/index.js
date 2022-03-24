const fs = require("fs");
const bytecodeTools = require("../bytecode-tools");
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

        var invertedCgraph = invertControlGraph(cgraph);

        fs.writeFileSync(__dirname + "/icgraph", JSON.stringify(invertedCgraph, null, 2));
        
        var globalVarnameCounters = { blocks: {}, vars: {} };

        console.log(bytecode);
        
        Object.values(bytecode).forEach(x=>ssaBlock(x, bytecode, cgraph, invertedCgraph, globalVarnameCounters));

        context.output = bytecode;
        context.status = "pass";

        fs.writeFileSync(__dirname + "/bc", bytecodeTools.formatBc(bytecode));
    }
});

function ssaBlock(block, bytecode, cgraph, invertedCgraph, globalVarnameCounters) {
    if(hasBeenSsadAlready(block, globalVarnameCounters)) return;

    initVariableCounter(block, globalVarnameCounters);
    markAsSsad(block, globalVarnameCounters);

    ensureParentsAreSsad(block, bytecode, cgraph, invertedCgraph, globalVarnameCounters);

    if(!block.code) {
        console.log(block);
        throw "bad block";
    }

    ssaBytecodeArray(block.code, block.label, cgraph, invertedCgraph, globalVarnameCounters);
    ssaBytecodeArray(block.jumps, block.label, cgraph, invertedCgraph, globalVarnameCounters);
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

        if(isVariableSettingInstr(instr)) incrementSingleStaticVariable(varInstr.__value, globalVarnameCounters);

        varInstr.__value = getSingleStaticVariableName(varInstr.__value, globalVarnameCounters);
    }
}

function incrementSingleStaticVariable(plainVariableName, globalVarnameCounters) {
    var variableRecord = getVariableSSARecord(plainVariableName, globalVarnameCounters);

    variableRecord.counter++;
    variableRecord.varname = plainVariableName + "@" + variableRecord.counter;
}

function getSingleStaticVariableName(plainVariableName, globalVarnameCounters) {
    var variableRecord = getVariableSSARecord(plainVariableName, globalVarnameCounters);

    return variableRecord.varname;
}

function getVariableSSARecord(plainVariableName, globalVarnameCounters) {
    var vc = globalVarnameCounters.vars;

    if(!vc[plainVariableName]) vc[plainVariableName] = {
        counter: 0,
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
    return globalVarnameCounters.blocks[block.label] && globalVarnameCounters.blocks[block.label].ssa;
}

function markAsSsad(block, globalVarnameCounters) {
    globalVarnameCounters.blocks[block.label].ssa = true;
}

function ensureParentsAreSsad(block, bytecode, cgraph, invertedCgraph, globalVarnameCounters) {
    var parents = invertedCgraph[block.label] || [];

    parents.forEach(x=>ssaBlock(bytecode[x], bytecode, cgraph, invertedCgraph, globalVarnameCounters));
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

function invertControlGraph(cgraph) {
    var igc = {};

    Object.entries(cgraph).forEach(x=> {
        var from = x[0];
        var tos = x[1];

        tos.forEach(to=> {
            if(!igc[to]) igc[to] = [];
            
            if(!igc[to].includes(from)) igc[to].push(from);
        });
    });
    return igc;
}